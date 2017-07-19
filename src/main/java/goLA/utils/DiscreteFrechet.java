package goLA.utils;

import goLA.model.Coordinate;
import goLA.model.Trajectory;

import java.util.List;

/**
 * Created by stem_dong on 2017-07-19.
 */
public class DiscreteFrechet {
    Double[][] ca;
    List<Coordinate> p_coordinates;
    List<Coordinate> q_coordinates;
    public Double distance(Trajectory q_tr, Trajectory t_tr){
        p_coordinates = q_tr.getCoordinates();
        int p = p_coordinates.size();

        q_coordinates = t_tr.getCoordinates();
        int q = q_coordinates.size();

        ca = new Double[p][q];
        for (int pi = 0 ; pi < p ; pi++){
            for (int qi = 0 ; qi < q; qi++){
                ca[pi][qi] = -1.0;
            }
        }

        return calc(p-1,q-1);
    }

    private Double calc(int i, int j){
        if (ca[i][j] != -1.0) return ca[i][j];
        else if (i == 0 && j ==0){
            ca[i][j] = EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j));
        }
        else if (i > 0 && j == 0){
            ca[i][j] = Math.max(calc(i-1, j), EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        }
        else if (i == 0 && j > 0){
            ca[i][j] = Math.max(calc(i, j-1), EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)));
        }
        else if (i > 0 && j > 0){
            ca[i][j] = Math.max(Math.min(Math.min(calc(i-1,j), calc(i-1, j-1)), calc(i, j-1)), EuclideanDistance.distance(p_coordinates.get(i), q_coordinates.get(j)) );
        }
        else
            ca[i][j] = -2.0;
        return ca[i][j];
    }
}
