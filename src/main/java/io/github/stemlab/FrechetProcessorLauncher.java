package io.github.stemlab;

import io.github.stemlab.data.impl.IndexImpl;
import io.github.stemlab.io.Exporter;
import io.github.stemlab.io.Importer;
import io.github.stemlab.manage.Manager;
import io.github.stemlab.manage.impl.ManagerImpl;
import io.github.stemlab.utils.Validator;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;


public class FrechetProcessorLauncher {

    public static void main(String[] args) throws IOException {
        Validator.checkArguments(args);

        Instant start = Instant.now();
        System.out.println("Reading data...");

        Manager manager = new ManagerImpl(new IndexImpl(), new Importer(), new Exporter(args[2]));
        manager.makeStructure(args[0]);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getIndex().size() + " data and put into index data structure : " + Duration.between(start, middle));

        manager.processQuery(args[1]);
        Instant queryTime = Instant.now();
        System.out.println("\nQuery Processing : " + Duration.between(middle, queryTime));

        Instant end = Instant.now();
        System.out.println("\nProgram execution time : " + Duration.between(start, end));
    }
}
