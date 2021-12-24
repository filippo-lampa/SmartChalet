package it.unicam.ids.smartchalet.asf;

import java.util.HashMap;

public class Listino {
    private HashMap<Integer, Double> prezziTipologia;
    private HashMap<ProdottoBar, Double> prezziBar;
    private HashMap<Integer, Double> prezziFascia;
    private final double prezzoBaseOmbrellone;

    public Listino(){
        prezziTipologia = new HashMap<>();
        prezziBar = new HashMap<>();
        prezziFascia = new HashMap<>();
        prezzoBaseOmbrellone = 10.00;
    }

    public double calcolaPrezzoPrenotazione(int idTipologia, int idFascia) {
        return prezzoBaseOmbrellone * prezziTipologia.get(idTipologia) * prezziFascia.get(idFascia) ;
    }

    public void getPrezzi() {

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
}