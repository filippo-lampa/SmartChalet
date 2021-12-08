package it.unicam.cs.ids.asf;

public class Ombrellone {
    private final int idOmbrellone;
    private int tipo;
    private int numeroLettiniAssociati;
    private boolean isPrenotato;

    public int getIdOmbrellone() {
        return idOmbrellone;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getNumeroLettiniAssociati() {
        return numeroLettiniAssociati;
    }

    public void setNumeroLettiniAssociati(int numeroLettiniAssociati) {
        this.numeroLettiniAssociati = numeroLettiniAssociati;
    }

    public boolean isPrenotato() {
        return isPrenotato;
    }

    public Ombrellone(int idOmbrellone, int tipo, int numeroLettiniAssociati, boolean isPrenotato) {
        this.idOmbrellone = idOmbrellone;
        this.tipo = tipo;
        this.numeroLettiniAssociati = numeroLettiniAssociati;
        this.isPrenotato = isPrenotato;
    }
}
