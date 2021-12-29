package it.unicam.cs.ids.asf;

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
    ResultSet queryWithOutput(String query) {
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
    boolean queryWithoutOutput(String query) {
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
    boolean closeConnection() {
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

        Coordinate coordinate1 = new Coordinate(1,1);
        Ombrellone ombrellone1 = new Ombrellone(1, coordinate1, 1);

        Coordinate coordinate2 = new Coordinate(1,2);
        Ombrellone ombrellone2 = new Ombrellone(1, coordinate2, 2);

        Coordinate coordinate3 = new Coordinate(2,1);
        Ombrellone ombrellone3 = new Ombrellone(1, coordinate3, 3);

        Coordinate coordinate4 = new Coordinate(2,2);
        Ombrellone ombrellone4 = new Ombrellone(1, coordinate4, 4);

        // Create n lists one by one and append to the
        // master list (ArrayList of ArrayList)
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
        return null;
    }

    public Listino ottieniListinoAggiornato() {
        Listino listino = new Listino();

        String nomeStringa = null;
        int idPrimo = 1;
        int idUltimo = 2;
        double fasciaPrezzo;

        FasciaDiPrezzo fasciaTest = new FasciaDiPrezzo(nomeStringa, idPrimo, idUltimo, 1);


        HashMap<FasciaDiPrezzo, Double> prezziFascia = new HashMap<FasciaDiPrezzo, Double>() {{
            put(fasciaTest, 10.0);
        }};
        listino.setPrezziFascia(prezziFascia);

        HashMap<Integer, Double> prezziTipologia = new HashMap<Integer, Double>() {{
            put(1, 10.0);
            put(2, 20.0);
        }};
        listino.setPrezziTipologia(prezziTipologia);

        return listino;
    }

}
