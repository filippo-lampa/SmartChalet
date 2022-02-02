package it.unicam.ids.smartchalet.asf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class HandlerPrenotazione {

    private final HandlerSpiaggia handlerSpiaggiaAssociato;
    private ArrayList<Prenotazione> listaPrenotazioni;
    private Prenotazione prenotazioneInCorso;
    private final DBMSController associatedDBMS;
    private Scanner sc = new Scanner(System.in);
    private HashMap<ProdottoBar, Integer> carrello;
    private ArrayList<String> codiciCouponValidi;
    private HandlerAttivita handlerAttivitaAssociato;
    private HandlerListino handlerListinoAssociato;
    private static HandlerPrenotazione instance = null;

    private HandlerPrenotazione() {
        this.handlerListinoAssociato = HandlerListino.getInstance();
        this.codiciCouponValidi = new ArrayList<>();
        listaPrenotazioni = new ArrayList<>();
        this.associatedDBMS = DBMSController.getInstance();
        this.handlerSpiaggiaAssociato = HandlerSpiaggia.getInstance();
        this.handlerAttivitaAssociato = HandlerAttivita.getInstance();
    }

    public static HandlerPrenotazione getInstance() {
        if (instance == null) {
            instance = new HandlerPrenotazione();
        }
        return instance;
    }

    public ArrayList<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    public void setListaPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
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
                if(this.associatedDBMS.aggiornaListaAttivita(this.handlerAttivitaAssociato.getListaAttivitaAssociata().ottieniListaAttivitaAggiornata())){
                    System.out.println("Operazione eseguita con successo");
                    return true;
                }
                else System.out.println("Operazioni fallita");
            }
        }
        else System.out.println("Stai cercando di prenotare più posti di quelli disponibili, l'operazione non sara' effettuata ");

        return false;
    }

    private Attivita sceltaAttivita(ArrayList<Attivita> listaAttivitaDisponibili) {
        do {
            System.out.println("Inserire il nome dell'attivita' che vuoi scegliere ");
            String nomeAttivita = this.sc.nextLine();
            for(Attivita attivita : listaAttivitaDisponibili)
                if(attivita.getNome().equals(nomeAttivita))
                    return attivita;
            System.out.println("Non hai selezionato nessuna attività esistente");
        }while(true);
    }

    private int provaScannerInt(){ //TODO controllo interi negativi
        while(true){
            try{
                int intero = this.sc.nextInt();
                this.sc.nextLine();
                return intero;
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Cio' che hai inserito non e' un valore numerico, ritenta ");
            }
        }
    }

    private void outputListaAttivitaDisponibili(ArrayList<Attivita> listaAttivitaDisponibili) {
        for(Attivita attivita : listaAttivitaDisponibili){
            attivita.printDettagliAttivita();
        }
    }

    private ArrayList<Attivita> filtroAttivitaDisponibili(ArrayList<Attivita> listaAttivita, ArrayList<Prenotazione> listaPrenotazioni) { //TODO modificare con controllo fasce
        ArrayList<Attivita> listaAttivitaFiltrata = new ArrayList<>();

        for(Prenotazione prenotazione : listaPrenotazioni){
            for(Date data : prenotazione.getMappaDateFasce().keySet()) {
                for (Attivita attivita : listaAttivita) {
                    if ((data.equals(attivita.getData()) && prenotazione.getMappaDateFasce().get(data) == attivita.getFasciaOraria()) || prenotazione.getMappaDateFasce().get(data) == 3) {
                        listaAttivitaFiltrata.add(attivita);
                    }
                }
            }
        }
        return listaAttivitaFiltrata;
    }

    public ArrayList<Prenotazione> filtroListaPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni, int idCliente) {
        ArrayList<Prenotazione> listaPrenotazioniFiltrata = new ArrayList<>();
        for(Prenotazione prenotazione : listaPrenotazioni){
            if(prenotazione.getIdCliente() == idCliente) listaPrenotazioniFiltrata.add(prenotazione);
        }
        return listaPrenotazioniFiltrata;
    }

    public void cancellaPrenotazione(int idCliente) {
        ArrayList<Prenotazione> listaPrenotazioni;
        ArrayList<Integer> listaPrenotazioniDaRimuovere = new ArrayList<>();
        listaPrenotazioni = associatedDBMS.ottieniListaPrenotazioni();
        double prezzoDaRimborsare = 0;

        boolean working = true;
        while (working) {
            System.out.println("Inserire l'id della prenotazione da cancellare:");
            outputListaPrenotazioniCliente(listaPrenotazioni, idCliente);
            int idPrenotazione = this.provaScannerInt();
            Prenotazione prenotazioneDaRimuovere = this.ottieniPrenotazione(idPrenotazione);
            if(prenotazioneDaRimuovere == null){
                System.out.println("Prenotazione scelta non valida.");
            }else if (prenotazioneDaRimuovere.getIdCliente() == idCliente) {
                System.out.println("Vuoi davvero cancellare la prenotazione con id ["+prenotazioneDaRimuovere.getId()+"]? [y/n] ");
                if (Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y")) {
                    prezzoDaRimborsare = prezzoDaRimborsare+prenotazioneDaRimuovere.getPrezzoTotale();
                    listaPrenotazioni.remove(prenotazioneDaRimuovere);
                    listaPrenotazioniDaRimuovere.add(prenotazioneDaRimuovere.getId());
                }
            } else {
                System.out.println("Prenotazione scelta non valida.");
            }

            System.out.println("Vuoi cancellare altre prenotazioni? [y/n]");
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }
        if (this.confermaOperazione()) {
            elaboraRimborso(prezzoDaRimborsare);
            this.associatedDBMS.rimuoviPrenotazioni(listaPrenotazioniDaRimuovere);
            System.out.println("Operazioni eseguite");
        } else System.out.println("Operazioni annullate");

    }

    private Prenotazione ottieniPrenotazione(int idPrenotazione) {
        for(Prenotazione currentPrenotazione : this.listaPrenotazioni) {
            if (currentPrenotazione.getId() == idPrenotazione)
                return currentPrenotazione;
        }
        return null;
    }

    public void prenotaOmbrellone(int idCliente) {
        this.handlerSpiaggiaAssociato.ottieniVistaSpiaggia();
        this.handlerListinoAssociato.ottieniListinoAggiornato();
        listaPrenotazioni = associatedDBMS.ottieniListaPrenotazioni();
        HashMap<Date, Integer> mappaDateFasceTemp = this.inserisciDate();
        double prezzoBaseAttuale = handlerListinoAssociato.ottieniPrezzoBaseOmbrellone();
        HashMap<FasciaDiPrezzo, Double> prezzoFasceAttuale = handlerListinoAssociato.ottieniPrezziFasce();
        HashMap<TipologiaOmbrellone, Double> prezzoTipologieAttuale = handlerListinoAssociato.ottieniPrezziTipologie();
        Map<Date, Integer> mappaDateFasceOrdinate = new TreeMap<>(mappaDateFasceTemp);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Date> dateDaRimuovere = new ArrayList<>();
        Date dataFinePrenotazione = this.ottieniDataFinePrenotazione(mappaDateFasceOrdinate);
        for (Map.Entry<Date, Integer> entry : mappaDateFasceOrdinate.entrySet()) {
            if(!this.aggiuntaOmbrelloniAData(entry, dateDaRimuovere, dataFinePrenotazione, idCliente)) {
                System.out.println("Prenotazione annullata.");
                return;
            }
        }
        for(Date data : dateDaRimuovere )
            mappaDateFasceOrdinate.remove(data);

        if (this.confermaOperazione()) {
            this.elaboraPagamento(this.richiestaDatiPagamento(), prenotazioneInCorso.getPrezzoTotale());
            if(this.associatedDBMS.aggiungiPrenotazione(prenotazioneInCorso)){
                this.recapPrenotazione(prenotazioneInCorso);
            }
            else System.out.println("Operazione fallita");
        } else {
            System.out.println("Prenotazione annullata.");
        }
    }

    private Date ottieniDataFinePrenotazione(Map<Date, Integer> mappaDateFasceOrdinate) {
        Date tempMaxDate = new Date(0);
        for(Date data : mappaDateFasceOrdinate.keySet()){
            if(data.getTime() > tempMaxDate.getTime()) {
                tempMaxDate = data;
            }
        }
        return tempMaxDate;
    }

    private boolean aggiuntaOmbrelloniAData(Map.Entry<Date, Integer> entry, ArrayList<Date> dateDaRimuovere, Date dataFinePrenotazione, int idCliente) {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println("Seleziona gli ombrelloni per il giorno " + formatter.format(entry.getKey()));
        boolean working=true;
        ArrayList<Ombrellone> ombrelloniScelti = new ArrayList<>();
        ArrayList<Ombrellone> listaPrenotabili;
        while(working) {
            listaPrenotabili = controlloOccupazione(entry.getKey(), entry.getValue());
            if (listaPrenotabili.isEmpty()) {
                System.out.println("Non ci sono ombrelloni disponibili il " + entry.getKey() + " nell'orario specificato, annullare la prenotazione?");

                if (Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y"))
                    return false;
                else {
                    dateDaRimuovere.add(entry.getKey());
                    break;
                }
            }
            listaPrenotabili.removeAll(ombrelloniScelti);
            if(listaPrenotabili.isEmpty()){
                System.out.println("Non ci sono altri ombrelloni da scegliere");
                break;
            }
            mostraDisposizioneOmbrelloniDisponibiliEPrezzo(listaPrenotabili);

            Ombrellone ombrelloneSelezionato = selezionaOmbrellone(listaPrenotabili);
            int numeroLettiniSelezionati;

            ombrelloniScelti.add(ombrelloneSelezionato);
            numeroLettiniSelezionati = selezionaNumeroLettini();

            if (prenotazioneInCorso == null) {
                this.prenotazioneInCorso = new Prenotazione(entry.getKey(), dataFinePrenotazione, idCliente);
            }
            prenotazioneInCorso.getMappaDateListaOmbrelloni().put(entry.getKey(),ombrelloniScelti);
            ombrelloneSelezionato.setNumeroLettiniAssociati(numeroLettiniSelezionati);

            prenotazioneInCorso.setPrezzoTotale(prenotazioneInCorso.getPrezzoTotale() + calcoloSubtotale(ombrelloneSelezionato));
            System.out.println("Il prezzo per questa prenotazione è ora di " + prenotazioneInCorso.getPrezzoTotale() + "€.\n");

            System.out.println("Aggiungere altri ombrelloni alla prenotazione? [y/n]");
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }

        return true;
    }

    private HashMap<Date,Integer> inserisciDate() {
        boolean dataCorretta = false;
        Date dataInizioPrenotazione = null;
        Date dataFinePrenotazione = null;
        while (!dataCorretta) {
            dataInizioPrenotazione = inserisciDataDesiderata("inizio");
            dataFinePrenotazione = inserisciDataDesiderata("fine");
            if (controlloDataCorretta(dataInizioPrenotazione, dataFinePrenotazione)) {
                dataCorretta = true;
            }
        }
        return associaFasciaOrario(dataInizioPrenotazione, dataFinePrenotazione);
    }

    /**
     * @return data di fine della prenotazione selezionata dal cliente
     */
    private Date inserisciDataDesiderata(String operazione) {
        boolean working = true;
        Date data = null;
        while (working) {
            System.out.print("Inserire la data di " + operazione + " [gg/mm/yyyy]: ");
            String dataTemp = sc.next();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            data = null;
            try {
                data = dateFormat.parse(dataTemp);
                working = false;
            } catch (ParseException e) {
                System.out.println(dataTemp + " è una data non valida, riprovare");
                continue;
            }
        }
        return data;
    }

    /**
     * @param dataStart data da verificare di inizio della prenotazione
     * @param dataEnd data da verificare di fine della prenotazione
     * @return true se le date possono essere applicate a una prenotazione
     */
    private boolean controlloDataCorretta(Date dataStart, Date dataEnd) {
        Date currentDate = new Date();
        if (dataStart.before(currentDate) || dataEnd.before(currentDate) || dataStart.after(dataEnd)) {
            System.out.println("Le date non sono corrette");
            return false;
        } else{
            return true;
        }
    }

    /**
     * @param dataInizio data di inizo della prenotazione selezionata dal cliente
     * @param dataFine data di fine della prenotazione selezionata dal cliente
     * @return hashmap dentro la quale si associano tutte le date tra l'inizio e la fine della
     * prenotazione a una fascia d'orario
     */
    private HashMap<Date, Integer> associaFasciaOrario(Date dataInizio, Date dataFine) {
        HashMap<Date, Integer> mappaDateFasceTemp = new HashMap<>();
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        int fasciaOraria;
        System.out.print("Inserire la fascia oraria desiderata per la giornata del "+formatter.format(dataInizio)+": \n 1 = solo mattina, 2 = solo pomeriggio, 3 = mattina e pomeriggio: ");
        fasciaOraria = provaScannerInt();
        mappaDateFasceTemp.put(dataInizio, fasciaOraria);
        while (dataInizio.before(dataFine)) {
            dataInizio = addDays(dataInizio, 1);
            System.out.print("Inserire la fascia oraria desiderata per la giornata del "+formatter.format(dataInizio)+": \n 1 = solo mattina, 2 = solo pomeriggio, 3 = mattina e pomeriggio: ");
            fasciaOraria = provaScannerInt();
            mappaDateFasceTemp.put(dataInizio, fasciaOraria);
        }
        return mappaDateFasceTemp;
    }

    private static Date addDays(Date d1, int i) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d1);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * @param dataPrenotazione data del calendario della prenotazione da controllare
     * @param fasciaOrariaPrenotazione fascia oraria della prenotazione da controllare
     * @return lista degli ombrelloni disponibili in quella data e fascia oraria
     */
    private ArrayList<Ombrellone> controlloOccupazione(Date dataPrenotazione, int fasciaOrariaPrenotazione) {
        ArrayList<Ombrellone> listaTemp = new ArrayList<>();
        for (ArrayList<Ombrellone> innerlist : handlerSpiaggiaAssociato.ottieniVistaSpiaggia()) {
            for (Ombrellone i : innerlist) {
                if(i != null) {
                    handlerSpiaggiaAssociato.getOmbrellone(i.getIdOmbrellone()).setIsBooked(false);
                    listaTemp.add(i);
                }
            }
        }
        ArrayList<Ombrellone> listaOmbrPrenotabili = new ArrayList<>();
        for (Prenotazione prenotazione : this.listaPrenotazioni) {
            for (Map.Entry<Date, Integer> entry : prenotazione.getMappaDateFasce().entrySet()) {
                if (entry.getKey().equals(dataPrenotazione) && (entry.getValue() == fasciaOrariaPrenotazione || entry.getValue() == 3 || fasciaOrariaPrenotazione ==3)) {
                    for (Ombrellone prenotato : prenotazione.getMappaDateListaOmbrelloni().get(entry.getKey())) {
                 //       handlerSpiaggiaAssociato.getOmbrellone(prenotato.getIdOmbrellone()).setIsBooked(true);
                        listaTemp.get(listaTemp.indexOf(prenotato)).setIsBooked(true);
                    }
                    if (listaTemp.stream().allMatch(Ombrellone::isBooked)) {
                        //la spiaggia è completamente occupata
                        return listaOmbrPrenotabili;
                    }
                }
            }
        }
        for (Ombrellone ombr : listaTemp) {
            if(!ombr.isBooked()){
                listaOmbrPrenotabili.add(ombr);
            }
        }
        return listaOmbrPrenotabili;
    }

    private void mostraDisposizioneOmbrelloniDisponibiliEPrezzo(ArrayList<Ombrellone> listaPrenotabili) {
        for (Ombrellone ombrellone : listaPrenotabili) {
            System.out.println("Ombrellone: "+ombrellone.getIdOmbrellone()+", Tipo: "+ombrellone.getNomeTipo()+", Coordinate: "+ombrellone.getLocation().toString()+", Prezzo: "+calcoloPrezzoOmbrellone(ombrellone));
        }
    }

    private Ombrellone getOmbrelloneById(int idOmbrellone, ArrayList<Ombrellone> listaTarget){
        for (Ombrellone ombrellone : listaTarget) {
            if (idOmbrellone==ombrellone.getIdOmbrellone()){
                return ombrellone;
            }
        }
        return null;
    }

    private double calcoloPrezzoOmbrellone(Ombrellone ombrellone) {
        double prezzoFasciaDiPrezzoOmbrellone = 0.0;
        double prezzoTipologiaOmbrellone = 0.0;

        HashMap<FasciaDiPrezzo, Double> prezzoFasceAttuale = handlerListinoAssociato.ottieniPrezziFasce();
        HashMap<TipologiaOmbrellone, Double> prezzoTipologieAttuale = handlerListinoAssociato.ottieniPrezziTipologie();

        String nomeFascia = getNomeFasciaOmbrellone(ombrellone);
        if(nomeFascia == null)
            prezzoFasciaDiPrezzoOmbrellone = 1;
        else {
            for (Map.Entry<FasciaDiPrezzo, Double> entry : prezzoFasceAttuale.entrySet()) {
                if(entry.getKey().getNome().equals(nomeFascia))
                    prezzoFasciaDiPrezzoOmbrellone = entry.getValue();
            }
        }
        for (Map.Entry<TipologiaOmbrellone, Double> entry : prezzoTipologieAttuale.entrySet()) {
            if (ombrellone.getNomeTipo().equals(entry.getKey().getNome())) {
                prezzoTipologiaOmbrellone = entry.getValue();
            }

        }
        return  handlerListinoAssociato.ottieniPrezzoBaseOmbrellone() * prezzoFasciaDiPrezzoOmbrellone * prezzoTipologiaOmbrellone;
    }

    private String getNomeFasciaOmbrellone(Ombrellone ombrellone){
        ArrayList<ArrayList<String>> griglia = this.handlerListinoAssociato.vistaSpiaggiaFasce(this.handlerSpiaggiaAssociato.ottieniVistaSpiaggia());
        return griglia.get(ombrellone.getLocation().getyAxis()).get(ombrellone.getLocation().getxAxis());
    }

    private void recapPrenotazione(Prenotazione prenotazioneInCorso) {
        System.out.println("Hai scelto questa prenotazione:");



        for (Map.Entry<Date, Integer> entry :prenotazioneInCorso.getMappaDateFasce().entrySet()){
            for (Map.Entry<Date, ArrayList<Ombrellone>> entry2 :prenotazioneInCorso.getMappaDateListaOmbrelloni().entrySet()){
                if (entry.getKey().equals(entry2.getKey())) {
                    System.out.println(entry.getKey() + " nella fascia oraria: " + entry.getValue() +".");
                    for (Ombrellone ombrellone : entry2.getValue()) {
                        System.out.println("Ombrellone " + ombrellone.getIdOmbrellone() + " e " + ombrellone.getNumeroLettiniAssociati() + " lettino/i.");
                    }
                }
            }
        }
        System.out.println("Costo: "+ prenotazioneInCorso.getPrezzoTotale());
    }

    private double calcoloSubtotale(Ombrellone ombrellone) {
        return this.calcoloPrezzoOmbrellone(ombrellone) + ombrellone.getNumeroLettiniAssociati()* handlerListinoAssociato.ottieniPrezzoLettino();
    }

    private Ombrellone selezionaOmbrellone(ArrayList<Ombrellone> listaPrenotabili) {
        int idOmbrelloneSelezionato;
        Ombrellone ombrelloneSelezionato;
        while(true) {
            System.out.print("Inserire l'id dell'ombrellone desiderato: (int)");
            idOmbrelloneSelezionato = provaScannerInt();
            ombrelloneSelezionato = getOmbrelloneById(idOmbrelloneSelezionato, listaPrenotabili);
            if(ombrelloneSelezionato == null) {
                System.out.println("L'ombrellone non è disponibile, sceglierne un altro");
            }
            else return ombrelloneSelezionato;
        }
    }

    private int selezionaNumeroLettini() {
        System.out.print("Inserire il numero di lettini desiderato: ");
        return provaScannerInt();
    }

    public void ordinaBar(int idCliente) {
        this.carrello = new HashMap<>();
        Menu menu = new Menu(this.handlerListinoAssociato);
        HashMap<ProdottoBar, Double> mappaProdottiPrezzi = menu.ottieniProdottiEPrezzi();
        if (!mappaProdottiPrezzi.isEmpty() && this.controlloOmbrelloneCorrentementeAssociato(idCliente)) {
            boolean flag;
            do {
                this.printProdottiEPrezzi(mappaProdottiPrezzi);
                System.out.println("Digitare 1 per aggiungere un prodotto al carrello o 2 per rimuovere un prodotto dal carrello");
                int op = this.provaScannerInt();
                sc.nextLine();
                if (op == 1) {
                    this.aggiungiProdottoOrdine(mappaProdottiPrezzi);
                }
                if (op == 2) {
                    this.rimuoviProdottoOrdine();
                }
                this.printStatoCarrello();
                System.out.println("Vuoi aggiungere o rimuovere altri prodotti dal carrello? [y/n] ");
                flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
            } while (flag);
            Double prezzoTotale = this.calcoloPrezzoTotaleCarrello();
            if(this.richiestaDisponibilitaCoupon())
                if(this.controlloCodiceCouponValido()) {
                    prezzoTotale = this.applicaScontoCoupon(prezzoTotale);
                }
            if (this.confermaOperazione()) {
                this.elaboraPagamento(this.richiestaDatiPagamento(), prezzoTotale);
                this.associatedDBMS.aggiungiOrdineBar(new OrdineBar(idCliente, this.carrello, getNewIdOrdineBar()));
            }
            else System.out.println("Operazioni annullate");
        } else System.out.println("Impossibile procedere con l'ordine, il cliente non ha una prenotazione associata o nessun prodotto bar disponibile");
    }

    private int getNewIdOrdineBar(){
        int highestId = -1;
        ArrayList<OrdineBar>listaOrdiniBar = this.associatedDBMS.ottieniListaOrdiniBar();
        if(!listaOrdiniBar.isEmpty())
            for(OrdineBar ordine : listaOrdiniBar)
                if(ordine.getIdOrdine() > highestId)
                    highestId = ordine.getIdOrdine();
        return highestId+1;
    }

    private Double applicaScontoCoupon(Double prezzoTotale){
        double prezzoPreSconto = prezzoTotale;
        prezzoTotale = prezzoTotale * 0.9;
        System.out.println("Prezzo totale pre coupon: " + prezzoPreSconto + "€");
        System.out.println("Prezzo totale scontato: " + prezzoTotale + "€");
        return prezzoTotale;
    }

    private boolean controlloOmbrelloneCorrentementeAssociato(int idCliente) {
        Date currentDate = new Date();
        for(Prenotazione currentPrenotazione : this.associatedDBMS.ottieniListaPrenotazioni()) {
            if (currentPrenotazione.getIdCliente() == idCliente && currentDate.getTime() >= currentPrenotazione.getDataInizio().getTime() && currentDate.getTime() <= currentPrenotazione.getDataFine().getTime() + 86400000 && this.controlloOrarioCorrenteFascia(currentPrenotazione.getMappaDateFasce())) {
                for(Date data : currentPrenotazione.getMappaDateListaOmbrelloni().keySet()) {
                    if(currentDate.getTime() >= data.getTime() && currentDate.getTime() <= data.getTime() + 86400000)
                        if (!currentPrenotazione.getMappaDateListaOmbrelloni().get(data).isEmpty() && currentPrenotazione.getMappaDateListaOmbrelloni().get(data) != null)
                            return true;
                }
            }
        }
        return false;
    }

    private boolean controlloOrarioCorrenteFascia(HashMap<Date,Integer> mappaDate) {
        Date currentDate = new Date();

        for(Date data : mappaDate.keySet()){
            int fascia = mappaDate.get(data);

            if(fascia == 1 && currentDate.getTime() >= data.getTime() && currentDate.getTime() < data.getTime()+43200000) return true;

            if(fascia == 2 && currentDate.getTime() >= data.getTime()+43200000 && currentDate.getTime() < data.getTime()+86400000) return true;

            if(fascia == 3 && currentDate.getTime() >= data.getTime() && currentDate.getTime() < data.getTime() + 86400000) return true;
        }
        return false;
    }

    private void elaboraPagamento(ArrayList<String> datiPagamento, double prezzo) {
        System.out.println("Pagamento elaborato con successo.");
        System.out.println("Riepilogo pagamento");
        System.out.println("-------------------");
        System.out.println("Intestatario carta: " + datiPagamento.get(0));
        System.out.println("Codice carta: " + datiPagamento.get(1));
        System.out.println("CVV carta: " + datiPagamento.get(2));
        System.out.println("Totale pagamento: " + prezzo + "€");
    }

    private ArrayList<String> richiestaDatiPagamento() {
        ArrayList<String> datiPagamento = new ArrayList<>();
        System.out.println("Inserire l'intestatario della carta");
        datiPagamento.add(this.sc.nextLine());
        System.out.println("Inserire il codice della carta");
        datiPagamento.add(this.sc.nextLine());
        System.out.println("Inserire il CVV della carta");
        datiPagamento.add(this.sc.nextLine());
        return datiPagamento;
    }

    private void printStatoCarrello() {
        Double prezzoTotale = this.calcoloPrezzoTotaleCarrello();
        this.printProdottiCarrello();
        System.out.println("Subtotale : " + prezzoTotale);
    }

    private Double calcoloPrezzoTotaleCarrello() {
        Double totale = 0.0;
        for(ProdottoBar currentProdotto : this.carrello.keySet())
            totale = totale + (this.carrello.get(currentProdotto) * this.handlerListinoAssociato.getPrezziBar().get(currentProdotto));
        return totale;
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

    private boolean controlloCodiceCouponValido() {
        boolean flag;
        this.codiciCouponValidi = this.associatedDBMS.ottieniListaCouponValidi();
        do {
            System.out.println("Inserire il codice del coupon");
            String codiceCoupon = this.sc.nextLine();
            if(this.codiciCouponValidi.contains(codiceCoupon)){
                return true;
            }
            else System.out.println("Il codice coupon inserito non è valido");
            flag = this.richiestaDisponibilitaCoupon();
        } while (flag);
        return false;
    }

    private boolean richiestaDisponibilitaCoupon() {
        System.out.println("Hai a disposizione coupon promozionali? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
    }

    private void elaboraRimborso(double prezzoDaRimborsare) {
        System.out.println("Verrà rimborsato l'importo versato per le prenotazioni pari a: "+prezzoDaRimborsare+"€");
    }

    private void rimuoviProdottoOrdine() {
        if(!this.carrello.isEmpty()) {
            this.printProdottiCarrello();
            ProdottoBar prodottoSelezionato = this.selezionaProdottoCarrello();
            System.out.println("Inserire la quantita' del prodotto selezionato da rimuovere dal carrello");
            int quantitaProdotto = this.provaScannerInt();
            sc.nextLine();
            this.rimuoviProdottoDalCarrello(prodottoSelezionato, quantitaProdotto);
        }
        else System.out.println("Nessun prodotto presente nel carrello");
    }

    private void rimuoviProdottoDalCarrello(ProdottoBar prodottoSelezionato, int quantitaProdotto) {
        if (!this.carrello.containsKey(prodottoSelezionato)) {
            System.out.println("Impossibile rimuovere il prodotto poiche' non e' presente nel carrello");
            return;
        }
        if (this.carrello.get(prodottoSelezionato) - quantitaProdotto <= 0) {
            this.carrello.remove(prodottoSelezionato);
        } else{
            this.carrello.put(prodottoSelezionato, this.carrello.get(prodottoSelezionato) - quantitaProdotto);
        }
    }

    private ProdottoBar selezionaProdottoCarrello() {
        do {
            System.out.println("Inserire il nome del prodotto");
            String nomeProdotto = this.sc.nextLine();
            for (ProdottoBar currentProdotto : this.carrello.keySet()) {
                if (currentProdotto.getNomeProdotto().equals(nomeProdotto))
                    return currentProdotto;
            }
            System.out.println("Il prodotto selezionato non è presente nel carrello");
        } while(true);
    }

    private void printProdottiCarrello() {
        System.out.println("Prodotti nel carrello");
        System.out.println("---------------------");
        for(ProdottoBar currentProdotto : this.carrello.keySet())
            if(this.carrello.get(currentProdotto) > 0)
                System.out.println(currentProdotto.getNomeProdotto());
    }

    private void aggiungiProdottoOrdine(HashMap<ProdottoBar,Double> mappaProdottiPrezzi) {
        this.printProdottiEPrezzi(mappaProdottiPrezzi);
        ProdottoBar prodottoSelezionato = this.selezionaProdotto(mappaProdottiPrezzi);
        this.printInfoProdotto(prodottoSelezionato,mappaProdottiPrezzi);
        System.out.println("Inserire la quantita' desiderata del prodotto selezionato");
        int quantitaProdotto = this.sc.nextInt();
        sc.nextLine();
        this.aggiungiProdottoAlCarrello(prodottoSelezionato,quantitaProdotto);
    }

    private void aggiungiProdottoAlCarrello(ProdottoBar prodottoSelezionato, int quantitaProdotto) {
        int quantitaPrecedente = 0;
        if(carrello.containsKey(prodottoSelezionato))
            quantitaPrecedente = this.carrello.get(prodottoSelezionato);
        this.carrello.put(prodottoSelezionato, quantitaProdotto + quantitaPrecedente);
        System.out.println("Prodotto aggiunto al carrello con successo");
    }

    private void printInfoProdotto(ProdottoBar prodottoSelezionato, HashMap<ProdottoBar,Double> mappaProdottiPrezzi){
        System.out.println("Informazioni prodotto selezionato: ");
        System.out.println(prodottoSelezionato.getNomeProdotto() + ": " + mappaProdottiPrezzi.get(prodottoSelezionato));
        System.out.println(prodottoSelezionato.getDescrizione());
    }

    private ProdottoBar selezionaProdotto(HashMap<ProdottoBar, Double> mappaProdottiPrezzi) {
        do {
            System.out.println("Inserire il nome del prodotto desiderato");
            String nomeProdotto = this.sc.nextLine();
            for (ProdottoBar currentProdotto : mappaProdottiPrezzi.keySet()) {
                if (currentProdotto.getNomeProdotto().equals(nomeProdotto))
                    return currentProdotto;
            }
            System.out.println("Il prodotto selezionato non è presente nel menu'");
        } while(true);
    }

    private void printProdottiEPrezzi(HashMap<ProdottoBar, Double> mappaProdottiPrezzi) {
        System.out.println("Listino prodotti bar");
        System.out.println("--------------------");
        for(ProdottoBar currentProdotto : mappaProdottiPrezzi.keySet())
            System.out.println(currentProdotto.getNomeProdotto() + ": " + mappaProdottiPrezzi.get(currentProdotto) + "€");
    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
    }

    public boolean isOmbrellonePrenotato(Ombrellone ombrelloneSelezionato) {
        this.listaPrenotazioni = this.associatedDBMS.ottieniListaPrenotazioni();
        for(Prenotazione prenotazione : this.listaPrenotazioni)
            for(Date data : prenotazione.getMappaDateListaOmbrelloni().keySet())
                for(Ombrellone ombrellone : prenotazione.getMappaDateListaOmbrelloni().get(data))
                    if(ombrellone.equals(ombrelloneSelezionato))
                        return true;
        return false;
    }
}