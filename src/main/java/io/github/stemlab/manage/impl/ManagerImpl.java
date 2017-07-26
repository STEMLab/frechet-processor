package io.github.stemlab.manage.impl;

import io.github.stemlab.data.Index;
import io.github.stemlab.io.DataExporter;
import io.github.stemlab.io.DataImporter;
import io.github.stemlab.manage.Manager;
import io.github.stemlab.model.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter dataImporter;
    private DataExporter dataExporter;
    private Index index;

    public ManagerImpl(Index index, DataImporter dataImporter, DataExporter dataExporter) {
        this.dataImporter = dataImporter;
        this.index = index;
        this.dataExporter = dataExporter;
    }

    @Override
    public void makeStructure(String path) {
        dataImporter.loadFiles(path, index);
    }

    @Override
    public void processQuery(String path) throws IOException {
        List<Query> queries = dataImporter.getQueries(path);
        int counter = 0;
        for (Query query : queries) {
            System.out.println("\n\n---- Processing query: " + query.getTrajectory().getName() + ", with query distance: " + query.getDistance() + " -------");
            dataExporter.export(index.getQueryResult(query), counter);
            counter++;
        }
    }

    @Override
    public Index getIndex() {
        return this.index;
    }

    @Override
    public List<HashSet<String>> processQueryAndGetResult(String path) throws IOException {
        List<HashSet<String>> result = new ArrayList<>();
        List<Query> queries = dataImporter.getQueries(path);
        for (Query query : queries) {
            System.out.println("\n\n---- Processing query: " + query.getTrajectory().getName() + ", with query distance: " + query.getDistance() + " -------");
            result.add(index.getQueryResult(query));
        }
        return result;
    }

}
