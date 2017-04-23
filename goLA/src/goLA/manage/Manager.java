package goLA.manage;

import goLA.io.DataExporter;
import goLA.model.TrajectoryHolder;

import java.util.List;

public interface Manager {

    void makeStructure(String path);

    List<List<String>> findResult(String query_path);

    void printResult(DataExporter dx, List<List<String>> result);

    TrajectoryHolder getTrajectoryHolder();
}
