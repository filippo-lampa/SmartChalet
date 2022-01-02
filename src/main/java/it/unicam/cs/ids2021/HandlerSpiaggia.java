package it.unicam.cs.ids2021;

import java.util.*;

public class HandlerSpiaggia {

    private final Spiaggia spiaggiaGestita;
    private final DBMSController associatedDBMS;
    private Ombrellone ombrelloneSelezionato;
    private Listino listino;
    private Scanner sc;

    public HandlerSpiaggia(Spiaggia spiaggiaGestita, DBMSController associatedDBMS, Listino listino){
        sc = new Scanner(System.in);
        this.associatedDBMS = associatedDBMS;
        this.spiaggiaGestita = spiaggiaGestita;
        this.listino = listino;
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

    /**
     * Questo metodo serve a modificare un ombrellone creato precedentemente
     */
    public void modificaOmbrellone(){
        //associatedDBMS.ottieniVistaSpiaggia();
        ottieniVistaSpiaggia();
        boolean flag = true;
        while(flag){
            System.out.println("Inserire l'id dell'ombrellone da modificare");
            int idOmbrellone = sc.nextInt();
            sc.nextLine();
            selezionaOmbrellone(idOmbrellone);
            System.out.println("Digitare 1 per rimuovere l'ombrellone, 2 per spostare l'ombrellone o 3 per modificare la tipologia dell'ombrellone");
            int op = sc.nextInt();
            sc.nextLine();
            if(op == 1){
                rimuoviOmbrellone();
            }
            if(op == 2){
                Coordinate nuovaPosizione = selezionaPosto();
                spostaOmbrellone(nuovaPosizione.getxAxis(), nuovaPosizione.getyAxis());
            }
            if(op == 3){
                modificaTipologiaOmbrellone();
            }
            System.out.println("Modificare altri ombrelloni? y/n" );
            String response = sc.next();
            flag = response.equals("y");
        }
        ottieniVistaSpiaggia();
    }

    private Coordinate selezionaPosto(){
        System.out.println("Inserire la fila (y)");
        int fila = this.sc.nextInt();
        sc.nextLine();
        System.out.println("Inserire la colonna (x)");
        int colonna = this.sc.nextInt();
        sc.nextLine();
        return new Coordinate(fila, colonna);
    }

    private void ottieniVistaSpiaggia(){
        ArrayList<ArrayList<Ombrellone>> vistaSpiaggiaCorrente = spiaggiaGestita.getListaOmbrelloni();
        int posizioneOmbrelloneCounter = 0;
        for(ArrayList<Ombrellone> currentRow : vistaSpiaggiaCorrente) {
            for (Ombrellone currentOmbrellone : currentRow) {
                System.out.println("Posizione numero: " + posizioneOmbrelloneCounter + " ");
                if (currentOmbrellone != null) {
                    System.out.println("Id ombrellone: " + currentOmbrellone.getIdOmbrellone());
                    System.out.println("Tipo: " + currentOmbrellone.getIdTipo() + " ");
                    System.out.println("Numero lettini associati: " + currentOmbrellone.getNumeroLettiniAssociati() + " ");
                    System.out.println("L'ombrellone è prenotato: " + currentOmbrellone.isBooked() + " ");
                }
                else System.out.println("Posizione vuota, nessun ombrellone piazzato");
                posizioneOmbrelloneCounter++;
            }
        }
    }

    private Ombrellone selezionaOmbrellone(int idOmbrellone){
        for(ArrayList<Ombrellone> currentRow : spiaggiaGestita.getListaOmbrelloni()) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if(currentOmbrellone.getIdOmbrellone() == idOmbrellone) {
                    ombrelloneSelezionato = currentOmbrellone;
                    return currentOmbrellone;
                }
            }
        }
        return null;
    }

    private void rimuoviOmbrellone(){
        if(confermaOperazione())
            spiaggiaGestita.rimuoviOmbrellone(ombrelloneSelezionato);
    }

    private Coordinate selezionaPosto(int riga, int colonna){
        return new Coordinate(riga, colonna);
    }

    private void spostaOmbrellone(int riga, int colonna){
        Coordinate nuoveCoordinate = new Coordinate(riga, colonna);
        if(confermaOperazione()) {
            if (spiaggiaGestita.isLocationOccupied(nuoveCoordinate))
                spiaggiaGestita.scambiaOmbrelloni(spiaggiaGestita.getOmbrelloneAtLocation(nuoveCoordinate), ombrelloneSelezionato);
            else spiaggiaGestita.spostaOmbrellone(ombrelloneSelezionato, nuoveCoordinate);
            System.out.println("Ombrellone spostato");
        }
    }

    /**
     * Questo metodo serve ad aggiungere una tipologia di ombrellone
     */
    public void aggiungiTipologiaOmbrellone() {

        HashMap<TipologiaOmbrellone, Double> listaTipi = this.listino.getPrezziTipologia();
        this.outputListaTipologie(listaTipi);
        boolean flag;
        do{
            this.inserisciInformazioniTipologia(listaTipi);

            listaTipi = this.listino.getPrezziTipologia();
            this.outputListaTipologie(listaTipi);

            System.out.println("Vuoi aggiungere altre tipologie? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            this.sc.nextLine();
        }while(flag);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO aggiungere aggiornamento database
        else System.out.println("Operazioni annullate");
    }

    private void outputListaTipologie(HashMap<TipologiaOmbrellone, Double> listaTipi){
        if(listaTipi.isEmpty()){
            System.out.println("Al momento non ci sono tipologie salvate");
        }
        else{
            System.out.println("Tipi: ");
            for (TipologiaOmbrellone tipologia: listaTipi.keySet()) {
                System.out.println(tipologia+" Moltipilicatore: "+ listaTipi.get(tipologia));
            }
        }
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
        HashMap<TipologiaOmbrellone, Double> listaTipi = this.listino.getPrezziTipologia();
        this.outputListaTipologie(listaTipi);
        if(listaTipi.isEmpty()) {
            System.out.println("Non sono state ancora aggiunte tipologie, non è possibile aggiungere un ombrellone, annullamento operazioni");
            return null;
        }
        String nomeTipologia = this.sc.nextLine();

        while(!this.controlloPresenzaTipologiaInserita(listaTipi,nomeTipologia)){
            System.out.println("Il tipo cercato non è presente nella lista, riprova");
            nomeTipologia = this.sc.nextLine();
        }
        return nomeTipologia;
    }

    private void modificaTipologiaOmbrellone(){
        ArrayList<Integer> listaTipologie = new ArrayList<>();
        ArrayList<Integer> listaTipologieDisponibili;
        if(ombrelloneSelezionato.isBooked())
            listaTipologieDisponibili = controlloTipologia(ombrelloneSelezionato.getIdTipo());
        else listaTipologieDisponibili = listaTipologie;
        for(int i=0; i<listaTipologie.size(); i++)
            System.out.println(listaTipologie.get(i));
        System.out.println("Inserire la nuova tipologia dell'ombrellone");
        int idTipologia = sc.nextInt();
        sc.nextLine();
        aggiornaTipologiaOmbrellone(idTipologia);
    }

    private ArrayList<Integer> controlloTipologia(int idTipologia){
        return null;
    }

    private void aggiornaTipologiaOmbrellone(int idTipologia){
        sc.nextLine();
        if(confermaOperazione())
            spiaggiaGestita.aggiornaTipologiaOmbrellone(ombrelloneSelezionato, idTipologia);
    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
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

    private boolean aggiuntaAllaSpiaggia(){
        Coordinate coordinateScelte = this.selezionaPosto();
        String tipo = this.sceltaTipoOmbrellone();
        if(tipo == null) return false;
        this.spiaggiaGestita.aggiungiOmbrellone(new Ombrellone(1,coordinateScelte)); //TODO combiare 1 e capire la cosa degli idTipologie
        return true;
    }

}