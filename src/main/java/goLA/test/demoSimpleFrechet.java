package goLA.test;

import goLA.compute.impl.QueryProcessorImpl;
import goLA.data.impl.RTree;
import goLA.filter.impl.SimplificationFilter;
import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.manage.impl.ManagerImpl;
import goLA.model.Trajectory;
import goLA.utils.Validator;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;


//change class name to new project name
public class demoSimpleFrechet {

    public static void main(String[] args) throws IOException {

        Validator.checkArguments(args);

        DataExporter de = new DataExporter(args[2]);

        Instant start = Instant.now();
        System.out.println("Reading data...");

        Manager manager = new ManagerImpl(new QueryProcessorImpl(), new RTree(), new DataImporter(), new SimplificationFilter());
        manager.makeStructure(args[0]);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getTree().size() + " data and put into index data structure : " + Duration.between(start, middle));


        List<List<Trajectory>> result = manager.findResult(args[1]);

        Instant queryTime = Instant.now();
        System.out.println("\nQuery Processing : " + Duration.between(middle, queryTime));

        for (int index = 0; index < result.size(); index++) {
            de.export(result.get(index), index);
        }

        Instant end = Instant.now();
        System.out.println("\nProgram execution time : " + Duration.between(start, end));
    }
}
