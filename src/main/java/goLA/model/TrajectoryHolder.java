package goLA.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import goLA.exceptions.CustomException;

public class TrajectoryHolder {

    private HashMap<String, Trajectory> trajectories = new HashMap<>();

    public void addTrajectory(String a, Trajectory coords) {
        trajectories.put(a, coords);
    }

    public int size() {
        return trajectories.size();
    }

    public List<TrajectoryQuery> getQueryTrajectory(String query_path) {
    	List<TrajectoryQuery> list = new ArrayList<>();
    	try (Stream<String> stream = Files.lines(Paths.get(query_path))) {
    		stream.forEach(e -> {
    			String lines[] = e.split("\\s+");
    			if (lines.length != 2) new CustomException("Query Line doesn't have two properties");
    			
    			Trajectory q_tr = trajectories.get(lines[0]);
    			double dist = Double.parseDouble(lines[1]);
    			
    			if (q_tr == null){
    				new CustomException("Error");
    			}
    			
    			TrajectoryQuery temp = new TrajectoryQuery(q_tr, dist);
    			
    			list.add(temp);
    			
    			}
    		);

    	}catch (NoSuchFileException e){
    		new CustomException("query file not found");
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    	return list;
    }
}