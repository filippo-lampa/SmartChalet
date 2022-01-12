package it.unicam.cs.ids2021;

import java.util.HashMap;

public class ListaAttrezzatura {

    private HashMap<Attrezzatura,Integer> mappaAttrezzaturaAssociata ;

    public ListaAttrezzatura() {
        this.mappaAttrezzaturaAssociata = new HashMap<>();
    }

    public HashMap<Attrezzatura, Integer> getMappaAttrezzaturaAssociata() {
        return mappaAttrezzaturaAssociata;
    }

    public void aggiornaMappaAttrezzatura(){

    }

    public boolean controlloDisponibilitaAttrezzatura(int idAttrezzatura, int numeroAttrezzatura){
        return true;
    }

    public void riservaAttrezzatura(int idAttrezzatura, int numeroAttrezzatura){

    }

    public boolean controlloAttrezzaturaEsistente(String nomeAttrezzatura){
        return true;
    }

    public void aggiungiAttrezzatura(Attrezzatura attrezzatura, int numeroAttrezzatura){

    }

    public void rimuoviAttrezzatura(int idAttrezzatura, int numeroAttrezzatureDaRimuovere){

    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for(Attrezzatura attrezzatura: this.mappaAttrezzaturaAssociata.keySet()){
            str.append(attrezzatura).append("\tNumero di attrezzature disponibili:").append(this.mappaAttrezzaturaAssociata.get(attrezzatura).toString()).append("\t");
            str.append("\n");
        }

        return str.toString();
    }

}
