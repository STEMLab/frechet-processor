package goLA.data;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import com.github.davidmoten.rtree.geometry.Rectangle;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;
import rx.Observable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dong on 2017. 5. 11..
 */
public class SE_MBR_Rtree implements Tree {
    private int size;
    private RTree<Trajectory, Rectangle> mbr_tree;

    public SE_MBR_Rtree(){
        mbr_tree = mbr_tree.star().create();
    }

    @Override
    public void addTrajectory(String id, Trajectory tr) {
        List<Coordinates> list = tr.getCoordinates();

        Coordinates start = list.get(0);
        Coordinates end = list.get(list.size()-1);

        double x1,x2;
        double y1,y2;

        if (start.getPointX() >= end.getPointX()){
            x2 = start.getPointX();
            x1 = end.getPointX();
        }
        else{
            x1 = start.getPointX();
            x2 = end.getPointX();
        }

        if (start.getPointY() >= end.getPointY()){
            y2 = start.getPointY();
            y1 = end.getPointY();
        }
        else{
            y1 = start.getPointY();
            y2 = end.getPointY();
        }

        mbr_tree = mbr_tree.add(tr, Geometries.rectangle(x1,y1,x2,y2));

        size++;
    }

    @Override
    public TrajectoryHolder getPossible(TrajectoryQuery query) {
        Coordinates q_start = query.getTrajectory().getCoordinates().get(0);
        Coordinates q_end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        double dist = query.dist;

        double x1,x2;
        double y1,y2;

        if (q_start.getPointX() >= q_end.getPointX()){
            x2 = q_start.getPointX();
            x1 = q_end.getPointX();
        }
        else{
            x1 = q_start.getPointX();
            x2 = q_end.getPointX();
        }

        if (q_start.getPointY() >= q_end.getPointY()){
            y2 = q_start.getPointY();
            y1 = q_end.getPointY();
        }
        else{
            y1 = q_start.getPointY();
            y2 = q_end.getPointY();
        }


        Observable<Entry<Trajectory, Rectangle>> results = mbr_tree.search(Geometries.rectangle(x1,y1,x2,y2), dist);

        TrajectoryHolder poss = new TrajectoryHolder();

        results.forEach(
                e->{
                    poss.addTrajectory(e.value().getName(), e.value());
                }
        );
        return poss;
    }

    @Override
    public int size() {
        return this.size;
    }
}
