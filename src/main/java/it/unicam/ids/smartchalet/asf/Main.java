package it.unicam.ids.smartchalet.asf;

public class Main {

    public static void main(String[] args) {
        DBMSController dbmsController = new DBMSController();
        Spiaggia spiaggiaTest = new Spiaggia();
        Listino listino = new Listino();
        ListaAttrezzature listaAttrezzature = new ListaAttrezzature();
        HandlerPrenotazione handlerPrenotazione = new HandlerPrenotazione(spiaggiaTest, dbmsController, listino);
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsController, listino);
        HandlerListino handlerListino = new HandlerListino(listino, dbmsController, spiaggiaTest);
        HandlerAttrezzatura handlerAttrezzaturaTest = new HandlerAttrezzatura(listaAttrezzature, dbmsController);
        //handlerSpiaggiaTest.aggiungiTipologiaOmbrellone();
        //handlerSpiaggiaTest.aggiungiGrigliaSpiaggia();
        //handlerSpiaggiaTest.aggiungiOmbrellone();
        //handlerSpiaggiaTest.modificaOmbrellone();
        handlerPrenotazione.prenotaOmbrellone();
        //handlerListino.aggiungiProdottoBar();
        //handlerListino.modificaFasciaDiPrezzo();
        //handlerListino.aggiungiFasciaDiPrezzo();
        //handlerListino.modificaFasciaDiPrezzo();
        //handlerListino.impostaPrezziOmbrellone();
        //handlerAttrezzaturaTest.aggiungiAttrezzatura();
        //handlerAttrezzaturaTest.rimuoviAttrezzatura();

    }
}
