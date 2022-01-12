package it.unicam.ids.smartchalet.asf;

import java.util.*;
import java.sql.*;
import java.util.Date;

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
        ArrayList<ArrayList<Ombrellone>> matriceOmbrelloni = new ArrayList<ArrayList<Ombrellone>>();

        Coordinate coordinate1 = new Coordinate(0,0);
        Ombrellone ombrellone1 = new Ombrellone("Uno", coordinate1, 1);

        Coordinate coordinate2 = new Coordinate(0,1);
        Ombrellone ombrellone2 = new Ombrellone("Uno", coordinate2, 2);

        Coordinate coordinate3 = new Coordinate(1,0);
        Ombrellone ombrellone3 = new Ombrellone("Uno", coordinate3, 3);

        Coordinate coordinate4 = new Coordinate(1,1);
        Ombrellone ombrellone4 = new Ombrellone("Uno", coordinate4, 4);

        ArrayList<Ombrellone> primaFila = new ArrayList<Ombrellone>();
        primaFila.add(ombrellone1);
        primaFila.add(ombrellone2);
        matriceOmbrelloni.add(primaFila);

        ArrayList<Ombrellone> secondaFila = new ArrayList<Ombrellone>();
        secondaFila.add(ombrellone3);
        secondaFila.add(null);
        matriceOmbrelloni.add(secondaFila);

        return matriceOmbrelloni;
    }

    public ArrayList<Prenotazione> richiestaListaPrenotazioni() {
        ArrayList<Prenotazione> listaPrenotazioneTest = new ArrayList<Prenotazione>();
        Prenotazione prenotazioneTest1 = new Prenotazione();
        Prenotazione prenotazioneTest2 = new Prenotazione();

        Date dataStart1 = new GregorianCalendar(2022, Calendar.FEBRUARY, 10).getTime();
        Date dataEnd1 = new GregorianCalendar(2022, Calendar.FEBRUARY, 12).getTime();
        int fasciaOraria1 = 3;

        prenotazioneTest1.setId(0);
        prenotazioneTest1.setDataInizio(dataStart1);
        prenotazioneTest1.setDataFine(dataEnd1);
        prenotazioneTest1.setFasciaOraria(fasciaOraria1);
        prenotazioneTest1.setNumeroLettini(1);

        Date dataStart2 = new GregorianCalendar(2022, Calendar.FEBRUARY, 7).getTime();
        Date dataEnd2= new GregorianCalendar(2022, Calendar.FEBRUARY, 14).getTime();
        int fasciaOraria2 = 1;

        prenotazioneTest2.setId(1);
        prenotazioneTest2.setDataInizio(dataStart2);
        prenotazioneTest2.setDataFine(dataEnd2);
        prenotazioneTest2.setFasciaOraria(fasciaOraria2);
        prenotazioneTest2.setNumeroLettini(2);

        listaPrenotazioneTest.add(prenotazioneTest1);
        listaPrenotazioneTest.add(prenotazioneTest2);

        return listaPrenotazioneTest;
    }

    public void aggiugniGrigliaSpiaggia(ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia) {
        System.out.println("Nuova griglia spiaggia aggiunta al database");
    }

    public Listino ottieniListinoAggiornato() {
        Listino listino = new Listino();

        String nomeFascia = "FasciaTest";
        Coordinate coordinateInizio = new Coordinate(0,0);
        Coordinate coordinateFine = new Coordinate(1,1);
        double fasciaPrezzo;

        FasciaDiPrezzo fasciaTest = new FasciaDiPrezzo(nomeFascia, coordinateInizio, coordinateFine);


        HashMap<FasciaDiPrezzo, Double> prezziFascia = new HashMap<>() {{
            put(fasciaTest, 10.0);
        }};
        listino.setPrezziFascia(prezziFascia);

        TipologiaOmbrellone tipoTest = new TipologiaOmbrellone("tipologiaTest", "descrizioneTest");

        HashMap<TipologiaOmbrellone, Double> prezziTipologia = new HashMap<>() {{
            put(tipoTest, 10.0);
        }};
        listino.setPrezziTipologia(prezziTipologia);

        return listino;
    }

    public void aggiungiProdottiBar(HashMap<ProdottoBar,Double> listinoBarAggiornato) {
        System.out.println("Nuovi prodotti bar aggiunti al database");
    }

    public HashMap<Attrezzatura, Integer> ottieniListaAttrezzature(){
        Attrezzatura sdraio = new Attrezzatura("Sdraio", "Roba su cui ci si sdraia");
        Attrezzatura posacenere = new Attrezzatura("Posacenere", "Roba in cui si cicca");

        HashMap<Attrezzatura, Integer> listaAttrezzaturaTest = new HashMap<>();
        listaAttrezzaturaTest.put(sdraio, 10);
        listaAttrezzaturaTest.put(posacenere, 5);

        return listaAttrezzaturaTest;

    }

}
