package goLA.compute;

import goLA.model.Trajectory;
import goLA.model.Query;

import java.util.List;

public interface QueryProcessor {
    List<Trajectory> query(Query query, List<Trajectory> trh);
}
