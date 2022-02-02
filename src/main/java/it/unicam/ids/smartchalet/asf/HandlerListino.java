package it.unicam.ids.smartchalet.asf;

import java.util.*;

public class HandlerListino {

    private Listino listinoGestito;

    private final DBMSController associatedDBMS;

    private Scanner sc;

    private HandlerSpiaggia handlerSpiaggiaAssociato;

    private static HandlerListino instance = null;

    private HandlerListino() {
        sc = new Scanner(System.in);
        this.listinoGestito = new Listino();
        this.associatedDBMS = DBMSController.getInstance();
    }

    public static HandlerListino getInstance() {
        if (instance == null) {
            instance = new HandlerListino();
        }
        return instance;
    }

    public void setHandlerSpiaggiaAssociato(){
        this.handlerSpiaggiaAssociato = HandlerSpiaggia.getInstance();
    }

    public void aggiungiProdottoBar() {
        this.listinoGestito.aggiornaMappaProdotti(this.associatedDBMS.ottieniMappaProdottiBar());
        boolean flag;
        mostraListinoBar();
        do {
            ProdottoBar nuovoProdottoBar = this.creaProdottoBar();
            System.out.println("Inserire prezzo prodotto");
            Double prezzoProdotto = this.provaScannerDouble();
            if (!this.listinoGestito.controlloProdottoEsistente(nuovoProdottoBar)) {
                this.listinoGestito.aggiungiAllaListaProdotti(nuovoProdottoBar,prezzoProdotto);
            } else {
                System.out.println("Il prodotto non può essere aggiunto poichè già presente nel listino");
            }
            mostraListinoBar();
            System.out.println("Vuoi aggiungere altri prodotti? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);
        if (this.confermaOperazione()) {
            HashMap<ProdottoBar, Double> listinoBarAggiornato = this.listinoGestito.getPrezziBar();
            this.associatedDBMS.aggiornaMappaProdottoBar(listinoBarAggiornato);
        }
    }

    private ProdottoBar creaProdottoBar() {
        System.out.println("Inserire nome prodotto");
        String nomeProdotto = sc.nextLine();
        System.out.println("Inserire descrizione prodotto");
        String descrizioneProdotto = sc.nextLine();
        return new ProdottoBar(descrizioneProdotto, nomeProdotto);
    }

    public void aggiungiFasciaDiPrezzo() {
        handlerSpiaggiaAssociato.ottieniVistaSpiaggia();
        this.listinoGestito.aggiornaMappaFasce(this.associatedDBMS.ottieniMappaFasce());
        this.outputVistaSpiaggiaFasce(this.vistaSpiaggiaFasce(this.handlerSpiaggiaAssociato.ottieniVistaSpiaggia()));
        this.listinoGestito.mostraFasceEPrezzi();

        boolean working = true;

        while (working) {

            Coordinate coordinateInizio = this.selezionaPosto();
            if(!this.controlloEsistenzaCoordinate(coordinateInizio, this.handlerSpiaggiaAssociato.getSpiaggiaGestita().getListaOmbrelloni())){
                continue;
            }
            Coordinate coordinateFine = this.selezionaPosto();
            if(!this.controlloEsistenzaCoordinate(coordinateFine, this.handlerSpiaggiaAssociato.getSpiaggiaGestita().getListaOmbrelloni())){
                continue;
            }
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
            this.associatedDBMS.aggiornaMappaFasce(this.listinoGestito.getPrezziFascia());
            System.out.println("Operazioni eseguite");
        } else System.out.println("Operazioni annullate");
    }

    /**
     * Questo metodo serve per modificare un ProdottoBar presente nel listino
     */
    public void modificaProdottoBar(){
        this.listinoGestito.setPrezziBar(this.associatedDBMS.ottieniMappaProdottiBar());
        HashMap<ProdottoBar, Double> prezziBar = this.listinoGestito.getPrezziBar();
        this.mostraListinoBar();

        boolean flag;
        do {
            ProdottoBar prodottoScelto = this.sceltaProdottoBar(prezziBar);
            this.sceltaOperazioneProdottoBar(prodottoScelto);
            mostraListinoBar();
            System.out.println("Vuoi modificare altri prodotti? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);

        if (this.confermaOperazione()) {
            HashMap<ProdottoBar, Double> listinoBarAggiornato = this.listinoGestito.getPrezziBar();
            if(this.associatedDBMS.aggiornaMappaProdottoBar(listinoBarAggiornato)) System.out.println("Operazione eseguita con successo");
            else System.out.println("Operazione fallita");
        }
        else System.out.println("Operazione annullata");
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
        if(!isProdottoInOrdine(prodottoScelto)) {
            if (this.confermaOperazione()) {
                if (this.listinoGestito.eliminaProdotto(prodottoScelto) != null)
                    System.out.println("Prodotto eliminato");
                else System.out.println("Operazione fallita");
            } else System.out.println("Operazione annullato");
        } else System.out.println("Prodotto non eliminabile poichè presente in un ordine");
    }

    private boolean isProdottoInOrdine(ProdottoBar prodotto){
        for(OrdineBar ordine : this.associatedDBMS.ottieniListaOrdiniBar())
            if(ordine.getProdottiOrdinati().containsKey(prodotto))
                return true;
        return false;
    }

    private ProdottoBar sceltaProdottoBar(HashMap<ProdottoBar, Double> prezziBar) {
        System.out.println("Inserire il nome del prodotto che vuoi modificare");

        String nomeProdotto = this.sc.nextLine();

        for( ProdottoBar prodottoBar: prezziBar.keySet()){
            if(prodottoBar.getNomeProdotto().equals(nomeProdotto)) return prodottoBar;
        }

        System.out.println("Non hai selezionato nessun prodotto esistente");
        this.mostraListinoBar();
        return this.sceltaProdottoBar(prezziBar);
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
                System.out.println(currentProdottoBar.getNomeProdotto() + ": " + this.listinoGestito.getPrezziBar().get(currentProdottoBar) + "€");
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
        this.outputVistaSpiaggiaFasce(this.vistaSpiaggiaFasce(listaOmbrelloni));
        listinoGestito.mostraFasceEPrezzi();

        boolean flag;
        do {
            FasciaDiPrezzo fasciaDaModificare = this.selezioneFascia(prezziFascia);
            if (fasciaDaModificare == null) return;

            this.sceltaTipoModifiche(fasciaDaModificare,listaOmbrelloni);
            prezziFascia = this.listinoGestito.getPrezziFascia();

            this.outputVistaSpiaggiaFasce(this.vistaSpiaggiaFasce(listaOmbrelloni));
            listinoGestito.mostraFasceEPrezzi();

            System.out.println("Vuoi modificare altro? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);

        if(this.confermaOperazione()){
            if(this.associatedDBMS.aggiornaMappaFasce(this.listinoGestito.getPrezziFascia())){
                System.out.println("Operazione eseguita con successo");
            }
            else System.out.println("Operazioni fallita");
        }
        else System.out.println("Operazioni annullate");
    }

    public ArrayList<ArrayList<String>> vistaSpiaggiaFasce(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        ArrayList<ArrayList<String>> griglia = this.convertiAGrigliaVuota(listaOmbrelloni);

        for(FasciaDiPrezzo fascia : this.listinoGestito.getPrezziFascia().keySet()){
            this.inserisciFasciaInGriglia(fascia,griglia);
        }
        return griglia;
    }

    private void outputVistaSpiaggiaFasce(ArrayList<ArrayList<String>> griglia){
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
                if(yInizioFascia == yFineFascia){
                    if(yInizioFascia == y && x >= xInizioFascia && x <= xFineFascia){
                        griglia.get(y).set(x,fascia.getNome());
                        x++;
                        continue;
                    }
                }else{
                    if ((x >= xInizioFascia && y == yInizioFascia) || (y > yInizioFascia && y < yFineFascia) || (y == yFineFascia && x <= xFineFascia)) {
                        griglia.get(y).set(x, fascia.getNome());
                    }
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

    public void impostaPrezziOmbrellone() {
        this.ottieniListinoAggiornato();
        boolean working = true;
        while (working) {
            sceltaOperazione();
            System.out.println("Vuoi modificare altro? (y/n)");
            working = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");

        }

        if (this.confermaOperazione()) {
            this.associatedDBMS.aggiornaMappaTipologie(this.listinoGestito.getPrezziTipologia());
            this.associatedDBMS.aggiornaPrezziBase(this.listinoGestito.getPrezzoBaseOmbrellone(),this.listinoGestito.getPrezzoBaseLettino());
            System.out.println("Operazioni eseguite");
        }
        else System.out.println("Operazioni annullate");
    }

    private void sceltaOperazione() {
        boolean working = true;
        int answer;

        System.out.println("Scegliere il prezzo da modificare: ");
        System.out.println("[1]: Prezzo Base");
        System.out.println("[2]: Tipologie");
        System.out.println("[3]: Prezzo lettino");
        do {
            answer = this.provaScannerInt();
            if (answer == 1) {
                sceltaPrezzoBase();
                working = false;
            } else if (answer == 2) {
                sceltaPrezzoTipologie();
                working = false;
            } else if (answer == 3) {
                sceltaPrezzoLettino();
                working = false;
            } else {
                System.out.println("Reinserire (1, 2 o 3): ");
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

    private void sceltaPrezzoLettino() {
        double nuovoPrezzoLettino;
        System.out.println("Il precedente prezzo dei lettini è " + listinoGestito.getPrezzoBaseLettino() + ".");
        nuovoPrezzoLettino = sceltaPrezzo();
        listinoGestito.setPrezzoBaseLettino(nuovoPrezzoLettino);
        System.out.println("Il nuovo prezzo dei lettini è " + nuovoPrezzoLettino + ".\n");
    }

    private void sceltaPrezzoBase() {
        double nuovoPrezzoBase;
        System.out.println("Il precedente prezzo base degli ombrelloni è " + listinoGestito.getPrezzoBaseOmbrellone() + ".");
        nuovoPrezzoBase = sceltaPrezzo();
        listinoGestito.setPrezzoBaseOmbrellone(nuovoPrezzoBase);
        System.out.println("Il nuovo prezzo base degli ombrelloni è " + nuovoPrezzoBase + ".\n");
    }

    public double ottieniPrezzoBaseOmbrellone() {
        return this.listinoGestito.getPrezzoBaseOmbrellone();
    }

    public HashMap<FasciaDiPrezzo, Double> ottieniPrezziFasce() {
        return this.listinoGestito.getPrezziFascia();
    }

    private double sceltaPrezzo(){
        double nuovoPrezzo;
        do{
            System.out.println("Inserire il nuovo prezzo: ");
            nuovoPrezzo = this.provaScannerDouble();
        }while(isPrezzoNegativo(nuovoPrezzo));
        return nuovoPrezzo;
    }

    public HashMap<TipologiaOmbrellone, Double> ottieniPrezziTipologie() {
        this.listinoGestito.aggiornaTipologie(this.associatedDBMS.ottieniTipologie());
        return this.listinoGestito.getPrezziTipologia();
    }

    public void aggiungiNuovaTipologia(TipologiaOmbrellone tipologiaOmbrellone) {
        this.listinoGestito.aggiungiTipologia(tipologiaOmbrellone);
    }

    public Listino getListinoGestito() {
        return listinoGestito;
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
        this.associatedDBMS.aggiornaMappaTipologie(this.listinoGestito.getPrezziTipologia());
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
        this.outputVistaSpiaggiaFasce(this.vistaSpiaggiaFasce(listaOmbrelloni));

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
            listaOmbrelloni.get(coordinate.getyAxis()).get(coordinate.getxAxis());
        }catch (Exception e) {
            System.out.println("Le coordinate inserite non fanno parte della spiaggia, le operazioni verranno annullate ");
            return false;
        }
        return true;
    }

    private void eliminaFascia(FasciaDiPrezzo fasciaDaModificare) {
        if (this.confermaOperazione()) this.listinoGestito.eliminaFasciaDiPrezzo(fasciaDaModificare);
    }

    private Coordinate selezionaPosto() {
        System.out.println("Inserire la fila (y)");
        int fila = this.provaScannerInt();
        System.out.println("Inserire la colonna (x)");
        int colonna = this.provaScannerInt();
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

    public HashMap<ProdottoBar, Double> getPrezziBar() {
        this.listinoGestito.setPrezziBar(this.associatedDBMS.ottieniMappaProdottiBar());
        return this.listinoGestito.getPrezziBar();
    }

    private int provaScannerInt(){
        while(true){
            try{
                int intero = this.sc.nextInt();
                this.sc.nextLine();
                return intero;
            } catch (Exception e) {
                this.sc.nextLine();
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
                this.sc.nextLine();
                System.out.println("Cio' che hai inserito non e' un valore numerico, ritenta ");
            }
        }
    }

    public ArrayList<TipologiaOmbrellone> getTipologie() {
        return this.listinoGestito.getTipologie();
    }

    public double ottieniPrezzoLettino() {return this.listinoGestito.getPrezzoBaseLettino(); }

    public void ottieniListinoAggiornato() {
        this.listinoGestito.setPrezziBar(this.associatedDBMS.ottieniMappaProdottiBar());
        this.listinoGestito.setPrezziTipologia(this.associatedDBMS.ottieniTipologie());
        this.listinoGestito.setPrezziFascia(this.associatedDBMS.ottieniMappaFasce());
        this.listinoGestito.setPrezzoBaseOmbrellone(this.associatedDBMS.ottieniPrezzoBaseOmbrellone());
        this.listinoGestito.setPrezzoBaseLettino(this.associatedDBMS.ottieniPrezzoBaseLettino());
    }
}
