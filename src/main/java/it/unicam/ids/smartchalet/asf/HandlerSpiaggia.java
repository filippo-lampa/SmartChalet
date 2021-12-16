package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class HandlerSpiaggia {

    private final Spiaggia spiaggiaGestita;

    private final DBMSController associatedDBMS;

    private Ombrellone ombrelloneSelezionato;

    private Scanner sc;

    public HandlerSpiaggia(Spiaggia spiaggiaGestita, DBMSController associatedDBMS){
        sc = new Scanner(System.in);
        this.associatedDBMS = associatedDBMS;
        this.spiaggiaGestita = spiaggiaGestita;
    }

    public Spiaggia getSpiaggiaGestita() {
        return spiaggiaGestita;
    }

    public void aggiungiOmbrellone() {

        //this.associatedDBMS.ottieniVistaSpiaggia(); // Sistemare per metterlo nella spiaggia gestita
        boolean flag;
        do {
            ArrayList<Coordinate> coordinate = this.spiaggiaGestita.ottieniPostiSenzaOmbrelloni();
            if(coordinate.size()==0) {
                System.out.println("Non ci sono più posti in cui aggiungere un ombrellone");
                break;
            }
            else{ //tutta questa cosa verrà sostituita con output
                System.out.println("Coordinate posti disponibili per aggiungere ombrelloni: ");
                int appoggio=0;
                for (Coordinate coord: coordinate) {
                    System.out.print("Posto "+appoggio+" : \t");
                    if(coord == null) System.out.println("Occupato");
                    else System.out.println(coord.getxAxis()+"\t"+ coord.getyAxis());
                    appoggio++;
                }
            }
            Coordinate coordinateScelte = this.selezionaPosto();
            int tipo = this.sceltaTipoOmbrellone();
            this.ombrelloneSelezionato = new Ombrellone(tipo,coordinateScelte, this.spiaggiaGestita.getTotaleOmbrelloni());

            this.spiaggiaGestita.aggiungiOmbrellone(this.ombrelloneSelezionato);

            System.out.println(this.getSpiaggiaGestita().toString());  //TODO trasformare in vista spiaggia aggiornata x output

            System.out.println("Vuoi aggiungere altri ombrelloni? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");

        }while(flag);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");    //modificare il caso d'uso e diagramma se vogliamo messaggio output
    }

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
                spostaOmbrellone(nuovaPosizione.xAxis, nuovaPosizione.yAxis);
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
        System.out.println("Inserire la fila");
        int fila = this.sc.nextInt();
        sc.nextLine();
        System.out.println("Inserire la colonna");
        int colonna = this.sc.nextInt();
        sc.nextLine();
        return new Coordinate(fila, colonna);
    }

     void ottieniVistaSpiaggia(){
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

    public void aggiungiTipologiaOmbrellone() {//TODO modificare
        //prendi dalla lista ciò che l'utente ha inserito
        System.out.println("Inserisci il nome della tipologia: ");
        String nome = this.sc.nextLine();
        System.out.println("Inserisci descrizione della tipologia: ");
        String info = this.sc.nextLine();
        this.inserisciInformazioniTipologia(nome,info);
    }

    private void inserisciInformazioniTipologia(String nome, String info) {
        ArrayList<ArrayList<Object>> listaTipi = this.getTipi(); //TODO controllare posizione e sostituire con call a database
        int contatore = 0;

        for (ArrayList<Object> tipo: listaTipi) {
            if(tipo.get(1).equals(nome)) contatore++; //ricontrollare i parametri necessari
        }
        if(contatore == 0){
            //TODO inserire chiamata database x inserire nuova tipologia con moltiplicatore null
            System.out.println("La nuova tipologia è stata aggiunta");
        }
        else{   //controllare che non sono sicuro di questa cosa
            System.out.println("La tipologia inserita è già presente!");
            this.aggiungiTipologiaOmbrellone();
        }

    }

    private int sceltaTipoOmbrellone() {
        //TODO sostituire con call database x i tipi
        ArrayList<ArrayList<Object>> listaTipi = this.getTipi();

        //inserire filtro per le tipologie //TODO controllare se esiste ancora la cosa del filtro

        System.out.println("Tipi: ");
        for (ArrayList<Object> tipo: listaTipi) {
            if (tipo.get(2) == null) continue;  //controllare come viene gestito
            System.out.println("Tipo "+tipo.get(0)+"\t"+ tipo.get(1)+"\t"+ tipo.get(2));
        }
        int tipo = this.sc.nextInt();
        sc.nextLine();
        while(tipo < 0 || tipo >= listaTipi.size()){
            System.out.println("Il tipo cercato non è presente nella lista, riprova");
            tipo = this.sc.nextInt();
            sc.nextLine();
        }
        return tipo;
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

    private boolean confermaOperazione(){ //TODO controllare x il merge
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

    private ArrayList<ArrayList<Object>> getTipi(){ //TODO eliminare quando impostato database
        ArrayList<ArrayList<Object>> listaTipi = new ArrayList<>();
        for(int i=0;i<4;i++){
            ArrayList<Object> lista= new ArrayList<>();
            lista.add(i);
            lista.add("Nome"+i);
            lista.add(1.5); //moltiplicatore costo esempio
            listaTipi.add(lista);
        }

        return listaTipi;
    }

}
