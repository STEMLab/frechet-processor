package goLA.compute.impl;

import goLA.compute.QueryProcessor;
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
public class QueryProcessorImpl implements QueryProcessor {

    @Override
    public List<String> query(Query query, List<Trajectory> trh) {
        if (trh.size() == 0) return new ArrayList<>();

        List<String> trajectories = trh
                .stream()
                .filter(t ->
                        t.isResult() || DiscreteFrechetDistance.decisionDP(query.q_tr, t, query.dist) || FrechetDistance.decisionDP(query.q_tr, t, query.dist)
                )
                .map( e -> e.getName())
                .collect(Collectors.toList());

        return trajectories;
    }
}
