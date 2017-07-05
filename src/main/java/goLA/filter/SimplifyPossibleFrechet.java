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
        double epsilon = (DouglasPeucker.getMaxEpsilon(q.getTrajectory()) + q.dist / 5)/ 2;
        Map<String, Trajectory> ret = trh.getTrajectories().entrySet()
                .stream()
                .filter(t ->
                        FrechetDistance.decisionDP(q.getTrajectory(), DouglasPeucker.getReduced(t.getValue(), DouglasPeucker.getMaxEpsilon(t.getValue())), q.dist + epsilon)
                )
                .collect(Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue()
                ));
//
        ret.entrySet().stream().forEach((t)->{
                    t.getValue().isResult = false;
                    if (FrechetDistance.decisionDP(q.getTrajectory(),
                            DouglasPeucker.getReduced(t.getValue(), DouglasPeucker.getMaxEpsilon(t.getValue())),
                            q.dist - DouglasPeucker.getMaxEpsilon(t.getValue()) * 2 )){
                        t.getValue().isResult = true;
                    }
                }
        );

        TrajectoryHolder rettrh = new TrajectoryHolder();
        rettrh.setTrajectories(new HashMap<>(ret));
        return rettrh;
    }
}
