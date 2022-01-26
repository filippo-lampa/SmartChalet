package it.unicam.ids.smartchalet.asf;

import org.w3c.dom.Attr;

import java.util.Date;
import java.util.HashMap;

public class Attivita {

    private int id;
    private Date data;
    private int fasciaOraria;   //TODO controllare modifica

    public void setNumeroIscritti(int numeroIscritti) {
        this.numeroIscritti = numeroIscritti;
    }

    private int numeroIscritti; //TODO controllare aggiunta parametro ed aggiornare diagramma
    private String nome;
    private String descrizione;
    private int maxPartecipanti;
    private String animatore;
    private HashMap<Attrezzatura,Integer> attrezzatureAssociate;
    private int oreDurata;

    public Attivita(int id, String nome, String descrizione, Date data, int maxPartecipanti, String animatore, int oreDurata, int fasciaOraria){
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

        System.out.println("Attività{" +
                "data=" + data +
                ", nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", maxPartecipanti=" + maxPartecipanti +
                ", animatore='" + animatore + '\'' +
                ", oreDurata=" + oreDurata
        );

        System.out.println("attrezzatura associata: ");
        printAttrezzatureAssociate();
        System.out.println(" }");
    }

    public void printAttrezzatureAssociate(){
        System.out.println("Attrezzature associate:");
        for(Attrezzatura attrezzatura : this.attrezzatureAssociate.keySet()){
            System.out.println("Nome: " + attrezzatura.getNome() + "\t|\t" + this.attrezzatureAssociate.get(attrezzatura) + "pz");
        }
    }

    public int getId() {
        return id;
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
