package goLA.model;

import java.util.HashMap;

public class TrajectoriesImpl implements Trajectories{
	
	private HashMap<String, Trajectory> t_map;

	@Override
	public void addTrajectory(String a, Trajectory coords) {
		// TODO Auto-generated method stub
		t_map.put(a, coords);
	}

	
	
}
