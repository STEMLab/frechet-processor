package io.github.stemlab.utils;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.util.HashSet;

/**
 * Created by dong on 2017. 7. 28..
 */
public class StraightSimpleFrechetDecision {
    public static void query(Query query, HashSet<Trajectory> trajectories, HashSet<String> resultSet) {
        double dist = query.getDistance();
        query.getTrajectory().setSimplified(StraightFoward.getReduced(query.getTrajectory(), dist) );
        for (Trajectory trajectory : trajectories){
            if (StraightSimpleFrechetDecision.decisionIsInResult(query, trajectory)){
                resultSet.add(trajectory.getName());
            }
        }
    }

    public static boolean decisionIsInResult(Query query, Trajectory trajectory){
        Trajectory simplified_query = query.getTrajectory().getSimplified();
        Trajectory simplified_trajectory = StraightFoward.getReduced(trajectory, query.getDistance());
        trajectory.setSimplified(simplified_trajectory);
        if (isFiltered(simplified_query, simplified_trajectory, query.getDistance())) {
            if (isResult(simplified_query, trajectory, query.getDistance())) {
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
    public static boolean isResult(Trajectory simple_query, Trajectory trajectory, double dist) {
        double modified_dist_2 = dist - 1 * dist * StraightFoward.Epslion * StraightFoward.Constant;
        if(DiscreteFrechetDistance.decision(simple_query, trajectory, modified_dist_2)){
            return true;
        }
        else{
            //return false;
            return FrechetDistance.decision(simple_query, trajectory, modified_dist_2);
        }
    }

    /**
     * Decide whether simple_trajectory is sure in out of result.
     *
     */
    public static boolean isFiltered(Trajectory simple_query, Trajectory simple_trajectory, double dist) {
        double modified_dist = dist + 2 * dist * StraightFoward.Epslion * StraightFoward.Constant;
        return FrechetDistance.decision(simple_query, simple_trajectory, modified_dist);
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
