import java.util.ArrayList;
import java.util.Scanner;

public class HandlerSpiaggia {

    private final Spiaggia spiaggiaGestita;

    private final DBMSController associatedDBMS;

    private Ombrellone ombrelloneSelezionato;

    public HandlerSpiaggia(Spiaggia spiaggiaGestita, DBMSController associatedDBMS){
        this.associatedDBMS = associatedDBMS;
        this.spiaggiaGestita = spiaggiaGestita;
    }

    public void modificaOmbrellone(){
        ottieniVistaSpiaggia();
    }

    private void ottieniVistaSpiaggia(){
        ArrayList<ArrayList<Ombrellone>> vistaSpiaggiaCorrente = spiaggiaGestita.getListaOmbrelloni();
        int posizioneOmbrelloneCounter = 0;
        for(ArrayList<Ombrellone> currentRow : vistaSpiaggiaCorrente) {
            for (Ombrellone currentOmbrellone : currentRow) {
                System.out.println("Posizione numero: " + posizioneOmbrelloneCounter + " ");
                if (currentOmbrellone != null) {
                    System.out.println("Tipo: " + currentOmbrellone.getIdTipo() + " ");
                    System.out.println("Numero lettini associati: " + currentOmbrellone.getNumeroLettiniAssociati() + " ");
                    System.out.println("L'ombrellone è prenotato: " + currentOmbrellone.isBooked() + " ");
                }
                else System.out.println("Posizione vuota, nessun ombrellone piazzato");
            }
            posizioneOmbrelloneCounter++;
        }
    }

    public Ombrellone selezionaOmbrellone(int idOmbrellone){
        for(ArrayList<Ombrellone> currentRow : spiaggiaGestita.getListaOmbrelloni()) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if(currentOmbrellone.getIdOmbrellone() == idOmbrellone) {
                    ombrelloneSelezionato = currentOmbrellone;
                    return currentOmbrellone;
                }
            }
        }
        return null;
    }

    public void rimuoviOmbrellone(){
        for(ArrayList<Ombrellone> currentRow : spiaggiaGestita.getListaOmbrelloni()) {
            for (Ombrellone currentOmbrellone : currentRow) {
                if(currentOmbrellone.equals(ombrelloneSelezionato))
                    currentRow.remove(currentOmbrellone);
            }
        }
    }

    public void spostaOmbrellone(int riga, int colonna){
        if(spiaggiaGestita.isLocationOccupied(riga, colonna))
            spiaggiaGestita.scambiaOmbrelloni(spiaggiaGestita.getOmbrelloneAtLocation(riga, colonna), ombrelloneSelezionato);
        else spiaggiaGestita.spostaOmbrellone(ombrelloneSelezionato, riga, colonna);
    }

    public void modificaTipologiaOmbrellone(int idTipologia){

    }

    public boolean controlloTipologia(int idTipologia){
        return ombrelloneSelezionato.getIdTipo() == idTipologia;
    }

    public void aggiornaTipologiaOmbrellone(int idTipologia){
        spiaggiaGestita.aggiornaTipologiaOmbrellone(ombrelloneSelezionato, idTipologia);
    }


    public static void main(String[] args) {
        Spiaggia spiaggiaTest = new Spiaggia();
        DBMSController dbmsControllerTest = new DBMSController();
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsControllerTest);

    }

}
