package goLA.model;

import java.util.ArrayList;
import java.util.List;

public class Trajectory {

    public boolean isResult = false;
    public Double MaxEpsilon = null;
    public Trajectory simple = null;
    private String name;
    private List<Coordinate> coordinates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public Trajectory makeStraight() {
        Trajectory tr = new Trajectory();
        List<Coordinate> coord = new ArrayList<>();
        coord.add(this.coordinates.get(0));
        coord.add(this.coordinates.get(this.coordinates.size() - 1));
        tr.setCoordinates(coord);
        return tr;
    }
}
