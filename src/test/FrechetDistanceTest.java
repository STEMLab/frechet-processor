package test;

import goLA.compute.impl.QueryProcessorImpl;
import goLA.data.impl.RTree;
import goLA.filter.impl.SimplificationFilter;
import goLA.io.DataImporter;
import goLA.manage.Manager;
import goLA.manage.ManagerImpl;
import goLA.model.Trajectory;

import java.io.IOException;
import java.util.List;

/**
 * Just simple test class. If algorithm changed, run it to check result validity.
 */
public class FrechetDistanceTest {

    private static String TEST_DATA_SET_PATH = "src/test/data/dataset.txt";
    private static String TEST_QUERY_PATH = "src/test/data/queries.txt";

    public static void main(String[] args) throws IOException {

        System.out.println("Testing...");

        int passed = 0, failed = 0, count = 0;

        int[] results = {5, 2, 4, 4, 3, 5, 4, 3, 3, 5};

        Manager manager = new ManagerImpl(new QueryProcessorImpl(), new RTree(), new DataImporter(), new SimplificationFilter());

        manager.makeStructure(TEST_DATA_SET_PATH);
        List<List<Trajectory>> result = manager.findResult(TEST_QUERY_PATH, null);

        System.out.println("\n");
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).size() != results[i]) {
                System.out.printf("%s - Test '%s' - failed %n", ++count, "Query " + (i + 1));
                failed++;
            } else {
                System.out.printf("%s - Test '%s' - passed %n", ++count, "Query " + (i + 1));
                passed++;
            }
        }

        System.out.printf("%nResult : Total : %d, Passed: %d, Failed %d", count, passed, failed);

    }

}
