package io.github.stemlab.test;

import io.github.stemlab.data.impl.TestIndexImpl;
import io.github.stemlab.io.Exporter;
import io.github.stemlab.io.Importer;
import io.github.stemlab.manage.Manager;
import io.github.stemlab.manage.impl.ManagerImpl;
import io.github.stemlab.model.Query;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class demoSimpleFrechet {
    private static String TEST_DATA_SET_PATH = "dataset.txt";
    private static String QUERY_PATH = "queries.txt";
    private static String RESULT_PATH = "result/SampleData/";
    private static String TAG = "v0.1.8.2";


    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();
        System.out.println("Start Program");

        Exporter exporter = new Exporter(RESULT_PATH, TAG + "/");
        Manager manager = new ManagerImpl(new TestIndexImpl(), new Importer(), exporter);
        manager.makeStructure(TEST_DATA_SET_PATH);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getIndex().size() + " data and put into data structure : " + Duration.between(start, middle));

        List<HashSet<String>> result = manager.processQueryAndGetResult(QUERY_PATH);
        int q_size = result.size();

        Instant end = Instant.now();

        System.out.println("\nPush Data into index : " + Duration.between(start, middle));
        System.out.println("\nQuery Processing : " + Duration.between(middle, end));
        System.out.println("\nProgram execution time : " + Duration.between(start, end));

        System.out.println("\n\n\n write result? (Y/N)");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String b = in.readLine();
        if (b.contains("Y") || b.contains("y")) {
            int counter = 0;
            for (HashSet<String> res : result) {
                exporter.export(res, counter);
                counter++;
            }
            writeEvaluation(manager.getIndex().size(), q_size,
                    Duration.between(middle, end), Duration.between(start, end));
        }
    }

    private static void writeEvaluation(int d_num, int q_num, Duration q, Duration whole) throws IOException {
        Path path = Paths.get(String.format(RESULT_PATH + "%s.txt", TAG));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
            writer.append(new Date().toString() + "\n");
            writer.append("Data number : " + d_num + "\n");
            writer.append("Query number : " + q_num + "\n");
            writer.append("Push Data into index : " + (whole.minus(q)) +"\n" );
            writer.append("Query Processing : " + q + "\n");
            writer.append("Whole Time : " + whole + "\n\n\n");
            writer.close();
        }
    }

}
