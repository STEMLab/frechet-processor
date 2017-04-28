package goLA.io;

import goLA.exceptions.CustomException;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DataImporter {

    public void loadFiles(String src, TrajectoryHolder trajectoryHolder) {

        try (Stream<String> stream = Files.lines(Paths.get(src))) {
            stream.forEach(e -> {
                        Trajectory trajectory = new Trajectory();
                        trajectory.setCoordinates(getCoordList(e));
                        trajectoryHolder.addTrajectory(e, trajectory);
                    }
            );

        }catch (NoSuchFileException e){
            new CustomException("Dataset not found");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Coordinates<Double, Double>> getCoordList(String s) {

        //TODO: remove in future
        String temp = "files/" + s;
        List<Coordinates<Double, Double>> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(temp)).skip(1)) {
            AtomicInteger index = new AtomicInteger(0);
            stream.forEach(e -> {
                        String lines[] = e.split("\\s+");
                        if (lines.length < 4) new CustomException("One of trajectory properties(x,y,k,tid) not found in file \"" + temp + "\"");
                        Coordinates<Double, Double> coordinates = new Coordinates<>();
                        coordinates.setPointX(Double.valueOf(lines[0]));
                        coordinates.setPointY(Double.valueOf(lines[1]));
                        coordinates.setOrder(Integer.valueOf(lines[2]));
                        coordinates.setId(Integer.valueOf(lines[3]));
                        list.add(coordinates);
                    }
            );

        }
        catch (NoSuchFileException e){
            new CustomException("File not found : \""+temp+"\"");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}