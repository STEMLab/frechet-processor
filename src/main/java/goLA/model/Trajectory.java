package goLA.model;

import java.util.List;

public class Trajectory {

    private boolean isResult;
    private Double maxEpsilon;
    private Double avgEpsilon;

    //TODO remove
    private int simpleMode; // 1 : Max, 2 : Avg

    private Trajectory simplified;
    private String name;
    private List<Coordinate> coordinates;

    public Trajectory() {
        this.isResult = false;
        this.maxEpsilon = null;
        this.avgEpsilon = null;
        this.simpleMode = 0; // 1 : Max, 2 : Avg
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

    public boolean isResult() {
        return this.isResult;
    }

    public void setResult(boolean result) {
        this.isResult = result;
    }

    public Double getMaxEpsilon() {
        return maxEpsilon;
    }

    public void setMaxEpsilon(Double maxEpsilon) {
        this.maxEpsilon = maxEpsilon;
    }

    public Double getAvgEpsilon() {
        return avgEpsilon;
    }

    public void setAvgEpsilon(Double avgEpsilon) {
        this.avgEpsilon = avgEpsilon;
    }

    public int getSimpleMode() {
        return simpleMode;
    }

    public void setSimpleMode(int simpleMode) {
        this.simpleMode = simpleMode;
    }

    public Trajectory getSimplified() {
        return simplified;
    }

    public void setSimplified(Trajectory simplified) {
        this.simplified = simplified;
    }

}
