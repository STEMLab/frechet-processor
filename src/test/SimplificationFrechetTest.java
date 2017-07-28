import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDListIter;
import io.github.stemlab.data.impl.TestIndexImpl;
import io.github.stemlab.io.Importer;
import io.github.stemlab.model.Coordinate;
import io.github.stemlab.model.Query;
import io.github.stemlab.model.Trajectory;
import io.github.stemlab.utils.*;

/**
 * Created by stem-dong-li on 17. 7. 6.
 */
public class SimplificationFrechetTest {
    private static double MAX_RAN = 40000;
    private static double MIN_RAN = 5000;

    public static void main(String[] args) {
        TestIndexImpl tree = new TestIndexImpl();
        Importer di = new Importer();
        di.loadFiles("dataset.txt", tree);
        System.out.println("--- Complete put All data in Tree ---");
        for (int i = 0; i < 300; i++) {
            int index = (int) (Math.random() * (tree.size() - 1));
            double dist = MIN_RAN + (Math.random() * MAX_RAN);
            Trajectory q = (Trajectory) tree.holder.values().toArray()[index];
            System.out.println("--- " + i + " : " + index + " ---");
            System.out.println("dist : " + dist);

            Query query = new Query(q, dist);
            Coordinate start = query.getTrajectory().getCoordinates().get(0);
            Coordinate end = query.getTrajectory().getCoordinates().get(query.getTrajectory().getCoordinates().size() - 1);

            DoubleDBIDList result = tree.rStarTree.search(new double[]{start.getPointX(), start.getPointY()}, dist);

            Trajectory simple = StraightFowrad.getReduced(query.getTrajectory(), dist);
            q.setSimplified(simple);
            for (DoubleDBIDListIter x = result.iter(); x.valid(); x.advance()) {
                Trajectory trajectory = tree.holder.get(tree.rStarTree.getRecordName(x));
                Coordinate last = trajectory.getCoordinates().get(trajectory.getCoordinates().size() - 1);
                if (EuclideanDistance.distance(last, end) <= dist) {
                    Trajectory simple_query = query.getTrajectory().getSimplified();
                    Trajectory simple_trajectory = StraightFowrad.getReduced(trajectory, query.getDistance());
                    double modified_dist = dist + 2 * dist * StraightFowrad.Epslion * StraightFowrad.Constant;
                    double modified_dist2 = dist - 1 * dist * StraightFowrad.Epslion * StraightFowrad.Constant;
                    if (!FrechetDistance.decision(simple_query, simple_trajectory, modified_dist)){
                        if (!FrechetDistance.decision(q, trajectory, dist)) {

                        } else {
                            System.out.println("wrong");
                        }
                    }
                    if (DiscreteFrechetDistance.decision(q, simple_trajectory, modified_dist2) ){
                        if (FrechetDistance.decision(q, trajectory, dist)) {

                        } else {

                            System.out.println("is Result wrong");
                        }
                    }

                }

            }
        }
    }

}
