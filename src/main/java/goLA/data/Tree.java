package goLA.data;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem_dong on 2017-05-02.
 */
public interface Tree {
    void addTrajectory(String id, Trajectory tr);

    void initialize();

    TrajectoryHolder getPossible(TrajectoryQuery query);

    ConcurrentHashMap<String, Trajectory> getHolder();

    int size();
}
