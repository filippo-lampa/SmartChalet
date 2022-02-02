package it.unicam.ids.smartchalet.asf;

import org.w3c.dom.Attr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Attivita {

    private Date data;
    private int fasciaOraria;
    private int numeroIscritti;
    private String nome;
    private String descrizione;
    private int maxPartecipanti;
    private String animatore;
    private HashMap<Attrezzatura,Integer> attrezzatureAssociate;
    private int oreDurata;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attivita attivita = (Attivita) o;
        return fasciaOraria == attivita.fasciaOraria && Objects.equals(data, attivita.data) && Objects.equals(nome, attivita.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, fasciaOraria, nome);
    }

    public Attivita(String nome, String descrizione, Date data, int maxPartecipanti, String animatore, int oreDurata, int fasciaOraria){
        this.attrezzatureAssociate = new HashMap<>();
        this.nome = nome;
        this.descrizione = descrizione;
        this.data = data;
        this.oreDurata = oreDurata;
        this.maxPartecipanti = maxPartecipanti;
        this.animatore = animatore;
        this.fasciaOraria = fasciaOraria;
        this.numeroIscritti = 0;
    }

    public void setNumeroIscritti(int numeroIscritti) {
        this.numeroIscritti = numeroIscritti;
    }

    public int getFasciaOraria() {
        return fasciaOraria;
    }

    public void setFasciaOraria(int fasciaOraria) {
        this.fasciaOraria = fasciaOraria;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public void setAnimatore(String animatore) {
        this.animatore = animatore;
    }

    public void setAttrezzatureAssociate(HashMap<Attrezzatura, Integer> attrezzatureAssociate) {
        this.attrezzatureAssociate = attrezzatureAssociate;
    }

    public void setOreDurata(int oreDurata) {
        this.oreDurata = oreDurata;
    }

    public void printDettagliAttivita(){
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(nome + ":\t" +
                formatter.format(data) + '\n' +
                descrizione + '\n' + "Numero massimo di partecipanti: " +
                maxPartecipanti + '\n' + "Numero posti disponibili: " +
                (maxPartecipanti - numeroIscritti) + '\n' +
                "Animatore: " + animatore + '\n' +
                "Durata (ore): " + oreDurata
        );
        printAttrezzatureAssociate();
        System.out.println();
    }

    public void printAttrezzatureAssociate(){
        System.out.println("Attrezzature associate:");
        for(Attrezzatura attrezzatura : this.attrezzatureAssociate.keySet()){
            System.out.println("Nome: " + attrezzatura.getNome() + "\t|\t" + this.attrezzatureAssociate.get(attrezzatura) + "pz");
        }
    }

    public Date getData() {
        return data;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public String getAnimatore() {
        return animatore;
    }

    public HashMap<Attrezzatura, Integer> getAttrezzatureAssociate() {
        return attrezzatureAssociate;
    }

    public int getOreDurata() {
        return oreDurata;
    }

    public int getNumeroIscritti() {
        return this.numeroIscritti;
    }

    public boolean isAttrezzaturaAssociata(String nomeAttrezzatura) {
        for(Attrezzatura attrezzatura : this.attrezzatureAssociate.keySet())
            if(attrezzatura.getNome().equals(nomeAttrezzatura))
                return true;
        return false;
    }

    public void aggiornaPostiDisponibiliAttivita(int numeropartecipanti) {
        this.setNumeroIscritti(this.getNumeroIscritti()+numeropartecipanti);
    }
}
