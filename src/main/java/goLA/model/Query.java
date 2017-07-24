package goLA.model;

public class Query {
    private Trajectory q_tr;
    private double dist;

    public Query(Trajectory q_tr, double dist) {
        super();
        this.q_tr = q_tr;
        this.setDistance(dist);
    }

    public Trajectory getTrajectory() {
        return q_tr;
    }

    public double getDistance() {
        return dist;
    }

    public void setDistance(double dist) {
        this.dist = dist;
    }
}
