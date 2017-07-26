package io.github.stemlab.compute;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.util.List;

public interface QueryProcessor {
    /**
     * Among trajectories in trh, only return Frechet distance with query trajectory is lower than query distance.
     *
     */
    List<String> query(Query query, List<Trajectory> trh);

    boolean decision(Query q, Trajectory t);
}
