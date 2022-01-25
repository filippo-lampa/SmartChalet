package it.unicam.ids.smartchalet.asf;

import java.util.*;

public class HandlerAttrezzatura {

    private ListaAttrezzatura listaAttrezzaturaAssociata;
    private DBMSController associatedDBMS;
    Scanner sc;

    public HandlerAttrezzatura(ListaAttrezzatura listaAttrezzaturaAssociata, DBMSController associatedDBMS){
        this.listaAttrezzaturaAssociata = listaAttrezzaturaAssociata;
        this.associatedDBMS = associatedDBMS;
        this.sc = new Scanner(System.in);
    }

    public ArrayList<Attrezzatura> ottieniListaAttrezzaturaAggiornata() {
        return this.listaAttrezzaturaAssociata.ottieniListaAttrezzaturaAggiornata();
    }

    public HashMap<Attrezzatura,Integer> ottieniMappaAttrezzature() {
        this.listaAttrezzaturaAssociata.aggiornaListaAttrezzature(this.associatedDBMS.ottieniMappaAttrezzature());
        return this.listaAttrezzaturaAssociata.getMappaAttrezzatura();
    }

    public ListaAttrezzatura getListaAttrezzatura(){
        return this.listaAttrezzaturaAssociata;
    }

    public HashMap<Attrezzatura, Integer> getMappaAttrezzaturaAssociata() {
        return this.listaAttrezzaturaAssociata.getMappaAttrezzatura();
    }

    public boolean controlloDisponibilitaAttrezzatura(String nomeAttrezzatura, int numeroAttrezzature) {
        return this.listaAttrezzaturaAssociata.controlloDisponibilitaAttrezzatura(nomeAttrezzatura, numeroAttrezzature);
    }

    public boolean riservaAttrezzature(String nomeAttrezzatura, int numeroAttrezzatureDesiderato) {
        return this.listaAttrezzaturaAssociata.riservaAttrezzatura(nomeAttrezzatura, numeroAttrezzatureDesiderato);
    }


    public void aggiungiAttrezzatura() {
        listaAttrezzaturaAssociata.aggiornaMappaAttrezzatura(associatedDBMS.ottieniMappaAttrezzature());
        listaAttrezzaturaAssociata.printMappaAttrezzature();

        boolean working = true;

        while (working) {

            System.out.println("Digitare 1 per creare una nuova attrezzatura o 2 per aggiungere elementi ad un'attrezzatura esistente");
            int op = sc.nextInt();
            sc.nextLine();
            if(op == 1){
                creaAttrezzatura();
            }
            if(op == 2){
                aggiungiElementiAdAttrezzatura();
            }
            listaAttrezzaturaAssociata.printMappaAttrezzature();
            System.out.println("Vuoi eseguire altre operazioni sulle attrezzature? [y/n]");
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }
        if (this.confermaOperazione()) {
            // this.associatedDBMS.aggiornaMappaAttrezzature(this.listaAttrezzaturaAssociata.getMappaAttrezzatura());
            System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database

        } else System.out.println("Operazioni annullate");
    }

    private void aggiungiElementiAdAttrezzatura() {
        String nomeAttrezzatura = this.richiestaNomeAttrezzatura();
        if (this.listaAttrezzaturaAssociata.controlloAttrezzaturaEsistente(nomeAttrezzatura)) {
            this.listaAttrezzaturaAssociata.aggiungiQuantitaAttrezzatura(nomeAttrezzatura, richiestaNumeroAttrezzatura());
        }
    }

    private void creaAttrezzatura(){
        String nomeAttrezzatura;
        String descrizioneAttrezzatura;

        nomeAttrezzatura = richiestaNomeAttrezzatura();

        if (!this.listaAttrezzaturaAssociata.controlloAttrezzaturaEsistente(nomeAttrezzatura)) {
            descrizioneAttrezzatura = richiestaDescrizioneAttrezzatura();

            Attrezzatura nuovaAttrezzatura = new Attrezzatura(nomeAttrezzatura, descrizioneAttrezzatura);
            listaAttrezzaturaAssociata.addAttrezzatura(nuovaAttrezzatura, richiestaNumeroAttrezzatura());
        }
        else System.out.println("L'attrezzatura che si desidera inserire esiste già");

    }

    private String richiestaNomeAttrezzatura() {
        boolean flag = true;
        String nuovoNome;
        do {
            System.out.println("Inserire nome dell'attrezzatura");
            nuovoNome = sc.nextLine();
            if (!nuovoNome.isEmpty()){
                flag = false;
            }
        } while (flag);

        return nuovoNome;
    }

    private String richiestaDescrizioneAttrezzatura() {
        String nuovaDescrizione;
        System.out.println("Inserire descrizione della nuova attrezzatura");
        nuovaDescrizione = sc.nextLine();
        return nuovaDescrizione;
    }

    private int richiestaNumeroAttrezzatura() {
        boolean flag = true;
        int nuovaQuantita;
        do {
            System.out.println("Inserire quantità di attrezzatura");
            nuovaQuantita = sc.nextInt();
            sc.nextLine();
            if (isPrezzoNegativo(nuovaQuantita)) {
                continue;
            } else {
                flag = false;
            }

        } while (flag);

        return nuovaQuantita;
    }

    private boolean isPrezzoNegativo(int quantita) {
        if (Integer.compare(quantita, 0) < 0) {
            System.out.println("Il prezzo inserito è negativo");
            return true;
        }
        return false;
    }

    private boolean confermaOperazione() {
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
    }

    public void rimuoviAttrezzatura() {
       // listaAttrezzaturaAssociata.aggiornaMappaAttrezzatura(associatedDBMS.ottieniMappaAttrezzature());  //TODO
        listaAttrezzaturaAssociata.printMappaAttrezzature();

        boolean working = true;

        int quantitaAttrezzatura = 0;
        while (working) {
            String nomeAttrezzatura;

            nomeAttrezzatura = richiestaNomeAttrezzatura();

            boolean goodAmount = false;
            while (!goodAmount) {
                quantitaAttrezzatura = richiestaNumeroAttrezzatura();

                if (listaAttrezzaturaAssociata.controlloDisponibilitaAttrezzatura(nomeAttrezzatura, quantitaAttrezzatura)) {
                    goodAmount = true;
                } else System.out.println("Non è possibile rimuovere " + quantitaAttrezzatura + " elementi, inserire un altro numero.");

            }
            listaAttrezzaturaAssociata.rimuoviQuantitaAttrezzatura(nomeAttrezzatura, quantitaAttrezzatura);
            listaAttrezzaturaAssociata.printMappaAttrezzature();
            System.out.println("Rimuovere altre attrezzature? [y/n]");
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        }
        if (this.confermaOperazione()) {
            // this.associatedDBMS.aggiornaMappaAttrezzature(this.listaAttrezzaturaAssociata.getMappaAttrezzatura());
            System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database

        } else System.out.println("Operazioni annullate");
    }


    public void aggiornaListaAttrezzatura(){
        this.listaAttrezzaturaAssociata.aggiornaMappaAttrezzatura(this.associatedDBMS.ottieniMappaAttrezzature());

    }

}
