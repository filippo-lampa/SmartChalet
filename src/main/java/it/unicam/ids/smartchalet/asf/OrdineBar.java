package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdineBar {

    private int idOrdine;
    private ArrayList<Ombrellone> ombrelloniCliente;
    private int idCliente;
    private HashMap<ProdottoBar, Integer> prodottiOrdinati;

    public OrdineBar(int idCliente, HashMap<ProdottoBar,Integer> prodottiOrdinati, ArrayList<Ombrellone> ombrelloniCliente){
        this.idCliente = idCliente;
        this.prodottiOrdinati = prodottiOrdinati;
    }

    public ArrayList<Ombrellone> getOmbrelloniCliente() {
        return ombrelloniCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public HashMap<ProdottoBar, Integer> getProdottiOrdinati() {
        return prodottiOrdinati;
    }

    public void printProdottiOrdine(){
        for(ProdottoBar currentProdotto : this.prodottiOrdinati.keySet())
            System.out.println(currentProdotto.getNomeProdotto() + ": " + this.prodottiOrdinati.get(currentProdotto) + "pz");
    }

    public void printOmbrelloniCliente(){
        for(Ombrellone currentOmbrellone : this.ombrelloniCliente) {
            System.out.println("Id ombrellone: " + currentOmbrellone.getIdOmbrellone());
            System.out.println("Location: " + currentOmbrellone.getLocation().getxAxis() + " " + currentOmbrellone.getLocation().getyAxis());
            System.out.println("------------------------");
        }
    }

}
