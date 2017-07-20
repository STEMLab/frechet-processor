package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;
import goLA.utils.DiscreteFrechet;
import goLA.utils.FrechetDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stem_dong on 2017-07-19.
 */
public class DiscreteAndSimple implements QueryProcessor {

    @Override
    public List<Trajectory> query(TrajectoryQuery query, List<Trajectory> trh) {
        if (trh.size() == 0) return new ArrayList<>();

        DiscreteFrechet df = new DiscreteFrechet();
        List<Trajectory> trajectories = trh
                .stream()
                .filter(t->
                        t.isResult || (df.distance(query.q_tr, t) < query.dist) || FrechetDistance.decisionDP(query.q_tr, t, query.dist)
                )
                .collect(Collectors.toList());

        return trajectories;
    }
}
