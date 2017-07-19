package goLA.filter;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.DiscreteFrechet;
import goLA.utils.DouglasPeucker;
import goLA.utils.FrechetDistance;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public class SimplificationFrechet implements Filter {
    @Override
    public TrajectoryHolder doFilter(TrajectoryQuery q, TrajectoryHolder trh) {
        Trajectory simple = DouglasPeucker.getReduced(q.getTrajectory(), DouglasPeucker.getMaxEpsilon(q.getTrajectory()));
        double q_max_E = DouglasPeucker.getMaxEpsilon(q.getTrajectory());

        Map<String, Trajectory> trajectoryHashMap = trh.getTrajectories().entrySet()
                .stream()
                .filter(t ->
                        FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(t.getValue(), q_max_E),
                                q.dist + q_max_E * 2 ))
                .collect(Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue()
                ));

        trajectoryHashMap.entrySet()
                .stream()
                .forEach((t) -> {
                    t.getValue().isResult = false;
                        if (FrechetDistance.decisionDP(q.getTrajectory(),
                                t.getValue().simple,
                                q.dist - q_max_E)) {
                            t.getValue().isResult = true;
                        }
                }
        );

        //TODO : removed
        int isResult = 0;
        for (Map.Entry<String, Trajectory> tr : trajectoryHashMap.entrySet()){
            if (tr.getValue().isResult) isResult++;
        }
        System.out.println("****** sure answer : " + isResult + " *******");


        return new TrajectoryHolder.Builder().setTrajectories(new HashMap<>(trajectoryHashMap) ).build();
    }
}
