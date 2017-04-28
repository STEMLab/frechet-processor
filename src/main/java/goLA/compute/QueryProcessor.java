package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

public interface QueryProcessor {
	public TrajectoryHolder findTrajectoriesFrom(TrajectoryQuery query, TrajectoryHolder trh);
	
}
