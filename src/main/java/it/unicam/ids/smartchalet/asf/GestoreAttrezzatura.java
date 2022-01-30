package it.unicam.ids.smartchalet.asf;

public class GestoreAttrezzatura {
    private int id;
    private String nome;
    private String cognome;
    private int telefono;
    private String mail;
    private HandlerAttrezzatura handlerAttrezzatura;

    public void aggiungiAttrezzatura() {
        this.handlerAttrezzatura.aggiungiAttrezzatura();
    }
    public void rimuoviAttrezzatura() {
        this.handlerAttrezzatura.rimuoviAttrezzatura();
    }

    public int getId() {
        return this.id;
    }
}
