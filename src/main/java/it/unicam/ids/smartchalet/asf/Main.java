package it.unicam.ids.smartchalet.asf;

public class Main {

    public static void main(String[] args) {
        DBMSController dbmsController = new DBMSController();
        Spiaggia spiaggiaTest = new Spiaggia();
        Listino listino = new Listino();
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsController, listino);
        HandlerListino handlerListino = new HandlerListino(listino, dbmsController, spiaggiaTest);
        HandlerPrenotazione handlerPrenotazione = new HandlerPrenotazione(spiaggiaTest, dbmsController, listino);
        ListaAttrezzatura listaAttrezzatura = new ListaAttrezzatura();
        HandlerAttrezzatura handlerAttrezzatura = new HandlerAttrezzatura(listaAttrezzatura, dbmsController);
        ListaAttivita listaAttivita = new ListaAttivita();
        HandlerAttivita handlerAttivita = new HandlerAttivita(listaAttivita, dbmsController, handlerAttrezzatura);
     /* handlerListino.aggiungiProdottoBar();
        handlerAttivita.aggiungiAttivita();
        handlerAttrezzatura.aggiungiAttrezzatura();
        handlerAttivita.aggiungiAttivita();
        handlerAttivita.modificaAttivita();
        handlerSpiaggiaTest.aggiungiTipologiaOmbrellone();
        handlerSpiaggiaTest.aggiungiGrigliaSpiaggia();
        handlerSpiaggiaTest.aggiungiOmbrellone();
        handlerSpiaggiaTest.modificaGrigliaSpiaggia();
        handlerSpiaggiaTest.modificaOmbrellone();
        handlerListino.aggiungiProdottoBar();
        handlerListino.modificaFasciaDiPrezzo();
        handlerListino.aggiungiFasciaDiPrezzo();
        handlerListino.modificaFasciaDiPrezzo();
        handlerListino.impostaPrezziOmbrellone();
     */
        //handlerAttrezzatura.aggiungiAttrezzatura();
        //handlerAttrezzatura.rimuoviAttrezzatura();

        handlerPrenotazione.prenotaOmbrellone();
        //handlerPrenotazione.cancellaPrenotazione();


    }
}
