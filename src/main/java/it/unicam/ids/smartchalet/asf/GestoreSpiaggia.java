package it.unicam.ids.smartchalet.asf;

public class GestoreSpiaggia {

    private DBMSController associatedDBMSController;

    private HandlerSpiaggia associatedHandlerSpiaggia;

    public GestoreSpiaggia(DBMSController associatedDBMSController, HandlerSpiaggia associatedHandlerSpiaggia){
        this.associatedHandlerSpiaggia = associatedHandlerSpiaggia;
        this.associatedDBMSController = associatedDBMSController;
    }

    public void modificaOmbrellone(){
        this.associatedHandlerSpiaggia.modificaOmbrellone();
    }
}
