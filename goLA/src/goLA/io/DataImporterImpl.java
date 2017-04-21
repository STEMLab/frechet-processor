package goLA.io;

import java.io.*;
import java.util.List;

import goLA.model.*;

public class DataImporterImpl implements DataImporter {

	@Override
	public void loadFiles(String src, Trajectories trj) {
		try {
		      
		      BufferedReader in = new BufferedReader(new FileReader(src));
		      String s;

		      while ((s = in.readLine()) != null) {
		    	  Trajectory coords = getCoordList(s);
		    	  trj.addTrajectory(s, coords);
		      }
		      in.close();
		      
		    } catch (IOException e) {
		        System.err.println(e); 
		        System.exit(1);
		    }
	}

	private Trajectory getCoordList(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
