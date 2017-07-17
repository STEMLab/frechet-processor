package goLA.filter;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.DouglasPeucker;
import goLA.utils.FrechetDistance;

import java.util.HashMap;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public class SimplificationFrechet implements Filter {
    @Override
    public TrajectoryHolder doFilter(TrajectoryQuery q, TrajectoryHolder trh) {
        Trajectory simple = DouglasPeucker.getReduced(q.getTrajectory(), DouglasPeucker.getMaxEpsilon(q.getTrajectory()));
        double q_max_E = DouglasPeucker.getMaxEpsilon(q.getTrajectory());

        HashMap<String, Trajectory> trajectoryHashMap = trh.getTrajectories();

        trajectoryHashMap.entrySet()
                .stream()
                .filter(t ->
                        FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(t.getValue(), q_max_E),
                                q.dist + q_max_E * 2))
                .forEach((t) -> {
                    t.getValue().isResult = false;
                    if (FrechetDistance.decisionDP(q.getTrajectory(),
                            DouglasPeucker.getReduced(t.getValue(), q_max_E),
                            q.dist - q_max_E)) {
                        t.getValue().isResult = true;
                    }
                });

        return new TrajectoryHolder.Builder().setTrajectories(trajectoryHashMap).build();
    }
}
