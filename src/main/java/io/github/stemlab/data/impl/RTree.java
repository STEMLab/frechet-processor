package io.github.stemlab.data.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.data.Tree;
import io.github.stemlab.data.elki.ELKIRStarTree;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem-dong-li on 17. 7. 5.
 */
public class RTree implements Tree {
    private ELKIRStarTree rStarTree;
    private HashMap<String, Trajectory> holder;

    private int size;

    public RTree() {
        this.rStarTree = new ELKIRStarTree();
        this.holder = new HashMap<>();
    }

    @Override
    public void addTrajectory(String id, Trajectory trajectory) {
        List<Coordinate> list = trajectory.getCoordinates();

        Coordinate start = list.get(0);
        rStarTree.add(trajectory.getName(), new double[]{start.getPointX(), start.getPointY()});

        size++;

        holder.put(trajectory.getName(), trajectory);
    }

    @Override
    public void initialize() {
        rStarTree.initialize();
    }


    @Override
    public DoubleDBIDList rangeQuery(Query query) {
        Coordinate start = query.getTrajectory().getCoordinates().get(0);
        double dist = query.getDistance();

        DoubleDBIDList result = rStarTree.search(new double[]{start.getPointX(), start.getPointY()}, dist);

        return result;

    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String getRecordName(DoubleDBIDListIter x) {
        return rStarTree.getRecordName(x);
    }

    @Override
    public Trajectory getTrajectory(String key) {
        return this.holder.get(key);
    }

    @Override
    public HashMap<String, Trajectory> getHolder() {
        return holder;
    }
}
