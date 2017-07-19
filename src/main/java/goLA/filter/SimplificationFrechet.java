package goLA.filter;

import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;
import goLA.utils.DouglasPeucker;
import goLA.utils.FrechetDistance;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public class SimplificationFrechet implements Filter {
    @Override
    public List<Trajectory> doFilter(TrajectoryQuery q, List<Trajectory> trh) {
        Trajectory simple = DouglasPeucker.getReduced(q.getTrajectory(), DouglasPeucker.getMaxEpsilon(q.getTrajectory()));
        double q_max_E = DouglasPeucker.getMaxEpsilon(q.getTrajectory());

        List<Trajectory> trajectoryList = trh
                .stream()
                .filter(t ->
                        FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(t, q_max_E),
                                q.dist + q_max_E * 2 ))
                .collect(Collectors.toList());

        trajectoryList
                .stream()
                .forEach((t) -> {
                    t.isResult = false;
                        if (FrechetDistance.decisionDP(q.getTrajectory(),
                                t.simple,
                                q.dist - q_max_E)) {
                            t.isResult = true;
                        }
                }
        );

        //TODO : removed
        int isResult = 0;
        for (Trajectory tr : trajectoryList){
            if (tr.isResult) isResult++;
        }
        System.out.println("****** sure answer : " + isResult + " *******");


        return trajectoryList;
    }
}
