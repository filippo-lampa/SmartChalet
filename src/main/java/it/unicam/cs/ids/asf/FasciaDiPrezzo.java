package it.unicam.cs.ids.asf;

public class FasciaDiPrezzo {
    String nome;
    int idPrimoOmbrellone;
    int idUltimoOmbrellone;
    final int id;

    public FasciaDiPrezzo(String nome, int idPrimoOmbrellone, int idUltimoOmbrellone, int id) {
        this.nome = nome;
        this.idPrimoOmbrellone = idPrimoOmbrellone;
        this.idUltimoOmbrellone = idUltimoOmbrellone;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdPrimoOmbrellone() {
        return idPrimoOmbrellone;
    }

    public void setIdPrimoOmbrellone(int idPrimoOmbrellone) {
        this.idPrimoOmbrellone = idPrimoOmbrellone;
    }

    public int getIdUltimoOmbrellone() {
        return idUltimoOmbrellone;
    }

    public void setIdUltimoOmbrellone(int idUltimoOmbrellone) {
        this.idUltimoOmbrellone = idUltimoOmbrellone;
    }

    public int getId() {
        return id;
    }
}
