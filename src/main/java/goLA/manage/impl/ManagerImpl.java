package goLA.manage.impl;

import goLA.compute.QueryProcessor;
import goLA.data.Tree;
import goLA.filter.Filter;
import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.model.Query;
import goLA.model.Trajectory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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
    public int process(String path, DataExporter de) throws IOException {
        List<Query> query = di.getQueries(path);

        int index = 0;
        for (Query q : query) {
            Instant start = Instant.now();
            System.out.println("\n\n---- Query processing : " + q.getTrajectory().getName() + ", " + q.getDistance() + " -------");
            List<String> q_res = q_processor.query(q, tree, filter);


            int size2 = q_res.size();
            System.out.println("---- result number : " + size2 + " -------");

            Instant end = Instant.now();
            System.out.println("---- calculate Dist Time : " + Duration.between(middle2, end) + " -------");
            if (de != null) {
                de.export(q_res, index);
                de.exportQuery(index,
                        q, possible_trajectoryHolder.size(), this.filter != null,
                        filtered_list.size(), size2,
                        start, middle1, middle2, end
                );
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
