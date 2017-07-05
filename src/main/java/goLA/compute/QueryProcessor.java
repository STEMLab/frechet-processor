package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

public interface QueryProcessor {
    TrajectoryHolder query(TrajectoryQuery query, TrajectoryHolder trh);
}
