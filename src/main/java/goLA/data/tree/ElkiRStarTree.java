package goLA.data.tree;

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
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;

import java.util.ArrayList;
import java.util.Collection;

import static de.lmu.ifi.dbs.elki.database.AbstractDatabase.Parameterizer.INDEX_ID;

/**
 * Created by Azamat on 5/15/2017.
 */
public class ElkiRStarTree {

    private ArrayList<double[]> tree = new ArrayList<>();
    private ArrayList<String> treeLabels = new ArrayList<>();
    private Database db;
    private Relation<LabelList> labelListRelation;
    private Relation<DoubleVector> vectors;
    private DistanceQuery<DoubleVector> euclidean;
    private RangeQuery<DoubleVector> rangeQuery;

    public void add(String id, double[] coordinates) {
        this.tree.add(new double[]{coordinates[0], coordinates[1]});
        this.treeLabels.add(id);
    }

    public DoubleDBIDList search(double[] point, double dist) {
        //TODO add custom annotation to run initialize by itself
        initialize();
        
        return rangeQuery.getRangeForObject(DoubleVector.FACTORY.newNumberVector(new double[]{point[0], point[1]}), dist);
    }

    public String getRecordName(DoubleDBIDListIter res) {
        return String.valueOf(labelListRelation.get(res));
    }

    private void initialize() {

        ListParameterization spatparams = new ListParameterization();
        spatparams.addParameter(INDEX_ID, RStarTreeFactory.class);
        spatparams.addParameter(AbstractRStarTreeFactory.Parameterizer.INSERTION_STRATEGY_ID, ApproximativeLeastOverlapInsertionStrategy.class);
        spatparams.addParameter(ApproximativeLeastOverlapInsertionStrategy.Parameterizer.INSERTION_CANDIDATES_ID, 1);
        spatparams.addParameter(RStarTreeFactory.Parameterizer.BULK_SPLIT_ID, SortTileRecursiveBulkSplit.class);

        //TODO avoid data copy
        double[][] d = new double[tree.size()][2];
        for (int i = 0; i < tree.size(); i++) {
            d[i][0] = tree.get(i)[0];
            d[i][1] = tree.get(i)[1];
        }

        String[] h = treeLabels.parallelStream().toArray(String[]::new);

        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(d, h);

        Collection<IndexFactory<?, ?>> indexFactories = new ArrayList();

        RStarTreeFactory<DoubleVector> f =
                ClassGenericsUtil.parameterizeOrAbort(RStarTreeFactory.class, spatparams);
        indexFactories.add(f);

        db = new StaticArrayDatabase(dbc, indexFactories);
        db.initialize();

        vectors = db.getRelation(TypeUtil.DOUBLE_VECTOR_FIELD);
        labelListRelation = db.getRelation(TypeUtil.STRING);
        euclidean = db.getDistanceQuery(vectors, EuclideanDistanceFunction.STATIC);
        rangeQuery = db.getRangeQuery(euclidean, vectors.size());
    }
}
