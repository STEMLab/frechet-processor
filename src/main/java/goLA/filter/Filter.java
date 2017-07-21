package goLA.filter;

import goLA.model.Trajectory;
import goLA.model.Query;

import java.util.List;

/**
 * Created by stem-dong-li on 17. 7. 4.
 */
public interface Filter {
    List<Trajectory> doFilter(Query q, List<Trajectory> trh);
}
