package goLA.filter;

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
        //Trajectory simple = q.getTrajectory().makeStraight();
        Trajectory simple = DouglasPeucker.getReduced(q.getTrajectory(), DouglasPeucker.getMaxEpsilon(q.getTrajectory()));
        double dist = DouglasPeucker.getMaxEpsilon(q.getTrajectory());
        Map<String, Trajectory> ret = trh.getTrajectories().entrySet()
                .stream()
                .filter(t ->
                        FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(t.getValue(), DouglasPeucker.getMaxEpsilon(t.getValue())),
                                q.dist + dist + DouglasPeucker.getMaxEpsilon(t.getValue())))
                .collect(Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue()
                ));

        ret.entrySet().stream().forEach((t)->{
                    t.getValue().isResult = false;
                    if (FrechetDistance.decisionDP(q.getTrajectory(),
                            DouglasPeucker.getReduced(t.getValue(), DouglasPeucker.getMaxEpsilon(t.getValue())),
                            q.dist - dist - DouglasPeucker.getMaxEpsilon(t.getValue()))){
                        t.getValue().isResult = true;
                    }
                }
        );

        TrajectoryHolder rettrh = new TrajectoryHolder();
        rettrh.setTrajectories(new HashMap<>(ret));
        return rettrh;
    }
}
