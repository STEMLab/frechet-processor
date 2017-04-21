package goLA.manage;

import java.util.List;

import goLA.io.*;
import goLA.model.*;

public class ManagerImpl implements Manager{
	private Trajectories trajectories;
	
	public ManagerImpl(){
		
	}

	@Override
	public void makeStructure(DataImporter dti, String src_path) {
		dti.loadFiles(src_path, trajectories);
		
	}

	@Override
	public List<List<String>> findResult(String query_path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printResult(DataExporter dx, List<List<String>> result) {
		// TODO Auto-generated method stub
		
	}
	
	
	
		
}
