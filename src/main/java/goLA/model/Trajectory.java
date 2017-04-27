package goLA.model;

import java.util.List;

public class Trajectory {

	private List<Coordinates<Double, Double>> coordinates;

	public List<Coordinates<Double, Double>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinates<Double, Double>> coordinates) {
		this.coordinates = coordinates;
	}
}
