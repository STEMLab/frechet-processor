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
    public List<Trajectory> query(Query query, List<Trajectory> trajectories) {
        if (trajectories.size() == 0) return new ArrayList<>();

        List<Trajectory> filteredTrajectories = trajectories
                .stream()
                .filter(trajectory ->
                        trajectory.isResult() || DiscreteFrechetDistance.decisionDP(query.getQueryTrajectory(), trajectory, query.getDistance()) || FrechetDistance.decisionDP(query.getQueryTrajectory(), trajectory, query.getDistance())
                )
                .collect(Collectors.toList());

        return filteredTrajectories;
    }
}
