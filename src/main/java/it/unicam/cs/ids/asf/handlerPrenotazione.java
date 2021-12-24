package it.unicam.cs.ids.asf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class handlerPrenotazione {
    private ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();

    Prenotazione prenotazioneInCorso;


    public void prenotaOmbrellone() {
        prenotazioneInCorso = new Prenotazione();

        inserisciOrarioDesiderato();
        //TODO ottieniVistaSpiaggia();
        //TODO richiestaListaPrenotazioni();
        //TODO controlloDisponibilita();
        selezionaOmbrelloneELettini();
        //controlloOccupazione(prenotazioneInCorso.getId(), prenotazioneInCorso.getData(), prenotazioneInCorso.getFasciaOraria(), listaPrenotazioni);

        Scanner scans = new Scanner(System.in);

        String answer;
        boolean yn;

        System.out.println("Hai scelto questa prenotazione:");
        System.out.println("Ombrellone "+prenotazioneInCorso.getId()+" e "+prenotazioneInCorso.getNumeroLettini()+" lettino/i.");
        System.out.println("da "+prenotazioneInCorso.getDataInizio()+" a "+prenotazioneInCorso.getDataFine()+".");
        System.out.println("Confermi la prenotazione? (y/n)");
        while (true) {
            answer = scans.nextLine().trim().toLowerCase();
            if (answer.equals("y")) {
                yn = true;
                confermaOperazione();
                break;
            } else if (answer.equals("n")) {
                yn = false;
                prenotaOmbrellone();
                break;
            } else {
                System.out.println("Reinserire (y/n)");
            }
        }
        inviaDatiPagamento();
        //TODO: UpdateDatabase();

    }


    private void inserisciOrarioDesiderato() {
        //TODO: da modificare
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserire la data di inizio [gg/mm/yyyy]: ");
        Date dataStart = null;
        try {
            dataStart = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
        } catch (ParseException e) {
            System.out.println("Formato data non valido.");
            inserisciOrarioDesiderato();

        }

        System.out.print("Inserire la data di fine [gg/mm/yyyy]: ");
        Date dataEnd = null;
        try {
            dataEnd = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
        } catch (ParseException e) {
            System.out.println("Formato data non valido.");
            inserisciOrarioDesiderato();
        }

        System.out.print("Inserire fascia oraria: 1 = solo mattina, 2 = solo pomeriggio, 3 = mattina e pomeriggio: ");
        int fasciaOraria = sc.nextInt();

        Date dataCorrente = new Date();
        System.out.println(dataCorrente);
        if (dataStart.before(dataCorrente) || dataEnd.before(dataCorrente)) {
            throw new IllegalArgumentException("Data non corretta");
        }

        if (fasciaOraria > 3 || fasciaOraria < 1) {
            throw new IllegalArgumentException("Fascia oraria non corretta");
        }

        prenotazioneInCorso.setDataInizio(dataStart);
        prenotazioneInCorso.setDataFine(dataEnd);
        prenotazioneInCorso.setFasciaOraria(fasciaOraria);

    }

    private void selezionaOmbrelloneELettini() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserire l'ombrellone desiderato: ");
        int idOmbrellone = sc.nextInt();

        System.out.print("Inserire il numero di lettini desiderato: ");
        int numLettini = sc.nextInt();

        //TODO: check dell'Id degli ombrelloni più grande

        prenotazioneInCorso.setId(idOmbrellone);
        prenotazioneInCorso.setNumeroLettini(numLettini);
    }

    private boolean controlloOccupazione(int idOmbrellone, Date dataPrenotazione, int fasciaOrariaPrenotazione, ArrayList<Prenotazione> listaPrenotazioni) {
        for (int i = 0;i <= listaPrenotazioni.size() ; i++) {
            if (idOmbrellone == listaPrenotazioni.get(i).getId()) {
                if (dataPrenotazione.equals(listaPrenotazioni.get(i).getData())) {
                    if (fasciaOrariaPrenotazione == 3 || fasciaOrariaPrenotazione == listaPrenotazioni.get(i).getFasciaOraria()){
                        System.out.println("L'ombrellone selezionato è occupato");
                        selezionaOmbrelloneELettini();
                    }
                }
            }
        }
        return false;
    }

    private void confermaOperazione() {
        listaPrenotazioni.add(prenotazioneInCorso);

        inviaDatiPagamento();
    }

    private void inviaDatiPagamento() {
        //TODO: permettere l'inserimento dei dati per il pagamento
    }
}
