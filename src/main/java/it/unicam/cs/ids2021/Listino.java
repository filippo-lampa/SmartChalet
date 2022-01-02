package it.unicam.cs.ids2021;

import java.util.HashMap;

public class Listino {
    private HashMap<TipologiaOmbrellone, Double> prezziTipologia;
    private HashMap<ProdottoBar, Double> prezziBar;
    private HashMap<FasciaDiPrezzo, Double> prezziFascia;
    private double prezzoBaseOmbrellone;

    public Listino() {
        this.prezziTipologia = new HashMap<>();
        this.prezziBar = new HashMap<>();
        this.prezziFascia = new HashMap<>();
        this.prezzoBaseOmbrellone = 10.00;
    }

    public HashMap<TipologiaOmbrellone, Double> getPrezziTipologia() {
        return prezziTipologia;
    }

    public void setPrezziTipologia(HashMap<TipologiaOmbrellone, Double> prezziTipologia) {
        this.prezziTipologia = prezziTipologia;
    }

    public HashMap<ProdottoBar, Double> getPrezziBar() {
        return prezziBar;
    }

    public void setPrezziBar(HashMap<ProdottoBar, Double> prezziBar) {
        this.prezziBar = prezziBar;
    }

    public HashMap<FasciaDiPrezzo, Double> getPrezziFascia() {
        return prezziFascia;
    }

    public void setPrezziFascia(HashMap<FasciaDiPrezzo, Double> prezziFascia) {
        this.prezziFascia = prezziFascia;
    }

    public double getPrezzoBaseOmbrellone() {
        return prezzoBaseOmbrellone;
    }

    public void setPrezzoBaseOmbrellone(double prezzoBaseOmbrellone) {
        this.prezzoBaseOmbrellone = prezzoBaseOmbrellone;
    }

    public double calcolaPrezzoPrenotazione(int idTipologia, int idFascia) {
        double subTotale = prezzoBaseOmbrellone * prezziTipologia.get(idTipologia) * prezziFascia.get(idFascia) ;
        return subTotale;
    }

    public void aggiornaListino(){

    }


    public void eliminaFasciaDiPrezzo(FasciaDiPrezzo fasciaDaModificare) {
        this.prezziFascia.remove(fasciaDaModificare);
    }

    public void modificaPrezzoFascia(FasciaDiPrezzo fasciaDaModificare, double nuovoPrezzo) {
        this.prezziFascia.replace(fasciaDaModificare,nuovoPrezzo);
    }

    public void modificaLocazioniFascia(FasciaDiPrezzo fasciaDaModificare, FasciaDiPrezzo fasciaTemporanea) {
        fasciaDaModificare.setCoordinateInizio(fasciaTemporanea.getCoordinateInizio());
        fasciaDaModificare.setCoordinateFine(fasciaTemporanea.getCoordinateFine());

    }
}