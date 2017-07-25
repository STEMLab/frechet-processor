package goLA.compute;

import goLA.model.Query;
import goLA.model.Trajectory;
import goLA.utils.FrechetDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RealFrechet implements QueryProcessor {

    @Override
    public List<String> query(Query query, List<Trajectory> trh) {
        if (trh.size() == 0) return new ArrayList<>();

        List<String> trajectories =
                trh.stream().filter(t ->
                        t.isResult() || FrechetDistance.decisionDP(query.q_tr, t, query.dist)
                )
                        .map(e->e.getName())
                        .collect(Collectors.toList());

        return trajectories;
    }

}
