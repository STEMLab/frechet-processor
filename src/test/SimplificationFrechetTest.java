
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.data.Index;
import io.github.stemlab.data.impl.TestIndexImpl;
import io.github.stemlab.io.DataImporter;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.DiscreteFrechetDistance;
import io.github.stemlab.utils.DouglasPeucker;
import io.github.stemlab.utils.EuclideanDistance;
import io.github.stemlab.utils.FrechetDistance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 6.
 */
public class SimplificationFrechetTest {
    private static double MAX_RAN = 40000;
    private static double MIN_RAN = 5000;

    public static void main(String[] args) {
        TestIndexImpl tree = new TestIndexImpl();
        DataImporter di = new DataImporter();
        di.loadFiles("dataset.txt", tree);
        System.out.println("--- Complete put All data in Tree ---");
        for (int i = 0; i < 300; i++) {
            int index = (int) (Math.random() * (tree.size() - 1));
            double q_dist = MIN_RAN + (Math.random() * MAX_RAN);
            Trajectory q = (Trajectory)tree.holder.values().toArray()[index];
            System.out.println("--- " + i + " : " + index + " ---");
            System.out.println("dist : " + q_dist);

            Query query = new Query(q, q_dist);
            Coordinate start = query.getTrajectory().getCoordinates().get(0);
            Coordinate end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);

            DoubleDBIDList result = tree.rStarTree.search(new double[]{start.getPointX(), start.getPointY()}, q_dist);

            Trajectory simple = DouglasPeucker.getReduced(query.getTrajectory(), DouglasPeucker.getMaxEpsilon(query.getTrajectory()));
            double maxEpsilon = DouglasPeucker.getMaxEpsilon(query.getTrajectory());

            for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
                Trajectory trajectory = tree.holder.get(tree.rStarTree.getRecordName(x));
                Coordinate last = trajectory.getCoordinates().get(trajectory.getCoordinates().size() - 1);
                if (EuclideanDistance.distance(last, end) <= q_dist) {
                    Trajectory C = trajectory;
                    if (!FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(C, maxEpsilon),
                            q_dist + maxEpsilon * 2)) {
                        if (!FrechetDistance.decisionDP(q, C, q_dist)) {

                        } else {
                            System.out.println("wrong");
                        }
                    }
                    if (DiscreteFrechetDistance.decisionDP(q,
                            DouglasPeucker.getReduced(C, maxEpsilon),
                            q_dist - maxEpsilon)) {
                        if (FrechetDistance.decisionDP(q, C, q_dist)) {

                        } else {
                            System.out.println("is Result wrong");
                        }
                    }
                }

            }
        }
    }

}
