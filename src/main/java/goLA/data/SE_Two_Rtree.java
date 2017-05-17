package goLA.data;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import goLA.data.tree.ElkiRStarTree;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem_dong on 2017-05-02.
 * UPPER LEFT is (0,0)
 */

public class SE_Two_Rtree implements Tree {

    private ElkiRStarTree start_tree;
    private ElkiRStarTree end_tree;
    private ConcurrentHashMap<String, Trajectory> holder;

    private int size;

    public SE_Two_Rtree() {
        this.start_tree = new ElkiRStarTree();
        this.end_tree = new ElkiRStarTree();
        this.holder = new ConcurrentHashMap<>();
    }

    public int size() {
        return this.size;
    }

    @Override
    public void addTrajectory(String id, Trajectory tr) {
        List<Coordinates> list = tr.getCoordinates();

        Coordinates start = list.get(0);
        start_tree.add(tr.getName(), new double[]{start.getPointX(), start.getPointY()});

        Coordinates end = list.get(list.size() - 1);
        end_tree.add(tr.getName(), new double[]{end.getPointX(), end.getPointY()});

        size++;

        holder.put(tr.getName(), tr);
    }

    @Override
    public TrajectoryHolder getPossible(TrajectoryQuery query) {

        Coordinates q_start = query.getTrajectory().getCoordinates().get(0);
        Coordinates q_end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.dist;

        DoubleDBIDList s_results = start_tree.search(new double[]{q_start.getPointX(), q_start.getPointY()}, dist);
        DoubleDBIDList e_results = end_tree.search(new double[]{q_end.getPointX(), q_end.getPointY()}, dist);

        TrajectoryHolder poss = new TrajectoryHolder();

        int i = 0;
        int j = 0;
        for (DoubleDBIDListIter x = s_results.iter(); x.valid(); x.advance(), i++) {
            for (DoubleDBIDListIter y = e_results.iter(); y.valid(); y.advance(), j++) {
                if (start_tree.getRecordName(x).equals(end_tree.getRecordName((y)))) {
                    poss.addTrajectory(start_tree.getRecordName(x), holder.get(start_tree.getRecordName(x)));
                }
            }
        }

        return poss;
    }
}

