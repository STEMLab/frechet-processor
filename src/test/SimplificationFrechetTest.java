package test;

import goLA.data.StartRTree;
import goLA.data.Tree;
import goLA.exceptions.CustomException;
import goLA.model.Coordinate;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.DouglasPeucker;
import goLA.utils.EuclideanDistance;
import goLA.utils.FrechetDistance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

//TODO : delete class

/**
 * Created by stem-dong-li on 17. 7. 6.
 */
public class SimplificationFrechetTest {
    private static double MAX_RAN = 40000;
    private static double MIN_RAN = 5000;

    public static void main(String[] args) {
        Tree tree = new StartRTree();
        try (Stream<String> stream = Files.lines(Paths.get("dataset.txt"))) {
            stream.forEach(e -> {
                        if (!e.isEmpty() && e != null) {
                            Trajectory trajectory = new Trajectory();
                            trajectory.setCoordinates(getCoordList(e));
                            trajectory.setName(e);
                            //trajectoryHolder.addTrajectory(e, trajectory);
                            tree.addTrajectory(e, trajectory);
                        }
                    }
            );

        } catch (NoSuchFileException e) {
            new CustomException("Dataset not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tree.initialize();
        Object[] obj_list = tree.getHolder().values().toArray();
        for (int i = 0; i < 300; i++) {

            int index = (int) (Math.random() * (tree.size() - 1));
            double q_dist = MIN_RAN + (Math.random() * MAX_RAN);
            Trajectory q = (Trajectory) obj_list[index];
            System.out.println("--- " + i + " : " + index + " ---");
            double q_maxEpsilon = DouglasPeucker.getMaxEpsilon(q);
            Trajectory simple = DouglasPeucker.getReduced(q, q_maxEpsilon);

            TrajectoryQuery query = new TrajectoryQuery(q, q_dist);
            TrajectoryHolder ret = tree.getPossible(query);
            Map<String, Trajectory> trmap = ret.getTrajectories();

            Coordinate q_start = q.getCoordinates().get(0);
            Coordinate q_end = q.getCoordinates().get(q.getCoordinates().size() - 1);

            trmap.entrySet().forEach(e -> {
                Trajectory C = e.getValue();
                Coordinate c_start = C.getCoordinates().get(0);

                Coordinate c_end = C.getCoordinates().get(C.getCoordinates().size() - 1);
                if (EuclideanDistance.distance(c_start, q_start) <= q_dist) {
                    if (EuclideanDistance.distance(c_end, q_end) <= q_dist) {
                        if (!FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(C, q_maxEpsilon),
                                q_dist + q_maxEpsilon * 2)) {
                            if (!FrechetDistance.decisionDP(q, C, q_dist)) {

                            } else {
                                System.out.println("wrong");
                            }
                        }
                        double eps;

                        if (FrechetDistance.decisionDP(simple,
                                DouglasPeucker.getReduced(C, q_maxEpsilon),
                                q_dist - q_maxEpsilon * 2)) {
                            if (FrechetDistance.decisionDP(q, C, q_dist)) {

                            } else {
                                System.out.println("is Result wrong");
//                                List<Trajectory> trajectories1 = new ArrayList<>();
//                                trajectories1.add(q);
//                                trajectories1.add(C);
//                                trajectories1.add(simple);
//                                trajectories1.add(DouglasPeucker.getReduced(C, q_maxEpsilon));
//                                FrechetDistanceDemo2D.vis(trajectories1);
                            }
                        }

                    }
                }

            });

        }
    }

    private static List<Coordinate> getCoordList(String s) {

        //TODO: remove in future
        String temp = s;
        List<Coordinate> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(temp)).skip(1)) {
            AtomicInteger index = new AtomicInteger(0);
            stream.forEach(e -> {
                        String lines[] = e.split("\\s+");
                        if (lines.length < 4) {
                            new CustomException("One of trajectory properties(x,y,k,tid) not found in file \"" + temp + "\"");
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
            new CustomException("File not found : \"" + temp + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
