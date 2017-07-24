package goLA.io;

import goLA.model.Trajectory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DataExporter {

    public void export(List<Trajectory> list, int number) throws IOException {

        String output = list.stream().map(entry ->
                entry.getName())
                .collect(Collectors.joining("\n"));

        Path path = Paths.get(String.format("result/result-%04d.txt", number));
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(output);
        }
    }
}
