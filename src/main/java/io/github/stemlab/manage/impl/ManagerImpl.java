package io.github.stemlab.manage.impl;

import io.github.stemlab.data.Index;
import io.github.stemlab.io.DataImporter;
import io.github.stemlab.manage.Manager;
import io.github.stemlab.model.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter dataImporter;
    private Index index;

    public ManagerImpl(Index index, DataImporter dataImporter) {
        this.dataImporter = dataImporter;
        this.index = index;
    }

    @Override
    public void makeStructure(String path) {
        dataImporter.loadFiles(path, index);
    }

    /**
     * For each query get possible trajectories list from index, then
     * filter list, then run query processor
     *
     * @param path
     * @return list of result trajectories
     */
    @Override
    public List<HashSet<String>> findResult(String path) {
        List<HashSet<String>> result = new ArrayList<>();
        List<Query> queries = dataImporter.getQueries(path);
        for (Query query : queries) {
            System.out.println("\n\n---- Processing query: " + query.getTrajectory().getName() + ", with query distance: " + query.getDistance() + " -------");
            result.add(index.getQueryResult(query));
        }

        return result;
    }

    public Index getIndex() {
        return this.index;
    }


}
