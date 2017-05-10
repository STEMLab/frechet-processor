package goLA.data;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import rx.Observable;

import java.util.List;
import java.util.Map;

/**
 * Created by dong on 2017. 5. 10..
 */
public class SE_Manhattan_Rtree implements Tree{

    public RTree<Trajectory, Point> manh_tree;
    private Coordinates[] criteria;
    private int size;

    public SE_Manhattan_Rtree(){
        manh_tree = RTree.star().create();
    }

    @Override
    public void addTrajectory(String id, Trajectory tr) {
        List<Coordinates> list = tr.getCoordinates();
        if (size == 0){
            criteria = new Coordinates[2];
            criteria[0] = list.get(0);
            criteria[1] = list.get(list.size()-1);
        }
        Double start_man_dist = getSignedManhattanDist(list.get(0), criteria[0]);
        Double end_man_dist = getSignedManhattanDist(list.get(list.size()-1), criteria[1]);

        manh_tree = manh_tree.add(tr, Geometries.point(start_man_dist,end_man_dist));
        size++;
    }

    private Double getSignedManhattanDist(Coordinates coordinates, Coordinates criterion) {
        double x_dist=coordinates.getPointX() - criterion.getPointX();
        double y_dist=coordinates.getPointY() - criterion.getPointY();
//        if (x_dist + y_dist < 0){
//            return (Math.abs(x_dist) + Math.abs(y_dist));
//        }
//        else{
//            return (Math.abs(x_dist) + Math.abs(y_dist));
//        }
        return x_dist + y_dist;
    }

    @Override
    public TrajectoryHolder getPossible(TrajectoryQuery query) {
        Coordinates q_start = query.getTrajectory().getCoordinates().get(0);
        Coordinates q_end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.dist;

        Double s_q_dist = getSignedManhattanDist(q_start, criteria[0]);
        Double e_q_dist = getSignedManhattanDist(q_end, criteria[1]);

        Observable<Entry<Trajectory, Point>> results = manh_tree.search(Geometries.point(s_q_dist,e_q_dist), dist * 1.5);
        TrajectoryHolder poss = new TrajectoryHolder();

        results.forEach(e->{
            poss.addTrajectory(e.value().getName(), e.value());
        });

        return poss;
    }

    @Override
    public int size() {
        return size;
    }
}
