package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ListaAttrezzatura {

    private HashMap<Attrezzatura,Integer> mappaAttrezzature;

    public ListaAttrezzatura(){
        this.mappaAttrezzature = new HashMap<>();
    }

    public ArrayList<Attrezzatura> ottieniListaAttrezzaturaAggiornata() {
        return new ArrayList<>(this.mappaAttrezzature.keySet());
    }

    public HashMap<Attrezzatura,Integer> getMappaAttrezzatura() {
        return this.mappaAttrezzature;
    }

    public boolean riservaAttrezzatura(String nomeAttrezzatura, int numeroAttrezzatureDesiderato) {
        for(Attrezzatura attrezzatura : this.mappaAttrezzature.keySet())
            if(Objects.equals(attrezzatura.getNome().toLowerCase(), nomeAttrezzatura.toLowerCase())) {
                this.mappaAttrezzature.put(attrezzatura, this.mappaAttrezzature.get(attrezzatura) - numeroAttrezzatureDesiderato);
                return true;
            }
        return false;
    }

    public void aggiornaMappaAttrezzatura(HashMap<Attrezzatura, Integer> mappaAggiornata) {
        mappaAttrezzature = mappaAggiornata;
    }

    public void printMappaAttrezzature() {
        if(!this.mappaAttrezzature.isEmpty()) {
            mappaAttrezzature.entrySet().forEach(entry -> {
                System.out.println("Attrezzatura: " + entry.getKey().getNome() + " | Disponibilit√†: " + entry.getValue());
            });
        } else System.out.println("Al momento non ci sono attrezzature");
    }

    public boolean controlloAttrezzaturaEsistente(String nomeAttrezzatura) {
        for (Attrezzatura attrezzatura : this.mappaAttrezzature.keySet()) {
            if (Objects.equals(attrezzatura.getNome().toLowerCase(), nomeAttrezzatura.toLowerCase())) {
                System.out.println("L'attrezzatura √® presente nella lista.");
                return true;
            }
        }
        System.out.println("L'attrezzatura non √® presente nella lista.");
        return false;
    }

    public void addAttrezzatura(Attrezzatura nuovaAttrezzatura, int quantitaAttrezzatura) {
        this.mappaAttrezzature.put(nuovaAttrezzatura, quantitaAttrezzatura);
    }

    public boolean controlloDisponibilitaAttrezzatura(String nomeAttrezzatura, int numeroDaRimuovere) {
        for (Map.Entry<Attrezzatura, Integer> entry : mappaAttrezzature.entrySet()) {
            if (Objects.equals(entry.getKey().getNome().toLowerCase(), nomeAttrezzatura.toLowerCase())) {
                if ((entry.getValue() - numeroDaRimuovere) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void aggiungiQuantitaAttrezzatura(String attrezzatura, int quantitaAttrezzatura) {
        for (Map.Entry<Attrezzatura, Integer> entry : mappaAttrezzature.entrySet()) {
            if (Objects.equals(entry.getKey().getNome().toLowerCase(), attrezzatura.toLowerCase())) {
                this.mappaAttrezzature.replace(entry.getKey(), (entry.getValue() + quantitaAttrezzatura));
            }
        }
    }

    public void rimuoviQuantitaAttrezzatura(String attrezzatura, int quantitaAttrezzatura) {
        this.aggiungiQuantitaAttrezzatura(attrezzatura, -quantitaAttrezzatura);
    }
}
