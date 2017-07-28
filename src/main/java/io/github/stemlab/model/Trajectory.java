package io.github.stemlab.model;

import java.util.List;

public class Trajectory {
    private Trajectory simplified;
    private String name;
    private List<Coordinate> coordinates;

    public Trajectory() {
        this.simplified = null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public Trajectory getSimplified() {
        return simplified;
    }

    public void setSimplified(Trajectory simplified) {
        this.simplified = simplified;
    }

}
