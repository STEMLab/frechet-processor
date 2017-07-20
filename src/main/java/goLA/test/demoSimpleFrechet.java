package goLA.test;

import goLA.compute.SimpleFrechet;
import goLA.data.StartRTree;
import goLA.filter.SimplificationFrechet;
import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.manage.ManagerImpl;
import goLA.model.Trajectory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class demoSimpleFrechet {
    private static String TEST_DATA_SET_PATH = "dataset.txt";
    private static String QUERY_PATH = "queries_2.txt";
    private static String RESULT_PATH = "result/SampleData_2/";
    private static String TAG = "v0.1.4.6";

    public static void main(String[] args) throws IOException {
        DataExporter de = new DataExporter(RESULT_PATH, TAG + "/");

        Instant start = Instant.now();
        System.out.println("Start Program");

        Manager manager = new ManagerImpl(new SimpleFrechet(), new StartRTree(), new DataImporter(), new SimplificationFrechet());
        manager.makeStructure(TEST_DATA_SET_PATH);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getTree().size() + " data and put into data structure : " + Duration.between(start, middle));

        List<List<Trajectory>> result = manager.findResult(QUERY_PATH, de);

        for (int index = 0; index < result.size(); index++) {
            de.export(result.get(index), index);
        }

        Instant end = Instant.now();
        System.out.println("\nQuery Processing : " + Duration.between(middle, end));
        System.out.println("\nProgram execution time : " + Duration.between(start, end));

        System.out.println("\n\n\n write result? (Y/N)");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String b = in.readLine();
        if (b.contains("Y") || b.contains("y")) {
            writeEvaluation(manager.getTree().size(), result.size(),
                    Duration.between(middle, end), Duration.between(start, end));
        }
    }

    private static void writeEvaluation(int d_num, int q_num, Duration q, Duration whole) throws IOException {
        Path path = Paths.get(String.format(RESULT_PATH + "%s.txt", TAG));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
            writer.append(new Date().toString() + "\n");
            writer.append("Data number : " + d_num + "\n");
            writer.append("Query number : " + q_num + "\n");
            writer.append("Query Processing : " + q + "\n");
            writer.append("Whole Time : " + whole + "\n\n\n");
            writer.close();
        }
    }

}
