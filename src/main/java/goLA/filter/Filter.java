package goLA.filter;

import goLA.model.Trajectory;
import goLA.model.TrajectoryQuery;

import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public interface Filter {
    List<Trajectory> doFilter(TrajectoryQuery q, List<Trajectory> trh);
}
