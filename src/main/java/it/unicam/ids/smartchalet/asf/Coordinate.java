package it.unicam.ids.smartchalet.asf;

public class Coordinate {

    int xAxis;
    int yAxis;

    public Coordinate(int x, int y){
        this.xAxis = x;
        this.yAxis = y;
    }

    public int getyAxis() {
        return yAxis;
    }

    public int getxAxis() {
        return xAxis;
    }

    @Override
    public String toString() {
        return "[" + xAxis + "," + yAxis + ']';
    }
}
