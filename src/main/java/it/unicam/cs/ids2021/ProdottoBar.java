package it.unicam.cs.ids2021;

import java.util.Objects;

public class ProdottoBar {

    private String descrizione;

    private String nomeProdotto;

    private int idProdotto;

    public ProdottoBar(String descrizione, String nomeProdotto){
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProdottoBar that = (ProdottoBar) o;
        return idProdotto == that.idProdotto && Objects.equals(nomeProdotto, that.nomeProdotto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeProdotto, idProdotto);
    }

}