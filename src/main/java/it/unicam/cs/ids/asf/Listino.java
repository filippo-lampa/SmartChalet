package it.unicam.cs.ids.asf;

import java.util.*;

public class Listino {
    private double prezzoBaseOmbrellone = 10.00;
    private HashMap<Integer, Double> prezziTipologia = new HashMap<Integer, Double>();
    private HashMap<FasciaDiPrezzo, Double> prezziFascia;
    private HashMap<Integer, Double> prezziBar;


    public double getPrezzoBaseOmbrellone() {
        return prezzoBaseOmbrellone;
    }

    public void setPrezzoBaseOmbrellone(double prezzoBaseOmbrellone) {
        this.prezzoBaseOmbrellone = prezzoBaseOmbrellone;
    }

    public void setPrezziFascia(HashMap<FasciaDiPrezzo, Double> prezziFascia) {
        this.prezziFascia = prezziFascia;
    }

    public void addPrezziFascia(HashMap<FasciaDiPrezzo, Double> prezziFascia, FasciaDiPrezzo fasciaDaAggiungere, double prezzoDaAggiungere) {
        prezziFascia.put(fasciaDaAggiungere, prezzoDaAggiungere);
    }

    public HashMap<Integer, Double> getPrezziTipologia() {
        return prezziTipologia;
    }

    public void setPrezziTipologia(HashMap<Integer, Double> prezziTipologia) {
        this.prezziTipologia = prezziTipologia;
    }

    public void editPrezziTipologia(int integerTipologia, double nuovoPrezzoTipologia) {
        prezziTipologia.replace(integerTipologia, nuovoPrezzoTipologia);
    }

    public HashMap<FasciaDiPrezzo, Double> getPrezziFascia() {
        return prezziFascia;
    }

    public double calcolaPrezzoPrenotazione(int idTipologia, int idFascia) {
        double subTotale = prezzoBaseOmbrellone * prezziTipologia.get(idTipologia) * prezziFascia.get(idFascia) ;
        return subTotale;
    }

    public void getPrezzi() {

    }
}