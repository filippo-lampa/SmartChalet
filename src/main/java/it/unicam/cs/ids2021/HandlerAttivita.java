package it.unicam.cs.ids2021;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HandlerAttivita {

    private final ListaAttivita listaAttivitaAssociata ;
    private final HandlerAttrezzatura handlerAttrezzatura;
    private final DBMSController associatedDBMS;
    private final Scanner sc;

    public HandlerAttivita(DBMSController associatedDBMS,HandlerAttrezzatura handlerAttrezzatura, ListaAttivita listaAttivita) {
        this.listaAttivitaAssociata = listaAttivita;
        this.handlerAttrezzatura = handlerAttrezzatura;
        this.associatedDBMS = associatedDBMS;
        sc = new Scanner(System.in);
    }

    public void aggiungiAttivita(){
        this.associatedDBMS.ottieniListaAttivita();
        this.listaAttivitaAssociata.aggiornaListaAttivita();
        this.handlerAttrezzatura.aggiornaListaAttrezzatura();

        boolean flag;
        do {
            Attivita attivitaProvvisoria = this.inserisciInformazioniAttivita();
            if(this.listaAttivitaAssociata.isNuovaAttivita(attivitaProvvisoria)){
                this.associazioneAttrezzatureAdAttivita(attivitaProvvisoria);

                this.listaAttivitaAssociata.aggiungiAttivita(attivitaProvvisoria);
            }
            else System.out.println("L'attivita' che stai cercando di aggiungere e' gia' presente");

            System.out.println("Vuoi aggiungere altre attivita' ? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);

        if (this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");


    }

    public void modificaAttivita(){

    }

    private Attivita inserisciInformazioniAttivita(){
        System.out.println("Inserisci il nome della nuova attivita: ");
        String nome = this.sc.nextLine();
        System.out.println("Inserisci descrizione della nuova attivita: ");
        String descrizione = this.sc.nextLine();

        Date data = this.inserimentoDataUtente();

        System.out.println("Inserisci il numero massimo di partecipanti della nuova attivita (int): ");
        int maxPartecipanti = this.provaScannerInt();

        System.out.println("Inserisci il nome dell'animatore della nuova attivita: ");
        String animatore = this.sc.nextLine();

        System.out.println("Inserisci la durata in ore della nuova attivita (int): ");
        int oreDurata = this.provaScannerInt();

        return new Attivita(this.listaAttivitaAssociata.getNewIdAttivita(),nome,descrizione,data,maxPartecipanti,animatore,oreDurata);
    }

    private Date inserimentoDataUtente(){
        System.out.print("Inserisci la data della nuova attivita [gg/mm/yyyy]: ");
        Date data;
        while(true){
            try {
                data = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
                break;
            } catch (ParseException e) {
                System.out.println("Formato data non valido.");
            }
        }
       return data;
    }

    private void associazioneAttrezzatureAdAttivita(Attivita attivitaProvvisoria) {
        System.out.println("Vuoi associare attrezzature all'attivita' ?  [y/n]");
        if(Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y")){
            boolean flag;
            do {
                this.sceltaAttrezzatureDaAssociare(attivitaProvvisoria);

                System.out.println("Vuoi aggiungere altre attrezzature' ? [y/n] ");
                flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            } while (flag);
        }
    }

    private void sceltaAttrezzatureDaAssociare(Attivita attivitaProvvisoria){
        HashMap<Attrezzatura, Integer> attrezzature = this.handlerAttrezzatura.getMappaAttrezzaturaAssociata();
        System.out.println(this.handlerAttrezzatura.getListaAttrezzatura().toString());

        Attrezzatura attrezzatura = this.sceltaAttrezzatura(attrezzature);
        int numeroAttrezzature = this.sceltaNumeroAttrezzature(attrezzature,attrezzatura);

        this.handlerAttrezzatura.riservaAttrezzatura(attrezzatura.getId(),numeroAttrezzature);
        attivitaProvvisoria.getAttrezzatureAssociate().put(attrezzatura,numeroAttrezzature);
    }

    private int sceltaNumeroAttrezzature(HashMap<Attrezzatura, Integer> attrezzature, Attrezzatura attrezzatura) {
        while(true){
            System.out.println("Inserisci il numero di attrezzature da aggiungere all'attivita': ");
            int numeroAttrezzature = this.provaScannerInt();
            int numeroAttrezzatureDisponibili = attrezzature.get(attrezzatura);

            if( numeroAttrezzature <= numeroAttrezzatureDisponibili ){
                return numeroAttrezzature;
            }
            else{
                System.out.println("Il numero di attrezzature richieste e' maggiore di quelle disponibili, ritenta ");
            }
        }
    }

    private Attrezzatura sceltaAttrezzatura(HashMap<Attrezzatura, Integer> attrezzature) {
        while(true){
            System.out.println("Inserisci l'id dell'attrezzatura da aggiungere: ");
            int idAttrezzatura = this.provaScannerInt();
            for(Attrezzatura attrezzatura : attrezzature.keySet()){
                if(attrezzatura.getId() == idAttrezzatura) return attrezzatura;
            }
            System.out.println("L'id inserito non appartiene a nessuna attrezzatura, ritenta ");
        }
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

    private boolean confermaOperazione() {
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

}
