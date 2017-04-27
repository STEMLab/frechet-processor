package goLA.io;

import goLA.model.TrajectoryHolder;

public interface DataImporter {
    public void loadFiles(String path, TrajectoryHolder trajectoryHolder);
}
