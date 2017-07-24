package goLA.utils;

import goLA.model.Coordinate;
import goLA.model.Trajectory;

import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 5.
 */
public class FrechetDistance {
    /**
     * Decision Problem : Determine whether Frechet Distance between two trajectories <= distance.
     */
    static public boolean decisionDP(Trajectory q_tr, Trajectory t_tr, double dist) {
        if (dist < 0) return false;
        int p, q;

        List<Coordinate> p_coordinates = q_tr.getCoordinates();
        p = p_coordinates.size() - 1;

        List<Coordinate> q_coordinates = t_tr.getCoordinates();
        q = q_coordinates.size() - 1;

        if (EuclideanDistance.distance(p_coordinates.get(p), q_coordinates.get(q)) > dist || EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(0)) > dist)
            return false;

        boolean[][] result_bottom = new boolean[p + 1][q + 1];
        boolean[][] result_left = new boolean[p + 1][q + 1];

        for (int i = 0; i < p; i++) {
            result_bottom[i][0] = (dist >= EuclideanDistance.pointAndLine(q_coordinates.get(0), p_coordinates.get(i), p_coordinates.get(i + 1)));
        }

        for (int j = 0; j < q; j++) {
            result_left[0][j] = (dist >= EuclideanDistance.pointAndLine(p_coordinates.get(0), q_coordinates.get(j), q_coordinates.get(j + 1)));
        }

        for (int i = 0; i < p; i++) {
            boolean sw = false;
            for (int j = 0; j < q; j++) {
                if (!result_left[i][j] && !result_bottom[i][j]) {
                    result_left[i + 1][j] = false;
                    result_bottom[i][j + 1] = false;
                } else {
                    sw = true;
                    result_bottom[i][j + 1] = (dist >= EuclideanDistance.pointAndLine(q_coordinates.get(j + 1), p_coordinates.get(i), p_coordinates.get(i + 1)));
                    result_left[i + 1][j] = (dist >= EuclideanDistance.pointAndLine(p_coordinates.get(i + 1), q_coordinates.get(j), q_coordinates.get(j + 1)));
                }
            }
            if (!sw) return false;
        }
        if (result_left[p][q - 1] && result_bottom[p - 1][q]) {
            if (EuclideanDistance.distance(p_coordinates.get(p), q_coordinates.get(q)) <= dist && EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(0)) <= dist)
                return true;
        }
        return false;
    }

}
