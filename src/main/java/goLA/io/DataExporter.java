package goLA.io;

import goLA.model.Trajectory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class DataExporter {

    public void export(HashMap<String, Trajectory> map, int number) throws IOException {
        StringBuffer sbf = new StringBuffer();
        for (String key : map.keySet()){
            sbf.append(key);
        }

        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("result-" + number +".txt")));

        //write contents of StringBuffer to a file
        bwr.write(sbf.toString());

        //flush the stream
        bwr.flush();

        //close the stream
        bwr.close();

        System.out.println("Content of StringBuffer written to File.");
    }
}
