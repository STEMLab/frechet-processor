package io.github.stemlab.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Exporter {

    private String outputDirectory;
    public Exporter(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void export(HashSet<String> list, int number) throws IOException {

        String output = list.stream()
                .collect(Collectors.joining("\n"));

        Path path = Paths.get(String.format(this.outputDirectory + File.separator + "result-%04d.txt", number));
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(output);
        }
    }
}
