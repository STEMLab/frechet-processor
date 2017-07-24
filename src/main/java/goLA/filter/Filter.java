package goLA.filter;

import goLA.model.Query;
import goLA.model.Trajectory;

import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public interface Filter {
    List<Trajectory> doFilter(Query query, List<Trajectory> trajectories);
}
