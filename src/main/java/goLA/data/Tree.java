package goLA.data;

import goLA.model.Trajectory;
import goLA.model.Query;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem_dong on 2017-05-02.
 */
public interface Tree {
    void addTrajectory(String id, Trajectory tr);

    void initialize();

    List<Trajectory> getPossible(Query query);

    ConcurrentHashMap<String, Trajectory> getHolder();

    int size();
}
