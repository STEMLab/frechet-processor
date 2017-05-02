package goLA.model;

import com.vividsolutions.jts.geom.Coordinate;

public class Coordinates<X, Y> {

    private X pointX;
    private Y pointY;
    private int order;
    private int id;

    public Coordinates(X mx, Y my){
        this.pointY = my;
        this.pointX = mx;
    }

    public Coordinates (){

    }

    public X getPointX() {
        return pointX;
    }

    public void setPointX(X pointX) {
        this.pointX = pointX;
    }

    public Y getPointY() {
        return pointY;
    }

    public void setPointY(Y pointY) {
        this.pointY = pointY;
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
