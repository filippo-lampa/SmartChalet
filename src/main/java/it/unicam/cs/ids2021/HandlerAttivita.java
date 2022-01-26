package it.unicam.cs.ids2021;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HandlerAttivita {


    private ListaAttivita listaAttivitaAssociata;
    private DBMSController associatedDBMS;
    private HandlerAttrezzatura handlerAttrezzaturaAssociato;
    private Scanner sc;

    public HandlerAttivita(ListaAttivita listaAttivitaAssociata, DBMSController associatedDBMS, HandlerAttrezzatura handlerAttrezzaturaAssociato){
        this.listaAttivitaAssociata = listaAttivitaAssociata;
        this.associatedDBMS = associatedDBMS;
        this.handlerAttrezzaturaAssociato = handlerAttrezzaturaAssociato;
        this.sc = new Scanner(System.in);
    }

    public void aggiungiAttivita(){
        this.listaAttivitaAssociata.aggiornaListaAttivita(this.associatedDBMS.ottieniListaAttivita());
        this.handlerAttrezzaturaAssociato.aggiornaListaAttrezzatura();
        System.out.println(this.listaAttivitaAssociata.ottieniListaAttivitaAggiornata().toString());

        boolean flag;
        do {
            Attivita attivitaProvvisoria = this.inserisciInformazioniAttivita();
            if(this.listaAttivitaAssociata.isNuovaAttivita(attivitaProvvisoria)){
                this.associazioneAttrezzatureAdAttivita(attivitaProvvisoria);
                this.listaAttivitaAssociata.aggiungiAttivita(attivitaProvvisoria);
            }
            else System.out.println("L'attivita' che stai cercando di aggiungere e' gia' presente");

            System.out.println("Vuoi aggiungere altre attivita' ? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);

        if(this.confermaOperazione()){
            if(this.associatedDBMS.aggiornaListaAttivita(this.listaAttivitaAssociata.ottieniListaAttivitaAggiornata())){
                System.out.println("Operazione eseguita con successo");
            }
            else System.out.println("Operazioni fallita");
        }
        else System.out.println("Operazioni annullate");

    }

    private void associazioneAttrezzatureAdAttivita(Attivita attivitaProvvisoria) {
        System.out.println("Vuoi associare attrezzature all'attivita' ?  [y/n]");
        if(Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y")){
            boolean flag;
            do {
                this.sceltaAttrezzatureDaAssociare(attivitaProvvisoria);

                System.out.println("Vuoi aggiungere altre attrezzature' ? [y/n] ");
                flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
            } while (flag);
        }
    }

    private void sceltaAttrezzatureDaAssociare(Attivita attivitaProvvisoria){
        HashMap<Attrezzatura, Integer> attrezzature = this.handlerAttrezzaturaAssociato.getMappaAttrezzaturaAssociata();
        this.handlerAttrezzaturaAssociato.getListaAttrezzatura().printMappaAttrezzature();
        if(attrezzature.isEmpty()) {
            System.out.println("La lista delle attrezzature è vuota");
            return;
        }
        Attrezzatura attrezzatura = this.sceltaAttrezzatura(attrezzature);
        int numeroAttrezzature = this.sceltaNumeroAttrezzature(attrezzature,attrezzatura);

        this.handlerAttrezzaturaAssociato.riservaAttrezzature(attrezzatura.getNome() , numeroAttrezzature);
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
            System.out.println("Inserisci il nome dell'attrezzatura da aggiungere: ");
            String nomeAttrezzatura = sc.nextLine();
            for(Attrezzatura attrezzatura : attrezzature.keySet()){
                if(attrezzatura.getNome().equals(nomeAttrezzatura)) return attrezzatura;
            }
            System.out.println("Il nome inserito non appartiene a nessuna attrezzatura, ritenta ");
        }
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

        int fasciaOraria = this.inserimentoFasciaUtente();

        return new Attivita(this.listaAttivitaAssociata.getNewIdAttivita(),nome,descrizione,data,maxPartecipanti,animatore,oreDurata, fasciaOraria);
    }


    private int provaScannerInt(){
        while(true){
            try{
                int intero = this.sc.nextInt();
                this.sc.nextLine();
                if(intero < 0) {
                    System.out.println("L'intero inserito è negativo, reinserisci il valore");
                    continue;
                }
                return intero;
            } catch (Exception e) {
                System.out.println("Cio' che hai inserito non e' un valore numerico, ritenta ");
            }
        }
    }

    private int inserimentoFasciaUtente(){
        while(true){
            System.out.print("Inserisci la fascia oraria della nuova attivita (mattina/pomeriggio): ");
            String fascia = sc.nextLine();
            if(fascia.equals("mattina"))
                return 1;
            else if(fascia.equals("pomeriggio"))
                return 2;
            else System.out.println("La fascia inserita non esiste");
        }
    }

    private Date inserimentoDataUtente(){
        Date data;
        Date dataCorrente = new Date();
        while(true){
            System.out.print("Inserisci la data dell' attivita [gg/mm/yyyy]: ");
            try {
                data = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
                if (data.before(dataCorrente)) {
                    System.out.println("La data inserita è antecedente alla data odierna");
                    continue;
                }
                break;
            } catch (ParseException e) {
                System.out.println("Formato data non valido.");
            }
        }
        return data;
    }

    public void modificaAttivita(){
        //this.listaAttivitaAssociata.aggiornaListaAttivita(this.associatedDBMS.ottieniAttivitaAggiornate()); //TODO implementare richieste DB
        ArrayList<Attivita> attivitaAggiornate = this.listaAttivitaAssociata.ottieniListaAttivitaAggiornata();
        ArrayList<Attrezzatura> attrezzaturaAggiornata = this.handlerAttrezzaturaAssociato.ottieniListaAttrezzaturaAggiornata();
        if(attivitaAggiornate.isEmpty()) {
            System.out.println("La lista delle attività è vuota, impossibile moificare un'attività");
            return;
        }
        this.printListaAttivita(attivitaAggiornate);
        boolean completed;
        do{
            Attivita attivitaDaModificare = this.selezionaAttivita(attivitaAggiornate);
            System.out.println("Digitare 1 per modificare l'Attivita o 2 per eliminarla");
            int op = this.provaScannerInt();
            if(op == 1){
                modificaParametriAttivita(attivitaDaModificare);
            }
            if(op == 2){
                eliminaAttivita(attivitaDaModificare);
            }
            this.printListaAttivita(attivitaAggiornate);
            System.out.println("Vuoi modificare altre Attivita? [y/n] ");
            completed = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "n");
        }while(!completed);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");
    }

    private void eliminaAttivita(Attivita attivitaDaModificare) {
        System.out.println("Confermi di voler eliminare l'Attivita? [y/n] ");
        if(Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y"))
            this.listaAttivitaAssociata.rimuoviAttivita(attivitaDaModificare);
    }

    private void modificaParametriAttivita(Attivita attivitaDaModificare) {
        System.out.println("Inserire 1 per modificare il nome, 2 per modificare la descrizione, 3 per modificare la data, 4 per modificare il numero " +
                "massimo di posti o 5 per modificare l'attrezzatura associata all'Attivita");
        int op = provaScannerInt();
        switch (op) {
            case 1 -> modificaNomeAttivita(attivitaDaModificare);
            case 2 -> modificaDescrizioneAttivita(attivitaDaModificare);
            case 3 -> modificaDataAttivita(attivitaDaModificare);
            case 4 -> modificaNumMaxPostiAttivita(attivitaDaModificare);
            case 5 -> modificaAttrezzaturaAttivita(attivitaDaModificare);
        }

    }

    private void modificaAttrezzaturaAttivita(Attivita attivitaDaModificare) {
        boolean flag;
        do{
            attivitaDaModificare.printAttrezzatureAssociate();
            System.out.println("Inserire il nome dell'attrezzatura");
            String nomeAttrezzatura = sc.nextLine();
            if(attivitaDaModificare.isAttrezzaturaAssociata(nomeAttrezzatura)){
                System.out.println("Inserire il numero di attrezzature che si desidera associare all'Attivita");
                int numeroAttrezzatureDesiderato = this.provaScannerInt();
                if(this.handlerAttrezzaturaAssociato.controlloDisponibilitaAttrezzatura(nomeAttrezzatura, numeroAttrezzatureDesiderato)){
                    if(this.handlerAttrezzaturaAssociato.riservaAttrezzature(nomeAttrezzatura, numeroAttrezzatureDesiderato)) {
                        this.aggiornaNumeroAttrezzaturaAssociata(attivitaDaModificare, nomeAttrezzatura, numeroAttrezzatureDesiderato);
                        System.out.println("Attrezzature riservate correttamente per l'Attivita");
                    }
                }
                else System.out.println("Attrezzatura non disponibile, impossibile associare all'Attivita la quantità richiesta");
            }
            else{
                System.out.println("L'attrezzatura inserita non è associata all'Attivita, impossibile modificarla");
            }

            System.out.println("Vuoi modificare altre attrezzature' ? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");

        }while(flag);
    }

    private void aggiornaNumeroAttrezzaturaAssociata(Attivita attivitaDaModificare, String nomeAttrezzatura, int numeroAttrezzatureDesiderato) {
        for(Attrezzatura attrezzatura : attivitaDaModificare.getAttrezzatureAssociate().keySet())
            if(attrezzatura.getNome().equals(nomeAttrezzatura))
                attivitaDaModificare.getAttrezzatureAssociate().put(attrezzatura,attivitaDaModificare.getAttrezzatureAssociate().get(attrezzatura) + numeroAttrezzatureDesiderato);
    }

    private void modificaNumMaxPostiAttivita(Attivita attivitaDaModificare) {
        boolean flag = false;
        do{
            System.out.print("Inserire il nuovo numero massimo di posti dell'Attivita");
            int nuovoMaxPosti = this.provaScannerInt();
            int numeroIscrittiAttivita = this.listaAttivitaAssociata.ottieniNumeroIscrittiAttivita(attivitaDaModificare);
            if(nuovoMaxPosti >= numeroIscrittiAttivita) {
                this.listaAttivitaAssociata.aggiornaNumeroMassimoPostiAttivita(attivitaDaModificare, nuovoMaxPosti);
                flag = true;
            }
            else System.out.println("Impossibile aggiornare il numero massimo di posti poichè minore del numero di iscritti all'Attivita");
        }while(!flag);
    }

    private void modificaDataAttivita(Attivita attivitaDaModificare) {
        Date nuovaData = this.inserimentoDataUtente();
        int fasciaOraria = this.inserimentoFasciaUtente();
        this.listaAttivitaAssociata.aggiornaDataEOraAttivita(attivitaDaModificare, nuovaData, fasciaOraria);
    }

    private void modificaDescrizioneAttivita(Attivita attivitaDaModificare) {
        System.out.println("Inserire la nuova descrizione dell'Attivita");
        String descrizione = sc.nextLine();
        this.listaAttivitaAssociata.aggiornaDescrizioneAttivita(attivitaDaModificare, descrizione);
    }

    private void modificaNomeAttivita(Attivita attivitaDaModificare){
        boolean flag = false;
        do{
            System.out.println("Inserire il nuovo nome dell'Attivita");
            String nome = sc.nextLine();
            if(this.listaAttivitaAssociata.controlloDisponibilitaNome(nome, attivitaDaModificare.getData(), attivitaDaModificare.getFasciaOraria())) {
                this.listaAttivitaAssociata.aggiornaNomeAttivita(attivitaDaModificare, nome);
                flag = true;
            }
            else System.out.println("Il nome selezionato è già associato ad un'Attivita nella medesima data e fascia oraria, dunque non è disponibile");
        }while(!flag);
    }

    private Attivita selezionaAttivita(ArrayList<Attivita> listaAttivita) {
        do {
            System.out.println("Inserire il nome dell'Attivita da modificare");
            String Attivita = sc.nextLine();
            for (Attivita currentAttivita : listaAttivita) {
                if (currentAttivita.getNome().equals(Attivita)) {
                    return currentAttivita;
                }
            }
            System.out.println("L'Attivita inserita non esiste");
        }while(true);
    }

    private void printListaAttivita(ArrayList<Attivita> attivitaAggiornate) {
        for(Attivita attivita : attivitaAggiornate)
            if(attivita != null)
                attivita.printDettagliAttivita();
    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
    }

    public ArrayList<Attivita> ottieniListaAttivitaDisponibili() {
        this.listaAttivitaAssociata.aggiornaListaAttivita(this.associatedDBMS.ottieniListaAttivita());
        return this.listaAttivitaAssociata.ottieniListaAttivitaAggiornata();
    }

}
