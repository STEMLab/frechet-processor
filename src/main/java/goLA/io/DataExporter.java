package goLA.io;

import goLA.model.Trajectory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DataExporter {

    private String path ="";

    public DataExporter(){
        path = "";
    }

    public DataExporter(String ppath){
        File dir = new File(ppath);
        removeDIR(ppath);
        if (!dir.exists()){
            dir.mkdir();
        }
        path = ppath;
    }

    private void removeDIR(String source){
        File[] listFile = new File(source).listFiles();
        try{
            if(listFile.length > 0){
                for(int i = 0 ; i < listFile.length ; i++){
                    if(listFile[i].isFile()){
                        listFile[i].delete();
                    }else{
                        removeDIR(listFile[i].getPath());
                    }
                    listFile[i].delete();
                }
            }
        }catch(Exception e){
            System.err.println(System.err);
            System.exit(-1);
        }
    }

    public void export(HashMap<String, Trajectory> map, int number) throws IOException {

        String output = map.entrySet().stream().map(entry ->
                entry.getKey())
                .collect(Collectors.joining("\n"));

        Path path = Paths.get(String.format(this.path + "result-%04d.txt", number));
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(output);
        }
       // System.out.println("Content of StringBuffer written to File.");
    }

}
