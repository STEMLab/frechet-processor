package io.github.stemlab.data.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.decision.DecisionMaker;
import io.github.stemlab.model.Query;
import io.github.stemlab.data.Index;
import io.github.stemlab.data.elki.ELKIRStarTree;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.DouglasPeucker;
import io.github.stemlab.utils.EuclideanDistance;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class IndexImpl implements Index {

    private final DecisionMaker decisionMaker = new DecisionMaker();
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
        double q_dist = query.getDistance();
        double maxEpsilon = DouglasPeucker.getMaxEpsilon(query.getTrajectory());

        for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
            Trajectory trajectory = this.holder.get(rStarTree.getRecordName(x));
            Coordinate last = trajectory.getCoordinates().get(trajectory.getCoordinates().size() - 1);
            if (EuclideanDistance.distance(last, end) <= dist) {
                if (decisionMaker.decisionIsInResult(query, simple, q_dist, maxEpsilon, trajectory)){
                    resultSet.add(trajectory.getName());
                }
            }
        }
        return resultSet;
    }

    private boolean decisionIsInResult(Query query, Trajectory simple, double q_dist, double maxEpsilon, Trajectory trajectory) {
        return decisionMaker.decisionIsInResult(query, simple, q_dist, maxEpsilon, trajectory);
    }

    @Override
    public int size() {
        return this.size;
    }

    private boolean isResult(Query q, Trajectory tr, double q_max_E) {
        return decisionMaker.isResult(q, tr, q_max_E);
    }

    private boolean isFiltered(Trajectory simple, Trajectory tr, double dist, double q_max_E) {
        return decisionMaker.isFiltered(simple, tr, dist, q_max_E);
    }


    private boolean isTrajectoryInQueryRange(Query q, Trajectory t) {
        return decisionMaker.isTrajectoryInQueryRange(q, t);
    }
}
