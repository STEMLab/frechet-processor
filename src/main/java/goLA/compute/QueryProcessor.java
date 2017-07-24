package goLA.compute;

import goLA.model.Query;
import goLA.model.Trajectory;

import java.util.List;

public interface QueryProcessor {
    /**
     * Among trajectories in trh, only return Frechet distance with query trajectory is lower than query distance.
     *
     * @param query
     * @param trh
     * @return
     */
    List<Trajectory> query(Query query, List<Trajectory> trh);
}
