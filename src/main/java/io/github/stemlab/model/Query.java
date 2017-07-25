package io.github.stemlab.model;

public class Query {
    private Trajectory queryTrajectory;
    private double distance;

    public Query(Trajectory queryTrajectory, double distance) {
        super();
        this.queryTrajectory = queryTrajectory;
        this.distance = distance;
    }

    public Trajectory getTrajectory() {
        return this.queryTrajectory;
    }

    public Trajectory getQueryTrajectory() {
        return queryTrajectory;
    }

    public void setQueryTrajectory(Trajectory queryTrajectory) {
        this.queryTrajectory = queryTrajectory;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
