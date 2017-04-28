package goLA.manage;

import goLA.io.DataExporter;
import goLA.model.TrajectoryHolder;

import java.util.List;

public interface Manager {

    void makeStructure(String path);

    List<TrajectoryHolder> findResult(String query_path);

    TrajectoryHolder getTrajectoryHolder();
}
