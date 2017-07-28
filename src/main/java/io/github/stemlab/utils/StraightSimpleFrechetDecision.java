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
        query.getTrajectory().setSimplified(StraightFowrad.getReduced(query.getTrajectory(), dist) );
        for (Trajectory trajectory : trajectories){
            if (StraightSimpleFrechetDecision.decisionIsInResult(query, trajectory)){
                resultSet.add(trajectory.getName());
            }
        }
    }

    private static boolean decisionIsInResult(Query query, Trajectory trajectory){
        Trajectory simplified_query = query.getTrajectory().getSimplified();
        Trajectory simplified_trajectory = StraightFowrad.getReduced(trajectory, query.getDistance());
        if (isFiltered(simplified_query, simplified_trajectory, query.getDistance())) {
            if (isResult(simplified_query, simplified_trajectory, query.getDistance())) {
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
    public static boolean isResult(Trajectory simple_query, Trajectory simple_trajectory, double dist) {
      if(DiscreteFrechetDistance.decisionDP(simple_query,
                    simple_trajectory, dist)){
          return true;
      }
      else{
          return FrechetDistance.decisionDP(simple_query, simple_trajectory, dist);
      }

    }

    /**
     * Decide whether simple_trajectory is sure in out of result.
     *
     */
    public static boolean isFiltered(Trajectory simple_query, Trajectory simple_trajectory, double dist) {
        double modified_dist = dist + 2 * dist * StraightFowrad.Epslion * StraightFowrad.Constant;
        return FrechetDistance.decisionDP(simple_query, simple_trajectory,
                modified_dist);
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

}
