package it.unicam.cs.ids2021;

import java.util.*;

public class HandlerSpiaggia {

    private final Spiaggia spiaggiaGestita;
    private Ombrellone ombrelloneSelezionato;
    //private final DBMSController associatedDBMS;
    private final Scanner sc;


    public HandlerSpiaggia(){
        this.spiaggiaGestita = new Spiaggia();
        //this.associatedDBMS = new DBMSController();
        this.sc = new Scanner(System.in);
    }

    public Spiaggia getSpiaggiaGestita() {
        return this.spiaggiaGestita;
    }

    public void aggiungiOmbrellone() {

        //this.associatedDBMS.ottieniVistaSpiaggia(); // Sistemare per metterlo nella spiaggia gestita
        boolean flag;
        do {
            ArrayList<Coordinate> coordinate = this.ottieniPostiSenzaOmbrelloni();
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
            this.ombrelloneSelezionato = new Ombrellone(tipo,coordinateScelte);

            this.spiaggiaGestita.aggiungiOmbrellone(this.ombrelloneSelezionato);

            System.out.println(this.getSpiaggiaGestita().toString());  //TODO trasformare in vista spiaggia aggiornata x output

            System.out.println("Vuoi aggiungere altri ombrelloni? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");

        }while(flag);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");    //modificare il caso d'uso e diagramma se vogliamo messaggio output
    }

    private Coordinate selezionaPosto(){ //TODO quando fai il merge secondo me è meglio scritto così perchè lo uso anche io il metodo
        System.out.println("Inserire la fila dell'ombrellone da selezionare");
        int fila = this.sc.nextInt();
        System.out.println("Inserire la colonna dell'ombrellone da selezionare");
        int colonna = this.sc.nextInt();
        return new Coordinate(fila, colonna);
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

        return this.sc.nextInt();
    }

    public void aggiungiTipologiaOmbrellone() {//TODO modificare
        //prendi dalla lista ciò che l'utente ha inserito
        System.out.println("Inserisci il nome della tipologia: ");
        String nome = this.sc.next();
        System.out.println("Inserisci descrizione della tipologia: ");
        String info = this.sc.next();
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

    private boolean confermaOperazione(){ //TODO controllare x il merge
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }


    private ArrayList<Coordinate> ottieniPostiSenzaOmbrelloni(){

        ArrayList<Coordinate> coordinate = new ArrayList<>();
        int x = 0;
        int y = 0;

        for (ArrayList<Ombrellone> riga : this.spiaggiaGestita.getListaOmbrelloni()) {
            for (Ombrellone ombrellone : riga) {
                if(ombrellone == null){
                    //Coordinate coordinateAppoggio = new Coordinate(x,y);
                    coordinate.add(new Coordinate(x,y));
                }
                else coordinate.add(null);
                x++;
            }
            x=0;
            y++;
        }

        return coordinate;
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
