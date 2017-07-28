package io.github.stemlab.utils;

import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by dong on 2017. 7. 28..
 */
public class StraightFoward {
    public static double Epslion = 0.5;
    public static double Constant = 0.25;

    public static Trajectory getReduced(Trajectory trajectory, Double dist) {
        if (dist == 0.0) return trajectory;
        double param = dist * Epslion * Constant;
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
        for (int i = 1 ; i < coordinates.size() ; i++){
            double distance = EuclideanDistance.distance(start, coordinates.get(i));
            if (distance >= param){
                result.addAll(reduce(coordinates.subList(i, coordinates.size()), param));
                return result;
            }
        }

        result.add(coordinates.get(coordinates.size() - 1));
        return result;
    }
}
