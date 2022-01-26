package it.unicam.ids.smartchalet.asf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DBMSController {

    public ArrayList<ArrayList<Ombrellone>> ottieniVistaSpiaggia() {
        ArrayList<ArrayList<Ombrellone>> matriceOmbrelloni = new ArrayList<ArrayList<Ombrellone>>();

        Coordinate coordinate1 = new Coordinate(0,0);
        Ombrellone ombrellone1 = new Ombrellone("Uno", coordinate1, 1);

        Coordinate coordinate2 = new Coordinate(0,1);
        Ombrellone ombrellone2 = new Ombrellone("Uno", coordinate2, 2);

        Coordinate coordinate3 = new Coordinate(1,0);
        Ombrellone ombrellone3 = new Ombrellone("Uno", coordinate3, 3);

        Coordinate coordinate4 = new Coordinate(1,1);
        Ombrellone ombrellone4 = new Ombrellone("Uno", coordinate4, 4);

        ArrayList<Ombrellone> primaFila = new ArrayList<Ombrellone>();
        primaFila.add(ombrellone1);
        primaFila.add(ombrellone2);
        matriceOmbrelloni.add(primaFila);

        ArrayList<Ombrellone> secondaFila = new ArrayList<Ombrellone>();
        secondaFila.add(ombrellone3);
        secondaFila.add(null);
        matriceOmbrelloni.add(secondaFila);

        return matriceOmbrelloni;
    }

    public ArrayList<Prenotazione> richiestaListaPrenotazioni() {
        ArrayList<Prenotazione> listaPrenTest= new ArrayList<Prenotazione>();
        Prenotazione prenotazioneTest1 = new Prenotazione();
        prenotazioneTest1.setId(1);
        prenotazioneTest1.setIdCliente(1);

        Date dataInizio1 = null;
        Date dataFine1 = null;
        try {
            dataInizio1 = new SimpleDateFormat("dd/MM/yyyy").parse("22/02/2022");
            dataFine1 = new SimpleDateFormat("dd/MM/yyyy").parse("24/02/2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        prenotazioneTest1.setDataInizio(dataInizio1);
        prenotazioneTest1.setDataFine(dataFine1);
        prenotazioneTest1.setNumeroLettini(2);
        prenotazioneTest1.setPrezzoTotale(10.00);

        Prenotazione prenotazioneTest2 = new Prenotazione();
        prenotazioneTest2.setId(2);
        prenotazioneTest2.setIdCliente(1);
        Date dataInizio2 = null;
        Date dataFine2 = null;
        try {
            dataInizio2 = new SimpleDateFormat("dd/MM/yyyy").parse("23/02/2022");
            dataFine2 = new SimpleDateFormat("dd/MM/yyyy").parse("25/02/2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        prenotazioneTest2.setDataInizio(dataInizio2);
        prenotazioneTest2.setDataFine(dataFine2);
        prenotazioneTest2.setNumeroLettini(1);
        prenotazioneTest2.setPrezzoTotale(20.00);

        Prenotazione prenotazioneTest3 = new Prenotazione();
        prenotazioneTest3.setId(3);
        prenotazioneTest3.setIdCliente(1);
        Date dataInizio3 = null;
        Date dataFine3 = null;
        try {
            dataInizio3 = new SimpleDateFormat("dd/MM/yyyy").parse("23/02/2022");
            dataFine3 = new SimpleDateFormat("dd/MM/yyyy").parse("25/02/2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        prenotazioneTest3.setDataInizio(dataInizio2);
        prenotazioneTest3.setDataFine(dataFine2);
        prenotazioneTest3.setNumeroLettini(1);
        prenotazioneTest3.setPrezzoTotale(30.00);

        listaPrenTest.add(prenotazioneTest1);
        listaPrenTest.add(prenotazioneTest2);
        listaPrenTest.add(prenotazioneTest3);

        return listaPrenTest;
    }

    public boolean aggiungiGrigliaSpiaggia(ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia) {
        System.out.println("Nuova griglia spiaggia aggiunta al database");
        return true;
    }

    public boolean aggiornaMappaProdottoBar(HashMap<ProdottoBar, Double> listinoBarAggiornato) {
        return true;
    }

    public void ottieniListinoAggiornato() {

    }

    public void aggiungiProdottiBar(HashMap<ProdottoBar,Double> listinoBarAggiornato) {
        System.out.println("Nuovi prodotti bar aggiunti al database");
    }


    public HashMap<Attrezzatura, Integer> ottieniAttrezzatureAggiornate() {
        return null;
    }

    public HashMap<Attrezzatura, Integer> ottieniMappaAttrezzature(){
        Attrezzatura sdraio = new Attrezzatura("Sdraio", "Roba su cui ci si sdraia");
        Attrezzatura posacenere = new Attrezzatura("Posacenere", "Roba in cui si cicca");

        HashMap<Attrezzatura, Integer> listaAttrezzaturaTest = new HashMap<>();
        listaAttrezzaturaTest.put(sdraio, 10);
        listaAttrezzaturaTest.put(posacenere, 5);

        return listaAttrezzaturaTest;

    }

    public void aggiornaMappaAttrezzature(HashMap<Attrezzatura, Integer> mappaAttrezzatura) {
    }

    public ArrayList<Attivita> ottieniListaAttivita() {
        return null;
    }

    public HashMap<ProdottoBar, Double> ottieniMappaProdottiBar() {
        return null;
    }

    public void aggiungiOrdineBar(OrdineBar ordineBar) {
    }

    public ArrayList<OrdineBar> ottieniListaOrdiniBar() {
        return null;
    }

    public void aggiornaListaOrdiniBar(ArrayList<OrdineBar> ordiniInSospeso) {
    }

    public boolean aggiornaListaAttivita(ArrayList<Attivita> ottieniListaAttivitaDisponibili) {
        return true;
    }

    public void rimuoviPrenotazioni(ArrayList<Integer> listaPrenotazioniDaRimuovere) {
    }
}
