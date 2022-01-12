package it.unicam.ids.smartchalet.asf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ListaAttrezzature {

    public ListaAttrezzature() {
        mappaAttrezzature = new HashMap<>();
    }
    private HashMap<Attrezzatura, Integer> mappaAttrezzature;

    public void aggiornaListaAttrezzatura(HashMap<Attrezzatura, Integer> mappaAggiornata) {
        mappaAttrezzature = mappaAggiornata;
    }

    public void getAttrezzature() {
        mappaAttrezzature.entrySet().forEach(entry -> {
            System.out.println("Attrezzatura: " + entry.getKey().getNomeAttrezzatura() + " | Disponibilità: " + entry.getValue());
        });
    }

    public HashMap<Attrezzatura, Integer> getMappaAttrezzature() {
        return mappaAttrezzature;
    }

    public boolean controlloAttrezzaturaEsistente(String nomeAttrezzatura) {
        for (Attrezzatura attrezzatura : this.mappaAttrezzature.keySet()) {
            if (Objects.equals(attrezzatura.getNomeAttrezzatura().toLowerCase(), nomeAttrezzatura.toLowerCase())) {
                System.out.println("L'attrezzatura è presente nella lista.");
                return true;
            }
        }
        System.out.println("L'attrezzatura non è presente nella lista.");
        return false;
    }

    public void addAttrezzatura(Attrezzatura nuovaAttrezzatura, int quantitaAttrezzatura) {
        this.mappaAttrezzature.put(nuovaAttrezzatura, quantitaAttrezzatura);
    }

    public boolean controlloDisponibilitaAttrezzatura(String nomeAttrezzatura, int numeroDaRimuovere) {
        for (Map.Entry<Attrezzatura, Integer> entry : mappaAttrezzature.entrySet()) {
            if (Objects.equals(entry.getKey().getNomeAttrezzatura().toLowerCase(), nomeAttrezzatura.toLowerCase())) {
                if ((entry.getValue() - numeroDaRimuovere) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void aggiungiQuantitaAttrezzatura(String attrezzatura, int quantitaAttrezzatura) {
        for (Map.Entry<Attrezzatura, Integer> entry : mappaAttrezzature.entrySet()) {
            if (Objects.equals(entry.getKey().getNomeAttrezzatura().toLowerCase(), attrezzatura.toLowerCase())) {
                this.mappaAttrezzature.replace(entry.getKey(), (entry.getValue() + quantitaAttrezzatura));
            }
        }
    }

    public void rimuoviQuantitaAttrezzatura(String attrezzatura, int quantitaAttrezzatura) {
        for (Map.Entry<Attrezzatura, Integer> entry : mappaAttrezzature.entrySet()) {
            if (Objects.equals(entry.getKey().getNomeAttrezzatura().toLowerCase(), attrezzatura.toLowerCase())) {
                this.mappaAttrezzature.replace(entry.getKey(), (entry.getValue() - quantitaAttrezzatura));
            }
        }
    }

}