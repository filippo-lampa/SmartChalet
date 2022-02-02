package it.unicam.ids.smartchalet.asf;

import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;

public class DBMSController {

    private static DBMSController instance;
    private String url;
    private String user;
    private String pwd;
    private Connection conn = null;

    /**
     * Costruttore che inizializza un ControllerDB
     *
     */
    public DBMSController() {

    }

    public static DBMSController getInstance() {
        if(instance == null){
            instance = new DBMSController();
        }
        return instance;
    }

    public void setDBMSController(String url, String user, String pwd){
        this.url = url;
        this.user = user;
        this.pwd = pwd;
    }

    public boolean DBMSControllerTest(){
        boolean result = true;
        try{
            if(conn == null || !conn.isClosed()){
                connect();
                result = false;
            }
            DatabaseMetaData data = conn.getMetaData();
            System.out.println("Dettagli sul database: "+ data.getDatabaseProductName() + "\n" + "Versione: " + data.getDriverMajorVersion());
            this.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void connect(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Il Driver Postgresql JDBC non è presente, inseriscilo ");
            e.printStackTrace();
        }
        try{
            this.conn = DriverManager.getConnection(url, user, pwd);
            System.out.println("Database connesso ");
        }catch (SQLException e) {
            System.out.print("Problema durante il collegamneto al database ");
            e.printStackTrace();
        }
    }

    private void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Problems in closing the connection to the DB");
            e.printStackTrace();
        }
    }

    //testato
    public ArrayList<ArrayList<Ombrellone>> ottieniVistaSpiaggia() {
        String comandoSQL = "select * from spiaggia " +
                "left join ombrellone on (spiaggia.idombrellone = ombrellone.id) " +
                "order by spiaggia.fila,spiaggia.colonna";
        ArrayList<ArrayList<Ombrellone>> listaOmbrelloni = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(comandoSQL);
            int riga = -1;
            while (rs.next()) {
                if(rs.getInt(2) != riga) {
                    riga = rs.getInt(2);
                    listaOmbrelloni.add(new ArrayList<>());
                }
                if(rs.getString(1) == null){
                    listaOmbrelloni.get(riga).add(null);
                }
                else listaOmbrelloni.get(riga).add(new Ombrellone(rs.getString(5),new Coordinate(rs.getInt(3),rs.getInt(2)),rs.getInt(4)));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return listaOmbrelloni;
    }

    //da cambiare quando prenotazioni sono sistemate
    public ArrayList<Prenotazione> ottieniListaPrenotazioni() {
        String SQL = "SELECT * FROM prenotazione";
        ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            String SQL2;
            Statement stmt2 = conn.createStatement();
            while (rs.next()) {
                SQL2 = "SELECT * FROM Date_Fasce join ombrellone on (ombrellone = id) WHERE (idPrenotazione = " + rs.getInt(1) + ")";
                ResultSet rs2 = stmt2.executeQuery(SQL2);
                Prenotazione tempPrenotazione = new Prenotazione(rs.getInt(1), rs.getDate(3),rs.getDate(4),rs.getInt(2), rs.getDouble(5));
                ArrayList<Ombrellone> tempListaOmbrelloni = new ArrayList<>();
                java.util.Date data = null;
                while(rs2.next()) {
                    if(data != null && !data.equals(rs2.getDate(1))){
                        tempPrenotazione.getMappaDateListaOmbrelloni().put(data, tempListaOmbrelloni);
                    }
                    data = new Date(rs2.getDate(1).getTime());
                    Ombrellone ombrellone = new Ombrellone(rs2.getString(7),new Coordinate(rs2.getInt(9),rs2.getInt(8)),rs2.getInt(6));
                    ombrellone.setNumeroLettiniAssociati(rs2.getInt(5));
                    tempListaOmbrelloni.add(ombrellone);
                    tempPrenotazione.getMappaDateFasce().putIfAbsent(data, rs2.getInt(2));
                }
                tempPrenotazione.getMappaDateListaOmbrelloni().put(data, tempListaOmbrelloni);
                prenotazioni.add(tempPrenotazione);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return prenotazioni;
    }

    public boolean aggiungiGrigliaSpiaggia(ArrayList<ArrayList<Ombrellone>> grigliaSpiaggia) {
        String SQL;
        for (int i = 0; i < grigliaSpiaggia.size(); i++) {
            for (int j = 0; j < grigliaSpiaggia.get(i).size(); j++) {
                SQL = "INSERT INTO spiaggia (fila,colonna) VALUES (" + i + "," + j + ")";
                try {
                    Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(SQL);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    //testato
    public HashMap<Attrezzatura, Integer> ottieniMappaAttrezzature(){
        String SQL = "select * from attrezzatura";
        HashMap<Attrezzatura, Integer> mappaAttrezzature = new HashMap<>();
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                mappaAttrezzature.put(new Attrezzatura(rs.getString(3),rs.getString(2), rs.getInt(1)),rs.getInt(4));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return mappaAttrezzature;
    }

    //da testare (passando dal metodo principale)
    private boolean inserimentoNuovaAttrezzatura(Attrezzatura attrezzatura, HashMap<Attrezzatura, Integer> mappaAttrezzatura) {

        String comandoSQL1 = "insert into attrezzatura (descrizione,nome,attrezzaturadisponibile) " +
                "values ('"+attrezzatura.getDescrizione()+"','"+attrezzatura.getNome()+"',"+mappaAttrezzatura.get(attrezzatura)+")";

        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(comandoSQL1);

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;


    }

    //da testare (passando dal metodo principale)
    private boolean updateAttrezzatura(Attrezzatura attrezzatura, HashMap<Attrezzatura, Integer> mappaAttrezzatura) {
        String comandoSQL = "update attrezzatura " +
                "set attrezzaturaDisponibile = "+ mappaAttrezzatura.get(attrezzatura)+ " " +
                "where (id = "+ attrezzatura.getId() +")";

        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(comandoSQL);

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //da testare
    public boolean aggiornaMappaAttrezzature(HashMap<Attrezzatura, Integer> mappaAttrezzatura) {

        HashMap<Attrezzatura, Integer> mappaAttrezzaturaPreModifica = this.ottieniMappaAttrezzature();

        boolean controlloEsistenza;

        for(Attrezzatura attrezzatura : mappaAttrezzatura.keySet()){
            controlloEsistenza=false;
            for(Attrezzatura attrezzaturaPreModifica : mappaAttrezzaturaPreModifica.keySet()){
                if(attrezzatura.getId() == attrezzaturaPreModifica.getId()){
                    if(!this.updateAttrezzatura(attrezzatura,mappaAttrezzatura)) return false;
                    controlloEsistenza=true;
                }
            }
            if(controlloEsistenza) continue;

            if(!this.inserimentoNuovaAttrezzatura(attrezzatura,mappaAttrezzatura)) return false; //aggiunta a db
        }
        return true;

    }

    //testato
    public ArrayList<Attivita> ottieniListaAttivita() {
        String SQL = "select * from attivita_attrezzature ,attivita,attrezzatura " +
                "where (attivita_attrezzature.idattivita = attivita.id " +
                "and attivita_attrezzature.idattrezzatura = attrezzatura.id)";
        ArrayList<Attivita> listaAttivita = new ArrayList<>();
        Attivita attivita;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            boolean flag;
            while (rs.next()) {
                flag = false;
                attivita = new Attivita(rs.getString(6),rs.getString(7),new java.util.Date(rs.getDate(5).getTime()),rs.getInt(8),rs.getString(9),rs.getInt(10),rs.getInt(12));
                attivita.setNumeroIscritti(rs.getInt(11));
                for(Attivita attivitaLista : listaAttivita){
                    if(attivita.equals(attivitaLista)){
                        attivita = attivitaLista;
                        flag = true;
                    }
                }
                if(!flag) {
                    listaAttivita.add(attivita);
                }
                attivita.getAttrezzatureAssociate().put(new Attrezzatura(rs.getString(15),rs.getString(14),rs.getInt(2)),rs.getInt(3));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return listaAttivita;
    }

    //testato ma ricontrollare x sicurezza
    public HashMap<ProdottoBar, Double> ottieniMappaProdottiBar() {
        String SQL = "select * from prodottobar";
        HashMap<ProdottoBar, Double> mappaProdottiBar = new HashMap<>();
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                mappaProdottiBar.put(new ProdottoBar(rs.getString(2), rs.getString(1)), rs.getDouble(4));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return mappaProdottiBar;
    }


    //da testare
    public void aggiungiOrdineBar(OrdineBar ordineBar) {
        String SQL = "INSERT INTO ordinebar VALUES (" + ordineBar.getIdCliente() + "," + ordineBar.getIdOrdine() + ")" ;
        String SQL2;
        String SQL3;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
            Statement stmt2 = conn.createStatement();
            for(ProdottoBar prodotto : ordineBar.getProdottiOrdinati().keySet()){
                SQL3 = "select * from prodottobar where (nomeprodotto = '" + prodotto.getNomeProdotto() + "')";
                ResultSet rs = stmt2.executeQuery(SQL3);
                rs.next();
                SQL2 = "INSERT INTO prodotti_ordine VALUES (" + ordineBar.getIdOrdine() + "," + rs.getInt(3) + "," + ordineBar.getProdottiOrdinati().get(prodotto) + ")" ;
                stmt.executeUpdate(SQL2);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    //testato
    public HashMap<TipologiaOmbrellone, Double> ottieniTipologie() {
        String comandoSQL = "select * from tipologiaOmbrellone";
        HashMap<TipologiaOmbrellone, Double> prezziTipologie = new HashMap<>();

        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(comandoSQL);
            while (rs.next()) {
                double valore = rs.getDouble(3);
                if(valore == 0.0) prezziTipologie.put(new TipologiaOmbrellone(rs.getString(2),rs.getString(1)), null);
                else prezziTipologie.put(new TipologiaOmbrellone(rs.getString(2),rs.getString(1)), valore);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return prezziTipologie;
    }

    public boolean aggiornaMappaSpiaggia(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) { //TODO da ricontrollare

        this.aggiornaOmbrelloni(listaOmbrelloni);

        this.eliminaPostiNonPresenti(listaOmbrelloni);

        int x=0;
        int y=0;
        for(ArrayList<Ombrellone> riga : listaOmbrelloni) {
            x=0;
            for (Ombrellone ombrellone : riga) {
                if(ombrellone == null){
                    if(!this.aggiuntaElemento(x, y, null)) return false;
                }
                else if(!this.aggiuntaElemento(x, y,ombrellone)) return false;
                x++;
            }
            y++;
        }
        return true;
    }

    private void eliminaPostiNonPresenti(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        boolean condition;
        ArrayList<ArrayList<Ombrellone>> listaPreModifica = this.ottieniVistaSpiaggia();
        for(int i=0; i<listaPreModifica.size(); i++)
            for(int j=0; j<listaPreModifica.get(i).size() ; j++){
                condition = false;
                try{
                    listaOmbrelloni.get(i).get(j);
                }catch (IndexOutOfBoundsException e){
                    condition = true;
                }
                if(condition){
                    String SQL = "DELETE from spiaggia WHERE (fila = " + i + " AND colonna = " + j + ")" ;
                    try {
                        Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(SQL);
                    } catch (SQLException ex){
                        ex.printStackTrace();
                    }
                }
            }

    }

    private void aggiornaOmbrelloni(ArrayList<ArrayList<Ombrellone>> listaOmbrelloni) {
        ArrayList<Ombrellone> temp = new ArrayList<>();
        for(ArrayList<Ombrellone> riga : listaOmbrelloni)
            for(Ombrellone ombrellone : riga)
                if(ombrellone != null)
                    temp.add(ombrellone);
        String SQL = "select * from ombrellone";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while(rs.next()){
                for(int i=0; i<temp.size(); i++) {
                    if (temp.get(i).getIdOmbrellone() == rs.getInt(1)) {
                        this.updateOmbrellone(temp.get(i));
                        temp.remove(temp.get(i));
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        for(Ombrellone ombrellone : temp)
            this.aggiungiOmbrellone(ombrellone);
    }

    private void aggiungiOmbrellone(Ombrellone ombrellone) {
        String SQL = "INSERT INTO ombrellone VALUES (" + ombrellone.getIdOmbrellone() + ",'" + ombrellone.getNomeTipo() + "'," + ombrellone.getLocation().getyAxis() + "," + ombrellone.getLocation().getxAxis() + ")" ;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private void updateOmbrellone(Ombrellone ombrellone) {
        String SQL = "UPDATE ombrellone SET nometipologia = '" + ombrellone.getNomeTipo() + "', fila = " + ombrellone.getLocation().getyAxis() + ", colonna = " + ombrellone.getLocation().getxAxis() + "WHERE ( id = " + ombrellone.getIdOmbrellone() + ")" ;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private boolean aggiuntaElemento(int x, int y,Ombrellone ombrellone) {

        String comandoSQL;

        String comandoSQL1 = "select * from spiaggia where (fila = " + y + " AND colonna = " + x + ")";

        String comandoSQL2;

        if(ombrellone == null){
            comandoSQL2 = "insert into spiaggia values ( NULL," + y +","+ x + ")";
            comandoSQL =  "update spiaggia set idombrellone = NULL " +
                    "Where (fila = " + y + " AND colonna = " + x + ")";
        }else {
            comandoSQL2 = "insert into spiaggia values (" + ombrellone.getIdOmbrellone() + "," + y +","+ x + ")";
            comandoSQL = "update spiaggia set idombrellone = " + ombrellone.getIdOmbrellone() +
                    "Where (fila = " + y + " AND colonna = " + x + ")";
        }
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(comandoSQL1);
            if(rs.next())
                stmt.executeUpdate(comandoSQL);
            else stmt.executeUpdate(comandoSQL2);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //testato
    public boolean aggiornaMappaTipologie(HashMap<TipologiaOmbrellone, Double> prezziTipologia) {
        HashMap<TipologiaOmbrellone,Double> tipologiePreModifiche = this.ottieniTipologie();
        boolean controlloEsistenza;
        for(TipologiaOmbrellone tipologia : prezziTipologia.keySet()){
            controlloEsistenza=false;
            for(TipologiaOmbrellone fasciaPreModifica : tipologiePreModifiche.keySet()){
                if(tipologia.getNome().equals(fasciaPreModifica.getNome())){
                    if(!this.updateTipologie(tipologia,prezziTipologia)) return false;
                    controlloEsistenza=true;
                }
            }
            if(controlloEsistenza) continue;

            if(!this.aggiungiTipologie(tipologia,prezziTipologia)) return false; //aggiunta a db
        }

        return true;
    }

    //testato
    public boolean aggiungiTipologie(TipologiaOmbrellone tipologia, HashMap<TipologiaOmbrellone, Double> prezziTipologia) {
        String comandoSQL = "insert into tipologiaombrellone values ( '" + tipologia.getDescrizione()+"','"+ tipologia.getNome() + "', null )";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateTipologie(TipologiaOmbrellone tipologia, HashMap<TipologiaOmbrellone, Double> prezziTipologia) {
        String comandoSQL = "update tipologiaombrellone set prezzo = "+prezziTipologia.get(tipologia)+" where (nome = '"+tipologia.getNome()+"')";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean updateFascia(FasciaDiPrezzo fascia, HashMap<FasciaDiPrezzo, Double> prezziFascia) {
        String comandoSQL = "update fasciadiprezzo set filainizio = "+fascia.getCoordinateInizio().getyAxis()+","+
                "filafine = "+fascia.getCoordinateFine().getyAxis()+","+ "colonnainizio = "+fascia.getCoordinateInizio().getxAxis()+","+
                "colonnafine = "+fascia.getCoordinateFine().getxAxis()+ ", prezzo = " + prezziFascia.get(fascia) + " where (nome = '"+fascia.getNome()+"')";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void eliminazioneFasceDaNonPresenti(HashMap<FasciaDiPrezzo, Double> prezziFasciaPreModifica, HashMap<FasciaDiPrezzo, Double> prezziFascia) {
        int app;
        for(FasciaDiPrezzo fasciaPreModifica : prezziFasciaPreModifica.keySet()){
            app=0;
            for(FasciaDiPrezzo fascia : prezziFascia.keySet()){
                if(fasciaPreModifica.getNome().equals(fascia.getNome())) break;
                else app++;
            }
            if(app==prezziFascia.size()){ //se non è più nel database
                String comandoSQL = "delete from fasciadiprezzo where (nome='"+fasciaPreModifica.getNome()+"')";
                try {
                    Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(comandoSQL);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    //da testare con il metodo x aggiungere
    private boolean inserimentoNuovaFascia(FasciaDiPrezzo fascia, HashMap<FasciaDiPrezzo, Double> prezziFascia) {
        String comandoSQL = "insert into fasciadiprezzo (nome,filainizio,filafine,colonnainizio,colonnafine,prezzo) " +
                "values ( '" + fascia.getNome()+"',"+fascia.getCoordinateInizio().getyAxis()+","+ fascia.getCoordinateFine().getyAxis()+
                ","+fascia.getCoordinateInizio().getxAxis()+","+fascia.getCoordinateFine().getxAxis()+ "," + prezziFascia.get(fascia) + ")";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //testato
    public HashMap<FasciaDiPrezzo, Double> ottieniMappaFasce() {
        String SQL = "select * from fasciadiprezzo";
        HashMap<FasciaDiPrezzo, Double> prezziFasce = new HashMap<>();
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                prezziFasce.put(new FasciaDiPrezzo(rs.getString(1), new Coordinate(rs.getInt(4),rs.getInt(2)), new Coordinate(rs.getInt(5),rs.getInt(3))), rs.getDouble(7));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return prezziFasce;
    }

    //testare il pezzo dell'aggiunta
    public boolean aggiornaMappaFasce(HashMap<FasciaDiPrezzo, Double> prezziFascia) {

        HashMap<FasciaDiPrezzo, Double> fascePreModifiche = this.ottieniMappaFasce();

        boolean controlloEsistenza;

        this.eliminazioneFasceDaNonPresenti(fascePreModifiche,prezziFascia);

        for(FasciaDiPrezzo fascia : prezziFascia.keySet()){
            controlloEsistenza=false;
            for(FasciaDiPrezzo fasciaPreModifica : fascePreModifiche.keySet()){
                if(fascia.getNome().equals(fasciaPreModifica.getNome())){
                    if(!this.updateFascia(fascia,prezziFascia)) return false;
                    controlloEsistenza=true;
                }
            }
            if(controlloEsistenza) continue;

            if(!this.inserimentoNuovaFascia(fascia,prezziFascia)) return false; //aggiunta a db
        }
        return true;
    }

    public void rimuoviOmbrellone(Ombrellone ombrellone) {
        String SQL = "DELETE from ombrellone where (id = " + ombrellone.getIdOmbrellone() + ")";
        String SQL2 = "UPDATE spiaggia SET idombrellone = NULL WHERE (" + "fila = " + ombrellone.getLocation().getyAxis() + " AND colonna = " + ombrellone.getLocation().getxAxis() + ")";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL2);
            stmt.executeUpdate(SQL);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void aggiornaTipologiaOmbrellone(int idOmbrellone, String tipologia) {
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            String SQL = "UPDATE ombrellone SET nometipologia = '" + tipologia + "' WHERE id = " + idOmbrellone;
            stmt.executeUpdate(SQL);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public double ottieniPrezzoBaseOmbrellone() {
        String SQL = "select prezzobaseombrellone from listino";
        double prezzo = 0.0;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if(rs.next());
                prezzo = rs.getDouble(1);
        } catch (SQLException ex){
        }
        return prezzo;
    }

    public double ottieniPrezzoBaseLettino() {
        String SQL = "select prezzolettino from listino";
        double prezzo = 0.0;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if(rs.next());
                prezzo = rs.getDouble(1);
        } catch (SQLException ex){
        }
        return prezzo;
    }

    public void aggiornaPrezziBase(double prezzoBaseOmbrellone, double prezzoBaseLettino) {
        if(this.ottieniPrezzoBaseLettino() == 0.0 && this.ottieniPrezzoBaseOmbrellone() == 0.0){
            this.inserisciPrezziBase(prezzoBaseOmbrellone,prezzoBaseLettino);
        } else {
            String SQL = "update listino set prezzobaseombrellone = " + prezzoBaseOmbrellone + ", prezzolettino = " + prezzoBaseLettino;
            try {
                Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(SQL);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void inserisciPrezziBase(double prezzoBaseOmbrellone, double prezzoBaseLettino) {
        String SQL = "insert into listino (prezzobaseombrellone, prezzolettino) values (" + prezzoBaseOmbrellone + ","+ prezzoBaseLettino + ")";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQL);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void registraCliente(String nome, String cognome, String telefono, String mail) {
        String comandoSQL = "insert into cliente (nome,cognome,telefono,mail) values ( '" + nome +"','"+ cognome + "','" + telefono + "','" + mail + "')";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int getCliente(String nome, String cognome, String telefono, String mail) {
        String SQL = "select * from cliente where ( nome = '" + nome + "' and cognome = '" + cognome + "' and telefono = '" + telefono + "' and mail = '" + mail + "')";
        int idCliente = 0;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            idCliente = rs.getInt(1);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return idCliente;
    }

    public Cliente controlloIdCliente(int idCliente) {
        String SQL = "select * from cliente where ( id = " + idCliente + ")";
        Cliente cliente = null;
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            if(rs.next()) {
                cliente = new Cliente(rs.getInt(1), rs.getString(2), rs.getString(3), Double.toString(rs.getDouble(4)), rs.getString(5));
                idCliente = rs.getInt(1);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return cliente;
    }

    public ArrayList<OrdineBar> ottieniListaOrdiniBar() {
        String SQL = "select * from ordinebar join prodotti_ordine on (ordinebar.idordine = prodotti_ordine.idordine) join prodottobar on (prodottobarid = id)";
        ArrayList<OrdineBar> listaOrdini = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            boolean flag;
            while(rs.next()) {
                flag = false;
                for(OrdineBar currentOrdine : listaOrdini){
                    if(currentOrdine.getIdOrdine() == rs.getInt(2)){
                        flag = true;
                        currentOrdine.getProdottiOrdinati().put(new ProdottoBar(rs.getString(7),rs.getString(6)), rs.getInt(5));
                    }
                }
                if(!flag){
                    OrdineBar nuovoOrdine = new OrdineBar(rs.getInt(1), new HashMap<ProdottoBar,Integer>(),rs.getInt(2));
                    listaOrdini.add(nuovoOrdine);
                    nuovoOrdine.getProdottiOrdinati().put(new ProdottoBar(rs.getString(7),rs.getString(6)), rs.getInt(5));
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return listaOrdini;
    }

    public void aggiornaListaOrdiniBar(ArrayList<Integer> idOrdiniCompletati) {
        String SQL,SQL2;
        for(Integer currentId : idOrdiniCompletati) {
            SQL = "DELETE FROM ordinebar WHERE (idordine = " + currentId + ")";
            SQL2 = "DELETE FROM prodotti_ordine WHERE (idordine = " + currentId + ")";
            try {
                Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(SQL2);
                stmt.executeUpdate(SQL);
            } catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public boolean aggiungiPrenotazione(Prenotazione prenotazioneInCorso) {
        String comandoSQL = "insert into prenotazione (clienteid,datainizio,datafine,prezzototale) values ( " + prenotazioneInCorso.getIdCliente() +",'"+ new java.sql.Date(prenotazioneInCorso.getDataInizio().getTime()) + "','" + new java.sql.Date(prenotazioneInCorso.getDataFine().getTime()) + "'," + prenotazioneInCorso.getPrezzoTotale() + ")";
        for(java.util.Date data : prenotazioneInCorso.getMappaDateFasce().keySet()){
                    for(Ombrellone ombrellone : prenotazioneInCorso.getMappaDateListaOmbrelloni().get(data)){
                        String SQL = "INSERT INTO date_fasce VALUES ('" + new java.sql.Date(data.getTime()) + "'," + prenotazioneInCorso.getMappaDateFasce().get(data) + "," + prenotazioneInCorso.getId() + "," + ombrellone.getIdOmbrellone() + "," + ombrellone.getNumeroLettiniAssociati() + ")";
                        try {
                            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                            Statement stmt = conn.createStatement();
                            stmt.executeUpdate(SQL);
                        }catch (SQLException e){
                            e.printStackTrace();
                            return false;
                        }
                    }
        }
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean aggiornaMappaProdottoBar(HashMap<ProdottoBar, Double> mappaPrezziBar) {

        HashMap<ProdottoBar,Double> mappaBarPreModifica = this.ottieniMappaProdottiBar();

        boolean controlloEsistenza;

        this.eliminazioneProdottiDaNonPresenti(mappaBarPreModifica,mappaPrezziBar);

        for(ProdottoBar prodotto : mappaPrezziBar.keySet()){
            controlloEsistenza=false;
            for(ProdottoBar prodottoPreModifica : mappaBarPreModifica.keySet()){
                if(prodotto.getNomeProdotto().equals(prodottoPreModifica.getNomeProdotto())){
                    if(!this.updateProdottoBar(prodottoPreModifica,prodotto,mappaPrezziBar)) return false;
                    controlloEsistenza=true;
                }
            }
            if(controlloEsistenza) continue;

            if(!this.inserimentoNuovoProdottoBar(prodotto,mappaPrezziBar)) return false; //aggiunta a db
        }
        return true;
    }

    private boolean inserimentoNuovoProdottoBar(ProdottoBar prodotto, HashMap<ProdottoBar, Double> prezziProdottiBar) {
        String comandoSQL = "insert into prodottobar (nomeprodotto,descrizione,prezzo) " +
                "values ( '" + prodotto.getNomeProdotto() + "','" + prodotto.getDescrizione() +"',"+ prezziProdottiBar.get(prodotto) + ")";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean updateProdottoBar(ProdottoBar prodottoPreModifica,ProdottoBar prodotto, HashMap<ProdottoBar,Double> prezziProdotto) {
        String comandoSQL = "update prodottobar set nomeprodotto = '"+prodotto.getNomeProdotto() +"',"+
                "descrizione = '"+prodotto.getDescrizione() + "'," + "prezzo = "+prezziProdotto.get(prodotto)+ " WHERE (nomeprodotto = '" + prodottoPreModifica.getNomeProdotto() + "')";
        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void eliminazioneProdottiDaNonPresenti(HashMap<ProdottoBar,Double> prezziProdottoPreModifica, HashMap<ProdottoBar,Double> prezziProdotto) {
        int app;
        for(ProdottoBar prodottoPreModifica : prezziProdottoPreModifica.keySet()){
            app=0;
            for(ProdottoBar prodotto : prezziProdotto.keySet()){
                if(prodottoPreModifica.getNomeProdotto().equals(prodotto.getNomeProdotto())) break;
                else app++;
            }
            if(app==prezziProdotto.size()){ //se non è più nel database
                String comandoSQL = "delete from prodottobar where (nomeprodotto='"+prodottoPreModifica.getNomeProdotto()+"')";
                try {
                    Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(comandoSQL);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean aggiornaListaAttivita(ArrayList<Attivita> listaAttivita) {

        ArrayList<Attivita> attivitaPreModifica = this.ottieniListaAttivita();

        boolean controlloEsistenza;

        this.eliminazioneAttivita(attivitaPreModifica,listaAttivita);

        for(Attivita attivita : listaAttivita){
            controlloEsistenza=false;
            for(Attivita attivitaPre : attivitaPreModifica){
                if(attivita.getNome().equals(attivitaPre.getNome()) && attivita.getData().equals(attivitaPre.getData()) && attivita.getFasciaOraria() == attivitaPre.getFasciaOraria()){
                    if(!this.updateAttivita(attivita)) return false;
                    controlloEsistenza=true;
                }
            }
            if(controlloEsistenza) continue;

            if(!this.inserimentoNuovaAttivita(attivita)) return false; //aggiunta a db
        }
        return true;
    }

    private boolean inserimentoNuovaAttivita(Attivita attivita) {

        String comandoSQL = "insert into attivita (data,nome,descrizione,maxpartecipanti,animatore,oredurata,numeroiscritti,fasciaoraria) " +
                "values ( '" + attivita.getData() + "','" + attivita.getNome() +"','" + attivita.getDescrizione() + "',"+ attivita.getMaxPartecipanti() +",'"+ attivita.getAnimatore() + "'," + attivita.getOreDurata() + "," + attivita.getNumeroIscritti() + "," + attivita.getFasciaOraria() + ")";

        String comandoSQL2 = "select * from attivita where ( nome='" + attivita.getNome() + "' AND data = '" + new java.sql.Date(attivita.getData().getTime()) + "' AND fasciaoraria =  " + attivita.getFasciaOraria() + ")";

        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(comandoSQL);
            ResultSet rs = stmt.executeQuery(comandoSQL2);
            rs.next();
            for(Attrezzatura attrezzatura : attivita.getAttrezzatureAssociate().keySet()) {
                String comandoSQL3 = "insert into attivita_attrezzature values (" + rs.getInt(1) + "," + attrezzatura.getId() + "," + attivita.getAttrezzatureAssociate().get(attrezzatura) + ")";
                stmt.executeUpdate(comandoSQL3);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean updateAttivita(Attivita attivita) {

        String comandoSQL = "select * from attivita where ( nome='" + attivita.getNome() + "' AND data = '" + new java.sql.Date(attivita.getData().getTime()) + "' AND fasciaoraria =  " + attivita.getFasciaOraria() + ")";

        try {
            Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(comandoSQL);
            rs.next();
            String comandoSQL1 = "update attivita set data = '" + new java.sql.Date(attivita.getData().getTime()) + "', nome = '" +attivita.getNome() +"',"+
                    "descrizione = '"+attivita.getDescrizione() + "'," + "maxpartecipanti = "+ attivita.getMaxPartecipanti()+ ", animatore = '" + attivita.getAnimatore() + "', oredurata = " + attivita.getOreDurata() + ", numeroiscritti = " + attivita.getNumeroIscritti() + ", fasciaoraria = " + attivita.getFasciaOraria() + "WHERE (id = " + rs.getInt(1) + ")";
            Statement stmt2 = conn.createStatement();
            stmt2.executeUpdate(comandoSQL1);
            for(Attrezzatura attrezzatura : attivita.getAttrezzatureAssociate().keySet()) {
                String comandoSQL2 = "update attivita_attrezzature set numeroattrezzature = " + attivita.getAttrezzatureAssociate().get(attrezzatura) + " WHERE (idattivita = " + rs.getInt(1) + " AND idattrezzatura = " + attrezzatura.getId() + ")";
                stmt2.executeUpdate(comandoSQL2);
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void eliminazioneAttivita(ArrayList<Attivita> attivitaPreModifica, ArrayList<Attivita> listaAttivita) {
        int app;
        for (Attivita attivitaPre : attivitaPreModifica) {
            app = 0;
            for (Attivita attivita : listaAttivita) {
                if (attivitaPre.getNome().equals(attivita.getNome()) && attivitaPre.getFasciaOraria() == attivita.getFasciaOraria() && attivitaPre.getData().equals(attivita.getData())) break;
                else app++;
            }
            if (app == listaAttivita.size()) { //se non è più nel database
                String comandoSQL1 = "select * from attivita where ( nome='" + attivitaPre.getNome() + "' AND data = '" + new java.sql.Date(attivitaPre.getData().getTime()) + "' AND fasciaoraria =  " + attivitaPre.getFasciaOraria() + ")";
                try {
                    Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(comandoSQL1);
                    rs.next();
                    int id = rs.getInt(1);
                    String comandoSQL = "delete from attivita_attrezzature where (idattivita = " + id + ")";
                    stmt.executeUpdate(comandoSQL);
                    String comandoSQL2 = "delete from attivita where (id = " + id + ")";
                    stmt.executeUpdate(comandoSQL2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void rimuoviPrenotazioni(ArrayList<Integer> listaPrenotazioniDaRimuovere) {
        String comandoSQL;
        String comandoSQL2;
        for(Integer currentIdPrenotazione : listaPrenotazioniDaRimuovere){
            comandoSQL2 = "DELETE FROM date_fasce WHERE ( idprenotazione = " + currentIdPrenotazione + ")";
            comandoSQL = "DELETE FROM prenotazione WHERE ( id = " + currentIdPrenotazione + ")";
            try {
                Connection conn = DriverManager.getConnection(this.url, this.user, this.pwd);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(comandoSQL2);
                stmt.executeUpdate(comandoSQL);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> ottieniListaCouponValidi() {  //TODO prossima iterazione
        return new ArrayList<>();
    }

}

