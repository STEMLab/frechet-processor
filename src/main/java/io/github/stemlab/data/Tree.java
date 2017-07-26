package io.github.stemlab.data;

import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by stem_dong on 2017-05-02.
 */
public interface Tree {
    void addTrajectory(String id, Trajectory tr);

    void initialize();

    DoubleDBIDList rangeQuery(Query query);

    int size();

    String getRecordName(DoubleDBIDListIter x);

    Trajectory getTrajectory(String key);

    HashMap<String, Trajectory> getHolder();

}
