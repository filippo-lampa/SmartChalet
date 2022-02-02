package it.unicam.ids.smartchalet.asf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class ServizioBar {

    private DBMSController associatedDBMSController;
    private ArrayList<OrdineBar> ordiniInSospeso;
    private Scanner sc = new Scanner(System.in);
    private static ServizioBar instance = null;

    private ServizioBar(){
        this.associatedDBMSController = DBMSController.getInstance();
    }

    public static ServizioBar getInstance(){
        if (instance == null) {
            instance = new ServizioBar();
        }
        return instance;
    }

    public void completaOrdinazione(){
        this.aggiornaListaOrdini(this.associatedDBMSController.ottieniListaOrdiniBar());
        boolean flag;
        ArrayList<Integer> ordiniCompletati = new ArrayList<>();
        do {
            this.printListaOrdiniInSospeso();
            int idOrdine = richiestaIdOrdine();
            System.out.println("Confermi di voler completare l'ordine con id " + idOrdine + "?" + " [y/n] ");
            if(Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y")) {
                ordiniCompletati.add(idOrdine);
                this.ordineCompletato(idOrdine);
                this.printListaOrdiniInSospeso();
            }
            System.out.println("Vuoi aggiungere o rimuovere altri prodotti dal carrello? [y/n] ");
            flag = Objects.equals(this.sc.nextLine().trim().toLowerCase(Locale.ROOT), "y");
        } while (flag);
        this.associatedDBMSController.aggiornaListaOrdiniBar(ordiniCompletati);

    }

    private int richiestaIdOrdine(){
        do {
            System.out.println("Selezionare un ordine inserendo il suo id");
            int idOrdine = this.provaScannerInt();
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
            System.out.println("Id ordine: " + currentOrdine.getIdOrdine());
            System.out.println("Cliente: " + currentOrdine.getIdCliente());
            System.out.println("Prodotti: \t");
            currentOrdine.printProdottiOrdine();
        }
    }

    private void aggiornaListaOrdini(ArrayList<OrdineBar> ordini){
        this.ordiniInSospeso = ordini;
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
}
