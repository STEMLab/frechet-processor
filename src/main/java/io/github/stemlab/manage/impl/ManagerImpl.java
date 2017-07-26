package io.github.stemlab.manage.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.compute.QueryProcessor;
import io.github.stemlab.data.Tree;
import io.github.stemlab.filter.Filter;
import io.github.stemlab.io.DataExporter;
import io.github.stemlab.io.DataImporter;
import io.github.stemlab.manage.Manager;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.DouglasPeucker;
import io.github.stemlab.utils.EuclideanDistance;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter di;
    private QueryProcessor q_processor;
    private Tree tree;
    private Filter filter = null;

    //Determine How to calculate Query by using constructor parameter
    public ManagerImpl(QueryProcessor qp_impl, Tree trs, DataImporter pdi, Filter ft) {
        q_processor = qp_impl;
        di = pdi;
        tree = trs;
        filter = ft;
    }

    @Override
    public void setFilter(Filter ft) {
        filter = ft;
    }

    @Override
    public void makeStructure(String path) {
        di.loadFiles(path, tree);
    }

    @Override
    public List<Trajectory> getPossible(Query query) {
        DoubleDBIDList result = tree.rangeQuery(query);

        Coordinate end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        List<Trajectory> poss = new ArrayList<>();

        for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
            String key = tree.getRecordName(x);
            List<Coordinate> coordinates = tree.getTrajectory(key).getCoordinates();
            Coordinate last = coordinates.get(coordinates.size() - 1);
            if (EuclideanDistance.distance(last, end) <= query.getDistance())
                poss.add(tree.getTrajectory(key));
        }

        return poss;
    }


    @Override
    public List<List<String>> TestFindResult(String path, DataExporter de) {
        List<List<String>> result = new ArrayList<>();
        List<Query> query = di.getQueries(path);

        int index = 0;
        for (Query q : query) {
            Instant start = Instant.now();
            System.out.println("\n\n---- Query processing : " + q.getTrajectory().getName() + ", " + q.getDistance() + " -------");
            List<Trajectory> possible_trajectoryHolder = getPossible(q);
            int size1 = possible_trajectoryHolder.size();
            System.out.println("---- candidate number : " + size1 + " -------");
            Instant middle1 = Instant.now();
            System.out.println("---- getPossible Time : " + Duration.between(start, middle1));

            List<Trajectory> filtered_list;
            Instant middle2;

            if (this.filter != null) {
                filtered_list = filter.doFilter(q, possible_trajectoryHolder);
                middle2 = Instant.now();
                System.out.println("---- Filtering Time : " + Duration.between(middle1, middle2));
                System.out.println("---- After Filtering number : " + filtered_list.size() + " -------");
            } else {
                filtered_list = possible_trajectoryHolder;
                middle2 = middle1;
            }

            List<String> q_res = q_processor.query(q, filtered_list);
            int size2 = q_res.size();
            System.out.println("---- result number : " + size2 + " -------");

            Instant end = Instant.now();
            System.out.println("---- calculate Dist Time : " + Duration.between(middle2, end) + " -------");
            if (de != null) {
                de.exportQuery(index,
                        q, possible_trajectoryHolder.size(), this.filter != null,
                        filtered_list.size(), size2,
                        start, middle1, middle2, end
                );
                try {
                    de.export(q_res, index);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
        return result;
    }

    @Override
    public int process(String path, DataExporter de) throws IOException {
        List<Query> query = di.getQueries(path);

        int index = 0;
        for (Query q : query) {
            Instant start = Instant.now();
            System.out.println("\n\n---- Query processing : " + q.getTrajectory().getName() + ", " + q.getDistance() + " -------");

            DoubleDBIDList result = tree.rangeQuery(q);

            Coordinate c_end = q.getTrajectory().getCoordinates().get(q.getTrajectory().getCoordinates().size() - 1);

            HashSet<String> q_res = new HashSet<>();
            Trajectory simple = DouglasPeucker.getReduced(q.getTrajectory(), DouglasPeucker.getMaxEpsilon(q.getTrajectory()));
            double q_dist = q.getDistance();
            double q_max_E = DouglasPeucker.getMaxEpsilon(q.getTrajectory());

            for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
                String key = tree.getRecordName(x);
                Trajectory tr = tree.getTrajectory(key);
                List<Coordinate> coordinates = tr.getCoordinates();
                Coordinate last = coordinates.get(coordinates.size() - 1);
                if (EuclideanDistance.distance(last, c_end) <= q.getDistance()) {
                    if (filter.isFiltered(simple, tr, q_dist, q_max_E)){
                        if (filter.isResult(q, tr, q_dist, q_max_E)){
                            q_res.add(tr.getName());
                        }
                        else{
                            if (q_processor.decision(q,tr) ){
                                q_res.add(tr.getName());
                            }
                        }
                    }
                }
            }

            int size2 = q_res.size();
            System.out.println("---- result number : " + size2 + " -------");

            Instant end = Instant.now();
            System.out.println("---- query processing Time : " + Duration.between(start, end) + " -------");
            if (de != null) {
                de.export(q_res, index);
            }
            index++;
        }
        return index;
    }

    @Override
    public Tree getTree() {
        return this.tree;
    }


}
