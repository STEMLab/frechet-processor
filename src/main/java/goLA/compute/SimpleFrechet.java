package goLA.compute;

import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.EuclideanDistance;
import goLA.utils.FrechetDistance;

import java.util.HashMap;
import java.util.List;



public class SimpleFrechet implements QueryProcessor {

    @Override
    public TrajectoryHolder query(TrajectoryQuery query, TrajectoryHolder trh) {
        TrajectoryHolder result = new TrajectoryHolder();
        if (trh.size() == 0) return result;

        HashMap<String, Trajectory> trajectories = trh.getTrajectories();

        trajectories.forEach((key, value) -> {
            if (value.isResult) result.addTrajectory(key, value);
            else if (FrechetDistance.decisionDP(query.q_tr, value, query.dist)) result.addTrajectory(key, value);
        });

        return result;
    }

}
