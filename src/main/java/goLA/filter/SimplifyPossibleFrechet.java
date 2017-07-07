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
        Trajectory simple = DouglasPeucker.getReduced(q.getTrajectory(), DouglasPeucker.getMaxEpsilon(q.getTrajectory()));
        double q_max_E = DouglasPeucker.getMaxEpsilon(q.getTrajectory());
        Map<String, Trajectory> ret = trh.getTrajectories().entrySet()
                .stream()
                .filter(t ->
                        FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(t.getValue(), q_max_E),
                                q.dist + q_max_E * 2))
                .collect(Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue()
                ));

        ret.entrySet().stream().forEach((t)->{
                    t.getValue().isResult = false;
                    if (FrechetDistance.decisionDP(q.getTrajectory(),
                            DouglasPeucker.getReduced(t.getValue(), q_max_E),
                            q.dist - q_max_E * 2)){
                        t.getValue().isResult = true;
                    }
                }
        );
        //        Map<String, Trajectory> ret = trh.getTrajectories().entrySet()
//                .stream()
//                .filter(t ->
//                        FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(t.getValue(), DouglasPeucker.getMaxEpsilon(t.getValue())),
//                                q.dist + dist + DouglasPeucker.getMaxEpsilon(t.getValue())))
//                .collect(Collectors.toMap(
//                        (entry) -> entry.getKey(),
//                        (entry) -> entry.getValue()
//                ));
//
//        ret.entrySet().stream().forEach((t)->{
//                    t.getValue().isResult = false;
//                    if (FrechetDistance.decisionDP(q.getTrajectory(),
//                            DouglasPeucker.getReduced(t.getValue(), DouglasPeucker.getMaxEpsilon(t.getValue())),
//                            q.dist - dist - DouglasPeucker.getMaxEpsilon(t.getValue()))){
//                        t.getValue().isResult = true;
//                    }
//                }
//        );

        TrajectoryHolder rettrh = new TrajectoryHolder();
        rettrh.setTrajectories(new HashMap<>(ret));
        return rettrh;
    }
}
