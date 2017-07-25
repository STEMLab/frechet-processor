package io.github.stemlab.data;

import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.util.HashSet;


public interface Index {
    void addTrajectory(String id, Trajectory trajectory);

    void initialize();

    HashSet<String> getQueryResult(Query query);

    int size();
}
