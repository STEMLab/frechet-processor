package test;

import goLA.data.impl.RTree;
import goLA.data.Tree;
import goLA.io.DataImporter;
import goLA.model.Query;
import goLA.model.Trajectory;
import goLA.utils.DiscreteFrechetDistance;
import goLA.utils.DouglasPeucker;
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
        Tree tree = new RTree();
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

            Query query = new Query(q, q_dist);
            List<Trajectory> ret = tree.getPossible(query);

            ret.forEach(e -> {
                Trajectory C = e;
                if (!FrechetDistance.decisionDP(q, DouglasPeucker.getReduced(C, q_maxEpsilon),
                        q_dist + q_maxEpsilon)) {
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
}
