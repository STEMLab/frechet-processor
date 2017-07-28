package io.github.stemlab.utils;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

public class SimplificationFrechetDecision {

    /**
     * Decide whether trajectory is in query range.
     */
    public static boolean decisionIsInResult(Query query, double maxEpsilon, Trajectory trajectory) {
        Trajectory simple = query.getTrajectory().getSimplified();
        double q_dist = query.getDistance();
        if (isFiltered(simple, trajectory, q_dist, maxEpsilon)) {
            if (isResult(query, trajectory, maxEpsilon)) {
                return true;
            } else if (isTrajectoryInQueryRange(query, trajectory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Decide whether trajectory is sure in result by using simplification.
     */
    public static boolean isResult(Query query, Trajectory trajectory, double q_max_E) {
        if (query.getDistance() - q_max_E >= 0) {
            return DiscreteFrechetDistance.decision(query.getTrajectory(),
                    trajectory.getSimplified(), query.getDistance() - q_max_E);
        } else
            return false;
    }

    /**
     * Decide whether trajectory is sure in out of result.
     * @param maxEpsilon : In simplification, epsilon value.
     */
    public static boolean isFiltered(Trajectory simple, Trajectory trajectory, double dist, double maxEpsilon) {
        return FrechetDistance.decision(simple, DouglasPeucker.getReduced(trajectory, maxEpsilon),
                dist + maxEpsilon * 2);
    }

    /**
     * First, decide whether discrete Frechet Distance is lower than parameter, if not check real Frechet Distance.
     * @param query
     * @param trajectory
     * @return
     */
    public static boolean isTrajectoryInQueryRange(Query query, Trajectory trajectory) {
        if (DiscreteFrechetDistance.decision(query.getTrajectory(), trajectory, query.getDistance())) {
            return true;
        } else
            return FrechetDistance.decision(query.getTrajectory(), trajectory, query.getDistance());
    }
}