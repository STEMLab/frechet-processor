package goLA.test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import goLA.compute.*;
import goLA.data.*;
import goLA.filter.SimplifyPossibleFrechet;
import goLA.filter.SimplifyQueryFrechet;
import goLA.io.*;
import goLA.manage.*;
import goLA.model.TrajectoryHolder;

public class demoSimpleFrechet {
    private static String TEST_DATA_SET_PATH = "T_dataset.txt";
    private static String QUERY_PATH = "T_queries.txt";
    private static String RESULT_PATH = "result/T_Drive/";
    private static String TAG = "v0.1.3.3";

    public static void main(String[] args) throws IOException {
        DataExporter de = new DataExporter(RESULT_PATH, TAG +"/");

        Instant start = Instant.now();
        System.out.println("Start Program");

        Manager manager = new ManagerImpl(new SimpleFrechet(), new SE_Two_Rtree(), new DataImporter(), new SimplifyPossibleFrechet());
        //Manager manager = new ManagerImpl(new SimpleFrechet(), new SE_Two_Rtree(), new DataImporter());
        manager.makeStructure(TEST_DATA_SET_PATH);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getTree().size() + " data and put into data structure : "+ Duration.between(start, middle));
        
        List<TrajectoryHolder> result = manager.findResult(QUERY_PATH, de);

        for (int index = 0 ; index < result.size() ; index++){
        	result.get(index).printAllTrajectory(de, index);
        }

        Instant end = Instant.now();
        System.out.println("\nQuery Processing : "+ Duration.between(middle, end));
        System.out.println("\nProgram execution time : "+ Duration.between(start, end));

        System.out.println("\n\n\n write result? (Y/N)");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String b = in.readLine();
        if (b.contains("Y") || b.contains("y")){
            writeEvaluation(manager.getTree().size(), result.size(),
                    Duration.between(middle, end), Duration.between(start, end));
        }
        //manager.clear();
    }

    private static void writeEvaluation(int d_num, int q_num, Duration q, Duration whole) throws IOException{
        Path path = Paths.get(String.format(RESULT_PATH + "%s.txt", TAG));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
            writer.append(new Date().toString() + "\n");
            writer.append("Data number : " + d_num +"\n");
            writer.append("Query number : " + q_num + "\n");
            writer.append("Query Processing : "+ q + "\n");
            writer.append("Whole Time : " + whole +"\n\n\n");
            writer.close();
        }
    }

}
