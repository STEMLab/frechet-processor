package goLA.manage;

import goLA.data.Tree;
import goLA.filter.Filter;
import goLA.io.DataExporter;
import goLA.model.Trajectory;

import java.io.IOException;
import java.util.List;

public interface Manager {

    void setFilter(Filter ft);

    void makeStructure(String path);

    int process(String path, DataExporter de) throws IOException;

    Tree getTree();
}
