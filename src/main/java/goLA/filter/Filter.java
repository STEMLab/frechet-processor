package goLA.filter;

import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public interface Filter {
    TrajectoryHolder doFilter(TrajectoryQuery q, TrajectoryHolder trh);
}
