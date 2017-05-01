package goLA.test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import goLA.compute.*;
import goLA.manage.Manager;
import goLA.manage.ManagerImpl;
import goLA.model.TrajectoryHolder;

public class demoSimpleFrechet {

    public static void main(String[] args) {

        Instant start = Instant.now();
        System.out.println("start");
        
        String src_path = "dataset.txt";
        String query_path = "queries.txt";

        Manager manager = new ManagerImpl(new SimpleFrechet());

        manager.makeStructure(src_path);

        //get all data trajectories
        TrajectoryHolder trajectories = manager.getTrajectoryHolder();
        System.out.println("Loaded trajectories: " + trajectories.size());
        
        List<TrajectoryHolder> result = manager.findResult(query_path);

        for (int index = 0 ; index < result.size() ; index++){
        	System.out.println("---- " + index + " ------");
        	result.get(index).printAllTrajectory();
        }

        Instant end = Instant.now();

        System.out.println("\nProgram execution time : "+ Duration.between(start, end));
    }

}
