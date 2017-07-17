package goLA.data.tree;

import de.lmu.ifi.dbs.elki.data.DoubleVector;
import de.lmu.ifi.dbs.elki.data.LabelList;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.datasource.DatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.EuclideanDistanceFunction;
import de.lmu.ifi.dbs.elki.index.IndexFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.AbstractRStarTreeFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.rstar.RStarTreeFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.bulk.FileOrderBulkSplit;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.insert.ApproximativeLeastOverlapInsertionStrategy;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.split.AngTanLinearSplit;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;

import java.time.Duration;
import java.time.Instant;
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

    public void add(String id, double[] coordinates) {
        this.tree.add(new double[]{coordinates[0], coordinates[1]});
        this.treeLabels.add(id);
    }

    public DoubleDBIDList search(double[] point, double dist) {
        return db.getRangeQuery(db.getDistanceQuery(vectors, EuclideanDistanceFunction.STATIC), vectors.size()).getRangeForObject(DoubleVector.FACTORY.newNumberVector(new double[]{point[0], point[1]}), dist);
    }

    public String getRecordName(DoubleDBIDListIter res) {
        return String.valueOf(labelListRelation.get(res));
    }

    public void initialize() {

        Instant start = Instant.now();

        double[][] d = new double[tree.size()][2];
        d = tree.toArray(d);

        String[] h = treeLabels.parallelStream().toArray(String[]::new);

        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(d, h);

        db = new StaticArrayDatabase(dbc, getFactories());
        db.initialize();

        vectors = db.getRelation(TypeUtil.DOUBLE_VECTOR_FIELD);
        labelListRelation = db.getRelation(TypeUtil.STRING);

        Instant end = Instant.now();
        System.out.println("\nMake Tree time : " + Duration.between(start, end) + "\n");
    }

    private ListParameterization getParams() {
        //TODO play with params (see perfomance)
        ListParameterization params = new ListParameterization();
        params.addParameter(INDEX_ID, RStarTreeFactory.class);
        params.addParameter(AbstractRStarTreeFactory.Parameterizer.INSERTION_STRATEGY_ID, ApproximativeLeastOverlapInsertionStrategy.class);
        params.addParameter(RStarTreeFactory.Parameterizer.SPLIT_STRATEGY_ID, AngTanLinearSplit.class);
        params.addParameter(RStarTreeFactory.Parameterizer.BULK_SPLIT_ID, FileOrderBulkSplit.class);
        return params;
    }

    private Collection<IndexFactory<?, ?>> getFactories() {
        Collection<IndexFactory<?, ?>> indexFactories = new ArrayList();
        RStarTreeFactory<DoubleVector> factory =
                ClassGenericsUtil.parameterizeOrAbort(RStarTreeFactory.class, getParams());
        indexFactories.add(factory);
        return indexFactories;
    }
}
