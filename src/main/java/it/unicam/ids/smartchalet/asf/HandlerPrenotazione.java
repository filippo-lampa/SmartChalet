package it.unicam.ids.smartchalet.asf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HandlerPrenotazione {

    private ArrayList<Prenotazione> listaPrenotazioni;
    private Prenotazione prenotazioneInCorso;
    private Listino listinoGestito;
    private final DBMSController associatedDBMS;
    private Spiaggia spiaggiaAssociata;
    private Scanner sc;

    public HandlerPrenotazione(Spiaggia spiaggiaAssociata, DBMSController associatedDBMS, Listino listinoGestito) {
        sc = new Scanner(System.in);
        this.spiaggiaAssociata = spiaggiaAssociata;
        listaPrenotazioni = new ArrayList<>();
        this.associatedDBMS = associatedDBMS;
        this.listinoGestito = listinoGestito;

    }

    public void prenotaOmbrellone() {
        prenotazioneInCorso = new Prenotazione();

        boolean invalidDateFlag = true;
        do {
            inserisciOrarioDesiderato();

            //associatedDBMS.ottieniVistaSpiaggia();
            listinoGestito = associatedDBMS.ottieniListinoAggiornato();
            listaPrenotazioni = associatedDBMS.richiestaListaPrenotazioni();

            if (prenotazioneInCorso.getDataInizio() != null && prenotazioneInCorso.getDataFine() != null) {
                if (controlloDisponibilita(prenotazioneInCorso.getDataInizio(), prenotazioneInCorso.getDataFine(), prenotazioneInCorso.getFasciaOraria())) {
                    invalidDateFlag = false;
                }
            }
        } while (invalidDateFlag);

        /*
        listinoGestito.mostraFasceEPrezzi();
        double prezzoBase = listinoGestito.getPrezzoBaseOmbrellone();

        spiaggiaAssociata.getListaOmbrelloni();
        listinoGestito.outputListaTipologie();

        calcoloPrezzo();
        */


        boolean flag = true;
        do {
            selezionaOmbrelloneELettini();

            if (controlloOccupazione(prenotazioneInCorso.getId(), prenotazioneInCorso.getDataInizio(), prenotazioneInCorso.getDataFine(), prenotazioneInCorso.getFasciaOraria(), listaPrenotazioni)){
                continue;
            }

            System.out.println("Vuoi prenotare altri ombrelloni? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            this.sc.nextLine();
        } while (flag);

        String answer;
        boolean yn;

        System.out.println("Hai scelto questa prenotazione:");
        System.out.println("it.unicam.ids.smartchalet.asf.Ombrellone " + prenotazioneInCorso.getId() + " e " + prenotazioneInCorso.getNumeroLettini() + " lettino/i.");
        System.out.println("da " + prenotazioneInCorso.getDataInizio() + " a " + prenotazioneInCorso.getDataFine() + ".");
        System.out.println("Confermi la prenotazione? (y/n)");
        while (true) {
            answer = sc.nextLine().trim().toLowerCase();
            if (answer.equals("y")) {
                yn = true;
                confermaPrenotazioni();
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

        if (this.confermaOperazione())
            System.out.println("Operazioni eseguite"); //TODO aggiungere aggiornamento database
            //TODO: riceviRecap();
        else System.out.println("Operazioni annullate");
    }

    private void inserisciOrarioDesiderato() {
        //TODO: da modificare

        System.out.print("Inserire la data di inizio [gg/mm/yyyy]: ");
        Date dataStart = null;
        try {
            dataStart = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
        } catch (ParseException e) {
            System.out.println("Formato data non valido.");
            return;
        }

        System.out.print("Inserire la data di fine [gg/mm/yyyy]: ");
        Date dataEnd = null;
        try {
            dataEnd = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
        } catch (ParseException e) {
            System.out.println("Formato data non valido.");
            return;
        }

        System.out.print("Inserire fascia oraria: 1 = solo mattina, 2 = solo pomeriggio, 3 = mattina e pomeriggio: ");
        int fasciaOraria = sc.nextInt();
        sc.nextLine();

        prenotazioneInCorso.setDataInizio(dataStart);
        prenotazioneInCorso.setDataFine(dataEnd);
        prenotazioneInCorso.setFasciaOraria(fasciaOraria);

    }

    private boolean controlloDisponibilita(Date dataStart, Date dataEnd, int fasciaOraria) {
        Date dataCorrente = new Date();
        System.out.println(dataCorrente);

        if (dataStart.before(dataCorrente) || dataEnd.before(dataCorrente)) {
            System.out.println("Data non corretta");
            return false;
        } else if (fasciaOraria > 3 || fasciaOraria < 1) {
            System.out.println("Fascia oraria non corretta");
            return false;
        }

        return true;
    }

    private void selezionaOmbrelloneELettini() {
        System.out.print("Inserire l'ombrellone desiderato: ");
        int idOmbrellone = sc.nextInt();
        sc.nextLine();
        System.out.print("Inserire il numero di lettini desiderato: ");
        int numLettini = sc.nextInt();
        sc.nextLine();

        prenotazioneInCorso.setId(idOmbrellone);
        //spiaggiaAssociata.getOmbrellone(idOmbrellone).setIsBooked(true);
        prenotazioneInCorso.setNumeroLettini(numLettini);
    }

    private void confermaPrenotazioni() {
        listaPrenotazioni.add(prenotazioneInCorso);

        inviaDatiPagamento();
    }

    private boolean controlloOccupazione(int idOmbrellone, Date dataInizioPrenotazione, Date dataFinePrenotazione, int fasciaOrariaPrenotazione, ArrayList<Prenotazione> listaPrenotazioni) {
        for (Prenotazione prenotazione : listaPrenotazioni) {
            if (idOmbrellone == prenotazione.getId()) {
                if (dataInizioPrenotazione.before(prenotazione.getDataInizio()) && dataFinePrenotazione.after(prenotazione.getDataInizio()) ||
                        dataInizioPrenotazione.equals(prenotazione.getDataFine()) && dataFinePrenotazione.equals(prenotazione.getDataFine()) ||
                        dataInizioPrenotazione.before(prenotazione.getDataFine()) && dataFinePrenotazione.after(prenotazione.getDataFine()) ||
                        dataInizioPrenotazione.before(prenotazione.getDataInizio()) && dataFinePrenotazione.after(prenotazione.getDataFine()) ||
                        dataInizioPrenotazione.after(prenotazione.getDataInizio()) && dataFinePrenotazione.before(prenotazione.getDataFine())) {
                    System.out.println("L'ombrellone selezionato è occupato.");
                    return true;
                }
            }
        }
        return false;
    }

    /*
                }*/

    public void mostraDisposizioneEPrezzo() {
        ArrayList<ArrayList<Ombrellone>> vistaSpiaggiaCorrente = spiaggiaAssociata.getListaOmbrelloni();
        int posizioneOmbrelloneCounter = 0;
        for (ArrayList<Ombrellone> currentRow : vistaSpiaggiaCorrente) {
            for (Ombrellone currentOmbrellone : currentRow) {
                System.out.println("Posizione numero: " + posizioneOmbrelloneCounter);
                if (currentOmbrellone != null) {
                    System.out.println("\t" + " (Coordinate " + currentOmbrellone.getLocation().getxAxis() + " " + currentOmbrellone.getLocation().getyAxis() + " )");
                    System.out.println("\t" + " Id ombrellone: " + currentOmbrellone.getIdOmbrellone());
                    System.out.println("\t" + " Tipo: " + currentOmbrellone.getNomeTipo() + " ");
                    System.out.println("\t" + " Numero lettini associati: " + currentOmbrellone.getNumeroLettiniAssociati() + " ");
                    System.out.println("\t" + " L'ombrellone è prenotato: " + currentOmbrellone.isBooked() + " ");
                } else System.out.println("\t" + "Posizione vuota, nessun ombrellone piazzato");
                posizioneOmbrelloneCounter++;
            }
        }
    }

    private void inviaDatiPagamento() {
        //TODO: permettere l'inserimento dei dati per il pagamento
    }

    private boolean confermaOperazione() {
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

    private void calcoloPrezzo() {

    }

}