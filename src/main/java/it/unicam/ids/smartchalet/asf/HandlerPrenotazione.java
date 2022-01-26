package it.unicam.ids.smartchalet.asf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HandlerPrenotazione {

    private ArrayList<Prenotazione> listaPrenotazioni;
    private Prenotazione prenotazioneInCorso;
    private final DBMSController associatedDBMS;
    private Spiaggia spiaggiaAssociata;
    private Scanner sc = new Scanner(System.in);
    private HashMap<ProdottoBar, Integer> carrello;
    private ArrayList<String> codiciCouponValidi;
    private HandlerAttivita handlerAttivitaAssociato;
    private HandlerListino handlerListino;

    public HandlerPrenotazione(Spiaggia spiaggiaAssociata, DBMSController associatedDBMS, HandlerListino handlerListinoAssociato) {
        this.codiciCouponValidi = new ArrayList<>();
        this.spiaggiaAssociata = spiaggiaAssociata;
        listaPrenotazioni = new ArrayList<>();
        this.associatedDBMS = associatedDBMS;
        this.handlerListino = handlerListinoAssociato;

    }

    public void partecipaEvento(int idCliente){
        this.listaPrenotazioni = this.associatedDBMS.richiestaListaPrenotazioni();
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

    public void cancellaPrenotazione(int idCliente) {
        ArrayList<Prenotazione> listaPrenotazioni;
        ArrayList<Integer> listaPrenotazioniDaRimuovere = new ArrayList<>();
        listaPrenotazioni = associatedDBMS.richiestaListaPrenotazioni();
        double prezzoDaRimborsare = 0;

        boolean working = true;
        while (working) {
            System.out.println("Inserire l'id della prenotazione da cancellare:");
            outputListaPrenotazioniCliente(listaPrenotazioni, idCliente);
            int idPrenotazione = sc.nextInt();
            sc.nextLine();
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
            System.out.println("Operazioni eseguite");
            elaboraRimborso(prezzoDaRimborsare);
            this.associatedDBMS.rimuoviPrenotazioni(listaPrenotazioniDaRimuovere);

        } else System.out.println("Operazioni annullate");

    }

    private Prenotazione ottieniPrenotazione(int idPrenotazione) {
        for(Prenotazione currentPrenotazione : this.listaPrenotazioni) {
            if (currentPrenotazione.getId() == idPrenotazione)
                return currentPrenotazione;
        }
        return null;
    }

    public void prenotaOmbrellone() {
        prenotazioneInCorso = new Prenotazione();
        spiaggiaAssociata.aggiungiGrigliaSpiaggia(associatedDBMS.ottieniVistaSpiaggia());
        listaPrenotazioni = associatedDBMS.richiestaListaPrenotazioni();
        inserisciOrarioDesiderato();
        associaFasciaOrario(prenotazioneInCorso.getDataInizio(), prenotazioneInCorso.getDataFine());

        //ottieniPrezziFasce()
        //ottieniPrezzoBase()
        //getTipologie()

        Map<Date, Integer> m1 = new TreeMap(prenotazioneInCorso.getMappaDateFasce());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        for (Map.Entry<Date, Integer> entry : m1.entrySet()) {
            System.out.println(df.format(entry.getKey()));
            if (!controlloOccupazione(entry.getKey(), entry.getValue())) {
                System.out.println("Non ci sono ombrelloni disponibili il " + entry.getKey() + " nell'orario specificato, annullare la prenotazione?");

                if (Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y")) {
                    //TODO: trovare un modo per uscire e non applicare niente.
                }
            } else {
                //mostraDisposizioneOmbrelloniDisponibiliEPrezzo();
                selezionaOmbrellone();
                selezionaNumeroLettini();

                listaPrenotazioni.add(prenotazioneInCorso);


                prenotazioneInCorso.setPrezzoTotale(prenotazioneInCorso.getPrezzoTotale()+calcoloSubtotale());
                System.out.println("Il prezzo per questa prenotazione è ora di "+prenotazioneInCorso.getPrezzoTotale()+"€.");

            }
        }

        String answer;
        boolean yn;

        System.out.println("Hai scelto questa prenotazione:");
        System.out.println("Ombrellone " + prenotazioneInCorso.getId() + " e " + prenotazioneInCorso.getNumeroLettini() + " lettino/i.");
        System.out.println("da " + prenotazioneInCorso.getDataInizio() + " a " + prenotazioneInCorso.getDataFine() + ".");
        System.out.println("Confermi la prenotazione? (y/n)");
        while (true) {
            answer = sc.nextLine().trim().toLowerCase();
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

    private double calcoloSubtotale() {
        double subTotale = 0.0;
        return subTotale;
    }


    private void inserisciOrarioDesiderato() {
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
            prenotazioneInCorso.setDataFine(dataEnd);

            if (controlloDataCorretta(dataStart, dataEnd)) {
                working = false;
            }
        }
    }

    private void associaFasciaOrario(Date dataInizio, Date dataFine) {
        HashMap<Date, Integer> mappaDateFasceTemp = new HashMap<>();
        int fasciaOraria;
        System.out.print("Inserire la fascia oraria desiderata per la giornata del "+dataInizio+": \n 1 = solo mattina, 2 = solo pomeriggio, 3 = mattina e pomeriggio: ");
        fasciaOraria = provaScannerInt();
        mappaDateFasceTemp.put(dataInizio, fasciaOraria);
        while (dataInizio.before(dataFine)) {
            dataInizio = addDays(dataInizio, 1);
            System.out.print("Inserire la fascia oraria desiderata per la giornata del "+dataInizio+": \n 1 = solo mattina, 2 = solo pomeriggio, 3 = mattina e pomeriggio: ");
            fasciaOraria = provaScannerInt();
            mappaDateFasceTemp.put(dataInizio, fasciaOraria);
        } if (this.confermaOperazione()) {
            System.out.println("Operazioni eseguite");
            this.prenotazioneInCorso.setMappaDateFasce(mappaDateFasceTemp);
            //System.out.println(prenotazioneInCorso.getMappaDateFasce().toString());

        } else System.out.println("Operazioni annullate");
    }

    private static Date addDays(Date d1, int i) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d1);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
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

    //TODO: controllo se la giornata è sempre piena
    private boolean controlloOccupazione(Date dataPrenotazione, int fasciaOrariaPrenotazione) {
        return true;
    }

    private void selezionaOmbrellone() {
        System.out.print("Inserire l'ombrellone desiderato: ");
        int idOmbrellone = provaScannerInt();
        prenotazioneInCorso.getOmbrelloni().add(spiaggiaAssociata.getOmbrellone(idOmbrellone));
        spiaggiaAssociata.getOmbrellone(idOmbrellone).setIsBooked(true);
    }
    private void selezionaNumeroLettini() {
        System.out.print("Inserire il numero di lettini desiderato: ");
        int numLettini = provaScannerInt();
        prenotazioneInCorso.setNumeroLettini(numLettini);

    }


    public void ordinaBar(int idCliente) {
        this.carrello = new HashMap<>();
        Menu menu = new Menu();
        HashMap<ProdottoBar, Double> mappaProdottiPrezzi = menu.ottieniProdottiEPrezzi();
        ArrayList<Ombrellone> ombrelloniCliente = this.controlloOmbrelloneCorrentementeAssociato(idCliente);
        if (!mappaProdottiPrezzi.isEmpty() && ombrelloniCliente != null) {
            boolean flag;
            do {
                this.printProdottiEPrezzi(mappaProdottiPrezzi);
                System.out.println("Digitare 1 per aggiungere un prodotto al carrello o 2 per rimuovere un prodotto dal carrello");
                int op = sc.nextInt();
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
                    double prezzoPreSconto = prezzoTotale;
                    prezzoTotale -= (prezzoTotale * 10) / 100;
                    System.out.println("Prezzo totale pre coupon: " + prezzoPreSconto + "€");
                    System.out.println("Prezzo totale scontato: " + prezzoTotale + "€");
                }
            if (this.confermaOperazione()) {
                this.elaboraPagamento(this.richiestaDatiPagamento(), prezzoTotale);
                this.associatedDBMS.aggiungiOrdineBar(new OrdineBar(idCliente, this.carrello, ombrelloniCliente));
            }
            else System.out.println("Operazioni annullate");
        } else System.out.println("Impossibile procedere con l'ordine, il cliente non ha una prenotazione associata o nessun prodotto bar disponibile");
    }

    private ArrayList<Ombrellone> controlloOmbrelloneCorrentementeAssociato(int idCliente) {
        Date currentDate = new Date();
        for(Prenotazione currentPrenotazione : this.listaPrenotazioni) {
            if (currentPrenotazione.getIdCliente() == idCliente && currentDate.after(currentPrenotazione.getDataInizio()) && currentDate.before(currentPrenotazione.getDataFine()) && this.controlloOrarioCorrenteFascia(currentPrenotazione.getMappaDateFasce()))
                return currentPrenotazione.getOmbrelloni();
        }
        return null;
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
            totale = totale + this.carrello.get(currentProdotto);
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

        do {
            System.out.println("Inserire il codice del coupon");
            String codiceCoupon = this.sc.nextLine();
            if(this.codiciCouponValidi.contains(codiceCoupon)){   //TODO prendo lista coupon dal db
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
        this.printProdottiCarrello();
        ProdottoBar prodottoSelezionato = this.selezionaProdottoCarrello();
        System.out.println("Inserire la quantita' del prodotto selezionato da rimuovere dal carrello");
        int quantitaProdotto = this.sc.nextInt();
        sc.nextLine();
        this.rimuoviProdottoDalCarrello(prodottoSelezionato, quantitaProdotto);
    }

    private void rimuoviProdottoDalCarrello(ProdottoBar prodottoSelezionato, int quantitaProdotto) {
        if (!this.carrello.containsKey(prodottoSelezionato)) {
            System.out.println("Impossibile rimuovere il prodotto poiche' non e' presente nel carrello");
            return;
        }
        if (this.carrello.get(prodottoSelezionato) - quantitaProdotto <= 0) {
            this.carrello.remove(prodottoSelezionato);
        } else this.carrello.put(prodottoSelezionato, this.carrello.get(prodottoSelezionato) - quantitaProdotto);
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
        this.carrello.put(prodottoSelezionato, quantitaProdotto + this.carrello.get(prodottoSelezionato));
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

    private void inviaDatiPagamento() {
        //TODO: permettere l'inserimento dei dati per il pagamento
    }

}