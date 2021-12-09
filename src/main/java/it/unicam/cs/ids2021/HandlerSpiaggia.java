package it.unicam.cs.ids2021;

import java.util.*;

public class HandlerSpiaggia {

    private Spiaggia spiaggiaGestita;



    public HandlerSpiaggia(){
        this.spiaggiaGestita = new Spiaggia();
    }

    public Spiaggia getSpiaggiaGestita() {
        return this.spiaggiaGestita;
    }

    public void aggiungiOmbrellone() {

        //TODO aggiungere chiamata a database per la mappa della spiaggia e aggiornarla
        //this.spiaggiaGestita = new Spiaggia(); //al posto di questo, quello che ritorna dal metodo precedente da inserire
        Ombrellone ombrelloneProvvisorio;
        boolean continuare;
        do {

            ArrayList<String[]> coordinate = this.ottieniPostiOmbrelloni();
            if(coordinate.size()==0) {
                System.out.println("Non ci sono più posti in cui aggiungere un ombrellone");
                break;
            }
            String[] coordinateOmbrellone = this.selezionaPosto(coordinate);
            Coordinate coordinateScelte = new Coordinate(Integer.parseInt(coordinateOmbrellone[0]),Integer.parseInt(coordinateOmbrellone[1]));
            int tipo = this.sceltaTipoOmbrellone();
            ombrelloneProvvisorio = new Ombrellone(tipo,coordinateScelte);

            this.spiaggiaGestita.aggiungiOmbrellone(ombrelloneProvvisorio);
            //this.spiaggiaGestita.getListaOmbrelloni().get(Integer.parseInt(coordinateOmbrellone[1])).set(Integer.parseInt(coordinateOmbrellone[0]),this.ombrelloneProvvisorio);

            System.out.println(this.getSpiaggiaGestita().toString());  //TODO trasformare in vista spiaggia aggiornata

            System.out.println("Vuoi aggiungere altri ombrelloni? [y/n] ");
            Scanner sc = new Scanner(System.in);
            continuare = Objects.equals(sc.next().trim().toLowerCase(Locale.ROOT), "y");

        }while(continuare);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");    //modificare il caso d'uso e diagramma se vogliamo messaggio output
    }

    private String[] selezionaPosto(ArrayList<String[]> coordinate) { //TODO controllare perchè l'ho modificato da come scritto sul diagramma di progetto e in più non sono sicuro
        int i=0;
        for (String[] coordinateOmbrellone: coordinate) {
            System.out.println("Posto Libero "+i+": "+ Arrays.toString(coordinateOmbrellone));
            i++;
        }
        System.out.println("Scegli il numero del posto: ");
        Scanner sc = new Scanner(System.in);
        return coordinate.get(sc.nextInt());

    }

    private int sceltaTipoOmbrellone() {
        //TODO sostituire con call database x i tipi
        ArrayList<ArrayList<Object>> listaTipi = this.getTipi();

        //inserire filtro per le tipologie //TODO controllare se esiste ancora la cosa del filtro

        System.out.println("Tipi: "); //aggiungere lista tipi
        for (ArrayList<Object> tipo: listaTipi) {
            System.out.println("Tipo "+tipo.get(0)+"\t"+ tipo.get(1)+"\t"+ tipo.get(2));
        }
        Scanner sc = new Scanner(System.in); //prendi dalla lista ciò che l'utente ha inserito

        return sc.nextInt();
    }

    public void aggiungiTipologiaOmbrellone() {//TODO modificRE
        Scanner sc = new Scanner(System.in); //prendi dalla lista ciò che l'utente ha inserito
        System.out.println("Inserisci il nome della tipologia: ");
        String nome = sc.next();
        System.out.println("Inserisci descrizione della tipologia: ");
        String info = sc.next();
        this.inserisciInformazioniTipologia(nome,info);
    }

    private void inserisciInformazioniTipologia(String nome, String info) {
        ArrayList<ArrayList<Object>> listaTipi = this.getTipi(); //TODO controllare posizione e sostituire con call a database
        int contatore = 0;

        for (ArrayList<Object> tipo: listaTipi) {
            if(tipo.get(1).equals(nome)) contatore++;
        }
        if(contatore == 0){
            //TODO inserire chiamata database x inserire nuova tipologia
            System.out.println("La nuova tipologia è stata aggiunta");
        }
        else{   //controllare che non sono sicuro di questa cosa
            System.out.println("La tipologia inserita è già presente!");
            this.aggiungiTipologiaOmbrellone();
        }

    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        Scanner sc = new Scanner(System.in);
        return Objects.equals(sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }


    private ArrayList<String[]> ottieniPostiOmbrelloni(){ //TODO controllare tipo di ritorno (mettere COOrdinate)

        ArrayList<String[]> coordinate = new ArrayList<>();
        int x = 0;
        int y = 0;

        for (ArrayList<Ombrellone> riga : this.spiaggiaGestita.getListaOmbrelloni()) {
            for (Ombrellone ombrellone : riga) {
                if(ombrellone == null){
                    String[] app = new String[2];
                    app[0]= ""+x;
                    app[1]= ""+y;
                    coordinate.add(app);
                }
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
            lista.add(null);
            listaTipi.add(lista);
        }

        return listaTipi;
    }


}
