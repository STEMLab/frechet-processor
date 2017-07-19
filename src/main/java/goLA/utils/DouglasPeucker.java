package goLA.utils;

import goLA.model.Coordinate;
import goLA.model.Trajectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azamat on 7/4/2017.
 * Implemented from pseudo code on https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm
 */
public class DouglasPeucker {
    private static List<Coordinate> reduce(List<Coordinate> coordinates, Double epsilon) {

        double maxDistance = 0.0;
        int index = 0;
        for (int i = 0; i < coordinates.size() - 1; i++) {
            double distance = EuclideanDistance.pointAndLine(coordinates.get(i), coordinates.get(0), coordinates.get(coordinates.size() - 1));
            if (distance > maxDistance) {
                index = i;
                maxDistance = distance;
            }
        }

        List<Coordinate> result = new ArrayList<>();
        if (maxDistance > epsilon) {
            List<Coordinate> firstRecursiveResult = reduce(coordinates.subList(0, index + 1), epsilon);
            List<Coordinate> secondRecursiveResult = reduce(coordinates.subList(index, coordinates.size()), epsilon);

            result.addAll(firstRecursiveResult);
            result.addAll(secondRecursiveResult.subList(1, secondRecursiveResult.size()));
        } else {
            result.add(coordinates.get(0));
            result.add(coordinates.get(coordinates.size() - 1));
        }
        return result;
    }

    public static Trajectory getReducedMax(Trajectory trajectory){
        if (trajectory.simple != null && trajectory.Simplemode == 1) return trajectory.simple;
        Trajectory ret = getReduced(trajectory, getMaxEpsilon(trajectory));
        trajectory.simple = ret;
        trajectory.Simplemode = 1;
        return ret;
    }

    public static Trajectory getReducedAvg(Trajectory trajectory){
        if (trajectory.simple != null && trajectory.Simplemode == 2) return trajectory.simple;
        Trajectory ret = getReduced(trajectory, getAvgEpsilon(trajectory));
        trajectory.simple = ret;
        trajectory.Simplemode = 2;
        return ret;
    }


    public static Trajectory getReduced(Trajectory trajectory, Double epsilon) {
        if (epsilon == 0.0) return trajectory;
        Trajectory ret = new Trajectory();
        List<Coordinate> coordinates = reduce(trajectory.getCoordinates(), epsilon);
        ret.setCoordinates(coordinates);
        return ret;
    }

    public static double getMaxEpsilon(Trajectory trajectory) {
        if (trajectory.MaxEpsilon != null) return trajectory.MaxEpsilon;
        trajectory.MaxEpsilon = max(deviations(trajectory.getCoordinates()));
        return trajectory.MaxEpsilon;
    }

    public static double getAvgEpsilon(Trajectory trajectory) {
        if (trajectory.AvgEpsilon != null) return trajectory.AvgEpsilon;
        trajectory.AvgEpsilon = avg(deviations(trajectory.getCoordinates()));
        return trajectory.AvgEpsilon;
    }

    private static double[] deviations(List<Coordinate> coordinates) {
        double[] deviations = new double[Math.max(0, coordinates.size() - 2)];
        for (int i = 2; i < coordinates.size(); i++) {
            Coordinate lineStart = coordinates.get(i - 2);
            Coordinate point = coordinates.get(i - 1);
            Coordinate lineEnd = coordinates.get(i);
            double dev = EuclideanDistance.pointAndLine(point, lineStart, lineEnd);
            deviations[i - 2] = dev;
        }
        return deviations;
    }

    private static double avg(double[] values) {
        if (values.length > 0) {
            return sum(values) / values.length;
        } else {
            return 0.0;
        }
    }

    public static double max(double[] values) {
        double max = 0.0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }

    private static double sum(double[] values) {
        double sum = 0.0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

}
