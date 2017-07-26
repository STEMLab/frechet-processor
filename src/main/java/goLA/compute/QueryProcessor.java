package goLA.compute;

import goLA.data.Tree;
import goLA.filter.Filter;
import goLA.model.Query;
import goLA.model.Trajectory;

import java.util.List;

public interface QueryProcessor {
    /**
     * Among trajectories in trh, only return Frechet distance with query trajectory is lower than query distance.
     *
     */
    List<String> query(Query query, Tree tree, Filter filter);
}
