package goLA.utils;

import goLA.model.Coordinate;
import goLA.model.Trajectory;

import java.util.List;

/**
 * Created by stem_dong on 2017-07-19.
 */
public class DiscreteFrechetDistance {
    /**
     * Distance Calculation : Discrete Frechet distance
     */
    static public Double distance(Trajectory q_tr, Trajectory t_tr) {
        List<Coordinate> p_coordinates = q_tr.getCoordinates();
        int p = p_coordinates.size();

        List<Coordinate> q_coordinates = t_tr.getCoordinates();
        int q = q_coordinates.size();

        Double[][] ca = new Double[p][q];

        for (int pi = 0; pi < p; pi++) {
            for (int qi = 0; qi < q; qi++) {
                ca[pi][qi] = -1.0;
            }
        }
        return calc(p - 1, q - 1, ca, p_coordinates, q_coordinates);
    }

    static private Double calc(int i, int j, Double[][] ca, List<Coordinate> p_coordinates, List<Coordinate> q_coordinates) {
        if (ca[i][j] != -1.0) return ca[i][j];
        else if (i == 0 && j == 0) {
            ca[i][j] = EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j));
        } else if (i > 0 && j == 0) {
            ca[i][j] = Math.max(calc(i - 1, j, ca, p_coordinates, q_coordinates), EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        } else if (i == 0 && j > 0) {
            ca[i][j] = Math.max(calc(i, j - 1, ca, p_coordinates, q_coordinates), EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        } else if (i > 0 && j > 0) {
            ca[i][j] = Math.max(Math.min(Math.min(calc(i - 1, j, ca, p_coordinates, q_coordinates), calc(i - 1, j - 1, ca, p_coordinates, q_coordinates)), calc(i, j - 1, ca, p_coordinates, q_coordinates)),
                    EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        } else
            ca[i][j] = -2.0;
        return ca[i][j];
    }

    /**
     * Decision Problem : Determine whether discrete Frechet Distance between two trajectories <= distance.
     */
    static public boolean decisionDP(Trajectory pt, Trajectory qt, double dist) {
        if (dist < 0.0) return false;
        List<Coordinate> p_coordinates = pt.getCoordinates();
        int p = p_coordinates.size() - 1;

        List<Coordinate> q_coordinates = qt.getCoordinates();
        int q = q_coordinates.size() - 1;

        if (EuclideanDistance.distance(p_coordinates.get(p), q_coordinates.get(q)) > dist || EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(0)) > dist)
            return false;

        boolean[][] map = new boolean[p + 1][q + 1];
        map[0][0] = true;
        for (int i = 1; i < p + 1; i++) {
            map[i][0] = (map[i - 1][0] && EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(0)) <= dist);
        }
        for (int j = 1; j < q + 1; j++) {
            map[0][j] = (map[0][j - 1] && EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(j)) <= dist);
        }

        for (int i = 1; i < p + 1; i++) {
            for (int j = 1; j < q + 1; j++) {
                if ((map[i][j - 1] || map[i - 1][j]) && (EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)) <= dist)) {
                    map[i][j] = true;
                } else
                    map[i][j] = false;
            }
        }

        return map[p][q];
    }
}
