package it.unicam.cs.ids.asf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class handlerPrenotazione {
    private ArrayList<Prenotazione> listaPrenotazioni = new ArrayList<>();

    Prenotazione prenotazioneInCorso = new Prenotazione();


    public void prenotaOmbrellone() {
        inserisciOrarioDesiderato();
        //TODO ottieniVistaSpiaggia();
        //TODO richiestaListaPrenotazioni();
        selezionaOmbrelloneELettini();
        controllaDisponibilità(prenotazioneInCorso.getId(), prenotazioneInCorso.getData(), prenotazioneInCorso.getFasciaOraria(), listaPrenotazioni);

        Scanner kbd = new Scanner(System.in);

        String answer;
        boolean yn;

        System.out.println("Hai scelto questa prenotazione:");
        System.out.println("Ombrellone "+prenotazioneInCorso.getId()+" e "+prenotazioneInCorso.getNumeroLettini()+" lettino/i.");
        System.out.println("in data "+prenotazioneInCorso.getData()+".");
        System.out.println("Confermi la prenotazione? (y/n)");
        while (true) {
            answer = kbd.nextLine().trim().toLowerCase();
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

    }


    public void inserisciOrarioDesiderato() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserire la data [gg/mm/yyyy]: ");
        Date data = null;
        try {
            data = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato data non valido.");
        }

        System.out.print("Inserire fascia oraria: 1 = solo mattina, 2 = solo pomeriggio, 3 = mattina e pomeriggio: ");
        int fasciaOraria = sc.nextInt();

        Date dataCorrente = new Date();
        System.out.println(dataCorrente);
        if (data.before(dataCorrente)) {
            throw new IllegalArgumentException("Data non corretta");
        }

        if (fasciaOraria > 3 || fasciaOraria < 1) {
            throw new IllegalArgumentException("Fascia oraria non corretta");
        }

        prenotazioneInCorso.setData(data);
        prenotazioneInCorso.setFasciaOraria(fasciaOraria);

    }

    public void selezionaOmbrelloneELettini() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserire l'ombrellone desiderato: ");
        int idOmbrellone = sc.nextInt();

        System.out.print("Inserire il numero di lettini desiderato: ");
        int numLettini = sc.nextInt();

        //TODO: check dell'Id degli ombrelloni più grande

        prenotazioneInCorso.setId(idOmbrellone);
        prenotazioneInCorso.setNumeroLettini(numLettini);
    }

    public boolean controllaDisponibilità(int idOmbrellone, Date dataPrenotazione, int fasciaOrariaPrenotazione, ArrayList<Prenotazione> listaPrenotazioni) {
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

    public void confermaOperazione() {
        listaPrenotazioni.add(prenotazioneInCorso);

        inviaDatiPagamento();
    }

    public void inviaDatiPagamento() {
        //TODO: permettere l'inserimento dei dati per il pagamento
    }
}
