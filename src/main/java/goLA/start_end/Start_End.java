package goLA.start_end;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

import java.util.ArrayList;

/**
 * Created by stem_dong on 2017-05-02.
 */
public interface Start_End {
    void addTrajectory(String id, Trajectory tr);

    TrajectoryHolder getPossible(TrajectoryQuery query, TrajectoryHolder org);
}
