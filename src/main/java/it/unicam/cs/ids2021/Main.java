package it.unicam.cs.ids2021;

public class Main {

    public static void main(String[] args) {
        DBMSController dbmsController = new DBMSController();
        Spiaggia spiaggiaTest = new Spiaggia();
        //HandlerPrenotazione handlerPrenotazione = new HandlerPrenotazione(spiaggiaTest, dbmsController);
        Listino listino = new Listino();
        HandlerListino handlerListino = new HandlerListino(listino, dbmsController);
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsController, handlerListino);
        handlerListino.setHandlerSpiaggiaAssociato(handlerSpiaggiaTest);

        //handlerSpiaggiaTest.aggiungiTipologiaOmbrellone();
        handlerSpiaggiaTest.aggiungiGrigliaSpiaggia();
        //handlerSpiaggiaTest.modificaGrigliaSpiaggia();

        handlerListino.aggiungiFasciaDiPrezzo();
        handlerListino.modificaFasciaDiPrezzo();

        /*
        handlerSpiaggiaTest.aggiungiOmbrellone();

        handlerSpiaggiaTest.modificaOmbrellone();
        handlerPrenotazione.prenotaOmbrellone();
        handlerListino.aggiungiProdottoBar();
        handlerListino.modificaFasciaDiPrezzo();
        handlerListino.aggiungiFasciaDiPrezzo();
        handlerListino.modificaFasciaDiPrezzo();
        handlerListino.impostaPrezziOmbrellone();
         */
    }
}