package it.unicam.cs.ids2021;

import java.util.Objects;

public class Coordinate {

    private final int xAxis;
    private final int yAxis;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return xAxis == that.xAxis && yAxis == that.yAxis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xAxis, yAxis);
    }

    @Override
    public String toString() {
        return "[" + xAxis + "," + yAxis + ']';
    }
}