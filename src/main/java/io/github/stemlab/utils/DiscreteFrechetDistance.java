package io.github.stemlab.utils;

import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Trajectory;

import java.util.List;

/**
 * Created by stem_dong on 2017-07-19.
 */
public class DiscreteFrechetDistance {
    /**
     * Decision Problem using Dynamic Programming : Determine whether discrete Frechet Distance between two trajectories <= distance.
     */
    static public boolean decision(Trajectory p_trajectory, Trajectory q_trajectory, double dist) {
        if (dist < 0.0) return false;
        List<Coordinate> p_coordinates = p_trajectory.getCoordinates();
        int p_size = p_coordinates.size() - 1;

        List<Coordinate> q_coordinates = q_trajectory.getCoordinates();
        int q_size = q_coordinates.size() - 1;

        if (EuclideanDistance.distance(p_coordinates.get(p_size), q_coordinates.get(q_size)) > dist
                || EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(0)) > dist)
            return false;

        boolean[][] map = new boolean[p_size + 1][q_size + 1];
        map[0][0] = true;
        for (int i = 1; i < p_size + 1; i++) {
            map[i][0] = (map[i - 1][0] && EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(0)) <= dist);
        }
        for (int j = 1; j < q_size + 1; j++) {
            map[0][j] = (map[0][j - 1] && EuclideanDistance.distance(p_coordinates.get(0), q_coordinates.get(j)) <= dist);
        }

        for (int i = 1; i < p_size + 1; i++) {
            for (int j = 1; j < q_size + 1; j++) {
                if ((map[i][j - 1] || map[i - 1][j]) && (EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)) <= dist)) {
                    map[i][j] = true;
                } else
                    map[i][j] = false;
            }
        }
        return map[p_size][q_size];
    }

    /**
     * Distance Calculation : Discrete Frechet distance
     */
    static public Double distance(Trajectory p_trajectory, Trajectory q_trajectory) {
        List<Coordinate> p_coordinates = p_trajectory.getCoordinates();
        int p_size = p_coordinates.size();

        List<Coordinate> q_coordinates = q_trajectory.getCoordinates();
        int q_size = q_coordinates.size();

        Double[][] ca = new Double[p_size][q_size];

        for (int pi = 0; pi < p_size; pi++) {
            for (int qi = 0; qi < q_size; qi++) {
                ca[pi][qi] = -1.0;
            }
        }
        return calculate(p_size - 1, q_size - 1, ca, p_coordinates, q_coordinates);
    }


    /**
     * Calculate and put value into dynamic programming array recursively
     */
    static private Double calculate(int i, int j, Double[][] cache, List<Coordinate> p_coordinates, List<Coordinate> q_coordinates) {
        if (cache[i][j] != -1.0) return cache[i][j];
        else if (i == 0 && j == 0) {
            cache[i][j] = EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j));
        } else if (i > 0 && j == 0) {
            cache[i][j] = Math.max(calculate(i - 1, j, cache, p_coordinates, q_coordinates), EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        } else if (i == 0 && j > 0) {
            cache[i][j] = Math.max(calculate(i, j - 1, cache, p_coordinates, q_coordinates), EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        } else if (i > 0 && j > 0) {
            cache[i][j] = Math.max(Math.min(Math.min(calculate(i - 1, j, cache, p_coordinates, q_coordinates), calculate(i - 1, j - 1, cache, p_coordinates, q_coordinates)), calculate(i, j - 1, cache, p_coordinates, q_coordinates)),
                    EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        } else
            cache[i][j] = -2.0;
        return cache[i][j];
    }


}
