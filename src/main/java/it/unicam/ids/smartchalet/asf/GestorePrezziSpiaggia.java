package it.unicam.ids.smartchalet.asf;

public class GestorePrezziSpiaggia {
    private int id;
    private String nome;
    private String cognome;
    private int telefono;
    private String mail;
    private HandlerListino handlerListinoAssociato;

    public void impostaPrezziOmbrellone() {
        this.handlerListinoAssociato.impostaPrezziOmbrellone();
    }

    public int getId() {
        return this.id;
    }
}
