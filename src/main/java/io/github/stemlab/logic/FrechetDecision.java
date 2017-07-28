package io.github.stemlab.logic;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

public class FrechetDecision {
    public static boolean isInResult(Query query, Trajectory trajectory) {
        Trajectory simplifiedQuery = StraightForwardSimplification.getReduced(query.getTrajectory(), query.getDistance());
        Trajectory simplifiedTrajectory = StraightForwardSimplification.getReduced(trajectory, query.getDistance());
        if (isFiltered(simplifiedQuery, simplifiedTrajectory, query.getDistance())) { // Decide whether simple_trajectory is sure in out of result.
            if (isResult(simplifiedQuery, trajectory, query.getDistance())) { // Decide whether trajectory is sure in result by using simplification.
                return true;
            } else if (isTrajectoryInQueryRange(query, trajectory)) { // Decide whether frechet distance is lower than query distance.
                return true;
            }
        }
        return false;
    }


    public static boolean isResult(Trajectory simpleQuery, Trajectory trajectory, double distance) {
        double modifiedDistance = distance - (1 * distance * StraightForwardSimplification.EPSILON * StraightForwardSimplification.CONSTANT);
        if (DiscreteFrechet.decision(simpleQuery, trajectory, modifiedDistance)) {
            return true;
        } else {
            return RealFrechet.decision(simpleQuery, trajectory, modifiedDistance);
        }
    }

    public static boolean isFiltered(Trajectory simpleQuery, Trajectory simpleTrajectory, double distance) {
        double modifiedDistance = distance + (2 * distance * StraightForwardSimplification.EPSILON * StraightForwardSimplification.CONSTANT);
        return RealFrechet.decision(simpleQuery, simpleTrajectory, modifiedDistance);
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
