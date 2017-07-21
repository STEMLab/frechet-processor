package goLA.model;

public class Query {
    public Trajectory q_tr;
    public double dist;

    public Query(Trajectory q_tr, double dist) {
        super();
        this.q_tr = q_tr;
        this.dist = dist;
    }

    public Trajectory getTrajectory() {
        return q_tr;
    }
}
