
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.data.Tree;
import io.github.stemlab.data.impl.RTree;
import io.github.stemlab.io.DataImporter;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.DiscreteFrechetDistance;
import io.github.stemlab.utils.DouglasPeucker;
import io.github.stemlab.utils.EuclideanDistance;
import io.github.stemlab.utils.FrechetDistance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 6.
 */
public class SimplificationFrechetTest {
    private static double MAX_RAN = 40000;
    private static double MIN_RAN = 5000;

    public static void main(String[] args) {
        Tree tree = new RTree();
        DataImporter di = new DataImporter();
        di.loadFiles("dataset.txt", tree);
        Object[] obj_list = tree.getHolder().values().toArray();
        for (int i = 0; i < 300; i++) {
            int index = (int) (Math.random() * (tree.size() - 1));
            double q_dist = MIN_RAN + (Math.random() * MAX_RAN);
            Trajectory q = (Trajectory)obj_list[index];
            System.out.println("--- " + i + " : " + index + " ---");
            System.out.println("dist : " + q_dist);
            double q_maxEpsilon = DouglasPeucker.getMaxEpsilon(q);
            Trajectory simple = DouglasPeucker.getReduced(q, q_maxEpsilon);

            Query query = new Query(q, q_dist);
            List<Trajectory> ret = getPossible(tree, query);

            ret.forEach(e -> {
                Trajectory C = e;
                if (!FrechetDistance.decisionDP(simple, DouglasPeucker.getReduced(C, q_maxEpsilon),
                        q_dist + q_maxEpsilon * 2)) {
                    if (!FrechetDistance.decisionDP(q, C, q_dist)) {

                    } else {
                        System.out.println("wrong");
                    }
                }
                if (DiscreteFrechetDistance.decisionDP(q,
                        DouglasPeucker.getReduced(C, q_maxEpsilon),
                        q_dist - q_maxEpsilon)) {
                    if (FrechetDistance.decisionDP(q, C, q_dist)) {

                    } else {
                        System.out.println("is Result wrong");
                    }
                }
            });

        }
    }

    public static List<Trajectory> getPossible(Tree tree, Query query) {
        DoubleDBIDList result = tree.rangeQuery(query);

        Coordinate end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);
        List<Trajectory> poss = new ArrayList<>();

        for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
            String key = tree.getRecordName(x);
            List<Coordinate> coordinates = tree.getTrajectory(key).getCoordinates();
            Coordinate last = coordinates.get(coordinates.size() - 1);
            if (EuclideanDistance.distance(last, end) <= query.getDistance())
                poss.add(tree.getTrajectory(key));
        }

        return poss;
    }


}
