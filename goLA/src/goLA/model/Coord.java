package goLA.model;

public class Coord<X,Y> {
    private X x;
    private Y y;
    public Coord(X l, Y r){
        this.x = l;
        this.y = r;
    }
    public X getL(){ return x; }
    public Y getR(){ return y; }
    public void setL(X l){ this.x = l; }
    public void setR(Y r){ this.y = r; }
}
