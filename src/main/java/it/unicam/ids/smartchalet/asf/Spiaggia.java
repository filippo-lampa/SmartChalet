package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.util.Iterator;

public class Spiaggia {

    private ArrayList<ArrayList<Ombrellone>> listaOmbrelloni;
    private int totaleOmbrelloni;

    public Spiaggia(){
        this.totaleOmbrelloni = 0;
        this.listaOmbrelloni = new ArrayList<>();
        /*for(int i=0;i<4;i++){
            ArrayList<Ombrellone> lista= new ArrayList<>();
            for(int j=0;j<4;j++){
                lista.add(null);
            }
            listaOmbrelloni.add(lista);
        }*/

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

    public void aggiornaTipologiaOmbrellone(Ombrellone ombrellone, String tipologia) {
        int filaOmbrellone = ombrellone.getLocation().getxAxis();
        int colonnaOmbrellone = ombrellone.getLocation().getyAxis();
        listaOmbrelloni.get(filaOmbrellone).get(colonnaOmbrellone).setTipo(tipologia);
    }

    public void rimuoviOmbrellone(Ombrellone ombrellone){
        Ombrellone ombrelloneDaRimuovere;
        Ombrellone currentOmbrellone;
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
        if(coordinataY < this.listaOmbrelloni.size() && coordinataX < this.listaOmbrelloni.get(coordinataY).size() && coordinataX >= 0 && coordinataY >= 0) {
            this.listaOmbrelloni.get(coordinataY).set(coordinataX, ombrellone);
            totaleOmbrelloni++;
        }
        else System.out.println("Le coordinate inserite non rientrano nei limiti della griglia spiaggia, la scelta viene annullata");
    }

    public Ombrellone getOmbrellone(int idOmbrellone) {
        for(ArrayList<Ombrellone> currentRow: listaOmbrelloni)
            for(Ombrellone ombrelloneCorrente : currentRow)
                if(ombrelloneCorrente == null)
                    continue;
                else{
                    if(ombrelloneCorrente.getIdOmbrellone() == idOmbrellone)
                        return ombrelloneCorrente;
                }
        return null;
    }


    public ArrayList<Coordinate> ottieniPostiSenzaOmbrelloni(){

        ArrayList<Coordinate> coordinate = new ArrayList<>();
        int x = 0;
        int y = 0;

        for (ArrayList<Ombrellone> riga : this.getListaOmbrelloni()) {
            for (Ombrellone ombrellone : riga) {
                if(ombrellone == null){
                    coordinate.add(new Coordinate(x,y));
                }
                else coordinate.add(null);
                x++;
            }
            x=0;
            y++;
        }

        return coordinate;
    }

    public void aggiungiGrigliaSpiaggia(ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia) {
        this.listaOmbrelloni = grigliaSpiaggia;
    }

    public int getNewIdOmbrellone(){
        int highestId = -1;
        for(ArrayList<Ombrellone> riga : this.listaOmbrelloni)
            for(Ombrellone currentOmbrellone : riga){
                if(currentOmbrellone == null) {
                  continue;
                }
                if(currentOmbrellone.getIdOmbrellone() > highestId){
                    highestId = currentOmbrellone.getIdOmbrellone();
                }
            }
        return highestId + 1;
    }

    public boolean controlloEsistenzaOmbrellone(int idOmbrellone){
        for(ArrayList<Ombrellone> fila : this.listaOmbrelloni){
            for(Ombrellone ombrellone : fila){
                if(ombrellone != null)
                    if(ombrellone.getIdOmbrellone() == idOmbrellone)
                        return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.listaOmbrelloni.size(); i++) {
            for (int j = 0; j < this.listaOmbrelloni.get(i).size(); j++) {
                str.append(this.listaOmbrelloni.get(i).get(j)).append("\t");
            }
            str.append("\n");
        }
        return str.toString();
    }

}
