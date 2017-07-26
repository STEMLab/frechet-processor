package io.github.stemlab.test;

import io.github.stemlab.data.impl.TestIndexImpl;
import io.github.stemlab.io.DataExporter;
import io.github.stemlab.io.DataImporter;
import io.github.stemlab.manage.Manager;
import io.github.stemlab.manage.impl.ManagerImpl;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class demoSimpleFrechet {
    private static String TEST_DATA_SET_PATH = "T_dataset.txt";
    private static String QUERY_PATH = "T_queries.txt";
    private static String RESULT_PATH = "result/T_Drive/";
    private static String TAG = "v0.1.8";


    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();
        System.out.println("Start Program");

        Manager manager = new ManagerImpl(new TestIndexImpl(), new DataImporter(), new DataExporter(RESULT_PATH, TAG + "/"));
        manager.makeStructure(TEST_DATA_SET_PATH);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getIndex().size() + " data and put into data structure : " + Duration.between(start, middle));

        int q_size = manager.processQueryAndGetResult(QUERY_PATH).size();

        Instant end = Instant.now();

        System.out.println("\nPush Data into index : " + Duration.between(start, middle));
        System.out.println("\nQuery Processing : " + Duration.between(middle, end));
        System.out.println("\nProgram execution time : " + Duration.between(start, end));

        System.out.println("\n\n\n write result? (Y/N)");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String b = in.readLine();
        if (b.contains("Y") || b.contains("y")) {
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
