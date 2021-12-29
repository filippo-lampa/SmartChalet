package it.unicam.cs.ids.asf;

public class Main {

    public static void main(String[] args) {
        DBMSController dbmsController = new DBMSController();
        Spiaggia spiaggiaTest = new Spiaggia();
        HandlerPrenotazione handlerPrenotazione = new HandlerPrenotazione(spiaggiaTest, dbmsController);
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsController);
        Listino listino = new Listino();
        HandlerListino handlerListinoTest = new HandlerListino(dbmsController, listino, handlerSpiaggiaTest);
        //handlerSpiaggiaTest.aggiungiTipologiaOmbrellone();
        //handlerSpiaggiaTest.aggiungiOmbrellone();
        //handlerSpiaggiaTest.modificaOmbrellone();
        //handlerPrenotazione.prenotaOmbrellone();
        //handlerSpiaggiaTest.modificaOmbrellone();
        handlerListinoTest.impostaPrezziOmbrellone();
        //handlerListinoTest.aggiungiFasciaDiPrezzo();
    }
}
