package goLA.filter;

import goLA.compute.QueryProcessor;
import goLA.compute.SimpleFrechet;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.DouglasPeucker;
import goLA.utils.FrechetDistance;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public class SimplifyPossibleFrechet implements Filter{
    @Override
    public TrajectoryHolder doFilter(TrajectoryQuery q, TrajectoryHolder trh) {
        //TODO : get reasonable epsilon
        double epsilon = q.dist / 10;
        Map<String, Trajectory> ret = trh.getTrajectories()
                .entrySet()
                .stream()
                .filter(t ->
                        FrechetDistance.decisionDP(q.getTrajectory(), DouglasPeucker.getReduced(t.getValue(), epsilon), q.dist + epsilon)
                )
                .collect(Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue()
                ));
        TrajectoryHolder rettrh = new TrajectoryHolder();
        rettrh.setTrajectories(new HashMap<String, Trajectory>(ret));
        return rettrh;
    }
}
