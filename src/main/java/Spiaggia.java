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

    public boolean isLocationOccupied(Coordinate coordinate) {
        return listaOmbrelloni.get(coordinate.getyAxis()).get(coordinate.getyAxis()) == null;
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
        for(ArrayList<Ombrellone> currentRow : listaOmbrelloni) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if(currentOmbrellone.equals(ombrellone))
                    if(!currentOmbrellone.isBooked())
                        currentRow.remove(currentOmbrellone);
                    else System.out.println("Ombrellone prenotato non rimuovibile");
            }
        }
    }

}
