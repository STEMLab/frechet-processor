package io.github.stemlab.utils;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

/**
 * Created by dong on 2017. 7. 28..
 */
public class StraightSimpleFrechetDecision {
    public static boolean decisionIsInResult(Query query, Trajectory trajectory) {
        Trajectory simplified_query = StraightForward.getReduced(query.getTrajectory(), query.getDistance());
        Trajectory simplified_trajectory = StraightForward.getReduced(trajectory, query.getDistance());
        if (isFiltered(simplified_query, simplified_trajectory, query.getDistance())) { // Decide whether simple_trajectory is sure in out of result.
            if (isResult(simplified_query, trajectory, query.getDistance())) { // Decide whether trajectory is sure in result by using simplification.
                return true;
            } else if (isTrajectoryInQueryRange(query, trajectory)) { // Decide whether frechet distance is lower than query distance.
                return true;
            }
        }
        return false;
    }

    public static boolean isResult(Trajectory simple_query, Trajectory trajectory, double dist) {
        double modified_dist_2 = dist - 1 * dist * StraightForward.Epsilon * StraightForward.Constant;
        if (DiscreteFrechetDistance.decision(simple_query, trajectory, modified_dist_2)) {
            return true;
        } else {
            return FrechetDistance.decision(simple_query, trajectory, modified_dist_2);
        }
    }

    public static boolean isFiltered(Trajectory simple_query, Trajectory simple_trajectory, double dist) {
        double modified_dist = dist + 2 * dist * StraightForward.Epsilon * StraightForward.Constant;
        return FrechetDistance.decision(simple_query, simple_trajectory, modified_dist);
    }

    /**
     * First, decide whether discrete Frechet Distance is lower than parameter, if not check real Frechet Distance.
     * @param query
     * @param trajectory
     */
    public static boolean isTrajectoryInQueryRange(Query query, Trajectory trajectory) {
        if (DiscreteFrechetDistance.decision(query.getTrajectory(), trajectory, query.getDistance())) {
            return true;
        } else
            return FrechetDistance.decision(query.getTrajectory(), trajectory, query.getDistance());
    }

}
