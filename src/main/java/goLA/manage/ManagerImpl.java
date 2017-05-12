package goLA.manage;

import goLA.compute.QueryProcessor;
import goLA.io.DataImporter;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.data.Start_End_Rtree;
import goLA.data.Tree;

import java.util.ArrayList;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter di;
    private QueryProcessor q_processor;
    private Tree tree;
    
    //Determine How to calculate Query by using constructor parameter
    public ManagerImpl(QueryProcessor qp_impl, Tree trs, DataImporter pdi){
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
            System.out.println("----Query processing : " + q.getTrajectory().getName() +", " + q.dist + " -------");
            TrajectoryHolder possible_trajectoryHolder = tree.getPossible(q);
            System.out.println("---- result number : " + possible_trajectoryHolder.size() + " -------");
            result.add(q_processor.findTrajectoriesFrom(q , possible_trajectoryHolder));
        });

        return result;
    }

    @Override
    public Tree getTree() {
        return this.tree;
    }


}
