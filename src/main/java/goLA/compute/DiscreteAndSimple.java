package goLA.compute;

import goLA.model.Query;
import goLA.model.Trajectory;
import goLA.utils.DiscreteFrechetDistance;
import goLA.utils.FrechetDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stem_dong on 2017-07-19.
 */
public class DiscreteAndSimple implements QueryProcessor {

    @Override
    public List<Trajectory> query(Query query, List<Trajectory> trh) {
        if (trh.size() == 0) return new ArrayList<>();

        DiscreteFrechetDistance df = new DiscreteFrechetDistance();
        List<Trajectory> trajectories = trh
                .stream()
                .filter(t ->
                        t.isResult() || (df.distance(query.q_tr, t) < query.dist) || FrechetDistance.decisionDP(query.q_tr, t, query.dist)
                )
                .collect(Collectors.toList());

        return trajectories;
    }
}
