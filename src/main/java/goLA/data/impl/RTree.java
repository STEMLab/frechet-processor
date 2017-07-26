package goLA.data.impl;

import com.sun.xml.internal.ws.util.QNameMap;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.rstar.RStarTree;
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
    public ConcurrentHashMap<String, Trajectory> getHolder() {
        return holder;
    }
}
