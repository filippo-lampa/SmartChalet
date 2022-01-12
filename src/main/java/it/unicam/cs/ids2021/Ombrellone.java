package it.unicam.cs.ids2021;

import java.util.Objects;

public class Ombrellone {

    private String nomeTipo;
    private int numeroLettiniAssociati;
    private boolean prenotato;
    private int idOmbrellone;
    private Coordinate location;

    public Ombrellone(String nomeTipo, Coordinate location, int idOmbrellone){
        this.idOmbrellone = idOmbrellone;
        this.location = location;
        this.nomeTipo = nomeTipo;
        this.numeroLettiniAssociati = 0;
        this.prenotato = false;
    }


    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Coordinate getLocation() {
        return location;
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

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setTipo(String nomeTipoOmbrellone){
        this.nomeTipo = nomeTipoOmbrellone;
    }

    public void setNumeroLettiniAssociati(int numeroLettini){
        this.numeroLettiniAssociati = numeroLettini;
    }

    public void setIsBooked(boolean prenotato) {
        this.prenotato = prenotato;
    }

    @Override
    public String toString() {
        return "Ombrellone{" + "tipo=" + nomeTipo + " " + "id=" + idOmbrellone + '}';
    }
}



