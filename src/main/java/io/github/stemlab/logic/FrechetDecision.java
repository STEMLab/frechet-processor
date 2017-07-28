package io.github.stemlab.logic;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

/**
 * Created by dong on 2017. 7. 28..
 */
public class FrechetDecision {
    public static boolean isResult(Query query, Trajectory trajectory) {
        Trajectory simplifiedQuery = query.getTrajectory().getSimplified();
        if (isAbsoluteResult(simplifiedQuery, trajectory, query.getDistance())) { // Decide whether trajectory is sure in result by using simplification.
            return true;
        } else if (isTrajectoryInQueryRange(query, trajectory)) { // Decide whether frechet distance is lower than query distance.
            return true;
        }
        return false;
    }


    public static boolean isAbsoluteResult(Trajectory simpleQuery, Trajectory trajectory, double distance) {
        double modifiedDistance = distance - (distance * StraightForwardSimplification.EPSILON * StraightForwardSimplification.CONSTANT);
        if (DiscreteFrechet.decision(simpleQuery, trajectory, modifiedDistance)) {
            return true;
        } else {
            return RealFrechet.decision(simpleQuery, trajectory, modifiedDistance);
        }
    }

    /**
     * First, decide whether discrete Frechet Distance is lower than parameter, if not check real Frechet Distance.
     *
     * @param query
     * @param trajectory
     */
    public static boolean isTrajectoryInQueryRange(Query query, Trajectory trajectory) {
        if (DiscreteFrechet.decision(query.getTrajectory(), trajectory, query.getDistance())) {
            return true;
        } else
            return RealFrechet.decision(query.getTrajectory(), trajectory, query.getDistance());
    }

}
