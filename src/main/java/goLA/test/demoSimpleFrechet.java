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
import goLA.io.*;
import goLA.manage.*;
import goLA.model.TrajectoryHolder;

public class demoSimpleFrechet {

    public static void main(String[] args) throws IOException {

        Instant start = Instant.now();
        System.out.println("Start Program");
        
        String src_path = "dataset.txt";
        String query_path = "queries.txt";

        Manager manager = new ManagerImpl(new SimpleFrechet(), new SE_Two_Rtree(), new DataImporter());


        manager.makeStructure(src_path);

        //get all data trajectories
        Instant middle = Instant.now();
        System.out.println("\nGet " + manager.getTree().size() + " data and put into data structure : "+ Duration.between(start, middle));
        
        List<TrajectoryHolder> result = manager.findResult(query_path);

        DataExporter de = new DataExporter("result/QueryResult/");

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
            writeEvaluation("v0.1", manager.getTree().size(), result.size(),
                    Duration.between(middle, end), Duration.between(start, end));
        }
    }

    private static void writeEvaluation(String branch, int d_num, int q_num, Duration q, Duration whole) throws IOException{
        Path path = Paths.get(String.format("result/" + "%s.txt", branch));
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
