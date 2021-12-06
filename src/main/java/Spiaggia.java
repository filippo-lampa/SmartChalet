import java.lang.reflect.Array;
import java.util.ArrayList;

public class Spiaggia {

    private final ArrayList<ArrayList<Ombrellone>> listaOmbrelloni;
    private int totaleOmbrelloni;

    public Spiaggia(){
        this.totaleOmbrelloni = 0;
        this.listaOmbrelloni = new ArrayList<ArrayList<Ombrellone>>();
    }

    public ArrayList<ArrayList<Ombrellone>> getListaOmbrelloni(){
        return this.listaOmbrelloni;
    }

    public int getTotaleOmbrelloni(){
        return this.totaleOmbrelloni;
    }

    public boolean isLocationOccupied(int riga, int colonna) {
        return listaOmbrelloni.get(riga).get(colonna) == null;
    }

    public Ombrellone getOmbrelloneAtLocation(int riga, int colonna) {
        return listaOmbrelloni.get(riga).get(colonna);
    }

    public ArrayList<Integer> getLocationOmbrellone(Ombrellone ombrellone){
        int rowCounter = 0;
        int columnCounter = 0;
        ArrayList<Integer> locationOmbrellone = new ArrayList<>();
        for(ArrayList<Ombrellone> currentRow : listaOmbrelloni) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if (currentOmbrellone.equals(ombrellone)) {
                    locationOmbrellone.add(rowCounter);
                    locationOmbrellone.add(columnCounter);
                }
                columnCounter++;
            }
            rowCounter++;
        }
        return locationOmbrellone;
    }

    public void scambiaOmbrelloni(Ombrellone primoOmbrellone, Ombrellone secondoOmbrellone) {
        int filaPrimoOmbrellone, filaSecondoOmbrellone, colonnaPrimoOmbrellone, colonnaSecondoOmbrellone, rowCounter, columnCounter;
        filaPrimoOmbrellone = filaSecondoOmbrellone = colonnaPrimoOmbrellone = colonnaSecondoOmbrellone = rowCounter = columnCounter = 0;
        for(ArrayList<Ombrellone> currentRow : listaOmbrelloni) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if (currentOmbrellone.equals(primoOmbrellone)) {
                    filaPrimoOmbrellone = rowCounter;
                    colonnaPrimoOmbrellone = columnCounter;
                }
                if (currentOmbrellone.equals(secondoOmbrellone)) {
                    filaSecondoOmbrellone = rowCounter;
                    colonnaSecondoOmbrellone = columnCounter;
                }
                columnCounter++;
            }
            rowCounter++;
        }
        listaOmbrelloni.get(filaPrimoOmbrellone).set(colonnaPrimoOmbrellone, secondoOmbrellone);
        listaOmbrelloni.get(filaSecondoOmbrellone).set(colonnaSecondoOmbrellone, primoOmbrellone);
    }

    public void spostaOmbrellone(Ombrellone ombrellone, int riga, int colonna) {
        int rowCounter = 0;
        int columnCounter = 0;
        for(ArrayList<Ombrellone> currentRow : listaOmbrelloni) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if (currentOmbrellone.equals(ombrellone)) {
                    listaOmbrelloni.get(rowCounter).set(columnCounter, null);
                }
                columnCounter++;
            }
            rowCounter++;
        }
        listaOmbrelloni.get(riga).set(colonna, ombrellone);
    }

    public void aggiornaTipologiaOmbrellone(Ombrellone ombrellone, int idTipologia) {
        int rowCounter = 0;
        int columnCounter = 0;
        for(ArrayList<Ombrellone> currentRow : listaOmbrelloni) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if (currentOmbrellone.equals(ombrellone)) {
                    listaOmbrelloni.get(rowCounter).get(columnCounter).setTipo(idTipologia);
                }
                columnCounter++;
            }
            rowCounter++;
        }
    }


}
