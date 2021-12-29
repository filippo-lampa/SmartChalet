package it.unicam.cs.ids.asf;

import java.util.*;

public class HandlerListino {
    private final DBMSController associatedDBMS;
    private final HandlerSpiaggia handlerSpiaggia;
    //TODO: Prendere il listino dal DataBase
    private Listino listinoInGestione;

    public HandlerListino(DBMSController associatedDBMS, Listino listinoInGestione, HandlerSpiaggia handlerSpiaggia) {
        this.associatedDBMS = associatedDBMS;
        this.listinoInGestione = listinoInGestione;
        this.handlerSpiaggia = handlerSpiaggia;
    }

    public void aggiungiFasciaDiPrezzo() {
        ArrayList<ArrayList<Ombrellone>> vistaSpiaggia = associatedDBMS.ottieniVistaSpiaggia();
        handlerSpiaggia.ottieniVistaSpiaggia(vistaSpiaggia);
        //TODO: inserire l'arrayList di arrayList di Ombrelloni nell'oggetto spiaggia?
        listinoInGestione = associatedDBMS.ottieniListinoAggiornato();
        HashMap<FasciaDiPrezzo, Double> prezziFascia = listinoInGestione.getPrezziFascia();
        mostraFasceEPrezzi(prezziFascia);

        boolean working = true;
        String answer;
        Scanner scans = new Scanner(System.in);
        while (working) {

            String nomeStringa = null;
            int idPrimo;
            int idUltimo;
            double fasciaPrezzo;

            System.out.println("Scegliere il primo ombrellone della fascia: ");
            idPrimo = scans.nextInt();
            System.out.println("Scegliere il l'ultimo ombrellone della fascia: ");
            idUltimo = scans.nextInt();
            if (controlloValiditaFascia()) {
                System.out.println("Inserire il prezzo della fascia di prezzo: ");
                fasciaPrezzo = scans.nextDouble();
                System.out.println("[Opzionale] Inserire il nome della fascia di prezzo: ");
                nomeStringa = scans.nextLine();

                FasciaDiPrezzo nuovaFascia = new FasciaDiPrezzo(nomeStringa, idPrimo, idUltimo, nuovaChiaveFascia(prezziFascia));
                listinoInGestione.addPrezziFascia(prezziFascia, nuovaFascia, fasciaPrezzo);
                mostraFasceEPrezzi(prezziFascia);

            } else {
                System.out.println("Fascia non valida, reinserire: ");
            }
            System.out.println("Aggiungere altre fascie di prezzo? (y/n)");
            while (true) {
                answer = scans.nextLine().trim().toLowerCase();
                if (answer.equals("y")) {
                    working = true;
                    break;
                } else if (answer.equals("n")) {
                    working = false;
                    break;
                } else {
                    System.out.println("Reinserire (y/n)");
                }
            }
        }
        scans.close();
        //updateDB();
    }

    private void mostraFasceEPrezzi(HashMap<FasciaDiPrezzo, Double> prezziFascia) {
        for (Map.Entry<FasciaDiPrezzo, Double> entry : prezziFascia.entrySet()) {
            System.out.println(("Fascia " + entry.getKey().getId() + " - costo: " + entry.getValue() + "."));
        }
    }

    private int nuovaChiaveFascia(HashMap<FasciaDiPrezzo, Double> map) {
        Map.Entry<FasciaDiPrezzo, Double> entryWithMaxKey = null;
        int nuovaChiave;

        for (Map.Entry<FasciaDiPrezzo, Double> currentEntry : map.entrySet()) {
            if (entryWithMaxKey == null || currentEntry.getKey().getId() > entryWithMaxKey.getKey().getId()) {
                entryWithMaxKey = currentEntry;
            }
        }

        nuovaChiave = entryWithMaxKey.getKey().getId() + 1;
        return nuovaChiave;
    }

    public void impostaPrezziOmbrellone() {
        sceltaOperazione();
        //TODO: conferma finale e updateDB
    }

    private void sceltaOperazione() {
        boolean working = true;
        String answer;
        Scanner scans = new Scanner(System.in);

        while (working) {
            while (true) {
                System.out.println("Scegliere il prezzo da modificare: ");
                System.out.println("[1]: Prezzo Base");
                System.out.println("[2]: Tipologie");

                answer = scans.nextLine().trim().toLowerCase();
                if (answer.equals("1")) {
                    sceltaPrezzoBase();
                } else if (answer.equals("2")) {
                    sceltaPrezzoTipologie();
                } else {
                    System.out.println("Reinserire (1 o 2): ");
                }
                System.out.println("Modificare ancora i prezzi? (y/n)");
                    answer = scans.nextLine().trim().toLowerCase();
                    if (answer.equals("y")) {

                    } else if (answer.equals("n")) {
                        break;
                    } else {
                        System.out.println("Reinserire (y/n)");
                    }
                    working=false;
            }

        }
        scans.close();
    }

    private void sceltaPrezzoBase() {
        double nuovoPrezzoBase;
        double prezzoBaseDaModificare = listinoInGestione.getPrezzoBaseOmbrellone();
        System.out.println("Il precedente prezzo base degli ombrelloni è " + prezzoBaseDaModificare + ".");
        System.out.println("Inserire il nuovo prezzo: ");
        Scanner in = new Scanner(System.in);
        nuovoPrezzoBase = in.nextDouble();
        if (Double.compare(nuovoPrezzoBase, 0.0) < 0) {
            throw new IllegalArgumentException("Nuovo valore impossibile da inserire");
        } else {
            listinoInGestione.setPrezzoBaseOmbrellone(nuovoPrezzoBase);
            System.out.println("Il nuovo prezzo base degli ombrelloni è " + nuovoPrezzoBase + ".\n");
        }
    }

    private void sceltaPrezzoTipologie() {
        HashMap<Integer, Double> prezziTipologia;
        listinoInGestione = associatedDBMS.ottieniListinoAggiornato();
        prezziTipologia = listinoInGestione.getPrezziTipologia();

        double nuovoPrezzoTipologia;
        boolean risultatoSoddisfacente = false;
        int integerTipologia = 0;
        Scanner scan = new Scanner(System.in);

        while (!risultatoSoddisfacente) {
            System.out.println("Scegliere la tipologia di cui modificare il prezzo: ");
            mostraTipologie(prezziTipologia);
            integerTipologia = scan.nextInt();

            if (prezziTipologia.containsKey(integerTipologia)) {
                risultatoSoddisfacente = true;
            } else {
                System.out.println("La tipologia inserita non è contenuta nel listino, riprovare: ");
            }
        }

        double prezzoTipologiaDaModificare = prezziTipologia.get(integerTipologia);

        System.out.println("Il precedente prezzo prezzo della tipologia "+integerTipologia+" è " + prezzoTipologiaDaModificare + ".");

        nuovoPrezzoTipologia = scan.nextDouble();
        if (Double.compare(nuovoPrezzoTipologia, 0.0) < 0) {
            throw new IllegalArgumentException("Nuovo valore impossibile da inserire");
        } else {
            listinoInGestione.editPrezziTipologia(integerTipologia, nuovoPrezzoTipologia);
            System.out.println("Il nuovo prezzo della tipologia "+integerTipologia+" è " + nuovoPrezzoTipologia + ".");
        }
        //updateDB();
    }

    private void mostraTipologie(HashMap<Integer, Double> prezziTipologia) {
        for (Map.Entry<Integer, Double> entry : prezziTipologia.entrySet()) {
            System.out.println(("Tipologia " + entry.getKey() + " - costo: " + entry.getValue() + "."));
        }
    }

    //TODO
    boolean controlloValiditaFascia() {

        return true;
    }

}
