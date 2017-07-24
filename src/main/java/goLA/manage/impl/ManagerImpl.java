package goLA.manage.impl;

import goLA.compute.QueryProcessor;
import goLA.data.Tree;
import goLA.filter.Filter;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.model.Query;
import goLA.model.Trajectory;

import java.util.ArrayList;
import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter dataImporter;
    private QueryProcessor queryProcessor;
    private Tree tree;
    private Filter filter = null;

    //Determine How to calculate Query by using constructor parameter
    public ManagerImpl(QueryProcessor queryProcessor, Tree tree, DataImporter dataImporter, Filter filter) {
        this.queryProcessor = queryProcessor;
        this.dataImporter = dataImporter;
        this.tree = tree;
        this.filter = filter;
    }

    public ManagerImpl(QueryProcessor queryProcessor, Tree tree, DataImporter dataImporter) {
        this.queryProcessor = queryProcessor;
        this.dataImporter = dataImporter;
        this.tree = tree;
    }

    @Override
    public void makeStructure(String path) {
        dataImporter.loadFiles(path, tree);
    }

    @Override
    public List<List<Trajectory>> findResult(String path) {
        List<List<Trajectory>> result = new ArrayList<>();
        List<Query> queries = dataImporter.getQueries(path);

        for (Query query : queries) {
            if (query.getDistance() == 0) {
                List<Trajectory> q_res = new ArrayList<>();
                q_res.add(query.getTrajectory());
                result.add(q_res);
            }
            System.out.println("\n\n---- Processing query: " + query.getTrajectory().getName() + ", with query distance: " + query.getDistance() + " -------");
            List<Trajectory> possibleTrajectoryHolder = tree.getPossible(query);


            List<Trajectory> filteredList;

            if (this.filter != null) {
                filteredList = filter.doFilter(query, possibleTrajectoryHolder);
            } else {
                filteredList = possibleTrajectoryHolder;
            }

            List<Trajectory> queryResult = queryProcessor.query(query, filteredList);
            result.add(queryResult);

        }

        return result;
    }

    @Override
    public Tree getTree() {
        return this.tree;
    }


}
