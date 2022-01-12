package it.unicam.cs.ids2021;

import java.util.*;

public class HandlerSpiaggia {

    private final Spiaggia spiaggiaGestita;

    private final DBMSController associatedDBMS;

    private final Listino listinoAssociato;

    private final Scanner sc;

    public HandlerSpiaggia(Spiaggia spiaggiaGestita, DBMSController associatedDBMS, Listino listinoAssociato){
        sc = new Scanner(System.in);
        this.listinoAssociato = listinoAssociato;
        this.associatedDBMS = associatedDBMS;
        this.spiaggiaGestita = spiaggiaGestita;
    }

    public Spiaggia getSpiaggiaGestita() {
        return spiaggiaGestita;
    }

    /**
     * Questo metodo serve ad aggiungere un ombrellone alla Spiaggia
     */
    public void aggiungiOmbrellone() {
        //this.associatedDBMS.ottieniVistaSpiaggia(); // Sistemare per aggiornare la spiaggia gestita
        boolean flag;
        do {
            ArrayList<Coordinate> coordinate = this.spiaggiaGestita.ottieniPostiSenzaOmbrelloni();
            if(coordinate.size()==0) {
                System.out.println("Non ci sono più posti in cui aggiungere un ombrellone");
                break;
            }
            else this.outputListaCoordinate(coordinate);

            if(!this.aggiuntaAllaSpiaggia()) break;

            System.out.println(this.getSpiaggiaGestita().toString());

            System.out.println("Vuoi aggiungere altri ombrelloni? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            this.sc.nextLine();
        }while(flag);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");
    }

    public void modificaOmbrellone(){
        Ombrellone ombrelloneSelezionato;
        //associatedDBMS.ottieniVistaSpiaggia();
        boolean flag = true;
        while(flag){
            ottieniVistaSpiaggia();
            System.out.println("Inserire l'id dell'ombrellone da modificare");
            int idOmbrellone = sc.nextInt();
            if(!this.spiaggiaGestita.controlloEsistenzaOmbrellone(idOmbrellone)){
                System.out.println("L'ombrellone specificato non esiste");
                continue;
            }
            ombrelloneSelezionato = selezionaOmbrellone(idOmbrellone);
            System.out.println("Digitare 1 per rimuovere l'ombrellone, 2 per spostare l'ombrellone o 3 per modificare la tipologia dell'ombrellone");
            int op = sc.nextInt();
            sc.nextLine();
            if(op == 1){
                rimuoviOmbrellone(ombrelloneSelezionato);
            }
            if(op == 2){
                spostaOmbrellone(this.selezionaPosto(), ombrelloneSelezionato);
            }
            if(op == 3){
                modificaTipologiaOmbrellone(ombrelloneSelezionato);
            }
            System.out.println("Modificare altri ombrelloni? y/n" );
            String response = sc.next();
            flag = response.equals("y");
        }
        ottieniVistaSpiaggia();
    }

    public void aggiungiGrigliaSpiaggia(){
        System.out.println("Inserire il numero di file della spiaggia");
        int numeroFile = sc.nextInt();
        sc.nextLine();
        ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia = new ArrayList<>();
        int numeroElementiFila;
        for(int i=0; i<numeroFile; i++) {
            System.out.println("Inserire il numero di elementi della fila numero " + (i+1));
            numeroElementiFila = sc.nextInt();
            sc.nextLine();
            ArrayList<Ombrellone> listaElementiFilaCorrente = new ArrayList<>();
            for(int j=0; j<numeroElementiFila; j++){
                listaElementiFilaCorrente.add(null);
            }
            grigliaSpiaggia.add(listaElementiFilaCorrente);
        }
        if(this.confermaOperazione()) {
            this.spiaggiaGestita.aggiungiGrigliaSpiaggia(grigliaSpiaggia);
            this.associatedDBMS.aggiugniGrigliaSpiaggia(grigliaSpiaggia);
        }
    }

    public void modificaGrigliaSpiaggia(){

        this.associatedDBMS.ottieniVistaSpiaggia();
        //this.spiaggiaGestita.aggiornaSpiaggia();
        System.out.println(this.spiaggiaGestita.toString());
        ArrayList<ArrayList<Ombrellone>> listaOmbrelloni = this.spiaggiaGestita.getListaOmbrelloni();

        boolean flag;
        do {
            this.sceltaModificaGriglia(listaOmbrelloni);
            listaOmbrelloni = this.spiaggiaGestita.getListaOmbrelloni();

            System.out.println("Vuoi modificare ancora la griglia spiaggia? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            this.sc.nextLine();
        }while(flag);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");
    }

    private void sceltaModificaGriglia(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        System.out.println("Seleziona la riga che vuoi modificare (int)");
        int sceltaRiga = this.provaScannerInt();
        int sceltaOperazione = this.sceltaOperazioneModificaGriglia();
        if(sceltaOperazione == 1){
            this.allungamentoRiga(listaOmbrelloni.get(sceltaRiga));
        }
        if(sceltaOperazione == 2){
            this.accorciamentoRiga(listaOmbrelloni.get(sceltaRiga));
        }
        if(sceltaOperazione == 3){
            this.eliminazioneRiga(listaOmbrelloni,sceltaRiga);
        }

        System.out.println(this.spiaggiaGestita.toString());
    }


    private void accorciamentoRiga(ArrayList<Ombrellone> riga) {
        System.out.println("Scegli di quanto accorciare la riga (int)");
        int lunghezzaAccorciamento = this.provaScannerInt();
        String direzione = this.sceltaDirezioneOperazione("accorciare");

        if(lunghezzaAccorciamento >= riga.size()){
            System.out.println("Stai cercando di accorciare troppo la riga, l'operazione verrà annullata");
        }

        if(this.isParteRigaEmpty(riga,lunghezzaAccorciamento,direzione)){
            if(Objects.equals(direzione, "d")) for(int i=0;i<lunghezzaAccorciamento;i++) riga.remove(riga.get(riga.size()-1));
            else for(int i=0;i<lunghezzaAccorciamento;i++) riga.remove(0);
        }

    }

    private boolean isParteRigaEmpty(ArrayList<Ombrellone> riga, int lunghezzaAccorciamento, String direzione){
        ArrayList<Ombrellone> parteRiga = new ArrayList<>();

        if(Objects.equals(direzione, "d"))
            for(int i=0;i<lunghezzaAccorciamento;i++) parteRiga.add(riga.get(riga.size()-1-i));
        else
            for(int i=0;i<lunghezzaAccorciamento;i++) parteRiga.add(riga.get(i));

        for(Ombrellone ombrellone : parteRiga){
            if(ombrellone != null) return false;
        }
        return true;
    }

    private void eliminazioneRiga(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni, int sceltaRiga) {
        if(this.confermaOperazione()) {
            if (this.isRigaEmpty(listaOmbrelloni.get(sceltaRiga))) {
                listaOmbrelloni.remove(sceltaRiga);
            }
        }
    }

    private boolean isRigaEmpty(ArrayList<Ombrellone> riga){
        for(Ombrellone ombrellone : riga){
            if(ombrellone != null) return false;
        }
        return true;
    }

    private void allungamentoRiga(ArrayList<Ombrellone> riga) {
        System.out.println("Scegli di quanto allungare la riga (int)");
        int lunghezzaAllungamento = this.provaScannerInt();
        String direzione = this.sceltaDirezioneOperazione("allungare");

        if(Objects.equals(direzione, "d")) for(int i=0;i<lunghezzaAllungamento;i++) riga.add(null);
        else for(int i=0;i<lunghezzaAllungamento;i++) riga.add(0,null);
    }

    private String sceltaDirezioneOperazione(String operazione){
        String direzione;
        while(true){
            System.out.println("Sceglia la direzione in cui "+ operazione +" la riga [d/s]");
            direzione = this.sc.next().trim().toLowerCase(Locale.ROOT);
            if(Objects.equals(direzione, "d") || Objects.equals(direzione, "s")) return direzione;
            else System.out.println("Cio' che hai inserito non e' accettabile, ritenta");
        }

    }

    private int sceltaOperazioneModificaGriglia(){
        do{
            System.out.println("Scegli l'operazione da eseguire sulla riga (int)");
            System.out.println("1\tAllungare la riga\n2\tAccorciare la riga\n3\tEliminare la riga ");
            int sceltaOperazione = this.provaScannerInt();
            if(sceltaOperazione==1 || sceltaOperazione==2 || sceltaOperazione==3) return sceltaOperazione;
            else{
                System.out.println("Il numero inserito non rappresenta un'operazione, ritenta");
            }
        }while(true);
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

    private Coordinate selezionaPosto(){
        System.out.println("Inserire la fila");
        int fila = this.sc.nextInt();
        sc.nextLine();
        System.out.println("Inserire la colonna");
        int colonna = this.sc.nextInt();
        sc.nextLine();
        return new Coordinate(fila, colonna);
    }

    private void ottieniVistaSpiaggia(){
        ArrayList<ArrayList<Ombrellone>> vistaSpiaggiaCorrente = spiaggiaGestita.getListaOmbrelloni();
        int posizioneOmbrelloneCounter = 0;
        for(ArrayList<Ombrellone> currentRow : vistaSpiaggiaCorrente) {
            for (Ombrellone currentOmbrellone : currentRow) {
                System.out.println("Posizione numero: " + posizioneOmbrelloneCounter);
                if (currentOmbrellone != null) {
                    System.out.println("\t" + " (Coordinate " + currentOmbrellone.getLocation().getxAxis() + " " + currentOmbrellone.getLocation().getyAxis() + " )");
                    System.out.println("\t" + " Id ombrellone: " + currentOmbrellone.getIdOmbrellone());
                    System.out.println("\t" + " Tipo: " + currentOmbrellone.getNomeTipo() + " ");
                    System.out.println("\t" + " Numero lettini associati: " + currentOmbrellone.getNumeroLettiniAssociati() + " ");
                    System.out.println("\t" + " L'ombrellone è prenotato: " + currentOmbrellone.isBooked() + " ");
                }
                else System.out.println("\t" + "Posizione vuota, nessun ombrellone piazzato");
                posizioneOmbrelloneCounter++;
            }
        }
    }

    private Ombrellone selezionaOmbrellone(int idOmbrellone){
        for(ArrayList<Ombrellone> currentRow : spiaggiaGestita.getListaOmbrelloni()) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if(currentOmbrellone == null)
                    continue;
                if(currentOmbrellone.getIdOmbrellone() == idOmbrellone) {
                    return currentOmbrellone;
                }
            }
        }
        return null;
    }

    private void rimuoviOmbrellone(Ombrellone ombrelloneSelezionato){
        if(confermaOperazione()) spiaggiaGestita.rimuoviOmbrellone(ombrelloneSelezionato);
    }

    private boolean aggiuntaAllaSpiaggia(){
        Coordinate coordinateScelte = this.selezionaPosto();
        if(this.spiaggiaGestita.isLocationOccupied(coordinateScelte)) {
            System.out.println("Impossibile aggiungere un ombrellone ad una locazione occupata");
            return false;
        }
        String tipo = this.sceltaTipoOmbrellone();
        if(tipo == null) return false;
        this.spiaggiaGestita.aggiungiOmbrellone(new Ombrellone(tipo,coordinateScelte,this.spiaggiaGestita.getNewIdOmbrellone()));
        return true;
    }

    private void outputListaCoordinate(ArrayList<Coordinate> coordinate){
        System.out.println("Coordinate posti disponibili per aggiungere ombrelloni: ");
        int appoggio=0;
        for (Coordinate coord: coordinate) {
            System.out.print("Posto "+appoggio+" : \t");
            if(coord == null) System.out.println("Occupato");
            else System.out.println(coord.getxAxis()+"\t"+ coord.getyAxis());
            appoggio++;
        }
    }

    private void spostaOmbrellone(Coordinate coordinate, Ombrellone ombrelloneSelezionato){
        if(confermaOperazione()) {
            if (spiaggiaGestita.isLocationOccupied(coordinate))
                spiaggiaGestita.scambiaOmbrelloni(spiaggiaGestita.getOmbrelloneAtLocation(coordinate), ombrelloneSelezionato);
            else spiaggiaGestita.spostaOmbrellone(ombrelloneSelezionato, coordinate);
            System.out.println("Ombrellone spostato");
        }
    }

    /**
     * Questo metodo serve ad aggiungere una tipologia di ombrellone
     */
    public void aggiungiTipologiaOmbrellone() {
        HashMap<TipologiaOmbrellone, Double> listaTipi = this.listinoAssociato.getPrezziTipologia();
        this.listinoAssociato.outputListaTipologie();
        boolean flag;
        do{
            this.inserisciInformazioniTipologia(listaTipi);

            listaTipi = this.listinoAssociato.getPrezziTipologia();
            this.listinoAssociato.outputListaTipologie();

            System.out.println("Vuoi aggiungere altre tipologie? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            this.sc.nextLine();
        }while(flag);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO aggiungere aggiornamento database
        else System.out.println("Operazioni annullate");
    }

    private void inserisciInformazioniTipologia(HashMap<TipologiaOmbrellone, Double> listaTipi) {

        System.out.println("Inserisci il nome della nuova tipologia: ");
        String nome = this.sc.nextLine();
        System.out.println("Inserisci descrizione della nuova tipologia: ");
        String info = this.sc.nextLine();
        System.out.println("Inserisci il moltiplicatore della nuova tipologia: ");
        double moltiplicatore = this.sc.nextDouble();
        this.sc.nextLine();

        if(!this.controlloPresenzaTipologiaInserita(listaTipi,nome)){
            listaTipi.put(new TipologiaOmbrellone(nome,info),moltiplicatore);
            System.out.println("La nuova tipologia è stata aggiunta");
        }
        else{
            System.out.println("La tipologia inserita è già presente!");
        }

    }

    private boolean controlloPresenzaTipologiaInserita(HashMap<TipologiaOmbrellone, Double> listaTipi , String nome){
        if(!listaTipi.isEmpty()){
            for (TipologiaOmbrellone tipologia: listaTipi.keySet()) {
                if(tipologia.getNome().equals(nome)) return true;
            }
        }
        return false;
    }

    private String sceltaTipoOmbrellone() {
        HashMap<TipologiaOmbrellone, Double> listaTipi = this.listinoAssociato.getPrezziTipologia();
        this.listinoAssociato.outputListaTipologie();
        if(listaTipi.isEmpty()) {
            System.out.println("Non sono state ancora aggiunte tipologie, non è possibile aggiungere un ombrellone, annullamento operazioni");
            return null;
        }
        System.out.println("Inserisci il nome della tipologia da associare all'ombrellone");
        String nomeTipologia = this.sc.nextLine();

        while(!this.controlloPresenzaTipologiaInserita(listaTipi,nomeTipologia)){
            System.out.println("Il tipo cercato non è presente nella lista, riprova");
            nomeTipologia = this.sc.nextLine();
        }
        return nomeTipologia;
    }

    private void modificaTipologiaOmbrellone(Ombrellone ombrelloneSelezionato){

        Set<TipologiaOmbrellone> listaTipologieDisponibili = new HashSet<>();

        if(this.listinoAssociato.getPrezziTipologia().isEmpty()) {
            System.out.println("Non ci sono tipologie disponibili");
            return;
        }

        System.out.println("La tipologia corrente dell'ombrellone selezionato è: " + ombrelloneSelezionato.getNomeTipo());

        //TODO inserisco nel codice query db in controllo tipologia per verificare se esiste almeno una prenotazione dell'ombrellone selezionato
        if(ombrelloneSelezionato.isBooked()) {
            listaTipologieDisponibili = controlloTipologia(ombrelloneSelezionato.getNomeTipo());
            if(listaTipologieDisponibili.isEmpty()){
                System.out.println("Non ci sono tipologie disponibili");
                return;
            }
        }
        else listaTipologieDisponibili = this.listinoAssociato.getPrezziTipologia().keySet();

        System.out.println("Digitare uno tra i tipi disponibili per associarlo all'ombrellone selezionato: ");
        for (TipologiaOmbrellone tipologia: listaTipologieDisponibili) {
            System.out.println(tipologia);
        }

        String idTipologia;

        boolean flag = true;
        do{

            idTipologia = sc.nextLine();

            for(TipologiaOmbrellone tipologia : listaTipologieDisponibili){
                if(tipologia.getNome().equals(idTipologia))
                    flag = false;
            }
            if(flag)
                System.out.println("La tipologia inserita non è disponibile o non esiste, ritenta");

        }while(flag);

        aggiornaTipologiaOmbrellone(idTipologia, ombrelloneSelezionato);
    }

    private Set<TipologiaOmbrellone> controlloTipologia(String tipologia){
        Set<TipologiaOmbrellone> tipologieDisponibili = new HashSet<>();
        TipologiaOmbrellone tempTipologie = null;
        for(TipologiaOmbrellone tipo : this.listinoAssociato.getPrezziTipologia().keySet()){
            if(tipo.getNome().equals(tipologia)){
                tempTipologie = tipo;
            }
        }
        for(TipologiaOmbrellone tipo : this.listinoAssociato.getPrezziTipologia().keySet()) {
            if(this.listinoAssociato.getPrezziTipologia().get(tipo) > this.listinoAssociato.getPrezziTipologia().get(tempTipologie)){
                tipologieDisponibili.add(tipo);
            }
        }
        return tipologieDisponibili;
    }

    private void aggiornaTipologiaOmbrellone(String tipologia, Ombrellone ombrelloneSelezionato){
        if(confermaOperazione())
            spiaggiaGestita.aggiornaTipologiaOmbrellone(ombrelloneSelezionato, tipologia);
    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

}