package goLA.model;

import java.util.List;

public class Trajectory {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	private List<Coordinates<Double, Double>> coordinates;

	public List<Coordinates<Double, Double>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinates<Double, Double>> coordinates) {
		this.coordinates = coordinates;
	}
}
