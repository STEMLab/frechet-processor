package io.github.stemlab.utils;

import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Trajectory;

import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 5.
 */
public class FrechetDistance {
    /**
     * Decision Problem : Determine whether Frechet Distance between two trajectories <= distance.
     */
    static public boolean decision(Trajectory trajectory, Trajectory query, double dist) {
        if (dist < 0) return false;
        int p, q;

        List<Coordinate> p_coordinates = trajectory.getCoordinates();
        p = p_coordinates.size() - 1;

        List<Coordinate> q_coordinates = query.getCoordinates();
        q = q_coordinates.size() - 1;

        if (EuclideanDistance.distance(p_coordinates.get(p), q_coordinates.get(q)) > dist || EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(0)) > dist)
            return false;

        //array for white space
        boolean[][] bottom = new boolean[p + 1][q + 1];
        boolean[][] left = new boolean[p + 1][q + 1];

        for (int i = 0; i < p; i++) {
            bottom[i][0] = (dist >= EuclideanDistance.pointAndLine(q_coordinates.get(0), p_coordinates.get(i), p_coordinates.get(i + 1)));
        }

        for (int j = 0; j < q; j++) {
            left[0][j] = (dist >= EuclideanDistance.pointAndLine(p_coordinates.get(0), q_coordinates.get(j), q_coordinates.get(j + 1)));
        }

        for (int i = 0; i < p; i++) {
            boolean sw = false;
            for (int j = 0; j < q; j++) {
                if (!left[i][j] && !bottom[i][j]) {
                    left[i + 1][j] = false;
                    bottom[i][j + 1] = false;
                } else {
                    sw = true;
                    bottom[i][j + 1] = (dist >= EuclideanDistance.pointAndLine(q_coordinates.get(j + 1), p_coordinates.get(i), p_coordinates.get(i + 1)));
                    left[i + 1][j] = (dist >= EuclideanDistance.pointAndLine(p_coordinates.get(i + 1), q_coordinates.get(j), q_coordinates.get(j + 1)));
                }
            }
            if (!sw) return false;
        }
        if (left[p][q - 1] && bottom[p - 1][q]) {
            if (EuclideanDistance.distance(p_coordinates.get(p), q_coordinates.get(q)) <= dist && EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(0)) <= dist)
                return true;
        }
        return false;
    }

}
