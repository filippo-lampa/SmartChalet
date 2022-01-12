package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Listino {
    private HashMap<TipologiaOmbrellone, Double> prezziTipologia;
    private HashMap<ProdottoBar, Double> prezziBar;
    private HashMap<FasciaDiPrezzo, Double> prezziFascia;
    private double prezzoBaseOmbrellone;

    public Listino(){
        prezziTipologia = new HashMap<>();
        prezziBar = new HashMap<>();
        prezziFascia = new HashMap<>();
        prezzoBaseOmbrellone = 10.00;
    }

    public double calcolaPrezzoPrenotazione(int idTipologia, int idFascia) {
        return prezzoBaseOmbrellone * prezziTipologia.get(idTipologia) * prezziFascia.get(idFascia) ;
    }

    public void aggiornaListino(HashMap<ProdottoBar,Double> listinoBarAggiornato) {
        this.prezziBar = listinoBarAggiornato;
    }

    public boolean controlloProdottoEsistente(ProdottoBar prodottoBar) {
        return this.prezziBar.containsKey(prodottoBar);
    }

    public void aggiungiAllaListaProdotti(ProdottoBar nuovoProdottoBar, Double prezzoProdotto) {
        this.prezziBar.put(nuovoProdottoBar, prezzoProdotto);
    }

    public HashMap<ProdottoBar, Double> ottieniListinoBar() {
        return this.prezziBar;
    }

    public HashMap<TipologiaOmbrellone, Double> getPrezziTipologia() {
        return prezziTipologia;
    }

    public void setNuovoPrezzoTipologia(String nomeTipologia, double nuovoPrezzo){
        for(TipologiaOmbrellone tipologia : this.getPrezziTipologia().keySet()){
            if(tipologia.getNome().equals(nomeTipologia))
                this.getPrezziTipologia().put(tipologia, nuovoPrezzo);
        }
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

    public void eliminaFasciaDiPrezzo(FasciaDiPrezzo fasciaDaModificare) {
        this.prezziFascia.remove(fasciaDaModificare);
    }

    public void modificaPrezzoFascia(FasciaDiPrezzo fasciaDaModificare, double nuovoPrezzo) {
        this.prezziFascia.replace(fasciaDaModificare,nuovoPrezzo);
    }

    public void mostraFasceEPrezzi() {
        for (Map.Entry<FasciaDiPrezzo, Double> entry : this.prezziFascia.entrySet()) {
            System.out.println(("Fascia " + entry.getKey().getNome() + " - costo: " + entry.getValue() + "."));
        }
    }

    public void modificaLocazioniFascia(FasciaDiPrezzo fasciaDaModificare, FasciaDiPrezzo fasciaTemporanea) {
        fasciaDaModificare.setCoordinateInizio(fasciaTemporanea.getCoordinateInizio());
        fasciaDaModificare.setCoordinateFine(fasciaTemporanea.getCoordinateFine());
    }

    public void outputListaTipologie(){
        if(this.prezziTipologia.isEmpty()){
            System.out.println("Al momento non ci sono tipologie salvate");
        }
        else{
            System.out.println("Tipi: ");
            for (TipologiaOmbrellone tipologia: this.prezziTipologia.keySet()) {
                System.out.println(tipologia+" Moltipilicatore: "+ this.prezziTipologia.get(tipologia));
            }
        }
    }

    public void addPrezziFascia(FasciaDiPrezzo fasciaDaAggiungere, double prezzoDaAggiungere) {
        this.prezziFascia.put(fasciaDaAggiungere, prezzoDaAggiungere);
    }

    public boolean isNomeDisponibile(String nomeStringa) {
        for(FasciaDiPrezzo fascia : this.prezziFascia.keySet()){
            if(fascia.getNome().equals(nomeStringa)) {
                System.out.println("Il nome inserito non è disponibile");
                return false;
            }
        }
        return true;
    }

 /*   public int getNewIdTipologia(){
        int highestId = -1;
        if(!this.prezziTipologia.isEmpty()) {
            for (TipologiaOmbrellone tipologia : this.prezziTipologia.keySet())
                if (tipologia.() > highestId) {
                    highestId = currentOmbrellone.getIdOmbrellone();
                }
        }
        return highestId + 1;
    }*/

}