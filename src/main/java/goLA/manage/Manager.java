package goLA.manage;

import goLA.data.Tree;
import goLA.filter.Filter;
import goLA.io.DataExporter;
import goLA.model.Trajectory;

import java.util.List;

public interface Manager {

    void setFilter(Filter ft);

    void makeStructure(String path);

    List<List<String>> findResult(String path, DataExporter de);

    Tree getTree();
}
