package io.github.stemlab.compute.impl;

import io.github.stemlab.compute.QueryProcessor;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.DiscreteFrechetDistance;
import io.github.stemlab.utils.FrechetDistance;

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
                        t.isResult() || decision(query, t)
                )
                .map( e -> e.getName())
                .collect(Collectors.toList());

        return trajectories;
    }

    @Override
    public boolean decision(Query q, Trajectory t) {
        if (DiscreteFrechetDistance.decisionDP(q.getTrajectory(), t, q.getDistance())){
            return true;
        }
        else
            return FrechetDistance.decisionDP(q.getTrajectory(), t, q.getDistance());
    }
}
