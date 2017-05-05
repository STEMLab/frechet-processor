package goLA.manage;

import goLA.data.Tree;
import goLA.io.DataExporter;
import goLA.model.TrajectoryHolder;

import java.util.List;

public interface Manager {

    void makeStructure(String path);

    List<TrajectoryHolder> findResult(String query_path);

    Tree getTree();
}
