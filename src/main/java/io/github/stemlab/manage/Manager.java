package io.github.stemlab.manage;

import io.github.stemlab.data.Tree;
import io.github.stemlab.filter.Filter;
import io.github.stemlab.io.DataExporter;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;

import java.io.IOException;
import java.util.List;

public interface Manager {

    void setFilter(Filter ft);

    void makeStructure(String path);

    int process(String path, DataExporter de) throws IOException;

    List<List<String>> TestFindResult(String path, DataExporter de);

    List<Trajectory> getPossible(Query query);

    Tree getTree();
}
