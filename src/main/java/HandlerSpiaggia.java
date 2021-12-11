import java.util.ArrayList;
import java.util.Scanner;

public class HandlerSpiaggia {

    private final Spiaggia spiaggiaGestita;

    private final DBMSController associatedDBMS;

    private Ombrellone ombrelloneSelezionato;

    private Scanner sc;

    public HandlerSpiaggia(Spiaggia spiaggiaGestita, DBMSController associatedDBMS){
        sc = new Scanner(System.in);
        this.associatedDBMS = associatedDBMS;
        this.spiaggiaGestita = spiaggiaGestita;
    }

    public void modificaOmbrellone(){
        //associatedDBMS.ottieniVistaSpiaggia();
        ottieniVistaSpiaggia();
        boolean flag = true;
        while(flag){
            System.out.println("Inserire l'id dell'ombrellone da modificare");
            int idOmbrellone = sc.nextInt();
            selezionaOmbrellone(idOmbrellone);
            System.out.println("Digitare 1 per rimuovere l'ombrellone, 2 per spostare l'ombrellone o 3 per modificare la tipologia dell'ombrellone");
            int op = sc.nextInt();
            if(op == 1){
                rimuoviOmbrellone();
            }
            if(op == 2){
                Coordinate nuovaPosizione = selezionaPosto();
                spostaOmbrellone(nuovaPosizione.xAxis, nuovaPosizione.yAxis);
            }
            if(op == 3){
                modificaTipologiaOmbrellone();
            }
            System.out.println("Modificare altri ombrelloni? y/n" );
            String response = sc.next();
            flag = response.equals("y");
        }
        ottieniVistaSpiaggia();
    }

    private Coordinate selezionaPosto(){
        System.out.println("Inserire la fila in cui spostare l'ombrellone");
        int fila = sc.nextInt();
        System.out.println("Inserire la colonna in cui spostare l'ombrellone");
        int colonna = sc.nextInt();
        return new Coordinate(fila, colonna);
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

    private Ombrellone selezionaOmbrellone(int idOmbrellone){
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

    private void rimuoviOmbrellone(){
        System.out.println("Confermare di voler rimuovere l'ombrellone? true/false");
        boolean opConfermata = sc.nextBoolean();
        if(confermaOperazione(opConfermata))
            spiaggiaGestita.rimuoviOmbrellone(ombrelloneSelezionato);
    }
    private Coordinate selezionaPosto(int riga, int colonna){
        return new Coordinate(riga, colonna);
    }

    private void spostaOmbrellone(int riga, int colonna){
        System.out.println("Confermare di voler spostare l'ombrellone? true/false");
        boolean opConfermata = sc.nextBoolean();
        Coordinate nuoveCoordinate = new Coordinate(riga, colonna);
        if(confermaOperazione(opConfermata)) {
            if (spiaggiaGestita.isLocationOccupied(nuoveCoordinate))
                spiaggiaGestita.scambiaOmbrelloni(spiaggiaGestita.getOmbrelloneAtLocation(nuoveCoordinate), ombrelloneSelezionato);
            else spiaggiaGestita.spostaOmbrellone(ombrelloneSelezionato, nuoveCoordinate);
            System.out.println("Ombrellone spostato");
        }
    }

    private void modificaTipologiaOmbrellone(){
        ArrayList<Integer> listaTipologie = new ArrayList<>();
        ArrayList<Integer> listaTipologieDisponibili;
        if(ombrelloneSelezionato.isBooked())
             listaTipologieDisponibili = controlloTipologia(ombrelloneSelezionato.getIdTipo());
        else listaTipologieDisponibili = listaTipologie;
        for(int i=0; i<listaTipologie.size(); i++)
            System.out.println(listaTipologie.get(i));
        System.out.println("Inserire la nuova tipologia dell'ombrellone");
        int idTipologia = sc.nextInt();
        aggiornaTipologiaOmbrellone(idTipologia);
    }

    private ArrayList<Integer> controlloTipologia(int idTipologia){
        return null;
    }

    private void aggiornaTipologiaOmbrellone(int idTipologia){
        System.out.println("Confermare di voler aggiornare la tipologia dell'ombrellone? true/false");
        boolean opConfermata = sc.nextBoolean();
        if(confermaOperazione(opConfermata))
            spiaggiaGestita.aggiornaTipologiaOmbrellone(ombrelloneSelezionato, idTipologia);
    }

    private boolean confermaOperazione (boolean operazioneConfermata){
        return operazioneConfermata;
    }

    public static void main(String[] args) {
        Spiaggia spiaggiaTest = new Spiaggia();
        DBMSController dbmsControllerTest = new DBMSController();
        HandlerSpiaggia handlerSpiaggiaTest = new HandlerSpiaggia(spiaggiaTest, dbmsControllerTest);
        handlerSpiaggiaTest.modificaOmbrellone();
    }

}
