package goLA.io;

import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DataExporter {

    private String path = "";

    public DataExporter() {
        path = "";
    }

    public DataExporter(String root_path, String tag_path) {
        //TODO : remove
        File result = new File("result/");
        if (!result.exists()) result.mkdir();

        File root = new File(root_path);
        if (!root.exists()) root.mkdir();

        File dir = new File(root_path + tag_path);
        if (dir.exists())
            removeDIR(root_path + tag_path);
        if (!dir.exists())
            dir.mkdir();

        path = root_path + tag_path;

        File info = new File(this.path + "QueryInfo.txt");
        if (info.exists()) info.delete();
    }

    private void removeDIR(String source) {
        File[] listFile = new File(source).listFiles();
        try {
            if (listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].isFile()) {
                        listFile[i].delete();
                    } else {
                        removeDIR(listFile[i].getPath());
                    }
                    listFile[i].delete();
                }
            }
        } catch (Exception e) {
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

    public void exportQuery(int index, TrajectoryQuery q, int size1, boolean b, int size2, int size3, Instant start, Instant middle1, Instant middle2, Instant end) {
        Path path = Paths.get(this.path + "QueryInfo.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
            writer.append("\n\n---- " + index + " -------\n");
            writer.append("\n\n---- Query processing : " + q.getTrajectory().getName() + ", " + q.dist + " -------\n");
            writer.append("---- candidate number : " + size1 + " -------\n");
            writer.append("---- getPossible Time : " + Duration.between(start, middle1) + "\n");
            if (b) {
                writer.append("---- Filtering Time : " + Duration.between(middle1, middle2) + "\n");
                writer.append("---- After Filtering number : " + size2 + " -------" + "\n");
            }
            writer.append("---- result number : " + size3 + " -------" + "\n");
            writer.append("---- calculate Dist Time : " + Duration.between(middle2, end) + " -------" + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
