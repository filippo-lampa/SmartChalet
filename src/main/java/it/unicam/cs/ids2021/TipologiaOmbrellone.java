package it.unicam.cs.ids2021;

public class TipologiaOmbrellone {

    private String nome;
    private String descrizione;

    public TipologiaOmbrellone (String nome, String descrizione){
        this.nome = nome;
        this.descrizione = descrizione;
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
}
