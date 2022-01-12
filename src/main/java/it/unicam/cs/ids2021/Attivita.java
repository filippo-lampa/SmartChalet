package it.unicam.cs.ids2021;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Attivita {

    private final int id;
    private String nome;
    private String descrizione;
    private Date date;
    private int maxPartecipant;
    private String animatore;
    private HashMap<Attrezzatura,Integer> attrezzatureAssociate;
    private int oreDurata;

    public Attivita(int id, String nome, String descrizione, Date date, int maxPartecipant, String animatore, int oreDurata) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.date = date;
        this.maxPartecipant = maxPartecipant;
        this.animatore = animatore;
        this.attrezzatureAssociate = new HashMap<>();
        this.oreDurata = oreDurata;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMaxPartecipant() {
        return maxPartecipant;
    }

    public void setMaxPartecipant(int maxPartecipant) {
        this.maxPartecipant = maxPartecipant;
    }

    public String getAnimatore() {
        return animatore;
    }

    public void setAnimatore(String animatore) {
        this.animatore = animatore;
    }

    public HashMap<Attrezzatura, Integer> getAttrezzatureAssociate() {
        return attrezzatureAssociate;
    }

    public void setAttrezzatureAssociate(HashMap<Attrezzatura, Integer> attrezzatureAssociate) {
        this.attrezzatureAssociate = attrezzatureAssociate;
    }

    public int getOreDurata() {
        return oreDurata;
    }

    public void setOreDurata(int oreDurata) {
        this.oreDurata = oreDurata;
    }

    public void aggiungiAttrezzaturaAdAttivita(Attrezzatura attrezzatura, int numeroAttrezzatura){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attivita attivita = (Attivita) o;
        return id == attivita.id && maxPartecipant == attivita.maxPartecipant && oreDurata == attivita.oreDurata && nome.equals(attivita.nome) && descrizione.equals(attivita.descrizione) && date.equals(attivita.date) && animatore.equals(attivita.animatore) && Objects.equals(attrezzatureAssociate, attivita.attrezzatureAssociate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descrizione, date, maxPartecipant, animatore, attrezzatureAssociate, oreDurata);
    }
}
