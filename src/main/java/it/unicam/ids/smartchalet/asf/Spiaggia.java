package it.unicam.ids.smartchalet.asf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Spiaggia {

    private final ArrayList<ArrayList<Ombrellone>> listaOmbrelloni;
    private int totaleOmbrelloni;

    public Spiaggia(){
        this.totaleOmbrelloni = 0;
        this.listaOmbrelloni = new ArrayList<>();
        for(int i=0;i<4;i++){
            ArrayList<Ombrellone> lista= new ArrayList<>();
            for(int j=0;j<4;j++){
                lista.add(null);
            }
            listaOmbrelloni.add(lista);
        }
    }

    public ArrayList<ArrayList<Ombrellone>> getListaOmbrelloni(){
        return this.listaOmbrelloni;
    }

    public int getTotaleOmbrelloni(){
        return this.totaleOmbrelloni;
    }

    public boolean isLocationOccupied(Coordinate coordinate) {
        return listaOmbrelloni.get(coordinate.getyAxis()).get(coordinate.getyAxis()) != null;
    }

    public Ombrellone getOmbrelloneAtLocation(Coordinate location) {
        return listaOmbrelloni.get(location.getyAxis()).get(location.getyAxis());
    }

    public void scambiaOmbrelloni(Ombrellone primoOmbrellone, Ombrellone secondoOmbrellone) {
        int filaPrimoOmbrellone = primoOmbrellone.getLocation().getxAxis();
        int colonnaPrimoOmbrellone = primoOmbrellone.getLocation().getyAxis();
        int filaSecondoOmbrellone = secondoOmbrellone.getLocation().getxAxis();
        int colonnaSecondoOmbrellone = secondoOmbrellone.getLocation().getyAxis();
        listaOmbrelloni.get(filaPrimoOmbrellone).set(colonnaPrimoOmbrellone, secondoOmbrellone);
        listaOmbrelloni.get(filaSecondoOmbrellone).set(colonnaSecondoOmbrellone, primoOmbrellone);
    }

    public void spostaOmbrellone(Ombrellone ombrellone, Coordinate nuoveCoordinate) {
        int filaOmbrellone = ombrellone.getLocation().getxAxis();
        int colonnaOmbrellone = ombrellone.getLocation().getyAxis();
        listaOmbrelloni.get(filaOmbrellone).set(colonnaOmbrellone, null);
        listaOmbrelloni.get(nuoveCoordinate.getxAxis()).set(nuoveCoordinate.getyAxis(), ombrellone);
        ombrellone.setLocation(nuoveCoordinate);
    }

    public void aggiornaTipologiaOmbrellone(Ombrellone ombrellone, int idTipologia) {
        int filaOmbrellone = ombrellone.getLocation().getxAxis();
        int colonnaOmbrellone = ombrellone.getLocation().getyAxis();
        listaOmbrelloni.get(filaOmbrellone).get(colonnaOmbrellone).setTipo(idTipologia);
    }

    public void rimuoviOmbrellone(Ombrellone ombrellone){
        Ombrellone ombrelloneDaRimuovere;
        Ombrellone currentOmbrellone = null;
        for(ArrayList<Ombrellone> currentRow : listaOmbrelloni) {
            Iterator<Ombrellone>iter = currentRow.iterator();
            while(iter.hasNext()) {
                currentOmbrellone = iter.next();
                if(currentOmbrellone != null) {
                    if (currentOmbrellone.equals(ombrellone))
                        if (!currentOmbrellone.isBooked()) {
                            iter.remove();
                            totaleOmbrelloni--;
                        } else System.out.println("Ombrellone prenotato non rimuovibile");
                }
            }
        }
    }

    public void aggiungiOmbrellone(Ombrellone ombrellone){
        int coordinataX = ombrellone.getLocation().getxAxis();
        int coordinataY = ombrellone.getLocation().getyAxis();
        this.listaOmbrelloni.get(coordinataY).set(coordinataX,ombrellone);
        totaleOmbrelloni++;
    }

    public Ombrellone getOmbrellone(int idOmbrellone) {
        for(ArrayList<Ombrellone> currentRow: listaOmbrelloni)
            for(Ombrellone ombrelloneCorrente : currentRow)
                if(ombrelloneCorrente.getIdOmbrellone() == idOmbrellone)
                    return ombrelloneCorrente;
                return null;
    }
}
