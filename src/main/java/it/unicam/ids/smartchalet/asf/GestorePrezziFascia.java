package it.unicam.ids.smartchalet.asf;

public class GestorePrezziFascia {
    private int id;
    private String nome;
    private String cognome;
    private int telefono;
    private String mail;
    private HandlerListino handlerListinoAssociato;

    public void aggiungiFasciaPrezzo() {
        this.handlerListinoAssociato.aggiungiFasciaDiPrezzo();
    }

    public int getId() {
        return this.id;
    }
}
