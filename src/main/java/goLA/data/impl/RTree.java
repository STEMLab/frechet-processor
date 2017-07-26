package goLA.data.impl;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import goLA.data.Tree;
import goLA.data.elki.ELKIRStarTree;
import goLA.model.Coordinate;
import goLA.model.Query;
import goLA.model.Trajectory;
import goLA.utils.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem-dong-li on 17. 7. 5.
 */
public class RTree implements Tree {
    private ELKIRStarTree rStarTree;
    private ConcurrentHashMap<String, Trajectory> holder;

    private int size;

    public RTree() {
        this.rStarTree = new ELKIRStarTree();
        this.holder = new ConcurrentHashMap<>();
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
    public List<Trajectory> getPossible(Query query) {

        Coordinate start = query.getTrajectory().getCoordinates().get(0);
        Coordinate end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.getDistance();

        DoubleDBIDList result = rStarTree.search(new double[]{start.getPointX(), start.getPointY()}, dist);

        List<Trajectory> poss = new ArrayList<>();

        for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
            String key = rStarTree.getRecordName(x);
            List<Coordinate> coordinates = this.holder.get(key).getCoordinates();
            Coordinate last = coordinates.get(coordinates.size() - 1);
            if (EuclideanDistance.distance(last, end) <= dist)
                poss.add(this.holder.get(key));
        }



        return poss;
    }

    @Override
    public int size() {
        return this.size;
    }
}
