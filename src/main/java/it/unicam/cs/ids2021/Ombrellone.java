package it.unicam.cs.ids2021;

public class Ombrellone {

    private int idTipo;
    private int numeroLettiniAssociati;
    private boolean prenotato;
    private Coordinate coordinate;

    public Ombrellone(int tipo, Coordinate coordinate){
        this.idTipo = tipo;
        this.numeroLettiniAssociati = 0;
        this.prenotato = false;
        this.coordinate = coordinate;
    }

    public int getTipo() {
        return this.idTipo;
    }

    public void setTipo(int tipo) {
        this.idTipo=tipo;
    }

    public int getNumeroLettiniAssociati() {
        return this.numeroLettiniAssociati;
    }

    public void setNumeroLettiniAssociati(int numeroLettini) {
        this.numeroLettiniAssociati=numeroLettini;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isBooked(){
        return prenotato;
    }

    @Override
    public String toString() {
        return "Ombrellone{" +
                "tipo=" + idTipo +
                '}';
    }
}
