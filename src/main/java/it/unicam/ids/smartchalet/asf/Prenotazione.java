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
    private double prezzoTotale;
    private HashMap<Date, Integer> mappaDateFasce;
    private HashMap<Date, ArrayList<Ombrellone>> mappaDateListaOmbrelloni;



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

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public void setMappaDateFasce(HashMap<Date, Integer> mappaDateFasce) {
        this.mappaDateFasce = mappaDateFasce;
    }

    public HashMap<Date, Integer> getMappaDateFasce() {
        return mappaDateFasce;
    }

    public HashMap<Date, ArrayList<Ombrellone>> getMappaDateListaOmbrelloni() {
        return mappaDateListaOmbrelloni;
    }

    public void setMappaDateListaOmbrelloni(HashMap<Date, ArrayList<Ombrellone>> mappaDateListaOmbrelloni) {
        this.mappaDateListaOmbrelloni = mappaDateListaOmbrelloni;
    }
}