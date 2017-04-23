package goLA.model;

import java.util.HashMap;

public class TrajectoryHolder {

    private HashMap<String, Trajectory> trajectories = new HashMap<>();

    public void addTrajectory(String a, Trajectory coords) {
        trajectories.put(a, coords);
    }
}
