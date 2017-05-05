package goLA.model;

public class Coordinates {

    private double pointX;
    private double pointY;
    private int order;
    private int id;

    public Coordinates(double mx, double my){
        this.pointY = my;
        this.pointX = mx;
    }

    public Coordinates (){

    }

    public double getPointX() {
        return pointX;
    }

    public void setPointX(double pointX) {
        this.pointX = pointX;
    }

    public double getPointY() {
        return pointY;
    }

    public void setPointY(double pointY) {
        this.pointY = pointY;
    }

    public void setPoint(double x, double y){
        this.pointX = x;
        this.pointY = y;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
