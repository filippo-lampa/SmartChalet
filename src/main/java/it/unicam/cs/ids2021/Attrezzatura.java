package it.unicam.cs.ids2021;

public class Attrezzatura {

    private final int id;
    private String nome;
    private String descrizione;

    public Attrezzatura(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
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

    @Override
    public String toString() {
        return "[Id: " + id + ", Nome: " + nome + ", Descrizione: " + descrizione + ']';
    }
}
