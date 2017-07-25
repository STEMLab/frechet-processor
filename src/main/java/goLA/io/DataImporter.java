package goLA.io;

import goLA.data.Index;
import goLA.exceptions.CustomException;
import goLA.model.Coordinate;
import goLA.model.Query;
import goLA.model.Trajectory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataImporter {

    public void loadFiles(String src, Index index) {
        try (Stream<String> stream = Files.lines(Paths.get(src))) {
            stream.forEach(e -> {
                        if (!e.isEmpty() && !e.equals(null)) {
                            Trajectory trajectory = new Trajectory();
                            trajectory.setName(e);
                            trajectory.setCoordinates(getCoordinateList(e));
                            index.addTrajectory(e, trajectory);
                        }
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("Dataset not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        index.initialize();
    }

    public List<Query> getQueries(String path) {
        List<Query> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(e -> {
                        if (!e.isEmpty() && !e.equals(null)) {
                            String lines[] = e.split("\\s+");
                            if (lines.length != 2)
                                new CustomException("Query Line doesn't have two properties");

                            Trajectory q_tr = new Trajectory();
                            q_tr.setName(lines[0]);
                            q_tr.setCoordinates(getCoordinateList(lines[0]));
                            double dist = Double.parseDouble(lines[1]);

                            Query tq = new Query(q_tr, dist);

                            list.add(tq);
                        }
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("Query file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Coordinate> getCoordinateList(String s) {

        List<Coordinate> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(s)).skip(1)) {
            stream.forEach(e -> {
                        String lines[] = e.split("\\s+");
                        if (lines.length < 4) {
                            new CustomException("One of trajectory properties(x,y,k,tid) not found in file \"" + s + "\"");
                        }
                        Coordinate coordinate = new Coordinate();
                        coordinate.setPointX(Double.valueOf(lines[0]));
                        coordinate.setPointY(Double.valueOf(lines[1]));
                        coordinate.setOrder(Integer.valueOf(lines[2]));
                        coordinate.setId(Integer.valueOf(lines[3]));
                        list.add(coordinate);
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("File not found : \"" + s + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}
