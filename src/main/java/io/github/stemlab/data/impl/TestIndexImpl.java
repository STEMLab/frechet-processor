package io.github.stemlab.data.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.data.Index;
import io.github.stemlab.data.elki.ELKIRStarTree;
import io.github.stemlab.decision.DecisionMaker;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.DiscreteFrechetDistance;
import io.github.stemlab.utils.DouglasPeucker;
import io.github.stemlab.utils.EuclideanDistance;
import io.github.stemlab.utils.FrechetDistance;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem-dong-li on 17. 7. 26.
 */
public class TestIndexImpl implements Index{

    private final DecisionMaker decisionMaker = new DecisionMaker();
    public ELKIRStarTree rStarTree;
    public HashMap<String, Trajectory> holder;
    private int size;

    public TestIndexImpl() {
        this.rStarTree = new ELKIRStarTree();
        this.holder = new HashMap<>();
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

        Instant start_time = Instant.now();
        System.out.println("\n\n---- Query processing : " + query.getTrajectory().getName() + ", " + query.getDistance() + " -------");
        DoubleDBIDList sp_result = rStarTree.search(new double[]{start.getPointX(), start.getPointY()}, dist);
        int size1 = sp_result.size();
        System.out.println("---- candidate number for only start point : " + size1 + " -------");

        HashSet<Trajectory> tr_set = new LinkedHashSet<>();

        for (DoubleDBIDListIter x = sp_result.iter(); x.valid(); x.advance()) {
            Trajectory trajectory = this.holder.get(rStarTree.getRecordName(x));
            Coordinate last = trajectory.getCoordinates().get(trajectory.getCoordinates().size() - 1);
            if (EuclideanDistance.distance(last, end) <= dist) {
                tr_set.add(trajectory);
            }
        }

        int size2 = tr_set.size();
        System.out.println("---- candidate number for start and end point : " + size2 + " -------");

        Trajectory simple = DouglasPeucker.getReduced(query.getTrajectory(), DouglasPeucker.getMaxEpsilon(query.getTrajectory()));
        double maxEpsilon = DouglasPeucker.getMaxEpsilon(query.getTrajectory());
        HashSet<String> resultSet = new LinkedHashSet<>();
        for (Trajectory tr : tr_set){
            if (decisionMaker.decisionIsInResult(query, simple, dist, maxEpsilon, tr)){
                resultSet.add(tr.getName());
            }
        }

        int size3 = resultSet.size();
        System.out.println("---- result number : " + size3 + " -------");
        Instant end_time = Instant.now();
        System.out.println("---- calculate Dist Time : " + Duration.between(start_time, end_time) + " -------");
        return resultSet;
    }

    @Override
    public int size() {
        return this.size;
    }

}
