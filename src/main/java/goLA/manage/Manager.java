package goLA.manage;

import goLA.data.Tree;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;

import java.util.HashSet;
import java.util.List;

public interface Manager {

    void makeStructure(String path);

    List<TrajectoryHolder> findResult(String query_path);

    Tree getTree();

    HashSet<Trajectory> makeStructureToVisualize(String path);
}
