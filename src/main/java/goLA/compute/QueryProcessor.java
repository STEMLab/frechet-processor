package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

public interface QueryProcessor {
    TrajectoryHolder query(TrajectoryQuery query, TrajectoryHolder trh);
    boolean decideIn_FDist(Trajectory q_tr, Trajectory t_tr, double dist);
}
