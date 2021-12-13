package it.unicam.cs.ids2021;

public class Main {

    public static void main(String[] args){

        HandlerSpiaggia a = new HandlerSpiaggia();

        System.out.println(a.getSpiaggiaGestita().toString());
        System.out.println(a.getSpiaggiaGestita().getTotaleOmbrelloni());

        //a.aggiungiTipologiaOmbrellone();

        a.aggiungiOmbrellone();
        //System.out.println(a.getSpiaggiaGestita().toString());

    }
}
