package goLA.utils;

import goLA.model.Coordinates;

/**
 * Created by Azamat on 7/4/2017.
 */
public class ManhattanDistance {
    public static Double getSignedDistance(Coordinates coordinates, Coordinates criterion) {
        double x_dist = coordinates.getPointX() - criterion.getPointX();
        double y_dist = coordinates.getPointY() - criterion.getPointY();
        return x_dist + y_dist;
    }
}
