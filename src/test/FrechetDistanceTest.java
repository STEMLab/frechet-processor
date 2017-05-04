import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import goLA.compute.*;
import goLA.io.DataExporter;
import goLA.manage.Manager;
import goLA.manage.ManagerImpl;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;

/**
 * Just simple test class. If algorithm changed, we can run it to check result validity.
 */
public class FrechetDistanceTest {

    private static String TEST_DATA_SET_PATH = "src/test/data/dataset.txt";
    private static String TEST_QUERY_PATH = "src/test/data/queries.txt";

    public static void main(String[] args) throws IOException {

        System.out.println("Testing...");

        int passed = 0, failed = 0, count = 0;

        Manager manager = new ManagerImpl(new SimpleFrechet());
        manager.makeStructure(TEST_DATA_SET_PATH);
        List<TrajectoryHolder> result = manager.findResult(TEST_QUERY_PATH);


        if (result.get(0).getTrajectories().size() != 2) {
            System.out.printf("%s - Test '%s' - failed %n", ++count, "Query 1 failed");
            failed++;
        } else {
            System.out.printf("%s - Test '%s' - passed %n", ++count, "Query 1 proceed");
            passed++;
        }

        if (result.get(1).getTrajectories().size() != 1) {
            System.out.printf("%s - Test '%s' - failed %n", ++count, "Query 2 failed");
            failed++;
        } else {
            System.out.printf("%s - Test '%s' - passed %n", ++count, "Query 2 proceed");
            passed++;
        }

        System.out.printf("%nResult : Total : %d, Passed: %d, Failed %d", count, passed, failed);

    }

}
