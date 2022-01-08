package it.unicam.ids.smartchalet.asf;

import java.util.*;

public class HandlerListino {

    private Listino listinoGestito;

    private final DBMSController associatedDBMS;

    private Scanner sc;

    private Spiaggia spiaggia;

    public HandlerListino(Listino listinoGestito, DBMSController associatedDBMS, Spiaggia spiaggiaAssociata) {
        sc = new Scanner(System.in);
        this.listinoGestito = listinoGestito;
        this.associatedDBMS = associatedDBMS;
    }

    public void aggiungiProdottoBar() {
        //this.listinoGestito.aggiornaListino(); //TODO richiedere listino aggiornato al database
        boolean flag;
        do {
            System.out.println("Inserire nome prodotto");
            String nomeProdotto = sc.nextLine();
            System.out.println("Inserire descrizione prodotto");
            String descrizioneProdotto = sc.nextLine();
            System.out.println("Inserire prezzo prodotto");
            Double prezzoProdotto = sc.nextDouble();
            ProdottoBar nuovoProdottoBar = new ProdottoBar(descrizioneProdotto, nomeProdotto);
            if (!this.listinoGestito.controlloProdottoEsistente(nuovoProdottoBar)) {
                this.listinoGestito.aggiungiAllaListaProdotti(nuovoProdottoBar, prezzoProdotto);
            } else {
                System.out.println("Il prodotto non può essere aggiunto poichè già presente nel listino");
            }
            System.out.println("Vuoi aggiungere altri prodotti? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            sc.nextLine();
        } while (flag);
        if (this.confermaOperazione()) {
            HashMap<ProdottoBar, Double> listinoBarAggiornato = this.listinoGestito.ottieniListinoBar();
            this.associatedDBMS.aggiungiProdottiBar(listinoBarAggiornato);
            mostraListinoBar();
        }
    }

    public void aggiungiFasciaDiPrezzo() {
        //ArrayList<ArrayList<Ombrellone>> vistaSpiaggia = associatedDBMS.ottieniVistaSpiaggia(); //TODO call a db e comunicazione handler
        //handlerSpiaggia.ottieniVistaSpiaggia(vistaSpiaggia);
        //TODO: inserire l'arrayList di arrayList di Ombrelloni nell'oggetto spiaggia?
        //listinoGestito = associatedDBMS.ottieniListinoAggiornato(); //TODO call a db

        this.listinoGestito.mostraFasceEPrezzi();

        boolean working = true;

        while (working) {

            Coordinate coordinateInizio = this.selezionaPosto();
            Coordinate coordinateFine = this.selezionaPosto();
            FasciaDiPrezzo nuovaFascia = new FasciaDiPrezzo("temporanea", coordinateInizio, coordinateFine);
            if (!this.controlloCoordinate(nuovaFascia)) {
                System.out.println("Le coordinate dell'inizio dela fascia si trovano dopo la sua fine, l'operazione non verrà eseguita");
                continue;
            }

            if (controlloLocazioni(null, nuovaFascia)) {
                this.aggiungiFasciaAListino(nuovaFascia);
            } else {
                System.out.println("Fascia non valida, reinserire: ");
                continue;
            }
            System.out.println("Aggiungere altre fascie di prezzo? (y/n)");
            working = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
        }
        if (this.confermaOperazione()) {
            System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database//
        } else System.out.println("Operazioni annullate");
    }

    private void aggiungiFasciaAListino(FasciaDiPrezzo nuovaFascia) {
        String nomeStringa;
        Double fasciaPrezzo;

        do {
            System.out.println("Inserire il nome della fascia di prezzo: ");
            nomeStringa = this.sc.nextLine();
        } while (!listinoGestito.isNomeDisponibile(nomeStringa));
        nuovaFascia.setNome(nomeStringa);

        do {
            System.out.println("Inserire il prezzo della fascia di prezzo: ");
            fasciaPrezzo = this.sc.nextDouble();
            this.sc.nextLine();
        } while (!this.isPrezzoNegativo(fasciaPrezzo));

        this.listinoGestito.addPrezziFascia(nuovaFascia, fasciaPrezzo);
        this.listinoGestito.mostraFasceEPrezzi();

    }

    private boolean confermaOperazione() {
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

    public void mostraListinoBar() {
        for (ProdottoBar currentProdottoBar : this.listinoGestito.ottieniListinoBar().keySet()) {
            System.out.println(currentProdottoBar.getNomeProdotto() + ": " + this.listinoGestito.ottieniListinoBar().get(currentProdottoBar) + "€");
        }
    }

    /**
     * Questo metodo serve a modificare una fascia di prezzo esistente
     */
    public void modificaFasciaDiPrezzo() {
        this.associatedDBMS.ottieniVistaSpiaggia();
        // this.spiaggia.aggiornaSpiaggia(); //TODO  modificare quando viene data la posiibilità agli handler di comunicare tra di loro
        this.associatedDBMS.ottieniListinoAggiornato();
        // this.listinoGestito.aggiornaListino(); //TODO  modificare quando viene data la posiibilità agli handler di comunicare tra di loro
        listinoGestito.mostraFasceEPrezzi();
        boolean flag;
        do {
            FasciaDiPrezzo fasciaDaModificare = this.selezioneFascia();
            if (fasciaDaModificare == null) return;

            this.sceltaTipoModifiche(fasciaDaModificare);
            listinoGestito.mostraFasceEPrezzi();
            System.out.println("Vuoi modificare altro? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);

        if (this.confermaOperazione())
            System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");
    }


    public void impostaPrezziOmbrellone() {
        //this.associatedDBMS.ottieniListinoAggiornato();
        //this.listinoGestito.aggiornaListino(); //TODO
        boolean working = true;
        while (working) {
            sceltaOperazione();
            System.out.println("Vuoi modificare altro? (y/n)");
            working = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");

        }

        if (this.confermaOperazione())
            System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");
    }

    private void sceltaOperazione() {
        boolean working = true;
        int answer;

        System.out.println("Scegliere il prezzo da modificare: ");
        System.out.println("[1]: Prezzo Base");
        System.out.println("[2]: Tipologie");
        do {
            answer = this.sc.nextInt();
            sc.nextLine();
            if (answer == 1) {
                sceltaPrezzoBase();
                working = false;
            } else if (answer == 2) {
                sceltaPrezzoTipologie();
                working = false;
            } else {
                System.out.println("Reinserire (1 o 2): ");
            }
        } while (working);
    }

    private boolean isPrezzoNegativo(Double prezzo) {
        if(Double.compare(prezzo, 0.0) < 0){
            System.out.println("Il prezzo inserito è negativo");
            return true;
        }
        return false;
    }

    private void sceltaPrezzoBase() {
        double nuovoPrezzoBase;
        System.out.println("Il precedente prezzo base degli ombrelloni è " + listinoGestito.getPrezzoBaseOmbrellone() + ".");
        nuovoPrezzoBase = sceltaPrezzo();
        listinoGestito.setPrezzoBaseOmbrellone(nuovoPrezzoBase);
        System.out.println("Il nuovo prezzo base degli ombrelloni è " + nuovoPrezzoBase + ".\n");
    }

    private double sceltaPrezzo(){
        double nuovoPrezzo;
        do{
            System.out.println("Inserire il nuovo prezzo: ");
            nuovoPrezzo = this.sc.nextDouble();
            this.sc.nextLine();
        }while(isPrezzoNegativo(nuovoPrezzo));
        return nuovoPrezzo;
    }

    private void sceltaPrezzoTipologie() {
        HashMap<TipologiaOmbrellone, Double> prezziTipologia = listinoGestito.getPrezziTipologia();
        double nuovoPrezzoTipologia;
        boolean risultatoSoddisfacente = false;
        String nomeTipologia = null;

        while (!risultatoSoddisfacente) {
            System.out.println("Scegliere la tipologia di cui modificare il prezzo: ");
            this.listinoGestito.outputListaTipologie();
            nomeTipologia = this.sc.nextLine();
            for(TipologiaOmbrellone tipologia : prezziTipologia.keySet()){
                if(tipologia.getNome().equals(nomeTipologia))
                    risultatoSoddisfacente = true;
            }
            if(!risultatoSoddisfacente)
                System.out.println("La tipologia inserita non è contenuta nel listino, riprovare: ");
        }
        nuovoPrezzoTipologia = sceltaPrezzo();
        this.listinoGestito.setNuovoPrezzoTipologia(nomeTipologia, nuovoPrezzoTipologia);
    }

    private FasciaDiPrezzo selezioneFascia() {
        if (this.listinoGestito.getPrezziFascia().isEmpty()) {
            System.out.println("Non ci sono fasce al momento");
            return null;
        }
        FasciaDiPrezzo fasciaDaModificare = null;
        boolean flag = true;
        while (flag) {
            System.out.println("Scegli una fascia di prezzo da modificare: [nome]");
            String app = sc.next();
            for (FasciaDiPrezzo fasciaAttuale : this.listinoGestito.getPrezziFascia().keySet()) {
                if (fasciaAttuale.getNome().equals(app)) {
                    fasciaDaModificare = fasciaAttuale;
                }
            }
            if (fasciaDaModificare == null) {
                System.out.println("Non hai selezionato una fascia esistente, ritenta");
                continue;
            }

            flag = false;
        }
        return fasciaDaModificare;
    }

    private void sceltaTipoModifiche(FasciaDiPrezzo fasciaDaModificare) {
        System.out.println("Scegli il tipo di modifica da effettuare: ");
        System.out.println("1 : Modifica prezzo fascia \n2 : Modifica locazione fascia \n3 : Elimina fascia  ");
        boolean flag = true;
        while (flag) {
            int scelta = sc.nextInt();
            sc.nextLine();
            if (scelta == 1) {
                this.modificaPrezzo(fasciaDaModificare);
            } else if (scelta == 2) {
                this.modificaLocazione(fasciaDaModificare);
            } else if (scelta == 3) {
                this.eliminaFascia(fasciaDaModificare);
            } else {
                System.out.println(" Non hai selezionato una scelta esistente, ritenta ");
                continue;
            }
            flag = false;
        }
    }

    private void modificaPrezzo(FasciaDiPrezzo fasciaDaModificare) {
        System.out.println("Inserisci il nuovo prezzo della fascia: ");
        double nuovoPrezzo = sc.nextDouble();
        sc.nextLine();
        this.listinoGestito.modificaPrezzoFascia(fasciaDaModificare, nuovoPrezzo);
    }

    private void modificaLocazione(FasciaDiPrezzo fasciaDaModificare) {
        //TODO inserire output con fasce (sfruttare la griglia spiaggia quando verrà implementata)

        FasciaDiPrezzo fasciaTemporanea = new FasciaDiPrezzo("Temporanea");
        System.out.println("Inserisci la nuova prima locazione");
        fasciaTemporanea.setCoordinateInizio(this.selezionaPosto());
        System.out.println("Inserisci la nuova ultima locazione");
        fasciaTemporanea.setCoordinateFine(this.selezionaPosto());

        //TODO inserire controllo attraverso la griglia per controllare se le coordinate sono presenti
        if (!this.controlloCoordinate(fasciaTemporanea)) {
            System.out.println("Le coordinate dell'inizio dela fascia si trovano dopo la sua fine, l'operazione non verrà eseguita");
            return;
        }

        if (controlloLocazioni(fasciaDaModificare, fasciaTemporanea))
            this.listinoGestito.modificaLocazioniFascia(fasciaDaModificare, fasciaTemporanea);
        else
            System.out.println("La fascia con le nuove locazioni si sovrappone ad un'altra, l'operazione non verrà eseguita");
    }

    private void eliminaFascia(FasciaDiPrezzo fasciaDaModificare) {
        if (this.confermaOperazione()) this.listinoGestito.eliminaFasciaDiPrezzo(fasciaDaModificare);
    }

    private Coordinate selezionaPosto() {
        System.out.println("Inserire la fila (y)");
        int fila = this.sc.nextInt();
        sc.nextLine();
        System.out.println("Inserire la colonna (x)");
        int colonna = this.sc.nextInt();
        sc.nextLine();
        return new Coordinate(colonna, fila);
    }


    private boolean controlloLocazioni(FasciaDiPrezzo fasciaDaModificare, FasciaDiPrezzo fasciaTemporanea) {

        for (FasciaDiPrezzo fasciaAttuale : this.listinoGestito.getPrezziFascia().keySet()) {
            if (fasciaAttuale == fasciaDaModificare) continue;

            FasciaDiPrezzo appoggio1 = new FasciaDiPrezzo("Temporanea1", fasciaAttuale.getCoordinateFine(), fasciaTemporanea.getCoordinateInizio());
            FasciaDiPrezzo appoggio2 = new FasciaDiPrezzo("Temporanea2", fasciaTemporanea.getCoordinateFine(), fasciaAttuale.getCoordinateInizio());
            if (this.controlloCoordinate(appoggio1) || this.controlloCoordinate(appoggio2)) continue;

            return false;
        }
        return true;
    }

    private boolean controlloCoordinate(FasciaDiPrezzo fasciaTemporanea) {
        int yPrimoTemporanea = fasciaTemporanea.getCoordinateInizio().getyAxis();
        int xPrimoTemporanea = fasciaTemporanea.getCoordinateInizio().getxAxis();
        int yUltimoTemporanea = fasciaTemporanea.getCoordinateFine().getyAxis();
        int xUltimoTemporanea = fasciaTemporanea.getCoordinateFine().getxAxis();
        return yPrimoTemporanea < yUltimoTemporanea || (yPrimoTemporanea == yUltimoTemporanea && xPrimoTemporanea < xUltimoTemporanea);
    }

}