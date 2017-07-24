package goLA.filter.impl;

import goLA.filter.Filter;
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
public class SimplificationFilter implements Filter {
    @Override
    public List<Trajectory> doFilter(Query query, List<Trajectory> trajectories) {
        Trajectory simple = DouglasPeucker.getReduced(query.getTrajectory(), DouglasPeucker.getMaxEpsilon(query.getTrajectory()));
        double maxEpsilon = DouglasPeucker.getMaxEpsilon(query.getTrajectory());

        List<Trajectory> trajectoryList = trajectories
                .stream()
                .filter(trajectory ->
                        FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(trajectory, maxEpsilon),
                                query.getDistance() + maxEpsilon * 2))
                .collect(Collectors.toList());

        //only process if query distance is longer than query's max Epsilon.
        if (query.getDistance() - maxEpsilon >= 0) {
            trajectoryList
                    .stream()
                    .forEach((t) -> {
                                t.setResult(false);
                                if (DiscreteFrechetDistance.decisionDP(query.getTrajectory(),
                                        t.getSimplified(),
                                        query.getDistance() - maxEpsilon)) {
                                    t.setResult(true);
                                }
                            }
                    );
        }

        return trajectoryList;
    }
}
