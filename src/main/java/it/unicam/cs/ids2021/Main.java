package it.unicam.cs.ids2021;

public class Main {

    public static void main(String[] args) {
        DBMSController dbmsController = new DBMSController();
        Spiaggia spiaggiaTest = new Spiaggia();
        HandlerPrenotazione handlerPrenotazione = new HandlerPrenotazione(spiaggiaTest, dbmsController);
        Listino listino = new Listino();
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsController, listino);
        HandlerListino handlerListino = new HandlerListino(listino, dbmsController, spiaggiaTest);
        handlerSpiaggiaTest.aggiungiTipologiaOmbrellone();
        handlerSpiaggiaTest.aggiungiGrigliaSpiaggia();
        handlerSpiaggiaTest.aggiungiOmbrellone();
        handlerSpiaggiaTest.modificaOmbrellone();
        handlerPrenotazione.prenotaOmbrellone();
        handlerListino.aggiungiProdottoBar();
        handlerListino.modificaFasciaDiPrezzo();
        handlerListino.aggiungiFasciaDiPrezzo();
        handlerListino.modificaFasciaDiPrezzo();
        handlerListino.impostaPrezziOmbrellone();
    }
}