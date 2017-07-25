package goLA.data;

import goLA.model.Query;
import goLA.model.Trajectory;

import java.util.HashSet;


public interface Index {
    void addTrajectory(String id, Trajectory trajectory);

    void initialize();

    HashSet<String> getQueryResult(Query query);

    int size();
}
