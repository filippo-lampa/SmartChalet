package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;

public class DBMSController {

    private Statement stmt = null;

    /**
     * Costruttore che inizializza un ControllerDB
     *
     */
   /* public DBMSController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/prova", "root", "");
            stmt = conn.createStatement();
            System.out.println("Database is connected !");
        }catch (SQLException e){
            System.out.print("Do not connect to DB - Error:"+e);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Esegue una query che prevede un output
     *
     * @param query query da eseguire
     * @return un {@link ResultSet} contenente i risultati della query
     */
    ResultSet queryWithOutput(String query){
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Esegue una query che non prevede un output
     *
     * @param query query da eseguire
     * @return true se la query &egrave; andata a buon fine, false altrimenti
     */
    boolean queryWithoutOutput(String query){
        try {
            return stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Chiude la connessione al database
     *
     * @return true se la chiusura della connessione &egrave; andata a buon fine, false altrimenti
     */
    boolean closeConnection(){
        try {
            stmt.getConnection().close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<ArrayList<Ombrellone>> ottieniVistaSpiaggia() {
        return null;
    }

    public ArrayList<Prenotazione> richiestaListaPrenotazioni() {
        return null;
    }

    public void aggiungiGrigliaSpiaggia(ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia) {
        System.out.println("Nuova griglia spiaggia aggiunta al database");
    }

    public void ottieniListinoAggiornato() {

    }

    public void aggiungiProdottiBar(HashMap<ProdottoBar,Double> listinoBarAggiornato) {
        System.out.println("Nuovi prodotti bar aggiunti al database");
    }


    public HashMap<Attrezzatura, Integer> ottieniAttrezzatureAggiornate() {
        return null;
    }

    public HashMap<Attrezzatura, Integer> ottieniMappaAttrezzature(){
        Attrezzatura sdraio = new Attrezzatura("Sdraio", "Roba su cui ci si sdraia");
        Attrezzatura posacenere = new Attrezzatura("Posacenere", "Roba in cui si cicca");

        HashMap<Attrezzatura, Integer> listaAttrezzaturaTest = new HashMap<>();
        listaAttrezzaturaTest.put(sdraio, 10);
        listaAttrezzaturaTest.put(posacenere, 5);

        return listaAttrezzaturaTest;

    }

    public void aggiornaMappaAttrezzature(HashMap<Attrezzatura, Integer> mappaAttrezzatura) {
    }

    public ArrayList<Attivita> ottieniListaAttivita() {
        return null;
    }
}
