package it.unicam.cs.ids2021;

import java.util.ArrayList;

public class Spiaggia { //TODO da implementare quando sviluppati casi d'uso associati e database

    private final ArrayList<ArrayList<Ombrellone>> listaOmbrelloni;

    public Spiaggia (){ //TODO modificare, l'ho creato così solo per testare
        listaOmbrelloni = new ArrayList<>();
        for(int i=0;i<4;i++){
            ArrayList<Ombrellone> lista= new ArrayList<>();
            for(int j=0;j<4;j++){
                lista.add(null);
            }
            listaOmbrelloni.add(lista);
        }
    }

    public ArrayList<ArrayList<Ombrellone>> getListaOmbrelloni() {
        return this.listaOmbrelloni;
    }

    public int getTotaleOmbrelloni() {
        int contatore = 0;
        for (ArrayList<Ombrellone> riga :listaOmbrelloni) {
            for (Ombrellone ombrellone : riga) {
                if(ombrellone == null) ;
                else contatore++;
            }
        }
        return contatore;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < this.listaOmbrelloni.size(); i++) {
            for (int j = 0; j < this.listaOmbrelloni.get(i).size(); j++) {
               str.append(this.listaOmbrelloni.get(i).get(j)).append("\t");
            }
            str.append("\n");
        }
        return str.toString();
    }


    public void aggiungiOmbrellone(Ombrellone ombrellone){
        int coordinataX = ombrellone.getCoordinate().getxAxis();
        int coordinataY = ombrellone.getCoordinate().getyAxis();
        this.listaOmbrelloni.get(coordinataY).set(coordinataX,ombrellone);


    }




}
