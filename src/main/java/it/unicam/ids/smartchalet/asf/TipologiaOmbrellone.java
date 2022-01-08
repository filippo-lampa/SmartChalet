package it.unicam.ids.smartchalet.asf;

public class TipologiaOmbrellone {

    private String nome;
    private String descrizione;
    private int id;

    public TipologiaOmbrellone (String nome, String descrizione){
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
        return "[" + nome + ": '" + descrizione + '\'' + ']';
    }

}
