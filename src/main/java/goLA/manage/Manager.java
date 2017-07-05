package goLA.manage;

import goLA.data.Tree;
import goLA.filter.Filter;
import goLA.io.DataExporter;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;

import java.util.HashSet;
import java.util.List;

public interface Manager {

    void setFilter(Filter ft);

    void makeStructure(String path);

    List<TrajectoryHolder> findResult(String query_path, DataExporter de);

    Tree getTree();

    HashSet<Trajectory> makeStructureToVisualize(String path);
}
