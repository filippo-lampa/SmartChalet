package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu {

    private ArrayList<ProdottoBar> listaProdotti;
    private HandlerListino handlerListinoAssociato;

    public Menu(HandlerListino handlerListinoAssociato){
        this.handlerListinoAssociato = handlerListinoAssociato;
    }

    public HashMap<ProdottoBar,Double> ottieniProdottiEPrezzi(){
        return this.handlerListinoAssociato.getPrezziBar();
    }
}
