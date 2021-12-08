package it.unicam.cs.ids.asf;

import java.util.ArrayList;
import java.util.Date;

public class Prenotazione {
    private int id;
    private int fasciaOraria;
    private Date data;
    private int idCliente;
    private ArrayList<Ombrellone> ombrelloni;
    private int numeroLettini;
    private double prezzoTotale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFasciaOraria() {
        return fasciaOraria;
    }

    public void setFasciaOraria(int fasciaOraria) {
        this.fasciaOraria = fasciaOraria;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public ArrayList<Ombrellone> getOmbrelloni() {
        return ombrelloni;
    }

    public void setOmbrelloni(ArrayList<Ombrellone> ombrelloni) {
        this.ombrelloni = ombrelloni;
    }

    public int getNumeroLettini() {
        return numeroLettini;
    }

    public void setNumeroLettini(int numeroLettini) {
        this.numeroLettini = numeroLettini;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }
}
