package it.unicam.cs.ids2021;

import java.util.*;

public class HandlerListino {

    private final Listino listinoGestito;
    private final DBMSController dbmsController;
    private final Spiaggia spiaggia;
    private final Scanner sc;

    public HandlerListino(DBMSController dbmsController , Listino listino, Spiaggia spiaggia){
        this.listinoGestito = listino;
        this.dbmsController = dbmsController;
        this.spiaggia = spiaggia;
        this.sc = new Scanner(System.in);
    }

    public void aggiungiFasciaDiPrezzo(){

    }

    public void impostaPrezziOmbrellone(){

    }

    public void aggiungiProdottoBar(){

    }

    public void modificaProdottoBar(){

    }

    public void modificaFasciaDiPrezzo(){
        this.dbmsController.ottieniVistaSpiaggia();
        this.spiaggia.aggiornaSpiaggia();
        this.dbmsController.ottieniListino();
        this.listinoGestito.aggiornaListino();
        HashMap<FasciaDiPrezzo, Double> fasce = this.listinoGestito.getPrezziFascia();
        if(fasce==null){
            System.out.println("Non ci sono fasce al momento");
            return;
        }
        else System.out.println(fasce.toString());

        boolean flag;
        do {
            FasciaDiPrezzo fasciaDaModificare = this.selezioneFascia(fasce);
            if(fasciaDaModificare == null) return;

            this.sceltaTipoModifiche(fasciaDaModificare);

            fasce = this.listinoGestito.getPrezziFascia();
            System.out.println(fasce.toString());
            System.out.println("Vuoi modificare altro? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
        }while(flag);

        if(this.confermaOperazione()) System.out.println("Operazioni eseguite"); //TODO sostituire output con metodo legato al database
        else System.out.println("Operazioni annullate");    //modificare il caso d'uso e diagramma se vogliamo messaggio output
    }

    private FasciaDiPrezzo selezioneFascia(HashMap<FasciaDiPrezzo, Double> fasce){
        if(fasce.isEmpty()) {
            System.out.println("Non ci sono fasce al momento");
            return null;
        }
        FasciaDiPrezzo fasciaDaModificare = null;
        boolean flag = true;
        while(flag){
            System.out.println("Scegli una fascia di prezzo da modificare: [nome]");
            String app = sc.next();
            for (FasciaDiPrezzo fasciaAttuale : fasce.keySet()) {
                if (fasciaAttuale.getNome().equals(app)) {
                    fasciaDaModificare = fasciaAttuale;
                }
            }
            if(fasciaDaModificare == null){
                System.out.println("Non hai selezionato una fascia esistente, ritenta");
                continue;
            }

            flag = false;
        }
        return fasciaDaModificare;
    }

    private void sceltaTipoModifiche(FasciaDiPrezzo fasciaDaModificare){
        System.out.println("Scegli il tipo di modifica da effettuare: ");
        System.out.println("1 : Modifica prezzo fascia \n2 : Modifica locazione fascia \n3 : Elimina fascia  ");
        boolean flag = true;
        while(flag){
            int scelta = sc.nextInt();
            sc.nextLine();
            if(scelta == 1){
                this.modificaPrezzo(fasciaDaModificare);
            }
            else if(scelta == 2){
                this.modificaLocazione(fasciaDaModificare);
            }
            else if(scelta == 3){
                this.eliminaFascia(fasciaDaModificare);
            }
            else {
                System.out.println(" Non hai selezionato una scelta esistente, ritenta ");
                continue;
            }
            flag = false;
        }
    }

    private void modificaPrezzo(FasciaDiPrezzo fasciaDaModificare){
        System.out.println("Inserisci il nuovo prezzo della fascia: ");
        double nuovoPrezzo = sc.nextDouble();
        sc.nextLine();
        this.listinoGestito.modificaPrezzoFascia(fasciaDaModificare,nuovoPrezzo);
    }

    private void modificaLocazione(FasciaDiPrezzo fasciaDaModificare){
        //TODO inserire output con fasce (sfruttare la griglia spiaggia quando verrà implementata)

        FasciaDiPrezzo fasciaTemporanea = new FasciaDiPrezzo("Temporanea");
        System.out.println("Inserisci la nuova prima locazione");
        fasciaTemporanea.setCoordinateInizio(this.selezionaPosto());
        System.out.println("Inserisci la nuova ultima locazione");
        fasciaTemporanea.setCoordinateFine(this.selezionaPosto());

        if(controlloLocazioni(fasciaDaModificare,fasciaTemporanea)) this.listinoGestito.modificaLocazioniFascia(fasciaDaModificare,fasciaTemporanea);
        else System.out.println("La fascia con le nuove locazioni si sovrappone ad un'altra, l'operazione non verrà eseguita");
    }

    private void eliminaFascia(FasciaDiPrezzo fasciaDaModificare){
        if(this.confermaOperazione()) this.listinoGestito.eliminaFasciaDiPrezzo(fasciaDaModificare);
    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

    private Coordinate selezionaPosto(){
        System.out.println("Inserire la fila (y)");
        int fila = this.sc.nextInt();
        sc.nextLine();
        System.out.println("Inserire la colonna (x)");
        int colonna = this.sc.nextInt();
        sc.nextLine();
        return new Coordinate(colonna, fila);
    }

    private boolean controlloLocazioni(FasciaDiPrezzo fasciaDaModificare, FasciaDiPrezzo fasciaTemporanea){

        for ( FasciaDiPrezzo fasciaAttuale : this.listinoGestito.getPrezziFascia().keySet()) {
            if(fasciaAttuale == fasciaDaModificare) continue;
            int yPrimoTemporanea = fasciaTemporanea.getCoordinateInizio().getyAxis();
            int xPrimoTemporanea = fasciaTemporanea.getCoordinateInizio().getxAxis();
            int yUltimoTemporanea = fasciaTemporanea.getCoordinateFine().getyAxis();
            int xUltimoTemporanea = fasciaTemporanea.getCoordinateFine().getxAxis();
            int yPrimoAttuale = fasciaAttuale.getCoordinateInizio().getyAxis();
            int xPrimoAttuale = fasciaAttuale.getCoordinateInizio().getxAxis();
            int yUltimoAttuale = fasciaAttuale.getCoordinateFine().getyAxis();
            int xUltimoAttuale = fasciaAttuale.getCoordinateFine().getxAxis();

            if(yPrimoTemporanea > yUltimoAttuale || (yPrimoTemporanea == yUltimoAttuale && xPrimoTemporanea > xUltimoAttuale) ||
                    yUltimoTemporanea < yPrimoAttuale || (yUltimoTemporanea == yPrimoAttuale && xUltimoTemporanea < xPrimoAttuale)){
                continue;
            }
            return false;
        }
        return true;
    }
}
