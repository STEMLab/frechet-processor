package goLA.model;

import java.util.List;

public class Trajectory {

	private String name;

	private List<Coordinates> coordinates;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Coordinates> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}
}
