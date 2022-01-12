package it.unicam.ids.smartchalet.asf;

import java.util.*;

public class HandlerAttrezzatura {

    private ListaAttrezzature listaAttrezzaturaAssociata;
    private final DBMSController associatedDBMS;
    private Scanner sc;

    public HandlerAttrezzatura(ListaAttrezzature listaAttrezzaturaAssociata, DBMSController associatedDBMS) {
        sc = new Scanner(System.in);
        this.listaAttrezzaturaAssociata = listaAttrezzaturaAssociata;
        this.associatedDBMS = associatedDBMS;

    }

    public void aggiungiAttrezzatura() {
        HashMap<Attrezzatura, Integer> tempLista = associatedDBMS.ottieniListaAttrezzature();
        listaAttrezzaturaAssociata.aggiornaListaAttrezzatura(tempLista);
        listaAttrezzaturaAssociata.getAttrezzature();

        boolean working = true;

        while (working) {
            String nomeAttrezzatura;
            String descrizioneAttrezzatura;

            nomeAttrezzatura = richiestaNomeAttrezzaturaDaAggiungere();

            if (!this.listaAttrezzaturaAssociata.controlloAttrezzaturaEsistente(nomeAttrezzatura)) {
                descrizioneAttrezzatura = richiestaDescrizioneAttrezzatura();

                Attrezzatura nuovaAttrezzatura = new Attrezzatura(nomeAttrezzatura, descrizioneAttrezzatura);
                listaAttrezzaturaAssociata.addAttrezzatura(nuovaAttrezzatura, 0);
            }

            int quantitaAttrezzatura = richiestaNumeroAttrezzatura();

            listaAttrezzaturaAssociata.aggiungiQuantitaAttrezzatura(nomeAttrezzatura, quantitaAttrezzatura);

            listaAttrezzaturaAssociata.getAttrezzature();
            System.out.println("Aggiungere altre attrezzature? [y/n]");
            working = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            sc.nextLine();
        }
        if (this.confermaOperazione()) {
            sc.nextLine();
            System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
            listaAttrezzaturaAssociata.getAttrezzature();
        } else System.out.println("Operazioni annullate");
    }

    private String richiestaNomeAttrezzaturaDaAggiungere() {
        boolean flag = true;
        String nuovoNome;
        do {
            System.out.println("Inserire nome della nuova attrezzatura");
            nuovoNome = sc.nextLine();
            if (nuovoNome!=null){
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
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

    public void rimuoviAttrezzatura() {
        //HashMap<Attrezzatura, Integer> tempLista = associatedDBMS.ottieniListaAttrezzature();
        //listaAttrezzaturaAssociata.aggiornaListaAttrezzatura(tempLista);
        listaAttrezzaturaAssociata.getAttrezzature();

        boolean working = true;

        int quantitaAttrezzatura = 0;
        while (working) {
            String nomeAttrezzatura;

            nomeAttrezzatura = richiestaNomeAttrezzaturaDaRimuovere();

            boolean goodAmount = false;
            while (!goodAmount) {
                quantitaAttrezzatura = richiestaNumeroAttrezzatura();

                if (listaAttrezzaturaAssociata.controlloDisponibilitaAttrezzatura(nomeAttrezzatura, quantitaAttrezzatura)) {
                    goodAmount = true;
                } else {
                    System.out.println("Non è possibile rimuovere " + quantitaAttrezzatura + " elementi, inserire un altro numero.");
                    continue;
                }

            }
            listaAttrezzaturaAssociata.rimuoviQuantitaAttrezzatura(nomeAttrezzatura, quantitaAttrezzatura);
            listaAttrezzaturaAssociata.getAttrezzature();
            System.out.println("Rimuovere altre attrezzature? [y/n]");
            working = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            sc.nextLine();
        }
        if (this.confermaOperazione()) {
            sc.nextLine();
            System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
            listaAttrezzaturaAssociata.getAttrezzature();
        } else System.out.println("Operazioni annullate");
    }

    private String richiestaNomeAttrezzaturaDaRimuovere() {
        boolean flag = true;
        String nomeAttrezzatura;
        do {
            System.out.println("Inserire nome dell'attrezzatura di cui rimuovere unità:");
            nomeAttrezzatura = sc.nextLine();

            if (nomeAttrezzatura!=null) {
                flag = false;
            } else {
                continue;
            }

        } while (flag);

        return nomeAttrezzatura;
    }


    public void riservaAttrezzatura(Attrezzatura attrezzatura, int numeroAttrezzatura) {

        return;
    }


}
