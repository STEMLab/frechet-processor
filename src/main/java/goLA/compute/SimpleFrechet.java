package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;
import goLA.utils.FrechetDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SimpleFrechet implements QueryProcessor {

    @Override
    public List<Trajectory> query(TrajectoryQuery query, List<Trajectory> trh) {
        if (trh.size() == 0) return new ArrayList<>();

        List<Trajectory> trajectories =
                trh
                .stream()
                .filter(t->
                    t.isResult || FrechetDistance.decisionDP(query.q_tr, t, query.dist)
                )
                .collect(Collectors.toList());

        return trajectories;
    }

}
