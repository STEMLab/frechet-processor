package goLA.io;

import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataImporterImpl implements DataImporter {

    @Override
    public void loadFiles(String src, TrajectoryHolder trajectoryHolder) {

        try (Stream<String> stream = Files.lines(Paths.get(src))) {
            stream.forEach(e -> {
                        Trajectory trajectory = new Trajectory();
                        trajectory.setCoordinates(getCoordList(e));
                        trajectoryHolder.addTrajectory(e, trajectory);
                    }
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Coordinates<Double, Double>> getCoordList(String s) {

        //TODO: remove in future
        String temp = "files/" + s;
        List<Coordinates<Double, Double>> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(temp)).skip(1)) {
            stream.forEach(e -> {
                        String lines[] = e.split("\\s+");
                        Coordinates<Double, Double> coordinates = new Coordinates<>();
                        coordinates.setPointX(Double.valueOf(lines[0]));
                        coordinates.setPointY(Double.valueOf(lines[1]));
                        coordinates.setOrder(Integer.valueOf(lines[2]));
                        coordinates.setId(Integer.valueOf(lines[3]));
                        list.add(coordinates);
                    }
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}
