package goLA.manage;

import goLA.compute.QueryProcessor;
import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter di = new DataImporter();
    private TrajectoryHolder trajectoryHolder = new TrajectoryHolder();
    private QueryProcessor q_processor;
    
    //Determine How to calculate Query by using constructor parameter
    public ManagerImpl(QueryProcessor qp_impl){
    	q_processor = qp_impl;
    	di = new DataImporter();
    	trajectoryHolder = new TrajectoryHolder();
    }
    
    @Override
    public void makeStructure(String path) {
        di.loadFiles(path, trajectoryHolder);
    }

    @Override
    public List<TrajectoryHolder> findResult(String query_path) {
    	List<TrajectoryHolder> result = new ArrayList<>();
    	List<TrajectoryQuery> query = trajectoryHolder.getQueryTrajectory(query_path);
    	
    	for (int i = 0 ; i < query.size() ; i++){
    		result.add(q_processor.findTrajectoriesFrom(query.get(i) , trajectoryHolder));
    	}

        return result;
    }

    @Override
    public TrajectoryHolder getTrajectoryHolder() {
        return this.trajectoryHolder;
    }


}
