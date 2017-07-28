package io.github.stemlab.data.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.data.Index;
import io.github.stemlab.data.elki.ELKIRStarTree;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.EuclideanDistance;
import io.github.stemlab.utils.StraightForward;
import io.github.stemlab.utils.StraightSimpleFrechetDecision;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 26.
 */
public class TestIndexImpl implements Index{

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
        Instant mid_time = Instant.now();
        System.out.println("---- Range Query Time : " + Duration.between(start_time, mid_time) + " -------");


        int size1 = sp_result.size();
        System.out.println("---- candidate number for only start point : " + size1 + " -------");

        HashSet<String> resultSet = new LinkedHashSet<>();
        query.getTrajectory().setSimplified(StraightForward.getReduced(query.getTrajectory(), dist));

        for (DoubleDBIDListIter x = sp_result.iter(); x.valid(); x.advance()) {
            Trajectory trajectory = this.holder.get(rStarTree.getRecordName(x));
            Coordinate last = trajectory.getCoordinates().get(trajectory.getCoordinates().size() - 1);
            if (EuclideanDistance.distance(last, end) <= dist) {
                if (StraightSimpleFrechetDecision.decisionIsInResult(query, trajectory)){
                    resultSet.add(trajectory.getName());
                }
            }
        }


        int size3 = resultSet.size();
        System.out.println("---- result number : " + size3 + " -------");
        Instant end_time = Instant.now();
        System.out.println("---- Filtering and Decision Time : " + Duration.between(mid_time, end_time) + " -------");
        return resultSet;
    }

    @Override
    public int size() {
        return this.size;
    }

}
