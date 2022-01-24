package it.unicam.cs.ids2021;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HandlerPrenotazione {
    private ArrayList<Prenotazione> listaPrenotazioni;
    private Prenotazione prenotazioneInCorso;
    private final DBMSController associatedDBMS;
    private final HandlerAttivita handlerAttivitaAssociato;
    private final Scanner sc;

    public HandlerPrenotazione(DBMSController associatedDBMS, HandlerAttivita handlerAttivitaAssociato){
        listaPrenotazioni = new ArrayList<>();
        this.associatedDBMS = associatedDBMS;
        this.handlerAttivitaAssociato = handlerAttivitaAssociato;
        this.sc = new Scanner(System.in);
    }

    public void partecipaEvento(int idCliente){
        this.listaPrenotazioni = this.associatedDBMS.ottieniListaPrenotazioni();
        ArrayList<Prenotazione> listaPrenotazioniFiltrata = this.filtroListaPrenotazioni(this.listaPrenotazioni,idCliente);
        if(listaPrenotazioniFiltrata.isEmpty()){
            System.out.println("Non sono presenti prenotazioni per questo utente, non è possibile prenotare attività");
            return;
        }
        ArrayList<Attivita> listaAttivitaDisponibili = this.filtroAttivitaDisponibili(this.handlerAttivitaAssociato.ottieniListaAttivitaDisponibili(),listaPrenotazioniFiltrata);

        this.sceltaPrenotazioniAttivita(listaAttivitaDisponibili, listaPrenotazioniFiltrata);
    }

    private void sceltaPrenotazioniAttivita(ArrayList<Attivita> listaAttivita, ArrayList<Prenotazione> listaPrenotazioniFiltrata) {
        ArrayList<Attivita> listaAttivitaDisponibili = listaAttivita;
        this.outputListaAttivitaDisponibili(listaAttivitaDisponibili);

        boolean flag;
        do{
            if(listaAttivitaDisponibili.isEmpty()){
                System.out.println("Non sono presenti attivita' da scegliere");
                break;
            }
            Attivita attivitaScelta = this.sceltaAttivita(listaAttivitaDisponibili);
            System.out.println("Inserisci il numero di persone che parteciperanno: ");
            int numeropartecipanti = this.provaScannerInt();
            if(this.controlloPostiDisponibili(attivitaScelta, numeropartecipanti)){
                listaAttivitaDisponibili = this.filtroAttivitaDisponibili(this.handlerAttivitaAssociato.ottieniListaAttivitaDisponibili(),listaPrenotazioniFiltrata);
            }
            this.outputListaAttivitaDisponibili(listaAttivitaDisponibili);
            System.out.println("Vuoi prenotare altre attivita' ? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }while(flag);
    }

    private boolean controlloPostiDisponibili(Attivita attivitaScelta, int numeropartecipanti) {
        if(attivitaScelta.getMaxPartecipanti()-attivitaScelta.getNumeroIscritti() >= numeropartecipanti){
            if(this.confermaOperazione()){
                attivitaScelta.aggiornaPostiDisponibiliAttivita(numeropartecipanti);
                if(this.associatedDBMS.aggiornaListaAttivita(this.handlerAttivitaAssociato.ottieniListaAttivitaDisponibili())){
                    System.out.println("Operazione eseguita con successo");
                    return true;
                }
                else System.out.println("Operazioni fallita");
            }
        }
        else System.out.println("Stai cercando di prenotare più posti di quelli disponibili, l'operazione non sara' effettuata ");

        return false;
    }

    private boolean confermaOperazione() {
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
    }

    private Attivita sceltaAttivita(ArrayList<Attivita> listaAttivitaDisponibili) {
        System.out.println("Inserire l'id dell'attivita' che vuoi scegliere (int)");
        int idAttivita = this.provaScannerInt();

        for(Attivita attivita : listaAttivitaDisponibili){
            if(attivita.getId() == idAttivita) return attivita;
        }

        System.out.println("Non hai selezionato nessuna attivita' esistente");
        this.outputListaAttivitaDisponibili(listaAttivitaDisponibili);
        return this.sceltaAttivita(listaAttivitaDisponibili);

    }

    private void outputListaAttivitaDisponibili(ArrayList<Attivita> listaAttivitaDisponibili) {
        for(Attivita attivita : listaAttivitaDisponibili){
            System.out.println(attivita.toString());
        }
    }

    private ArrayList<Attivita> filtroAttivitaDisponibili(ArrayList<Attivita> listaAttivita, ArrayList<Prenotazione> listaPrenotazioni) {
        ArrayList<Attivita> listaAttivitaFiltrata = new ArrayList<>();

        for(Prenotazione prenotazione : listaPrenotazioni){
            for(Attivita attivita : listaAttivita){
                if(prenotazione.getDataInizio().compareTo(attivita.getData()) <=0 && prenotazione.getDataFine().compareTo(attivita.getData()) >= 0){
                    listaAttivitaFiltrata.add(attivita);
                }
            }
        }
        return listaAttivitaFiltrata;
    }

    private ArrayList<Prenotazione> filtroListaPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni, int idCliente) {
        ArrayList<Prenotazione> listaPrenotazioniFiltrata = new ArrayList<>();
        for(Prenotazione prenotazione : listaPrenotazioni){
            if(prenotazione.getIdCliente() == idCliente) listaPrenotazioniFiltrata.add(prenotazione);
        }
        return listaPrenotazioniFiltrata;
    }



    public void prenotaOmbrellone() {
        prenotazioneInCorso = new Prenotazione();
        inserisciOrarioDesiderato();
        associatedDBMS.ottieniVistaSpiaggia();
        associatedDBMS.richiestaListaPrenotazioni();
        //TODO controlloDisponibilita();
        selezionaOmbrelloneELettini();
        //controlloOccupazione(prenotazioneInCorso.getId(), prenotazioneInCorso.getData(), prenotazioneInCorso.getFasciaOraria(), listaPrenotazioni);

        Scanner scans = new Scanner(System.in);

        String answer;
        boolean yn;

        System.out.println("Hai scelto questa prenotazione:");
        System.out.println("it.unicam.ids.smartchalet.asf.Ombrellone "+prenotazioneInCorso.getId()+" e "+prenotazioneInCorso.getNumeroLettini()+" lettino/i.");
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
        sc.nextLine();
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
        sc.nextLine();
        System.out.print("Inserire il numero di lettini desiderato: ");
        int numLettini = sc.nextInt();
        sc.nextLine();
        //TODO: check dell'Id degli ombrelloni più grande

        prenotazioneInCorso.setId(idOmbrellone);
        //spiaggiaAssociata.getOmbrellone(idOmbrellone).setIsBooked(true);
        prenotazioneInCorso.setNumeroLettini(numLettini);
    }

    /*private void confermaOperazione() {
        listaPrenotazioni.add(prenotazioneInCorso);

        inviaDatiPagamento();
    }

    private boolean controlloOccupazione(int idOmbrellone, Date dataPrenotazione, int fasciaOrariaPrenotazione, ArrayList<it.unicam.ids.smartchalet.asf.Prenotazione> listaPrenotazioni) {
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
    }*/

    private void inviaDatiPagamento() {
        //TODO: permettere l'inserimento dei dati per il pagamento
    }

    private int provaScannerInt(){
        while(true){
            try{
                int intero = this.sc.nextInt();
                this.sc.nextLine();
                return intero;
            } catch (Exception e) {
                System.out.println("Cio' che hai inserito non e' un valore numerico, ritenta ");
            }
        }
    }
}
