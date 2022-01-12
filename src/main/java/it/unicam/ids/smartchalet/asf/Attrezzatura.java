package it.unicam.ids.smartchalet.asf;

public class Attrezzatura {
    private String nomeAttrezzatura;
    private String descrizione;

    public Attrezzatura(String nomeAttrezzatura, String descrizione) {
        this.nomeAttrezzatura = nomeAttrezzatura;
        this.descrizione = descrizione;
    }

    public String getNomeAttrezzatura() {
        return nomeAttrezzatura;
    }

}
