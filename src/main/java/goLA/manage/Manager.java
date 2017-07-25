package goLA.manage;

import goLA.data.Tree;
import goLA.model.Trajectory;

import java.util.List;

public interface Manager {

    void makeStructure(String path);

    List<List<String>> findResult(String path);

    Tree getTree();
}
