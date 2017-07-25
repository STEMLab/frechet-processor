package goLA.test;

import goLA.data.impl.IndexImpl;
import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.manage.impl.ManagerImpl;
import goLA.utils.Validator;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;


//change class name to new project name
public class FrechetProcessorLauncher {

    public static void main(String[] args) throws IOException {

        Validator.checkArguments(args);

        DataExporter de = new DataExporter(args[2]);

        Instant start = Instant.now();
        System.out.println("Reading data...");

        Manager manager = new ManagerImpl(new IndexImpl(), new DataImporter());
        manager.makeStructure(args[0]);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getIndex().size() + " data and put into index data structure : " + Duration.between(start, middle));


        List<HashSet<String>> result = manager.findResult(args[1]);

        Instant queryTime = Instant.now();
        System.out.println("\nQuery Processing : " + Duration.between(middle, queryTime));

        for (int index = 0; index < result.size(); index++) {
            de.export(result.get(index), index);
        }

        Instant end = Instant.now();
        System.out.println("\nProgram execution time : " + Duration.between(start, end));
    }
}
