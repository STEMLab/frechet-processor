package io.github.stemlab.utils;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.util.HashSet;

public class DouglasSimpleDecision {

    /**
     * Decide whether trajectory is in query range.
     */
    public static boolean decisionIsInResult(Query query, double maxEpsilon, Trajectory trajectory) {
        Trajectory simple = query.getTrajectory().getSimplified();
        query.getTrajectory().setSimplified(simple);
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
            return DiscreteFrechetDistance.decisionDP(query.getTrajectory(),
                    trajectory.getSimplified(), query.getDistance() - q_max_E);
        } else
            return false;
    }

    /**
     * Decide whether trajectory is sure in out of result.
     *
     */
    public static boolean isFiltered(Trajectory simple, Trajectory trajectory, double dist, double q_max_E) {
        trajectory.setSimplified(DouglasPeucker.getReduced(trajectory, q_max_E));
        return FrechetDistance.decisionDP(simple, trajectory.getSimplified(),
                dist + q_max_E * 2);
    }

    /**
     * First, decide whether discrete Frechet Distance is lower than parameter, if not check real Frechet Distance.
     * @param q
     * @param t
     * @return
     */
    public static boolean isTrajectoryInQueryRange(Query q, Trajectory t) {
        if (DiscreteFrechetDistance.decisionDP(q.getTrajectory(), t, q.getDistance())) {
            return true;
        } else
            return FrechetDistance.decisionDP(q.getTrajectory(), t, q.getDistance());
    }

    public static void query(Query query, HashSet<Trajectory> trajectories, HashSet<String> resultSet) {
        double maxEpsilon = DouglasPeucker.getMaxEpsilon(query.getTrajectory());
        query.getTrajectory().setSimplified(DouglasPeucker.getReduced(query.getTrajectory(), maxEpsilon) );

        for (Trajectory trajectory : trajectories){
            if (DouglasSimpleDecision.decisionIsInResult(query, maxEpsilon, trajectory)){
                resultSet.add(trajectory.getName());
            }
        }
    }
}