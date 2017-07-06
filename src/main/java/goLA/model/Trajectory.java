package goLA.model;

import java.util.ArrayList;
import java.util.List;

public class Trajectory {

	private String name;

	private List<Coordinates> coordinates;

	public boolean isResult = false;

	public Double MaxEpsilon = null;
	public Trajectory simple = null;

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

    public Trajectory makeStraight() {
		Trajectory tr = new Trajectory();
		List<Coordinates> coord = new ArrayList<>();
		coord.add(this.coordinates.get(0));
		coord.add(this.coordinates.get(this.coordinates.size() - 1));
		tr.setCoordinates(coord);
		return tr;
    }
}
