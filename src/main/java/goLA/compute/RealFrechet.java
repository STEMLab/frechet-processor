package goLA.compute;

import goLA.model.Query;
import goLA.model.Trajectory;
import goLA.utils.FrechetDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RealFrechet implements QueryProcessor {

    @Override
    public List<Trajectory> query(Query query, List<Trajectory> trh) {
        if (trh.size() == 0) return new ArrayList<>();

        List<Trajectory> trajectories =
                trh.stream().filter(t ->
                        t.isResult() || FrechetDistance.decisionDP(query.getTrajectory(), t, query.getDistance())
                )
                        .collect(Collectors.toList());

        return trajectories;
    }

}
