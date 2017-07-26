package goLA.data;

import com.sun.xml.internal.ws.util.QNameMap;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import goLA.model.Query;
import goLA.model.Trajectory;

import java.util.List;
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

    ConcurrentHashMap<String, Trajectory> getHolder();

}
