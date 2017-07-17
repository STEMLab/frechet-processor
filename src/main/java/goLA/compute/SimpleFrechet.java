package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.FrechetDistance;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class SimpleFrechet implements QueryProcessor {

    @Override
    public TrajectoryHolder query(TrajectoryQuery query, TrajectoryHolder trh) {
        TrajectoryHolder result = new TrajectoryHolder();
        if (trh.size() == 0) return result;

        Map<String, Trajectory> trajectories =
                trh.getTrajectories()
                .entrySet()
                .stream()
                .filter(t->
                    t.getValue().isResult || FrechetDistance.decisionDP(query.q_tr, t.getValue(), query.dist)
                )
                .collect(Collectors.toMap(
                        (entry) -> entry.getKey(),
                        (entry) -> entry.getValue()
                ));;

        result.setTrajectories(new HashMap<>(trajectories));

        return result;
    }

}
