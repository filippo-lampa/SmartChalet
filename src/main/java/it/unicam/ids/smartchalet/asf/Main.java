package it.unicam.ids.smartchalet.asf;

public class Main {

    public static void main(String[] args) {
        DBMSController dbmsController = new DBMSController();
        Spiaggia spiaggiaTest = new Spiaggia();
        HandlerPrenotazione handlerPrenotazione = new HandlerPrenotazione(spiaggiaTest, dbmsController);
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsController);
        Listino listino = new Listino();
        handlerSpiaggiaTest.aggiungiTipologiaOmbrellone();
        handlerSpiaggiaTest.aggiungiOmbrellone();
        handlerSpiaggiaTest.modificaOmbrellone();
        handlerPrenotazione.prenotaOmbrellone();
        handlerSpiaggiaTest.modificaOmbrellone();
    }
}
