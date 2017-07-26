package goLA.filter;

import goLA.model.Query;
import goLA.model.Trajectory;
import goLA.utils.DiscreteFrechetDistance;
import goLA.utils.DouglasPeucker;
import goLA.utils.FrechetDistance;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public class SimplificationFrechet implements Filter {
    @Override
    public List<Trajectory> doFilter(Query q, List<Trajectory> trh) {

        Trajectory simple = DouglasPeucker.getReduced(q.getTrajectory(), DouglasPeucker.getMaxEpsilon(q.getTrajectory()));
        double q_dist = q.getDistance();
        double q_max_E = DouglasPeucker.getMaxEpsilon(q.getTrajectory());

        List<Trajectory> trajectoryList = trh
                .stream()
                .filter(t -> isFiltered(simple, t, q_dist, q_max_E)
                        )
                .collect(Collectors.toList());

        //only process if query distance is longer than query's max Epsilon.
        if (q.getDistance() - q_max_E >= 0) {
            trajectoryList
                    .stream()
                    .forEach((t) -> {
                                t.setResult(false);
                                if (isResult(q, t, q_dist, q_max_E)) {
                                    t.setResult(true);
                                }
                            }
                    );
        }
        //TODO : removed
        int isResult = 0;
        for (Trajectory tr : trajectoryList) {
            if (tr.isResult()) isResult++;
        }
        System.out.println("****** sure answer : " + isResult + " *******");


        return trajectoryList;
    }

    @Override
    public boolean isFiltered(Trajectory simple, Trajectory tr, double dist, double q_max_E) {
        return FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(tr, q_max_E),
                dist + q_max_E * 2);
    }

    @Override
    public boolean isResult(Query q, Trajectory tr, double dist, double q_max_E) {
        return DiscreteFrechetDistance.decisionDP(q.getTrajectory(),
                tr.getSimplified(),
                q.getDistance() - q_max_E);
    }
}
