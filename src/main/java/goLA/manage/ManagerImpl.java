package goLA.manage;

import goLA.io.DataExporter;
import goLA.io.DataImporter;
import goLA.model.TrajectoryHolder;

import java.util.List;

public class ManagerImpl implements Manager {

    private DataImporter di = new DataImporter();
    private TrajectoryHolder trajectoryHolder = new TrajectoryHolder();

    @Override
    public void makeStructure(String path) {
        di.loadFiles(path, trajectoryHolder);
    }

    @Override
    public List<List<String>> findResult(String query_path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void printResult(DataExporter dx, List<List<String>> result) {
        // TODO Auto-generated method stub

    }

    @Override
    public TrajectoryHolder getTrajectoryHolder() {
        return this.trajectoryHolder;
    }


}
