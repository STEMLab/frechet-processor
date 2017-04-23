package goLA.test;

import goLA.manage.Manager;
import goLA.manage.ManagerImpl;
import goLA.model.TrajectoryHolder;

public class demo {

    public static void main(String[] args) {

        String src_path = "files/dataset.txt";
        String query_path = "files/queries.txt";

        Manager manager = new ManagerImpl();
        //DataExporter dx = new DataExporterImpl();

        manager.makeStructure(src_path);

        //get all data trajectories
        TrajectoryHolder trajectories = manager.getTrajectoryHolder();
        //List<List<String>> result = manager.findResult(query_path);

        //manager.printResult(dx, result);
    }

}
