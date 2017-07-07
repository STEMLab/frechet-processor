package goLA.data;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import goLA.data.tree.ElkiRStarTree;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import goLA.utils.EuclideanDistance;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem-dong-li on 17. 7. 5.
 */
public class StartRTree implements Tree{
    private ElkiRStarTree start_tree;
    private ConcurrentHashMap<String, Trajectory> holder;

    private int size;

    public StartRTree() {
        this.start_tree = new ElkiRStarTree();
        this.holder = new ConcurrentHashMap<>();
    }

    @Override
    public void addTrajectory(String id, Trajectory tr) {
        List<Coordinates> list = tr.getCoordinates();

        Coordinates start = list.get(0);
        start_tree.add(tr.getName(), new double[]{start.getPointX(), start.getPointY()});

        size++;

        holder.put(tr.getName(), tr);
    }

    @Override
    public void initialize() {
        start_tree.initialize();
    }

    @Override
    public TrajectoryHolder getPossible(TrajectoryQuery query) {

        Coordinates q_start = query.getTrajectory().getCoordinates().get(0);
        Coordinates q_end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.dist;

        DoubleDBIDList s_results = start_tree.search(new double[]{q_start.getPointX(), q_start.getPointY()}, dist);

        TrajectoryHolder poss = new TrajectoryHolder();
        HashSet<String> check_in_start = new HashSet<>();

        for (DoubleDBIDListIter x = s_results.iter(); x.valid(); x.advance()) {
            String key = start_tree.getRecordName(x);
            List<Coordinates> coords = this.holder.get(key).getCoordinates();
            Coordinates end = coords.get(coords.size()-1);
            if (EuclideanDistance.distance(end, q_end) <= dist)
                poss.addTrajectory(key, this.holder.get(key));
//            if (q_end.getPointX() - dist <= end.getPointX() && end.getPointX() <= q_end.getPointX() + dist) {
//                if (q_end.getPointY() - dist <= end.getPointY() && end.getPointY() <= q_end.getPointY() + dist) {
//                    poss.addTrajectory(key, this.holder.get(key));
//                }
//            }
        }
        return poss;
    }

    @Override
    public ConcurrentHashMap<String, Trajectory> getHolder() {
        return holder;
    }

    @Override
    public int size() {
        return this.size;
    }
}
