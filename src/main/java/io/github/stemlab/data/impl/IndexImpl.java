package io.github.stemlab.data.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.data.Index;
import io.github.stemlab.data.elki.ELKIRStarTree;
import io.github.stemlab.logic.FrechetDecision;
import io.github.stemlab.logic.StraightForwardSimplification;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.EuclideanDistance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;


public class IndexImpl implements Index {
    private ELKIRStarTree tree;
    private HashMap<String, Trajectory> holder;
    private int size;

    public IndexImpl() {
        this.tree = new ELKIRStarTree();
        this.holder = new HashMap<>();
    }

    @Override
    public void addTrajectory(String id, Trajectory trajectory) {
        List<Coordinate> list = trajectory.getCoordinates();

        Coordinate start = list.get(0);
        tree.add(trajectory.getName(), new double[]{start.getPointX(), start.getPointY()});
        holder.put(trajectory.getName(), trajectory);

        size++;
    }

    @Override
    public void initialize() {
        tree.initialize();
    }

    @Override
    public HashSet<String> getQueryResult(Query query) {
        Coordinate start = query.getTrajectory().getCoordinates().get(0);
        Coordinate end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.getDistance();

        DoubleDBIDList result = tree.search(new double[]{start.getPointX(), start.getPointY()}, dist);

        HashSet<String> resultSet = new LinkedHashSet<>();
        query.getTrajectory().setSimplified(StraightForwardSimplification.getReduced(query.getTrajectory(), dist));

        for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
            Trajectory trajectory = this.holder.get(tree.getRecordName(x));
            Coordinate last = trajectory.getCoordinates().get(trajectory.getCoordinates().size() - 1);
            if (EuclideanDistance.distance(last, end) <= dist) {
                if (FrechetDecision.isResult(query, trajectory)) {
                    resultSet.add(trajectory.getName());
                }
            }

        }
        return resultSet;
    }

    @Override
    public int size() {
        return this.size;
    }

}
