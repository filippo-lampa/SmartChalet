package it.unicam.cs.ids.asf;

import java.util.HashMap;

public class Listino {
    private HashMap<Integer, Double> prezziTipologia;
    private HashMap<Integer, Double> prezziBar;
    private HashMap<Integer, Double> prezziFascia;
    private final double prezzoBaseOmbrellone = 10.00;

    public double calcolaPrezzoPrenotazione(int idTipologia, int idFascia) {
        double subTotale = prezzoBaseOmbrellone * prezziTipologia.get(idTipologia) * prezziFascia.get(idFascia) ;
        return subTotale;
    }

    public void ottieniPrezzi() {

    }

}
