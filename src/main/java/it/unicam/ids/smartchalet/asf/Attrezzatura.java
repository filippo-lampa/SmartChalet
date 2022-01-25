package it.unicam.ids.smartchalet.asf;

public class Attrezzatura {

    private int id;
    private String nome;
    private String descrizione;

    public Attrezzatura(String nome, String descrizione){
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
