package it.unicam.ids.smartchalet.asf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HandlerPrenotazione {

    private ArrayList<Prenotazione> listaPrenotazioni;
    private Prenotazione prenotazioneInCorso;
    private final DBMSController associatedDBMS;
    private Spiaggia spiaggiaAssociata;
    private Listino listinoAssociato;
    Scanner sc;

    public HandlerPrenotazione(Spiaggia spiaggiaAssociata, DBMSController associatedDBMS, Listino listinoAssociato){
         this.spiaggiaAssociata = spiaggiaAssociata;
         this.listinoAssociato = listinoAssociato;
         listaPrenotazioni = new ArrayList<>();
         this.associatedDBMS = associatedDBMS;
         this.sc = new Scanner(System.in);
    }

    public void prenotaOmbrellone() {
        prenotazioneInCorso = new Prenotazione();
        spiaggiaAssociata.aggiungiGrigliaSpiaggia(associatedDBMS.ottieniVistaSpiaggia());
        listinoAssociato = associatedDBMS.ottieniListinoAggiornato();
        listaPrenotazioni = associatedDBMS.richiestaListaPrenotazioni();

        inserisciOrarioDesiderato();
        //TODO controlloDisponibilita();
        selezionaOmbrelloneELettini();
        //controlloOccupazione(prenotazioneInCorso.getId(), prenotazioneInCorso.getData(), prenotazioneInCorso.getFasciaOraria(), listaPrenotazioni);

        String answer;
        boolean yn;

        System.out.println("Hai scelto questa prenotazione:");
        System.out.println("it.unicam.ids.smartchalet.asf.Ombrellone "+prenotazioneInCorso.getId()+" e "+prenotazioneInCorso.getNumeroLettini()+" lettino/i.");
        System.out.println("da "+prenotazioneInCorso.getDataInizio()+" a "+prenotazioneInCorso.getDataFine()+".");
        System.out.println("Confermi la prenotazione? (y/n)");
        while (true) {
            answer = sc.nextLine().trim().toLowerCase();
            if (answer.equals("y")) {
                yn = true;
                break;
            } else if (answer.equals("n")) {
                yn = false;
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
        boolean working = true;
        while (working) {
            System.out.print("Inserire la data di inizio [gg/mm/yyyy]: ");
            String dataStartTemp = sc.next();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            Date dataStart =null;
            try {
                dataStart = dateFormat.parse(dataStartTemp);
                System.out.println(dataStart);
            } catch (ParseException e) {
                System.out.println(dataStartTemp+" è una data non valida, riprovare");
                continue;
            }

            System.out.print("Inserire la data di fine [gg/mm/yyyy]: ");
            String dataEndTemp = sc.next();
            Date dataEnd =null;
            try {
                dataEnd = dateFormat.parse(dataEndTemp);
                System.out.println(dataEnd);
            } catch (ParseException e) {
                System.out.println(dataEndTemp+" è una data non valida, riprovare");
                continue;
            }

            prenotazioneInCorso.setDataInizio(dataStart);
            prenotazioneInCorso.setDataInizio(dataEnd);
            
            if (controlloDataCorretta(dataStart, dataEnd)) {
                working = false;
            }
        }
    }

    /**
     * @param dataStart data da verificare di inizio della prenotazione
     * @param dataEnd data da verificare di fine della prenotazione
     * @return true se le date possono essere applicate a una prenotazione
     */
    private boolean controlloDataCorretta(Date dataStart, Date dataEnd) {
        if (dataStart.before(new java.util.Date()) || dataEnd.before(new java.util.Date()) || dataStart.after(dataEnd)) {
            System.out.println("Le date d");
            return false;
        } else{
            return true;
        }
    }

    //TODO: controllo se la giornata è sempre piena

    private void selezionaOmbrelloneELettini() {
        System.out.print("Inserire l'ombrellone desiderato: ");
        int idOmbrellone = sc.nextInt();
        sc.nextLine();
        System.out.print("Inserire il numero di lettini desiderato: ");
        int numLettini = sc.nextInt();
        sc.nextLine();
        //TODO: check dell'Id degli ombrelloni più grande

        prenotazioneInCorso.setId(idOmbrellone);
        spiaggiaAssociata.getOmbrellone(idOmbrellone).setIsBooked(true);
        prenotazioneInCorso.setNumeroLettini(numLettini);
    }

    private boolean confermaOperazione() {
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
    }

    private void aggiungiPrenotazione() {
        listaPrenotazioni.add(prenotazioneInCorso);

        inviaDatiPagamento();
    }

/*    private boolean controlloDisponibilita(Date dataInizio, Date dataFine) {
        if (dataInizio.before(new java.util.Date()) || dataFine.before(new java.util.Date())) {
            return false;
        } else {
            return true;
        }
    }*/

    /*private boolean controlloOccupazione(int idOmbrellone, Date dataPrenotazione, int fasciaOrariaPrenotazione, ArrayList<it.unicam.ids.smartchalet.asf.Prenotazione> listaPrenotazioni) {
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

    public void cancellaPrenotazione() {
        ArrayList<Prenotazione> listaPrenotazioni;
        listaPrenotazioni = associatedDBMS.richiestaListaPrenotazioni();
        //TODO: get id cliente
        int idCliente = 1;
        double prezzoDaRimborsare = 0;

        boolean working = true;
        while (working) {
            System.out.println("Selezionare la prenotazione da cancellare:");
            outputListaPrenotazioniCliente(listaPrenotazioni, idCliente);
            int indexPrenotazioneDaCancellare = sc.nextInt();
            sc.nextLine();
            if(indexPrenotazioneDaCancellare >= listaPrenotazioni.size()){
                System.out.println("Prenotazione scelta non valida.");
            }else if (listaPrenotazioni.get(indexPrenotazioneDaCancellare).getIdCliente() == idCliente) {
                System.out.println("Vuoi davvero cancellare la prenotazione ["+indexPrenotazioneDaCancellare+"]? [y/n] ");
                if (Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y")) {
                    prezzoDaRimborsare = prezzoDaRimborsare+listaPrenotazioni.get(indexPrenotazioneDaCancellare).getPrezzoTotale();
                    listaPrenotazioni.remove(indexPrenotazioneDaCancellare);
                }
            } else {
                System.out.println("Prenotazione scelta non valida.");
            }

            System.out.println("Vuoi cancellare altre prenotazioni? [y/n]");
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }
        if (this.confermaOperazione()) {
            // this.associatedDBMS.aggiornaMappaAttrezzature(this.listaAttrezzaturaAssociata.getMappaAttrezzatura());
            System.out.println("Operazioni eseguite");
            System.out.println("Verrà rimborsato l'importo versato per le prenotazioni pari a: "+prezzoDaRimborsare+"€");
            elaboraRimborso(prezzoDaRimborsare);
            //TODO sostituire output con metodo legato al database

        } else System.out.println("Operazioni annullate");

    }

    private void elaboraRimborso(double prezzoDaRimborsare) {
    }

    private void outputListaPrenotazioniCliente(ArrayList<Prenotazione> listaPrenotazioni, int idRichiedente){
        listaPrenotazioni.forEach(prenotazione -> {
            if(prenotazione.getIdCliente()==idRichiedente) {
                if (prenotazione.getDataFine().after(new Date()) && prenotazione.getDataInizio().after(new Date())) {
                    System.out.println("["+ listaPrenotazioni.indexOf(prenotazione)+"]: "+"id: " + prenotazione.getId() + " Cliente: " + prenotazione.getIdCliente() + " Da: " + prenotazione.getDataInizio() + " A: " + prenotazione.getDataFine());
                }
            }
        });
    }

}