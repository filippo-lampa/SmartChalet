package it.unicam.ids.smartchalet.asf;

import java.util.*;

public class HandlerSpiaggia {

    private final Spiaggia spiaggiaGestita;

    private final DBMSController associatedDBMS;

    private HandlerListino handlerListinoAssociato;

    private HandlerPrenotazione handlerPrenotazioneAssociato;

    private Scanner sc;

    private static HandlerSpiaggia instance = null;

    private HandlerSpiaggia(){
        this.handlerListinoAssociato = HandlerListino.getInstance();
        this.associatedDBMS = DBMSController.getInstance();
        this.spiaggiaGestita = new Spiaggia();
        sc = new Scanner(System.in);

    }

    public static HandlerSpiaggia getInstance() {
        if (instance == null) {
            instance = new HandlerSpiaggia();
        }
        return instance;
    }

    public void setHandlerPrenotazioneAssociato(){
        handlerPrenotazioneAssociato = HandlerPrenotazione.getInstance();
    }

    public Spiaggia getSpiaggiaGestita() {
        return spiaggiaGestita;
    }

    /**
     * Questo metodo serve ad aggiungere un ombrellone alla Spiaggia
     */
    public void aggiungiOmbrellone() {
        this.spiaggiaGestita.aggiornaSpiaggia(this.associatedDBMS.ottieniVistaSpiaggia());
        HashMap<TipologiaOmbrellone, Double> tipologie = this.handlerListinoAssociato.ottieniPrezziTipologie();
        ArrayList<TipologiaOmbrellone> listaTipologieUtilizzabili = this.controlloTipologiaUtilizzabili(tipologie);
        this.sceltaAggiuntaOmbrelloni(listaTipologieUtilizzabili);

        if(this.confermaOperazione()){
            if(this.associatedDBMS.aggiornaMappaSpiaggia(this.spiaggiaGestita.getListaOmbrelloni())){
                System.out.println("Operazione eseguita con successo");
            }
            else System.out.println("Operazioni fallita");
        }
        else System.out.println("Operazioni annullate");
    }

    private void sceltaAggiuntaOmbrelloni(ArrayList<TipologiaOmbrellone> listaTipologieUtilizzabili) {
        boolean flag;
        do {
            ArrayList<Coordinate> coordinate = this.spiaggiaGestita.ottieniPostiSenzaOmbrelloni();
            if(coordinate.isEmpty()) {
                System.out.println("Non ci sono pi?? posti in cui aggiungere un ombrellone");
                break;
            }
            else{
                System.out.println(this.spiaggiaGestita.toString());
                this.outputListaCoordinate(coordinate);
            }

            if(!this.aggiuntaAllaSpiaggia(listaTipologieUtilizzabili)) break;

            System.out.println(this.getSpiaggiaGestita().toString());

            System.out.println("Vuoi aggiungere altri ombrelloni? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }while(flag);

    }

    public ArrayList<TipologiaOmbrellone> controlloTipologiaUtilizzabili(HashMap<TipologiaOmbrellone, Double> tipologie) {
        ArrayList<TipologiaOmbrellone> listaTipologieUtilizzabili = new ArrayList<>();
        for( TipologiaOmbrellone tipologia: tipologie.keySet()){
            if(tipologie.get(tipologia) != null) listaTipologieUtilizzabili.add(tipologia);
        }
        return listaTipologieUtilizzabili;
    }

    public void modificaOmbrellone(){
        Ombrellone ombrelloneSelezionato;
        this.spiaggiaGestita.aggiornaSpiaggia(associatedDBMS.ottieniVistaSpiaggia());
        HashMap<TipologiaOmbrellone,Double> tipologie = this.handlerListinoAssociato.ottieniPrezziTipologie();
        System.out.println(this.spiaggiaGestita.toString());
        this.printVistaSpiaggia();
        boolean flag = true;
        while(flag){
            System.out.println("Inserire l'id dell'ombrellone da modificare");
            int idOmbrellone = this.provaScannerInt();
            if(this.spiaggiaGestita.controlloEsistenzaOmbrellone(idOmbrellone)) {
                ombrelloneSelezionato = selezionaOmbrellone(idOmbrellone);
                System.out.println("Digitare 1 per rimuovere l'ombrellone, 2 per spostare l'ombrellone o 3 per modificare la tipologia dell'ombrellone");
                int op = this.provaScannerInt();
                if (op == 1) {
                    rimuoviOmbrellone(ombrelloneSelezionato);
                }
                if (op == 2) {
                    spostaOmbrellone(this.selezionaPosto(), ombrelloneSelezionato);
                }
                if (op == 3) {
                    modificaTipologiaOmbrellone(ombrelloneSelezionato);
                }
            }
            else System.out.println("L'ombrellone specificato non esiste");
            printVistaSpiaggia();
            System.out.println("Modificare altri ombrelloni? y/n" );
            String response = sc.nextLine();
            flag = response.equals("y");
        }
    }

    public void aggiungiGrigliaSpiaggia(){
        System.out.println("Inserire il numero di file della spiaggia");
        int numeroFile = this.provaScannerInt();
        ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia = new ArrayList<>();
        int numeroElementiFila;
        for(int i=0; i<numeroFile; i++) {
            System.out.println("Inserire il numero di elementi della fila numero " + (i+1));
            numeroElementiFila = this.provaScannerInt();
            ArrayList<Ombrellone> listaElementiFilaCorrente = new ArrayList<>();
            for(int j=0; j<numeroElementiFila; j++){
                listaElementiFilaCorrente.add(null);
            }
            grigliaSpiaggia.add(listaElementiFilaCorrente);
        }
        if(this.confermaOperazione()) {
            this.spiaggiaGestita.aggiungiGrigliaSpiaggia(grigliaSpiaggia);
            this.associatedDBMS.aggiungiGrigliaSpiaggia(grigliaSpiaggia);
            System.out.println("Operazione di creazione griglia spiaggia effettuata");
        }
        else System.out.println("Operazione annullata");
    }

    public void modificaGrigliaSpiaggia(){

        this.spiaggiaGestita.aggiornaSpiaggia(this.associatedDBMS.ottieniVistaSpiaggia());
        ArrayList<ArrayList<Ombrellone>> listaOmbrelloni = this.spiaggiaGestita.getListaOmbrelloni();
        System.out.println(this.spiaggiaGestita.toString());

        boolean flag;
        do {
            this.sceltaModificaGriglia(listaOmbrelloni);
            listaOmbrelloni = this.spiaggiaGestita.getListaOmbrelloni();

            System.out.println("Vuoi modificare ancora la griglia spiaggia? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }while(flag);

        if(this.confermaOperazione()){
            if(this.associatedDBMS.aggiornaMappaSpiaggia(this.spiaggiaGestita.getListaOmbrelloni())){
                System.out.println("Operazione eseguita con successo");
            }
            else System.out.println("Operazioni fallita");
        }
        else System.out.println("Operazioni annullate");
    }

    private Coordinate selezionaPosto(){
        System.out.println("Inserire la fila");
        int fila = this.provaScannerInt();
        System.out.println("Inserire la colonna");
        int colonna = this.provaScannerInt();
        return new Coordinate(fila, colonna);
    }

    private void printVistaSpiaggia(){
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
        if(confermaOperazione()) {
            if(spiaggiaGestita.rimuoviOmbrellone(ombrelloneSelezionato))
                this.associatedDBMS.rimuoviOmbrellone(ombrelloneSelezionato);
        }
    }

    private boolean aggiuntaAllaSpiaggia(ArrayList<TipologiaOmbrellone> listaTipologieUtilizzabili){
        Coordinate coordinateScelte = this.selezionaPosto();
        if(this.spiaggiaGestita.isLocationOccupied(coordinateScelte)) {
            System.out.println("Impossibile aggiungere un ombrellone ad una locazione occupata");
            return false;
        }
        String tipo = this.sceltaTipoOmbrellone(listaTipologieUtilizzabili);
        if(tipo == null) return false;
        this.spiaggiaGestita.aggiungiOmbrellone(new Ombrellone(tipo,coordinateScelte,this.spiaggiaGestita.getNewIdOmbrellone()));
        return true;
    }

    private void outputListaCoordinate(ArrayList<Coordinate> coordinate){
        System.out.println("Coordinate posti disponibili per aggiungere ombrelloni: ");
        int appoggio=0;
        System.out.println("Posto   | Colonna | Fila");
        for (Coordinate coord: coordinate) {
            System.out.print("Posto "+appoggio+" : \t");
            if(coord == null) System.out.println("Occupato");
            else System.out.println(" "+coord.getxAxis()+"\t\t "+ coord.getyAxis());
            appoggio++;
        }
    }

    private void spostaOmbrellone(Coordinate coordinate, Ombrellone ombrelloneSelezionato){
        if(confermaOperazione()) {
            if (spiaggiaGestita.isLocationOccupied(coordinate)) {
                spiaggiaGestita.scambiaOmbrelloni(spiaggiaGestita.getOmbrelloneAtLocation(coordinate), ombrelloneSelezionato);
//                this.associatedDBMS.scambiaOmbrelloni(spiaggiaGestita.getOmbrelloneAtLocation(coordinate), ombrelloneSelezionato);
                this.associatedDBMS.aggiornaMappaSpiaggia(this.spiaggiaGestita.getListaOmbrelloni());
            }
            else{
                spiaggiaGestita.spostaOmbrellone(ombrelloneSelezionato, coordinate);
//                this.associatedDBMS.spostaOmbrellone(ombrelloneSelezionato, coordinate.getxAxis(), coordinate.getyAxis());
                this.associatedDBMS.aggiornaMappaSpiaggia(this.spiaggiaGestita.getListaOmbrelloni());
            }
            System.out.println("Ombrellone spostato");
        }
    }


    /**
     * Questo metodo serve ad aggiungere una tipologia di ombrellone
     */
    public void aggiungiTipologiaOmbrellone() {
        HashMap<TipologiaOmbrellone, Double> tipologie = this.handlerListinoAssociato.ottieniPrezziTipologie();
        this.handlerListinoAssociato.getListinoGestito().outputListaTipologie();

        boolean flag;
        do{
            this.inserisciInformazioniTipologia(tipologie);

            tipologie = this.handlerListinoAssociato.getListinoGestito().getPrezziTipologia();
            this.handlerListinoAssociato.getListinoGestito().outputListaTipologie();

            System.out.println("Vuoi aggiungere altre tipologie? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }while(flag);

        if(this.confermaOperazione()){
            if(this.associatedDBMS.aggiornaMappaTipologie(this.handlerListinoAssociato.getListinoGestito().getPrezziTipologia())){
                System.out.println("Operazione eseguita con successo");
            }
            else System.out.println("Operazioni fallita");
        }
        else System.out.println("Operazioni annullate");
    }

    private void inserisciInformazioniTipologia(HashMap<TipologiaOmbrellone, Double> listaTipi) {

        System.out.println("Inserisci il nome della nuova tipologia: ");
        String nome = this.sc.nextLine();
        System.out.println("Inserisci descrizione della nuova tipologia: ");
        String info = this.sc.nextLine();

        ArrayList<TipologiaOmbrellone> tipologie = new ArrayList<>(listaTipi.keySet().stream().toList()); //TODO controllare se funziona
        if(!this.controlloPresenzaTipologiaInserita(tipologie,nome)){
            this.handlerListinoAssociato.aggiungiNuovaTipologia(new TipologiaOmbrellone(nome,info));
            System.out.println("La nuova tipologia ?? stata aggiunta");
        }
        else{
            System.out.println("La tipologia inserita ?? gi?? presente!");
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

    private String sceltaTipoOmbrellone(ArrayList<TipologiaOmbrellone>listaTipologieUtilizzabili) {
        System.out.println("Tipologie:");
        for( TipologiaOmbrellone tipologia: listaTipologieUtilizzabili){
            System.out.println(tipologia.toString());
        }
        System.out.println("Inserisci il nome della tipologia da associare all'ombrellone");
        String nomeTipologia = this.sc.nextLine();

        while(!this.controlloPresenzaTipologiaInserita(listaTipologieUtilizzabili, nomeTipologia)){
            System.out.println("Il tipo cercato non ?? presente nella lista, riprova");
            nomeTipologia = this.sc.nextLine();
        }
        return nomeTipologia;
    }

    private boolean controlloPresenzaTipologiaInserita(ArrayList<TipologiaOmbrellone> listaTipi , String nome){
        for (TipologiaOmbrellone tipologia: listaTipi) {
            if(tipologia.getNome().equals(nome)) return true;
        }
        return false;
    }

    private void modificaTipologiaOmbrellone(Ombrellone ombrelloneSelezionato){
        ArrayList<TipologiaOmbrellone> listaTipologieDisponibili = this.handlerListinoAssociato.getTipologie();
        if(listaTipologieDisponibili.isEmpty()) {
            System.out.println("Non ci sono tipologie disponibili");
            return;
        }
        System.out.println("La tipologia corrente dell'ombrellone selezionato ??: " + ombrelloneSelezionato.getNomeTipo());

        if(this.handlerPrenotazioneAssociato.isOmbrellonePrenotato(ombrelloneSelezionato)) {
            listaTipologieDisponibili = controlloTipologia(ombrelloneSelezionato.getNomeTipo());
            if(listaTipologieDisponibili.isEmpty()){
                System.out.println("Non ci sono tipologie disponibili");
                return;
            }
        }
        else listaTipologieDisponibili = new ArrayList<>(this.handlerListinoAssociato.getListinoGestito().getPrezziTipologia().keySet());

        aggiornaTipologiaOmbrellone(richiestaTipologia(listaTipologieDisponibili), ombrelloneSelezionato);
    }

    private String richiestaTipologia(ArrayList<TipologiaOmbrellone> listaTipologieDisponibili) {
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
                System.out.println("La tipologia inserita non ?? disponibile o non esiste, ritenta");
        }while(flag);
        return idTipologia;
    }

    private ArrayList<TipologiaOmbrellone> controlloTipologia(String tipologia){
        Set<TipologiaOmbrellone> tipologieDisponibili = new HashSet<>();
        TipologiaOmbrellone tempTipologie = null;
        for(TipologiaOmbrellone tipo : this.handlerListinoAssociato.getListinoGestito().getPrezziTipologia().keySet()){
            if(tipo.getNome().equals(tipologia)){
                tempTipologie = tipo;
            }
        }
        for(TipologiaOmbrellone tipo : this.handlerListinoAssociato.getListinoGestito().getPrezziTipologia().keySet()) {
            if(this.handlerListinoAssociato.getListinoGestito().getPrezziTipologia().get(tipo) >= this.handlerListinoAssociato.getListinoGestito().getPrezziTipologia().get(tempTipologie)){
                tipologieDisponibili.add(tipo);
            }
        }
        return new ArrayList<>(tipologieDisponibili);
    }

    private void aggiornaTipologiaOmbrellone(String tipologia, Ombrellone ombrelloneSelezionato){
            spiaggiaGestita.aggiornaTipologiaOmbrellone(ombrelloneSelezionato, tipologia);
            this.associatedDBMS.aggiornaTipologiaOmbrellone(ombrelloneSelezionato.getIdOmbrellone(), tipologia);
    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
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
        if(sceltaOperazione == 4){
            this.aggiuntaRiga(sceltaRiga);
        }
        System.out.println(this.spiaggiaGestita.toString());
    }

    private void aggiuntaRiga(int sceltaRiga) {
        String direzione = "";
        boolean flag = true;
        while(flag){
            System.out.println("Scegli la direzione in cui aggiungere la riga [sopra/sotto]");
            direzione = this.sc.nextLine().trim().toLowerCase(Locale.ROOT);
            if(Objects.equals(direzione, "sopra") || Objects.equals(direzione, "sotto")) flag = false;
            else System.out.println("Cio' che hai inserito non e' accettabile, ritenta");
        }
        System.out.println("Scegli la lunghezza della nuova riga (int)");
        int lunghezzaNuovaRiga = this.provaScannerInt();

        this.spiaggiaGestita.aggiungiNuovaRiga(sceltaRiga,direzione,lunghezzaNuovaRiga);
    }

    private void accorciamentoRiga(ArrayList<Ombrellone> riga) {
        System.out.println("Scegli di quanto accorciare la riga (int)");
        int lunghezzaAccorciamento = this.provaScannerInt();
        String direzione = this.sceltaDirezioneOperazione("accorciare");

        if(lunghezzaAccorciamento >= riga.size()){
            System.out.println("Stai cercando di accorciare troppo la riga, l'operazione verr?? annullata");
            return;
        }

        if(this.isParteRigaEmpty(riga,lunghezzaAccorciamento,direzione)){
            if(Objects.equals(direzione, "d")) for(int i=0;i<lunghezzaAccorciamento;i++) riga.remove(riga.get(riga.size()-1));
            else for(int i=0;i<lunghezzaAccorciamento;i++) riga.remove(0);
        }
        else System.out.println("Impossibile accorciare la riga perch?? almeno un ombrellone ?? presente");

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
                this.spiaggiaGestita.eliminaRiga(sceltaRiga);
            }
            else System.out.println("La fila non pu?? essere eliminata poich?? contiene almeno un ombrellone");
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
            System.out.println("Scegli la direzione in cui "+ operazione +" la riga [d/s]");
            direzione = this.sc.nextLine().trim().toLowerCase(Locale.ROOT);
            if(Objects.equals(direzione, "d") || Objects.equals(direzione, "s")) return direzione;
            else System.out.println("Cio' che hai inserito non e' accettabile, ritenta");
        }

    }

    private int sceltaOperazioneModificaGriglia(){
        do{
            System.out.println("Scegli l'operazione da eseguire (int)");
            System.out.println("1\tAllungare la riga\n2\tAccorciare la riga\n3\tEliminare la riga\n4\tAggiungere una riga ");
            int sceltaOperazione = this.provaScannerInt();
            if(sceltaOperazione==1 || sceltaOperazione==2 || sceltaOperazione==3 || sceltaOperazione==4) return sceltaOperazione;
            else{
                System.out.println("Il numero inserito non rappresenta un'operazione, ritenta");
            }
        }while(true);
    }

    public ArrayList<ArrayList<Ombrellone>> ottieniVistaSpiaggia() {
        this.spiaggiaGestita.aggiornaSpiaggia(this.associatedDBMS.ottieniVistaSpiaggia());
        return this.spiaggiaGestita.getListaOmbrelloni();
    }

    private int provaScannerInt(){
        while(true){
            try{
                int intero = this.sc.nextInt();
                this.sc.nextLine();
                return intero;
            } catch (Exception e) {
                this.sc.nextLine();
                System.out.println("Cio' che hai inserito non e' un valore numerico, ritenta ");
            }
        }
    }

    public Ombrellone getOmbrellone(int idOmbrellone) {
        for(ArrayList<Ombrellone> currentRow: spiaggiaGestita.getListaOmbrelloni())
            for(Ombrellone ombrelloneCorrente : currentRow)
                if(ombrelloneCorrente == null)
                    continue;
                else{
                    if(ombrelloneCorrente.getIdOmbrellone() == idOmbrellone)
                        return ombrelloneCorrente;
                }
        return null;
    }
}
