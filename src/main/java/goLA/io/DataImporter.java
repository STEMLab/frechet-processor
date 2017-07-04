package goLA.io;

import goLA.data.Tree;
import goLA.exceptions.CustomException;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DataImporter {

    public void loadFiles(String src, Tree tree) {

        try (Stream<String> stream = Files.lines(Paths.get(src))) {
            stream.forEach(e -> {
                        Trajectory trajectory = new Trajectory();
                        trajectory.setCoordinates(getCoordList(e));
                        trajectory.setName(e);
                        //trajectoryHolder.addTrajectory(e, trajectory);
                        tree.addTrajectory(e, trajectory);
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("Dataset not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tree.initialize();
    }

    public HashSet<Trajectory> loadFilesToVisualize(String src) {
        HashSet<Trajectory> trajectoryHashMap = new LinkedHashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(src))) {
            stream.forEach(e -> {
                        Trajectory trajectory = new Trajectory();
                        trajectory.setCoordinates(getCoordList(e));
                        trajectory.setName(e);
                        trajectoryHashMap.add(trajectory);
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("Dataset not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trajectoryHashMap;
    }

    public List<TrajectoryQuery> getQueries(String path) {
        List<TrajectoryQuery> list = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(e -> {
                        String lines[] = e.split("\\s+");
                        if (lines.length != 2)
                            new CustomException("Query Line doesn't have two properties");

                        Trajectory q_tr = new Trajectory();
                        q_tr.setName(lines[0]);
                        q_tr.setCoordinates(getCoordList(lines[0]));
                        double dist = Double.parseDouble(lines[1]);

                        TrajectoryQuery tq = new TrajectoryQuery(q_tr, dist);

                        list.add(tq);
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("query file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Coordinates> getCoordList(String s) {

        //TODO: remove in future
        String temp = s;
        List<Coordinates> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(temp)).skip(1)) {
            AtomicInteger index = new AtomicInteger(0);
            stream.forEach(e -> {
                        String lines[] = e.split("\\s+");
                        if (lines.length < 4)
                            new CustomException("One of trajectory properties(x,y,k,tid) not found in file \"" + temp + "\"");
                        Coordinates coordinates = new Coordinates();
                        coordinates.setPointX(Double.valueOf(lines[0]));
                        coordinates.setPointY(Double.valueOf(lines[1]));
                        coordinates.setOrder(Integer.valueOf(lines[2]));
                        coordinates.setId(Integer.valueOf(lines[3]));
                        list.add(coordinates);
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("File not found : \"" + temp + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}
