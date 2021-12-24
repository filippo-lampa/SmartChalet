package it.unicam.ids.smartchalet.asf;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class HandlerListino {

    private Listino listinoGestito;

    private final DBMSController associatedDBMS;

    private Scanner sc;

    public HandlerListino(Listino listinoGestito, DBMSController associatedDBMS){
        sc = new Scanner(System.in);
        this.listinoGestito = listinoGestito;
        this.associatedDBMS = associatedDBMS;
    }

    public void aggiungiProdottoBar(){
        this.listinoGestito.aggiornaListino(this.associatedDBMS.ottieniListinoAggiornato().ottieniListinoBar());
        boolean flag;
        do{
            System.out.println("Inserire nome prodotto");
            String nomeProdotto = sc.nextLine();
            System.out.println("Inserire descrizione prodotto");
            String descrizioneProdotto = sc.nextLine();
            System.out.println("Inserire prezzo prodotto");
            Double prezzoProdotto = sc.nextDouble();
            ProdottoBar nuovoProdottoBar = new ProdottoBar(descrizioneProdotto, nomeProdotto);
            if(!this.listinoGestito.controlloProdottoEsistente(nuovoProdottoBar)){
                this.listinoGestito.aggiungiAllaListaProdotti(nuovoProdottoBar, prezzoProdotto);
            }else{
                System.out.println("Il prodotto non può essere aggiunto poichè già presente nel listino");
            }
            System.out.println("Vuoi aggiungere altri prodotti? [y/n] ");
            flag = Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
            sc.nextLine();
        }while(flag);
        if(this.confermaOperazione()){
            HashMap<ProdottoBar, Double> listinoBarAggiornato = this.listinoGestito.ottieniListinoBar();
            this.associatedDBMS.aggiungiProdottiBar(listinoBarAggiornato);
            mostraListinoBar();
        }
    }

    private boolean confermaOperazione(){
        System.out.println("Confermi l'operazione? [y/n] ");
        return Objects.equals(this.sc.next().trim().toLowerCase(Locale.ROOT), "y");
    }

    public void mostraListinoBar(){
        for(ProdottoBar currentProdottoBar : this.listinoGestito.ottieniListinoBar().keySet()){
            System.out.println(currentProdottoBar.getNomeProdotto() + ": " + this.listinoGestito.ottieniListinoBar().get(currentProdottoBar) + "€");
        }
    }
}
