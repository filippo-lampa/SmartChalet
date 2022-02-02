package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdineBar {

    private int idOrdine;
    private int idCliente;
    private HashMap<ProdottoBar, Integer> prodottiOrdinati;

    public OrdineBar(int idCliente, HashMap<ProdottoBar,Integer> prodottiOrdinati, int idOrdine){
        this.idOrdine = idOrdine;
        this.idCliente = idCliente;
        this.prodottiOrdinati = prodottiOrdinati;
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

}
