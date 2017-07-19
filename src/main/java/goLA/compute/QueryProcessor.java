package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;

import java.util.List;

public interface QueryProcessor {
    List<Trajectory> query(TrajectoryQuery query, List<Trajectory> trh);
}
