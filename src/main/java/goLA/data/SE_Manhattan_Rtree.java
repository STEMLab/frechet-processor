package goLA.data;

import de.lmu.ifi.dbs.elki.data.DoubleVector;
import de.lmu.ifi.dbs.elki.data.LabelList;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import de.lmu.ifi.dbs.elki.database.query.distance.DistanceQuery;
import de.lmu.ifi.dbs.elki.database.query.range.RangeQuery;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.datasource.DatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;
import de.lmu.ifi.dbs.elki.index.IndexFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.AbstractRStarTreeFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.rstar.RStarTreeFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.bulk.SortTileRecursiveBulkSplit;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.insert.ApproximativeLeastOverlapInsertionStrategy;
import de.lmu.ifi.dbs.elki.persistent.AbstractPageFileFactory;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;
import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

import java.awt.*;
import java.util.*;
import java.util.List;

import static de.lmu.ifi.dbs.elki.database.AbstractDatabase.Parameterizer.INDEX_ID;

/**
 * Created by dong on 2017. 5. 10..
 */
public class SE_Manhattan_Rtree implements Tree{

    /*public RTree<Trajectory, Point> manh_tree;*/
    public ArrayList<double[]> manh_tree = new ArrayList<>();
    public HashMap<String,Trajectory> t = new HashMap<>();
    public ArrayList<String> l = new ArrayList<>();
    private Coordinates[] criteria;
    private int size;
    Database db;

    public SE_Manhattan_Rtree(){

    }

    public void initialize(){
        ListParameterization spatparams = new ListParameterization();
        spatparams.addParameter(INDEX_ID, RStarTreeFactory.class);
        /*spatparams.addParameter(AbstractPageFileFactory.Parameterizer.PAGE_SIZE_ID, 900);*/
        spatparams.addParameter(AbstractRStarTreeFactory.Parameterizer.INSERTION_STRATEGY_ID, ApproximativeLeastOverlapInsertionStrategy.class);
        spatparams.addParameter(ApproximativeLeastOverlapInsertionStrategy.Parameterizer.INSERTION_CANDIDATES_ID, 1);
        spatparams.addParameter(RStarTreeFactory.Parameterizer.BULK_SPLIT_ID, SortTileRecursiveBulkSplit.class);

        double[][] d = new double[manh_tree.size()][2];
        for (int i=0;i<manh_tree.size();i++){
            d[i][0] = manh_tree.get(i)[0];
            d[i][1] = manh_tree.get(i)[1];
        }

        String[] h = new String[l.size()];
        for(int i=0;i<l.size();i++){
            h[i] = l.get(i);
        }

        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(d,h);

        Collection<IndexFactory<?, ?>> indexFactories = new ArrayList();

        RStarTreeFactory<DoubleVector> f =
                ClassGenericsUtil.parameterizeOrAbort(RStarTreeFactory.class, spatparams);
        indexFactories.add(f);

        db = new StaticArrayDatabase(dbc, indexFactories);
        db.initialize();
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

        manh_tree.add(new double[]{start_man_dist,end_man_dist});

        t.put(tr.getName(),tr);
        l.add(tr.getName());
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
        double query_dist = query.dist;

        Double s_q_dist = getSignedManhattanDist(q_start, criteria[0]);
        Double e_q_dist = getSignedManhattanDist(q_end, criteria[1]);
        DoubleVector v1_start = DoubleVector.FACTORY.newNumberVector(new double[]{s_q_dist, e_q_dist});

        initialize();

        Relation<DoubleVector> rep = db.getRelation(TypeUtil.DOUBLE_VECTOR_FIELD);
        Relation<LabelList> labelss = db.getRelation(TypeUtil.STRING);

        DistanceQuery<DoubleVector> eucl = db.getDistanceQuery(rep, EuclideanDistanceFunction.STATIC);

        RangeQuery<DoubleVector> rangeq = db.getRangeQuery(eucl, rep.size());
        DoubleDBIDList idds = rangeq.getRangeForObject(v1_start, query_dist * 1.5);

        int i = 0;
        TrajectoryHolder poss = new TrajectoryHolder();
        for (DoubleDBIDListIter res = idds.iter(); res.valid(); res.advance(), i++) {;
            poss.addTrajectory(String.valueOf(labelss.get(res)), t.get(String.valueOf(labelss.get(res))));
        }
        return poss;
    }

    @Override
    public int size() {
        return size;
    }
}
