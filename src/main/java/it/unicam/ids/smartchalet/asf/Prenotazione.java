package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Prenotazione {
    private int id;
    private Date dataInizio;
    private Date dataFine;
    private int idCliente;
    private double prezzoTotale;

    private HashMap<Date, Integer> mappaDateFasce;
    private HashMap<Date, ArrayList<Ombrellone>> mappaDateListaOmbrelloni;

    public Prenotazione(Date dataInizio, Date dataFine, int idCliente){
        this.mappaDateListaOmbrelloni = new HashMap<Date, ArrayList<Ombrellone>>();
        this.mappaDateFasce = new HashMap<Date, Integer>();
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.idCliente = idCliente;
    }

    public Prenotazione(int id, Date dataInizio, Date dataFine, int idCliente, double prezzoTotale) {
        this.id = id;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.idCliente = idCliente;
        this.prezzoTotale = prezzoTotale;
        this.mappaDateFasce = new HashMap<>();
        this.mappaDateListaOmbrelloni = new HashMap<>();
    }

    public HashMap<Date, ArrayList<Ombrellone>> getMappaDateListaOmbrelloni() {
        return mappaDateListaOmbrelloni;
    }

    public void setMappaDateFasce(HashMap<Date, Integer> mappaDateFasce) {
        this.mappaDateFasce = mappaDateFasce;
    }

    public void setMappaDateListaOmbrelloni(HashMap<Date, ArrayList<Ombrellone>> mappaDateListaOmbrelloni) {
        this.mappaDateListaOmbrelloni = mappaDateListaOmbrelloni;
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

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }
}