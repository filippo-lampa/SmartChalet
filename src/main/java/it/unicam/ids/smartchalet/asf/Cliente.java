package it.unicam.ids.smartchalet.asf;

public class Cliente {

    private int id;
    private String nome;
    private String cognome;
    private String telefono;
    private String mail;
    private HandlerPrenotazione handlerPrenotazioneAssociato;

    public Cliente(int id, String nome, String cognome, String telefono, String mail){
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.mail = mail;
        this.handlerPrenotazioneAssociato = HandlerPrenotazione.getInstance();
    }

    public void ordinaBar(){
        this.handlerPrenotazioneAssociato.ordinaBar(this.id);
    }

    public void cancellaPrenotazione(){
        this.handlerPrenotazioneAssociato.cancellaPrenotazione(this.id);
    }
    public int getId() {
        return this.id;
    }
}
