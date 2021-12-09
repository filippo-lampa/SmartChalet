package it.unicam.cs.ids2021;

public class Ombrellone {

    private int tipo;
    private int numeroLettini;
    private Coordinate coordinate;

    public Ombrellone(int tipo, Coordinate coordinate){
        this.tipo = tipo;
        this.numeroLettini = 0;
        this.coordinate = coordinate;
    }

    public int getTipo() {
        return this.tipo;
    }

    public void setTipo(int tipo) {
        this.tipo=tipo;
    }

    public int getNumeroLettiniAssociati() {
        return this.numeroLettini;
    }

    public void setNumeroLettiniAssociati(int numeroLettini) {
        this.numeroLettini=numeroLettini;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "Ombrellone{" +
                "tipo=" + tipo +
                '}';
    }
}
