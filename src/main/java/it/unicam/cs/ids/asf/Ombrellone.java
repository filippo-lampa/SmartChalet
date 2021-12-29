package it.unicam.cs.ids.asf;

import java.util.Objects;

public class Ombrellone {

    private int idTipo;
    private int numeroLettiniAssociati;
    private boolean prenotato;
    private int idOmbrellone;
    private Coordinate location;

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Coordinate getLocation() {
        return location;
    }

    public Ombrellone(int idTipo, Coordinate location, int idOmbrellone){
        this.idOmbrellone = idOmbrellone;
        this.location = location;
        this.idTipo = idTipo;
        this.numeroLettiniAssociati = 0;
        this.prenotato = false;
    }

    public int getIdTipo(){
        return this.idTipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ombrellone that = (Ombrellone) o;
        return idOmbrellone == that.idOmbrellone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOmbrellone);
    }

    public int getIdOmbrellone(){
        return this.idOmbrellone;
    }

    public boolean isBooked(){
        return prenotato;
    }

    public int getNumeroLettiniAssociati(){
        return this.numeroLettiniAssociati;
    }

    public void setTipo(int idTipoOmbrellone){
        this.idTipo = idTipoOmbrellone;
    }

    public void setNumeroLettiniAssociati(int numeroLettini){
        this.numeroLettiniAssociati = numeroLettini;
    }

    public void setIsBooked(boolean prenotato) {
        this.prenotato = prenotato;
    }
}
