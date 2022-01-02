package it.unicam.cs.ids2021;

public class Main {

        public static void main(String[] args) {
            DBMSController dbmsController = new DBMSController();
            Spiaggia spiaggiaTest = new Spiaggia();
            Listino listino = new Listino();
            HandlerPrenotazione handlerPrenotazione = new HandlerPrenotazione(spiaggiaTest, dbmsController);
            HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsController, listino);
            HandlerListino handlerListinoTest = new HandlerListino(dbmsController,listino, spiaggiaTest);

            listino.getPrezziFascia().put(new FasciaDiPrezzo("Fascia1",new Coordinate(1,1),new Coordinate(3,1)),1.5);
            listino.getPrezziFascia().put(new FasciaDiPrezzo("Fascia2",new Coordinate(0,0),new Coordinate(3,0)),1.2);
            listino.getPrezziFascia().put(new FasciaDiPrezzo("Fascia3",new Coordinate(2,2),new Coordinate(0,3)),1.1);
            //handlerListinoTest.modificaFasciaDiPrezzo();

            handlerSpiaggiaTest.aggiungiTipologiaOmbrellone();
            //handlerSpiaggiaTest.aggiungiOmbrellone();

            //handlerSpiaggiaTest.modificaOmbrellone();
            //handlerPrenotazione.prenotaOmbrellone();
            //handlerSpiaggiaTest.modificaOmbrellone();
        }
}
