package test;

import goLA.data.StartRTree;
import goLA.data.Tree;
import goLA.io.DataImporter;
import goLA.model.Coordinate;
import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;
import goLA.utils.DouglasPeucker;
import goLA.utils.EuclideanDistance;
import goLA.utils.FrechetDistance;

import java.util.List;

//TODO : delete class

/**
 * Created by stem-dong-li on 17. 7. 6.
 */
public class SimplificationFrechetTest {
    private static double MAX_RAN = 40000;
    private static double MIN_RAN = 5000;

    public static void main(String[] args) {
        Tree tree = new StartRTree();
        DataImporter di = new DataImporter();
        di.loadFiles("dataset.txt", tree);
        Object[] obj_list = tree.getHolder().values().toArray();
        for (int i = 0; i < 300; i++) {
            int index = (int) (Math.random() * (tree.size() - 1));
            double q_dist = MIN_RAN + (Math.random() * MAX_RAN);
            Trajectory q = (Trajectory) obj_list[index];
            System.out.println("--- " + i + " : " + index + " ---");
            System.out.println("dist : " + q_dist);
            double q_maxEpsilon = DouglasPeucker.getMaxEpsilon(q);
            Trajectory simple = DouglasPeucker.getReduced(q, q_maxEpsilon);

            TrajectoryQuery query = new TrajectoryQuery(q, q_dist);
            List<Trajectory> ret = tree.getPossible(query);

            Coordinate q_start = q.getCoordinates().get(0);
            Coordinate q_end = q.getCoordinates().get(q.getCoordinates().size() - 1);

            ret.forEach(e -> {
                Trajectory C = e;
                Coordinate c_start = C.getCoordinates().get(0);

                Coordinate c_end = C.getCoordinates().get(C.getCoordinates().size() - 1);
                if (EuclideanDistance.distance(c_start, q_start) <= q_dist) {
                    if (EuclideanDistance.distance(c_end, q_end) <= q_dist) {
                        if (!FrechetDistance.decisionDP(q, DouglasPeucker.getReduced(C, q_maxEpsilon),
                                q_dist + q_maxEpsilon)) {
                            if (!FrechetDistance.decisionDP(q, C, q_dist)) {

                            } else {
                                System.out.println("wrong");
                            }
                        }
                        if (FrechetDistance.decisionDP(q,
                                DouglasPeucker.getReduced(C, q_maxEpsilon),
                                q_dist - q_maxEpsilon)) {
                            if (FrechetDistance.decisionDP(q, C, q_dist)) {

                            } else {
                                System.out.println("is Result wrong");
                            }
                        }

                    }
                }

            });

        }
    }
}
