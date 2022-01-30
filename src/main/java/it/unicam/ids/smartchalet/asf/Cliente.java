package it.unicam.ids.smartchalet.asf;

public class Cliente {

    private int id;
    private String nome;
    private String cognome;
    private int telefono;
    private String mail;
    private HandlerPrenotazione handlerPrenotazioneAssociato;

    public Cliente(){

    }

    public void ordinaBar(){
        this.handlerPrenotazioneAssociato.ordinaBar(this.id);
    }

    public void prenotaOmbrellone() {this.handlerPrenotazioneAssociato.prenotaOmbrellone(this.id);}

    public void cancellaPrenotazione(){
        this.handlerPrenotazioneAssociato.cancellaPrenotazione(this.id);
    }
    public int getId() {
        return this.id;
    }
}
