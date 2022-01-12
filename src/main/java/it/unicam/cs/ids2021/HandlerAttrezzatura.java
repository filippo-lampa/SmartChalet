package it.unicam.cs.ids2021;

import java.util.HashMap;

public class HandlerAttrezzatura {

    private ListaAttrezzatura listaAttrezzatura ;
    private DBMSController associatedDBMS;

    public HandlerAttrezzatura(DBMSController associatedDBMS, ListaAttrezzatura listaAttrezzatura) {
        this.listaAttrezzatura = listaAttrezzatura;
        this.associatedDBMS = associatedDBMS;
    }

    public ListaAttrezzatura getListaAttrezzatura(){
        return this.listaAttrezzatura;
    }

    public HashMap<Attrezzatura, Integer> getMappaAttrezzaturaAssociata() {
        return this.listaAttrezzatura.getMappaAttrezzaturaAssociata();
    }

    public boolean controlloDisponibilitaAttrezzatura(int idAttrezzatura, int numeroAttrezzatura){
        return true;
    }

    public void riservaAttrezzatura(int idAttrezzatura, int numeroAttrezzatura){

    }

    public void aggiornaListaAttrezzatura(){
        this.associatedDBMS.ottieniListaAttrezzature();
        this.listaAttrezzatura.aggiornaMappaAttrezzatura();

    }

    public HashMap<Attrezzatura, Integer> ottieniMappaAttrezzaturaAssociata() {
        return null;
    }


}
