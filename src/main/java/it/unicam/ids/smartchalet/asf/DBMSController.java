package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;

public class DBMSController {

    private Statement stmt = null;

    private String url = "jdbc:postgresql://localhost/SmartChalet";
    private String user = "Filippo";
    private String password = "smartchaletasf";
    private Connection conn = null;
    private static DBMSController instance = null;

    /**
     * Costruttore che inizializza un ControllerDB
     *
     */
    private DBMSController() {
    }

    public void setDBMSController(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DBMSController getInstance(){
        if(instance == null){
            instance = new DBMSController();
        }
        return instance;
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    private void connect() {
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            System.out.println("Where is your PostgreSQL JDBC Driver? Include it in your library path");
            e.printStackTrace();
        }
        try {
            this.conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println("Problems in operning a connection to the DB");
            e.printStackTrace();
        }
    }

    public boolean DBTest(){
        Boolean result = true;
        try{
            if(conn == null || !conn.isClosed()){
                this.connect();
                result = false;
            }
            DatabaseMetaData data = conn.getMetaData();
            System.out.println("Details on DBMS: - " + data.getDatabaseProductName() + "\n" + " version: " + data.getDatabaseProductVersion()
            + " schemas: " + data.getSchemas().getRow() + "\n");
            this.closeConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Chiude la connessione al database
     *
     * @return true se la chiusura della connessione &egrave; andata a buon fine, false altrimenti
     */
    private void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Problems in closing the connection to the DB");
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Ombrellone>> ottieniVistaSpiaggia() {
        return null;
    }

    public ArrayList<Prenotazione> richiestaListaPrenotazioni() {
        String SQL = "SELECT * FROM prenotazione";
        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
        Prenotazione prenotazione;
        int i;
        try (Connection conn = DriverManager.getConnection(url, user, password);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);) {
            while (rs.next()) {
                //i = 1;
                prenotazione = new Prenotazione(rs.getDate(3), rs.getDate(4), rs.getInt(1));
                prenotazioni.add(prenotazione);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return prenotazioni;
    }

    public boolean aggiungiGrigliaSpiaggia(ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia) {
        System.out.println("Nuova griglia spiaggia aggiunta al database");
        return true;
    }

    public boolean aggiornaMappaProdottoBar(HashMap<ProdottoBar, Double> listinoBarAggiornato) {
        return true;
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


    public static void main(String[] args) {
        DBMSController controller = new DBMSController();
        controller.DBTest();
        for(Prenotazione prenotazione : controller.richiestaListaPrenotazioni()) {
            System.out.println(prenotazione.getIdCliente());
            System.out.println(prenotazione.getDataInizio());
            System.out.println(prenotazione.getDataFine());
        }
    }

    public HashMap<ProdottoBar, Double> ottieniMappaProdottiBar() {
        return null;
    }

    public void aggiungiOrdineBar(OrdineBar ordineBar) {
    }

    public ArrayList<OrdineBar> ottieniListaOrdiniBar() {
        return null;
    }

    public void aggiornaListaOrdiniBar(ArrayList<OrdineBar> ordiniInSospeso) {
    }

    public boolean aggiornaListaAttivita(ArrayList<Attivita> ottieniListaAttivitaDisponibili) {
        return true;
    }

    public void rimuoviPrenotazioni(ArrayList<Integer> listaPrenotazioniDaRimuovere) {
    }
}
