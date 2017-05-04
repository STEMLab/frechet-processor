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

    private DataImporter di = new DataImporter();
    private TrajectoryHolder trajectoryHolder = new TrajectoryHolder();
    private QueryProcessor q_processor;
    private Tree tree;
    
    //Determine How to calculate Query by using constructor parameter
    public ManagerImpl(QueryProcessor qp_impl, Tree trs){
    	q_processor = qp_impl;
    	di = new DataImporter();
    	trajectoryHolder = new TrajectoryHolder();
    	tree = trs;
    }
    
    @Override
    public void makeStructure(String path) {
        di.loadFiles(path, trajectoryHolder, tree);
    }

    @Override
    public List<TrajectoryHolder> findResult(String query_path) {
    	List<TrajectoryHolder> result = new ArrayList<>();
    	List<TrajectoryQuery> query = trajectoryHolder.getQueryTrajectory(query_path);

        query.forEach(q -> {
            TrajectoryHolder possible_trajectoryHolder = tree.getPossible(q, this.trajectoryHolder);
            result.add(q_processor.findTrajectoriesFrom(q , possible_trajectoryHolder));
        });

        return result;
    }

    @Override
    public TrajectoryHolder getTrajectoryHolder() {
        return this.trajectoryHolder;
    }


}
