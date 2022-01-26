package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class ServizioBar {

    private DBMSController associatedDBMSController;
    private ArrayList<OrdineBar> ordiniInSospeso;
    private Scanner sc = new Scanner(System.in);

/*    public ServizioBar(){
        this.associatedDBMSController = DBMSController.getInstance();
    }*/

    public void completaOrdinazione(){
        this.aggiornaListaOrdini(this.associatedDBMSController.ottieniListaOrdiniBar());
        this.printListaOrdiniInSospeso();
        int idOrdine = richiestaIdOrdine();
        System.out.println("Confermi di voler completare l'ordine con id " + idOrdine + "?" + " [y/n] ");
        if(Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y")) {
            this.ordineCompletato(idOrdine);
            this.printListaOrdiniInSospeso();
            this.associatedDBMSController.aggiornaListaOrdiniBar(this.ordiniInSospeso);
        }
    }

    private int richiestaIdOrdine(){
        do {
            System.out.println("Selezionare un ordine inserendo il suo id");
            int idOrdine = this.sc.nextInt();
            this.sc.nextLine();
            for (OrdineBar currentOrdine : this.ordiniInSospeso) {
                if (currentOrdine.getIdOrdine() == idOrdine)
                    return idOrdine;
            }
            System.out.println("L'ordine selezionato non è presente nella lista degli ordini in sospeso");
        } while(true);
    }

    private void ordineCompletato(int idOrdine) {
        ordiniInSospeso.removeIf(currentOrdine -> currentOrdine.getIdOrdine() == idOrdine);
    }

    private void printListaOrdiniInSospeso() {

        System.out.println("Ordini in sospeso");
        System.out.println("-----------------");

        for(OrdineBar currentOrdine : this.ordiniInSospeso){
            System.out.println("Cliente: " + currentOrdine.getIdCliente());
            System.out.println("Prodotti: ");
            currentOrdine.printProdottiOrdine();
            System.out.println("Ombrelloni cliente: ");
            currentOrdine.printOmbrelloniCliente();

        }
    }

    private void aggiornaListaOrdini(ArrayList<OrdineBar> ordini){
        this.ordiniInSospeso = ordini;
    }
}
