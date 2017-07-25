package goLA.data.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import goLA.data.Index;
import goLA.data.elki.ELKIRStarTree;
import goLA.model.Coordinate;
import goLA.model.Query;
import goLA.model.Trajectory;
import goLA.utils.DiscreteFrechetDistance;
import goLA.utils.DouglasPeucker;
import goLA.utils.EuclideanDistance;
import goLA.utils.FrechetDistance;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class IndexImpl implements Index {

    private ELKIRStarTree rStarTree;
    private ConcurrentHashMap<String, Trajectory> holder;
    private int size;

    public IndexImpl() {
        this.rStarTree = new ELKIRStarTree();
        this.holder = new ConcurrentHashMap<>();
    }

    @Override
    public void addTrajectory(String id, Trajectory trajectory) {
        List<Coordinate> list = trajectory.getCoordinates();

        Coordinate start = list.get(0);
        rStarTree.add(trajectory.getName(), new double[]{start.getPointX(), start.getPointY()});

        holder.put(trajectory.getName(), trajectory);

        size++;
    }

    @Override
    public void initialize() {
        rStarTree.initialize();
    }

    @Override
    public HashSet<String> getQueryResult(Query query) {

        Coordinate start = query.getTrajectory().getCoordinates().get(0);
        Coordinate end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.getDistance();

        DoubleDBIDList result = rStarTree.search(new double[]{start.getPointX(), start.getPointY()}, dist);

        HashSet<String> resultSet = new LinkedHashSet<>();

        Trajectory simple = DouglasPeucker.getReduced(query.getTrajectory(), DouglasPeucker.getMaxEpsilon(query.getTrajectory()));
        double maxEpsilon = DouglasPeucker.getMaxEpsilon(query.getTrajectory());

        for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
            Trajectory trajectory = this.holder.get(rStarTree.getRecordName(x));
            Coordinate last = trajectory.getCoordinates().get(trajectory.getCoordinates().size() - 1);
            if (EuclideanDistance.distance(last, end) <= dist) {
                if (FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(trajectory, maxEpsilon),
                        query.getDistance() + maxEpsilon * 2)) {
                    if (query.getDistance() - maxEpsilon >= 0) {
                        if (DiscreteFrechetDistance.decisionDP(query.getTrajectory(),
                                trajectory.getSimplified(),
                                query.getDistance() - maxEpsilon)) {
                            resultSet.add(trajectory.getName());
                            continue;
                        }
                    }
                    if (DiscreteFrechetDistance.decisionDP(query.getQueryTrajectory(), trajectory, query.getDistance())
                            || FrechetDistance.decisionDP(query.getQueryTrajectory(), trajectory, query.getDistance())) {
                        resultSet.add(trajectory.getName());
                    }
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
