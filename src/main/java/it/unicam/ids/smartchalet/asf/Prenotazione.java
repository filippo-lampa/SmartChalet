package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Prenotazione {
    private int id;
    private int fasciaOraria;
    private Date dataInizio;
    private Date dataFine;
    private int idCliente;
    private ArrayList<Ombrellone> ombrelloni;
    private int numeroLettini;
    private double prezzoTotale;

    private HashMap<Date, Integer> mappaDateFasce;

    //Costruttore aggiunto per testare il db
    public Prenotazione(Date dataInizio, Date dataFine, int idCliente){
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.idCliente = idCliente;
    }


    public HashMap<Date, Integer> getMappaDateFasce() {
        return mappaDateFasce;
    }
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

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
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