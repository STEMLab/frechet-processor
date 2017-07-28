package io.github.stemlab.io;

import io.github.stemlab.model.Query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Exporter {

    private String outputDirectory;

    public Exporter(String root_path, String tag_path) {
        File result = new File("result/");
        if (!result.exists()) result.mkdir();

        File root = new File(root_path);
        if (!root.exists()) root.mkdir();

        File dir = new File(root_path + tag_path);
        if (dir.exists())
            removeDIR(root_path + tag_path);
        if (!dir.exists())
            dir.mkdir();

        outputDirectory = root_path + tag_path;

        File info = new File(this.outputDirectory + "QueryInfo.txt");
        if (info.exists()) info.delete();
    }

    public Exporter(String outputDirectory) {
        this.outputDirectory = outputDirectory;
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

    public void export(HashSet<String> list, int number) throws IOException {

        String output = list.stream()
                .collect(Collectors.joining("\n"));

        Path path = Paths.get(String.format(this.outputDirectory + File.separator + "result-%04d.txt", number));
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(output);
        }
    }


    public void exportQuery(int index, Query q, int size1, boolean b, int size2, int size3, Instant start, Instant middle1, Instant middle2, Instant end) {
        Path path = Paths.get(this.outputDirectory + "QueryInfo.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) {
            writer.append("\n\n---- " + index + " -------\n");
            writer.append("\n\n---- Query processing : " + q.getTrajectory().getName() + ", " + q.getDistance() + " -------\n");
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
