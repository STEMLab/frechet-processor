package io.github.stemlab.utils;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.DiscreteFrechetDistance;
import io.github.stemlab.utils.DouglasPeucker;
import io.github.stemlab.utils.FrechetDistance;

public class SimplificationFrechetDecision {

    /**
     * Decide whether trajectory is in query range.
     */
    public static boolean decisionIsInResult(Query query, double q_dist, double maxEpsilon, Trajectory trajectory) {
        Trajectory simple = query.getTrajectory().getSimplified();
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
     * Decide whether trajectory is definetly
     */
    public static boolean isResult(Query q, Trajectory tr, double q_max_E) {
        if (q.getDistance() - q_max_E >= 0) {
            return DiscreteFrechetDistance.decisionDP(q.getTrajectory(),
                    tr.getSimplified(), q.getDistance() - q_max_E);
        } else
            return false;
    }

    public static boolean isFiltered(Trajectory simple, Trajectory tr, double dist, double q_max_E) {
        return FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(tr, q_max_E),
                dist + q_max_E * 2);
    }

    public static boolean isTrajectoryInQueryRange(Query q, Trajectory t) {
        if (DiscreteFrechetDistance.decisionDP(q.getTrajectory(), t, q.getDistance())) {
            return true;
        } else
            return FrechetDistance.decisionDP(q.getTrajectory(), t, q.getDistance());
    }
}