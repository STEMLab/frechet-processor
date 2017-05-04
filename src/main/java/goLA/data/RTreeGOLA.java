package goLA.data;

import goLA.model.Coordinates;
import goLA.model.TrajectoryHolder;

/**
 * Created by stem_dong on 2017-05-02.
 */
public interface RTreeGOLA {
    void create();

    void insert(Coordinates coord, String id);

    TrajectoryHolder search(Coordinates upper_left, Coordinates lower_right);

}
