package goLA.manage;

import goLA.compute.QueryProcessor;
import goLA.data.Tree;
import goLA.io.DataImporter;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter di;
    private QueryProcessor q_processor;
    private Tree tree;

    //Determine How to calculate Query by using constructor parameter
    public ManagerImpl(QueryProcessor qp_impl, Tree trs, DataImporter pdi) {
        q_processor = qp_impl;
        di = pdi;
        tree = trs;
    }

    @Override
    public void makeStructure(String path) {
        di.loadFiles(path, tree);
    }

    @Override
    public List<TrajectoryHolder> findResult(String query_path) {
        List<TrajectoryHolder> result = new ArrayList<>();
        List<TrajectoryQuery> query = di.getQueries(query_path);

        query.forEach(q -> {
            //TODO : print to file
            Instant start = Instant.now();
            System.out.println("\n\n---- Query processing : " + q.getTrajectory().getName() + ", " + q.dist + " -------");
            TrajectoryHolder possible_trajectoryHolder = tree.getPossible(q);
            System.out.println("---- candidate number : " + possible_trajectoryHolder.size() + " -------");

            Instant middle = Instant.now();
            System.out.println("---- getPossible Time : "+ Duration.between(start, middle));

            TrajectoryHolder q_res = q_processor.findTrajectoriesFrom(q, possible_trajectoryHolder);
            result.add(q_res);
            System.out.println("---- result number : " + q_res.size() + " -------");

            Instant end = Instant.now();
            System.out.println("---- calculate Dist Time : "+ Duration.between(middle, end) + " -------");

        });

        return result;
    }

    @Override
    public Tree getTree() {
        return this.tree;
    }


}
