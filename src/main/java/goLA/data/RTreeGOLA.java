package goLA.data;

import goLA.model.Coordinates;
import goLA.model.TrajectoryHolder;

import java.util.List;

/**
 * Created by stem_dong on 2017-05-02.
 */
public interface RTreeGOLA {
    void create();

    void add(String id, Coordinates coord);

    List<String> search(Coordinates upper_coord, Coordinates lower_coord);

    List<String> search(Coordinates center, double dist);
}
