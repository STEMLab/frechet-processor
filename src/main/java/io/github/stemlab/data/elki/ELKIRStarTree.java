package io.github.stemlab.data.elki;

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
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.rstar.RStarTreeFactory;
import de.lmu.ifi.dbs.elki.index.tree.spatial.rstarvariants.strategies.bulk.SortTileRecursiveBulkSplit;
import de.lmu.ifi.dbs.elki.persistent.AbstractPageFileFactory;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;

import java.util.ArrayList;
import java.util.Collection;

import static de.lmu.ifi.dbs.elki.database.AbstractDatabase.Parameterizer.INDEX_ID;


public class ELKIRStarTree {

    private ArrayList<double[]> tree = new ArrayList<>();
    private ArrayList<String> treeLabels = new ArrayList<>();
    private Database db;
    private Relation<LabelList> labelListRelation;
    private Relation<DoubleVector> vectors;

    /**
     * Add one by one to ArrayList before bulk upload on initializing tree
     *
     * @param id
     * @param coordinates
     */
    public void add(String id, double[] coordinates) {
        this.tree.add(new double[]{coordinates[0], coordinates[1]});
        this.treeLabels.add(id);
    }

    /**
     * Range query, accepts point coordinates and distance
     *
     * @param point
     * @param dist
     * @return list of points in given distance
     */
    public DoubleDBIDList search(double[] point, double dist) {
        return db.getRangeQuery(db.getDistanceQuery(vectors, EuclideanDistanceFunction.STATIC), vectors.size()).getRangeForObject(DoubleVector.FACTORY.newNumberVector(new double[]{point[0], point[1]}), dist);
    }

    public String getRecordName(DoubleDBIDListIter res) {
        return String.valueOf(labelListRelation.get(res));
    }

    public void initialize() {

        double[][] d = new double[tree.size()][2];
        d = tree.toArray(d);

        String[] h = treeLabels.parallelStream().toArray(String[]::new);

        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(d, h);

        db = new StaticArrayDatabase(dbc, getFactories());
        db.initialize();

        vectors = db.getRelation(TypeUtil.DOUBLE_VECTOR_FIELD);
        labelListRelation = db.getRelation(TypeUtil.STRING);

    }

    /**
     * Page size equal to dataset size.
     * Bulk loaded R* tree using Sort-Tile-Recursive (STR).
     *
     * @return params for constructing index
     */
    private ListParameterization getParams() {
        ListParameterization params = new ListParameterization();
        params.addParameter(INDEX_ID, RStarTreeFactory.class);
        params.addParameter(AbstractPageFileFactory.Parameterizer.PAGE_SIZE_ID, getPageSize());
        params.addParameter(RStarTreeFactory.Parameterizer.BULK_SPLIT_ID, SortTileRecursiveBulkSplit.class);
        return params;
    }

    private Collection<IndexFactory<?, ?>> getFactories() {
        Collection<IndexFactory<?, ?>> indexFactories = new ArrayList();
        RStarTreeFactory<DoubleVector> factory =
                ClassGenericsUtil.parameterizeOrAbort(RStarTreeFactory.class, getParams());
        indexFactories.add(factory);
        return indexFactories;
    }

    private int getPageSize() {
        if (tree.size() < 300) {
            return 300;
        } else {
            return tree.size();
        }
    }
}
