package io.github.stemlab.utils;

import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Trajectory;

import java.util.ArrayList;
import java.util.List;

public class StraightForward {

    public static double EPSILON = 0.5;
    public static double CONSTANT = 0.25;

    public static Trajectory getReduced(Trajectory trajectory, Double distance) {
        if (distance == 0.0) return trajectory;
        double param = distance * EPSILON * CONSTANT;
        Trajectory ret = new Trajectory();
        List<Coordinate> coordinates = reduce(trajectory.getCoordinates(), param);
        ret.setCoordinates(coordinates);
        return ret;
    }

    private static List<Coordinate> reduce(List<Coordinate> coordinates, Double param) {
        List<Coordinate> result = new ArrayList<>();
        result.add(coordinates.get(0));
        Coordinate start = coordinates.get(0);

        if (coordinates.size() == 1) return result;
        for (int i = 1; i < coordinates.size(); i++) {
            double distance = EuclideanDistance.distance(start, coordinates.get(i));
            if (distance >= param) {
                result.addAll(reduce(coordinates.subList(i, coordinates.size()), param));
                return result;
            }
        }

        result.add(coordinates.get(coordinates.size() - 1));
        return result;
    }
}
