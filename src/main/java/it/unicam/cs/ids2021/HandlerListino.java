package it.unicam.cs.ids2021;

import java.util.*;

public class HandlerListino {

    private final Listino listinoGestito;

    private final DBMSController associatedDBMS;

    private Scanner sc;

    private HandlerSpiaggia handlerSpiaggiaAssociato;

    public HandlerListino(Listino listinoGestito, DBMSController associatedDBMS) {
        sc = new Scanner(System.in);
        this.listinoGestito = listinoGestito;
        this.associatedDBMS = associatedDBMS;
    }

    public void setHandlerSpiaggiaAssociato(HandlerSpiaggia handlerSpiaggiaAssociato) {
        this.handlerSpiaggiaAssociato = handlerSpiaggiaAssociato;
    }

    public Listino getListinoGestito() {
        return listinoGestito;
    }

    public void aggiungiProdottoBar() {
        //this.listinoGestito.aggiornaListino(); //TODO richiedere listino aggiornato al database
        boolean flag;
        this.mostraListinoBar();
        do {
            System.out.println("Inserire nome prodotto");
            String nomeProdotto = sc.nextLine();
            System.out.println("Inserire descrizione prodotto");
            String descrizioneProdotto = sc.nextLine();
            System.out.println("Inserire prezzo prodotto");
            Double prezzoProdotto = sc.nextDouble();
            this.sc.nextLine();
            ProdottoBar nuovoProdottoBar = new ProdottoBar(descrizioneProdotto, nomeProdotto);
            if (!this.listinoGestito.controlloProdottoEsistente(nuovoProdottoBar)) {
                this.listinoGestito.aggiungiAllaListaProdotti(nuovoProdottoBar, prezzoProdotto);
            } else {
                System.out.println("Il prodotto non può essere aggiunto poichè già presente nel listino");
            }
            mostraListinoBar();
            System.out.println("Vuoi aggiungere altri prodotti? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);
        if (this.confermaOperazione()) {
            HashMap<ProdottoBar, Double> listinoBarAggiornato = this.listinoGestito.getPrezziBar();
            this.associatedDBMS.aggiungiProdottiBar(listinoBarAggiornato);
        }
    }

    /**
     * Questo metodo serve per modificare un ProdottoBar presente nel listino
     */
    public void modificaProdottoBar(){
        this.listinoGestito.setPrezziBar(this.associatedDBMS.ottieniMappaProdottiBar());
        HashMap<ProdottoBar, Double> prezziBar = this.listinoGestito.getPrezziBar();
        if(prezziBar.isEmpty()) System.out.println("Non ci sono prodotti bar nel listino da modificare");
        this.mostraListinoBar();

        boolean flag;
        do {
            ProdottoBar prodottoScelto = this.sceltaProdottoBar(prezziBar);
            this.sceltaOperazioneProdottoBar(prodottoScelto);
            mostraListinoBar();
            System.out.println("Vuoi aggiungere altri prodotti? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);

        if (this.confermaOperazione()) {
            HashMap<ProdottoBar, Double> listinoBarAggiornato = this.listinoGestito.getPrezziBar();
            if(this.associatedDBMS.aggiornaMappaProdottoBar(listinoBarAggiornato)) System.out.println("Operazione eseguita con successo");
            else System.out.println("Operazioni fallita");
        }
    }

    private void sceltaOperazioneProdottoBar(ProdottoBar prodottoScelto) {
        System.out.println("Scegli il tipo di modifica da effettuare: ");
        System.out.println("1 : Modifica nome prodotto \n2 : Modifica descrizione prodotto \n3 : Modifica prezzo prodotto \n4 : Elimina prodotto ");
        boolean flag = true;
        while (flag) {
            int scelta = this.provaScannerInt();
            if (scelta == 1) this.modificaNomeProdotto(prodottoScelto);
            else if (scelta == 2) this.modificaDescrizioneProdotto(prodottoScelto);
            else if (scelta == 3) this.modificaPrezzoProdotto(prodottoScelto);
            else if (scelta == 4) this.eliminaProdotto(prodottoScelto);
            else {
                System.out.println(" Non hai selezionato una scelta esistente, ritenta ");
                continue;
            }
            flag = false;
        }
    }

    private void modificaNomeProdotto(ProdottoBar prodottoScelto) {
        System.out.println("Inserisci il nuovo nome: ");
        String nuovoNome = this.sc.nextLine();
        if(!this.listinoGestito.aggiornaNomeProdotto(prodottoScelto,nuovoNome)){
            System.out.println("Nome prodotto presente, le operazioni verranno annullate");
        }
    }

    private void modificaDescrizioneProdotto(ProdottoBar prodottoScelto) {
        System.out.println("Inserisci la nuova descrizione: ");
        String descrizione = this.sc.nextLine();
        this.listinoGestito.aggiornaDescrizioneProdotto(prodottoScelto,descrizione);
    }

    private void modificaPrezzoProdotto(ProdottoBar prodottoScelto) {
        System.out.println("Prezzo attuale prodotto : "+this.listinoGestito.getPrezziBar().get(prodottoScelto));
        System.out.println("Inserisci nuovo prezzo: ");
        double nuovoPrezzo = this.provaScannerDouble();
        this.listinoGestito.aggiornaPrezzoProdotto(prodottoScelto, nuovoPrezzo);
    }

    private void eliminaProdotto(ProdottoBar prodottoScelto) {
        if(this.confermaOperazione()){
            if(this.listinoGestito.eliminaProdotto(prodottoScelto) != null) System.out.println("Prodotto eliminato");
            else System.out.println("Operazione fallita");
        }
        else System.out.println("Operazione annullato");
    }

    private ProdottoBar sceltaProdottoBar(HashMap<ProdottoBar, Double> prezziBar) {
        System.out.println("Inserire l'id del prodotto che vuoi modificare (int)");
        int idProdotto = this.provaScannerInt();

        for( ProdottoBar prodottoBar: prezziBar.keySet()){
            if(prodottoBar.getIdProdotto() == idProdotto) return prodottoBar;
        }

        System.out.println("Non hai selezionato nessun prodotto esistente");
        this.mostraListinoBar();
        return this.sceltaProdottoBar(prezziBar);
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
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
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
            fasciaPrezzo = this.provaScannerDouble();
        } while (this.isPrezzoNegativo(fasciaPrezzo));

        this.listinoGestito.addPrezziFascia(nuovaFascia, fasciaPrezzo);
        this.listinoGestito.mostraFasceEPrezzi();

    }

    private boolean confermaOperazione() {
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
    }

    public void mostraListinoBar() {
        if(this.listinoGestito.getPrezziBar().isEmpty()){
            System.out.println("Il listino bar è vuoto");
        }
        else {
            System.out.println("Listino Bar: ");
            for (ProdottoBar currentProdottoBar : this.listinoGestito.getPrezziBar().keySet()) {
                System.out.println(currentProdottoBar.toString() + ": " + this.listinoGestito.getPrezziBar().get(currentProdottoBar) + "€");
            }
        }
    }

    /**
     * Questo metodo serve a modificare una fascia di prezzo esistente
     */
    public void modificaFasciaDiPrezzo() {
        ArrayList<ArrayList<Ombrellone>> listaOmbrelloni = this.handlerSpiaggiaAssociato.ottieniVistaSpiaggia();
        this.listinoGestito.aggiornaMappaFasce(this.associatedDBMS.ottieniMappaFasce());
        HashMap<FasciaDiPrezzo, Double> prezziFascia = this.listinoGestito.getPrezziFascia();

        this.outputVistaSpiaggiaFasce(listaOmbrelloni);
        listinoGestito.mostraFasceEPrezzi();

        boolean flag;
        do {
            FasciaDiPrezzo fasciaDaModificare = this.selezioneFascia(prezziFascia);
            if (fasciaDaModificare == null) return;

            this.sceltaTipoModifiche(fasciaDaModificare,listaOmbrelloni);
            prezziFascia = this.listinoGestito.getPrezziFascia();

            this.outputVistaSpiaggiaFasce(listaOmbrelloni);
            listinoGestito.mostraFasceEPrezzi();

            System.out.println("Vuoi modificare altro? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);

        if(this.confermaOperazione()){
            if(this.associatedDBMS.aggiornaMappaFascie(this.listinoGestito.getPrezziFascia())){
                System.out.println("Operazione eseguita con successo");
            }
            else System.out.println("Operazioni fallita");
        }
        else System.out.println("Operazioni annullate");
    }


    public void impostaPrezziOmbrellone() {
        //this.associatedDBMS.ottieniListinoAggiornato();
        //this.listinoGestito.aggiornaListino(); //TODO
        boolean working = true;
        while (working) {
            sceltaOperazione();
            System.out.println("Vuoi modificare altro? (y/n)");
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");

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

    private FasciaDiPrezzo selezioneFascia(HashMap<FasciaDiPrezzo, Double> prezziFascia) {
        if (prezziFascia.isEmpty()) {
            System.out.println("Non ci sono fasce al momento");
            return null;
        }
        FasciaDiPrezzo fasciaDaModificare = null;
        boolean flag = true;
        while (flag) {
            System.out.println("Scegli una fascia di prezzo da modificare: [nome]");
            String nomeFascia = sc.nextLine();
            for (FasciaDiPrezzo fasciaAttuale : this.listinoGestito.getPrezziFascia().keySet()) {
                if (fasciaAttuale.getNome().equals(nomeFascia)) {
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

    private void sceltaTipoModifiche(FasciaDiPrezzo fasciaDaModificare, ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        System.out.println("Scegli il tipo di modifica da effettuare: ");
        System.out.println("1 : Modifica prezzo fascia \n2 : Modifica locazione fascia \n3 : Elimina fascia  ");
        boolean flag = true;
        while (flag) {
            int scelta = this.provaScannerInt();
            if (scelta == 1) {
                this.modificaPrezzo(fasciaDaModificare);
            } else if (scelta == 2) {
                this.modificaLocazione(fasciaDaModificare,listaOmbrelloni);
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
        double nuovoPrezzo = this.provaScannerDouble();
        this.listinoGestito.modificaPrezzoFascia(fasciaDaModificare, nuovoPrezzo);
    }

    private void modificaLocazione(FasciaDiPrezzo fasciaDaModificare, ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        this.outputVistaSpiaggiaFasce(listaOmbrelloni);

        FasciaDiPrezzo fasciaTemporanea = new FasciaDiPrezzo("Temporanea");
        System.out.println("Inserisci la nuova prima locazione");
        fasciaTemporanea.setCoordinateInizio(this.selezionaPosto());
        if(!this.controlloEsistenzaCoordinate(fasciaTemporanea.getCoordinateInizio(),listaOmbrelloni)) return;
        System.out.println("Inserisci la nuova ultima locazione");
        fasciaTemporanea.setCoordinateFine(this.selezionaPosto());
        if(!this.controlloEsistenzaCoordinate(fasciaTemporanea.getCoordinateFine(),listaOmbrelloni)) return;

        if (!this.controlloCoordinate(fasciaTemporanea)) {
            System.out.println("Le coordinate dell'inizio dela fascia si trovano dopo la sua fine, l'operazione non verrà eseguita");
            return;
        }

        if (controlloLocazioni(fasciaDaModificare, fasciaTemporanea)) this.listinoGestito.modificaLocazioniFascia(fasciaDaModificare, fasciaTemporanea);
        else System.out.println("La fascia con le nuove locazioni si sovrappone ad un'altra, l'operazione non verrà eseguita");
    }

    private boolean controlloEsistenzaCoordinate(Coordinate coordinate, ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        try{
            listaOmbrelloni.get(coordinate.getyAxis()).get(coordinate.getyAxis());
        }catch (Exception e) {
            System.out.println("Le coordinate inserite non fanno parte della spiaggia, le operazioni verranno annullate ");
            return false;
        }
        return true;
    }

    private void outputVistaSpiaggiaFasce(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        ArrayList<ArrayList<String>> griglia = this.convertiAGrigliaVuota(listaOmbrelloni);

        for(FasciaDiPrezzo fascia : this.listinoGestito.getPrezziFascia().keySet()){
            this.inserisciFasciaInGriglia(fascia,griglia);
        }

        StringBuilder str = new StringBuilder();
        for (ArrayList<String> riga : griglia) {
            for (String posto : riga) {
                str.append(posto).append("\t");
            }
            str.append("\n");
        }
        System.out.println(str);
    }

    private void inserisciFasciaInGriglia(FasciaDiPrezzo fascia, ArrayList<ArrayList<String>> griglia) {
        int x = 0;
        int y = 0;
        for (ArrayList<String> riga : griglia) {
            for (String posto : riga) {
                int yInizioFascia = fascia.getCoordinateInizio().getyAxis();
                int xInizioFascia = fascia.getCoordinateInizio().getxAxis();
                int yFineFascia = fascia.getCoordinateFine().getyAxis();
                int xFineFascia = fascia.getCoordinateFine().getxAxis();
                if((x>=xInizioFascia && y==yInizioFascia)||(y>yInizioFascia && y<yFineFascia)||(y==yFineFascia && x<=xFineFascia)){
                    griglia.get(y).set(x, fascia.getNome());
                }
                x++;
            }
            x=0;
            y++;
        }
    }

    private ArrayList<ArrayList<String>> convertiAGrigliaVuota(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        ArrayList<ArrayList<String>> griglia = new ArrayList<>();

        for(ArrayList<Ombrellone> riga : listaOmbrelloni){
            ArrayList<String> rigaGriglia = new ArrayList<>();
            for(Ombrellone posto : riga){
                rigaGriglia.add(null);
            }
            griglia.add(rigaGriglia);
        }
        return griglia;
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
    
    private double provaScannerDouble(){
        while(true){
            try{
                double numero = this.sc.nextDouble();
                this.sc.nextLine();
                return numero;
            } catch (Exception e) {
                System.out.println("Cio' che hai inserito non e' un valore numerico, ritenta ");
            }
        }
    }


    public HashMap<TipologiaOmbrellone, Double> ottieniTipologie() {
        this.listinoGestito.aggiornaTipologie(this.associatedDBMS.ottieniTipologie());
        return this.listinoGestito.getPrezziTipologia();
    }

    public void aggiungiNuovaTipologia(TipologiaOmbrellone tipologiaOmbrellone) {
        this.listinoGestito.aggiungiTipologia(tipologiaOmbrellone);
    }
}