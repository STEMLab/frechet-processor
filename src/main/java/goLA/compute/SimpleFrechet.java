package goLA.compute;

import java.util.HashMap;
import java.util.List;

import goLA.model.Coordinates;
import goLA.model.Trajectory;
import goLA.model.TrajectoryHolder;
import goLA.model.TrajectoryQuery;

public class SimpleFrechet implements QueryProcessor {

	@Override
	public TrajectoryHolder findTrajectoriesFrom(TrajectoryQuery query, TrajectoryHolder trh){
		TrajectoryHolder result = new TrajectoryHolder();
		
		HashMap<String, Trajectory> trajectories = trh.getTrajectories();

		trajectories.forEach((key,value)->{
			if(decideIn_FDist(query.q_tr, value, query.dist)) result.addTrajectory(key, value);
		});
		
		return result;
	}
	
	private boolean decideIn_FDist(Trajectory q_tr, Trajectory t_tr, double dist){
		int p,q;
		
		List<Coordinates<Double, Double>> p_coordinates = q_tr.getCoordinates();
		p = p_coordinates.size() - 1;
		
		List<Coordinates<Double, Double>> q_coordinates = t_tr.getCoordinates();
		q = q_coordinates.size() - 1;
		
		boolean[][] bottom = new boolean[p+1][q+1];
		boolean[][] left = new boolean[p+1][q+1];
		
		boolean[][] result_bottom = new boolean[p+1][q+1];
		boolean[][] result_left = new boolean[p+1][q+1];
		
		for (int i = 0 ; i < p  ; i++){
			for (int j = 0 ; j < q  ; j++){
				//i && (j,j+1)
				left[i][j] = (dist >= calcDistancePointAndLine(p_coordinates.get(i),q_coordinates.get(j),q_coordinates.get(j+1)));					
						 
				//j && (i,i+1)
				bottom[i][j] = (dist >= calcDistancePointAndLine(q_coordinates.get(j),p_coordinates.get(i),p_coordinates.get(i+1)) );
			}
		}
		
		for (int i = 0 ; i < p ; i++){
			result_bottom[i][0] = bottom[i][0];
			bottom[i][q] = (dist >= calcDistancePointAndLine(q_coordinates.get(q),p_coordinates.get(i),p_coordinates.get(i+1)));	
		}
		
		for (int j = 0 ; j < q ; j++){
			result_left[0][j] = left[0][j];
			left[p][j] = (dist >= calcDistancePointAndLine(p_coordinates.get(p),q_coordinates.get(j),q_coordinates.get(j+1)));
		}
		
		for (int i = 0 ; i < p ; i++){
			for (int j = 0 ; j < q ; j++){
				if (!result_left[i][j] && !result_bottom[i][j]) {
					result_left[i+1][j] = false;
					result_bottom[i][j+1] = false;
				}	
				else{
					result_bottom[i][j+1] = bottom[i][j+1];
					result_left[i+1][j] = left[i+1][j];
				}
				
			}
		}
		if (result_left[p][q-1] && result_bottom[p-1][q]){
			if (EuclideanDistance(p_coordinates.get(p),q_coordinates.get(q)) <= dist && EuclideanDistance(p_coordinates.get(0),q_coordinates.get(0)) <= dist)
				return true;	
		}
		return false;
	}
	
	private double calcDistancePointAndLine(Coordinates<Double,Double> point, Coordinates<Double,Double> start, Coordinates<Double,Double> end){
		double lineLen = EuclideanDistance(start, end);
		if (lineLen == 0) return EuclideanDistance(point, start);
		
		double prj = ((point.getPointX() - start.getPointX())*(end.getPointX()-start.getPointX()) +
				(point.getPointY() - start.getPointY())*(end.getPointY()-start.getPointY())) / lineLen;
		if (prj < 0) return EuclideanDistance(point, start);
		else if (prj > lineLen) return EuclideanDistance(point, end);
		else {
			//return normal_projection
			return Math.abs((-1)*(point.getPointX()-start.getPointX())*(end.getPointY()-start.getPointY()) +
					(point.getPointY()-start.getPointY())*(end.getPointX()-start.getPointX()) )/ lineLen;
		}
	}

	private double EuclideanDistance(Coordinates<Double, Double> start, Coordinates<Double, Double> end) {
		return Math.sqrt(Math.pow(end.getPointX() - start.getPointX(), 2) + Math.pow(end.getPointY() - start.getPointY(), 2));
	}
	
	

}
