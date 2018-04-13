package main;

import main.NormalClasses.Anagrafica.*;
import main.NormalClasses.Gite.*;
import main.NormalClasses.Mensa.ChildIntolerance;
import main.NormalClasses.Mensa.Dish;
import main.NormalClasses.Mensa.Intolerance;
import main.NormalClasses.Mensa.PersonIntolerance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DMLCommandExecutor {

    private static DMLCommandExecutor instance = new DMLCommandExecutor();//Singleton

    public static synchronized DMLCommandExecutor getInstance() {
        return instance;
    }

    private ConnectionPool myPool;


    public DMLCommandExecutor() {

        this.myPool = new ConnectionPool();
    }

    public boolean insertChildDailyPresenceIntoDb(String codF){
        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO PRESENZABAMBINO (CodF, DataOra) VALUES('" + codF + "', sysdate());";
        try {
            if (stmt.executeUpdate(sql) == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertPersonDailyPresenceIntoDb(String codF){
        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO PRESENZAPERSONALE (CodF, DataOra) VALUES('" + codF + "', sysdate());";
        try {
            if (stmt.executeUpdate(sql) == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    //Query to check if a user is registered
    public User selectUserForLogin(String username, String psw) throws SQLException {

        String name = null;
        String pass = null;
        int type = 0;

        Connection conn = myPool.getConnection();
        Statement stmt = conn.createStatement();

        String sql = "SELECT * FROM Personale WHERE Personale.Username='" + username + "';";

        ResultSet rs = stmt.executeQuery(sql);

        myPool.releaseConnection(conn);

        //Extract data from result set

        while (rs.next()) {
            name = rs.getString("Username");
            pass = rs.getString("Password");
            type = rs.getInt("TypeFlag");
        }
        //Check if the user is found
        if (name != null) {
            if (username.equals(name) && pass.equals(psw))
                return new User(name, pass, User.UserTypeFlag.values()[type]);
        }

        return null;

    }

    //Insert a generic person into Db
    public boolean insertPersonIntoDb(Person p) {

        Connection conn = myPool.getConnection();
        boolean status = false;
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(generateSQLInsertStatement(p)) == 1)
                status = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    //Generate a SQL statement for insertPersonIntoDb method
    private String generateSQLInsertStatement(Person p) {//S
        String query = null;
        if (p instanceof Staff) {
            Staff s = (Staff) p;

            query = "INSERT INTO PERSONALE"
                    + "(CodF, Nome, Cognome, Username, Password, DataN, TypeFlag) " +
                    "VALUES('" + s.getCodiceFiscale() + "','" + s.getNome() + "','" + s.getCognome() + "' ,'" + s.getUsername() + "','"
                    + s.getPassword() + "','" + s.getDataNascita() + "','" + s.getTipo() + "');";
        } else if (p instanceof Child) {
            Child c = (Child) p;
            query = "INSERT INTO BAMBINO"
                    + "(CodF, Nome, Cognome, DataN, CodFGen1, CodFGen2, CodFPed) " +
                    "VALUES('" + c.getCodiceFiscale() + "','" + c.getNome() + "','" + c.getCognome() + "' ,'" + c.getDataNascita() + "','"
                    + c.getCodiceFiscaleGen1() + "','" + c.getCodiceFiscaleGen2() + "','" + c.getCodiceFiscalePediatra() + "');";
        } else if (p instanceof Contact) {
            Contact o = (Contact) p;

            query = "INSERT INTO ESTERNO"
                    + "(CodF, Nome, Cognome, Cell, TypeFlag) " +
                    "VALUES('" + o.getCodiceFiscale() + "','" + o.getNome() + "','" + o.getCognome() + "' ,'" + o.getCellulare() + "','"
                    + o.getTipo() + "');";
        }

        return query;
    }

    //Insert a supplier into db
    public boolean insertSupplierIntoDb(Supplier s) {

        Connection conn = myPool.getConnection();
        boolean status = false;
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO FORNITORE"
                + " (PIva, NomeF, Tel, Email)"
                + " VALUES('" + s.getpIva() + "','" + s.getNomeF() + "','" + s.getTel() + "' ,'" + s.getEmail() + "')"
                + " ON DUPLICATE KEY UPDATE NomeF='" + s.getNomeF() + "', Tel='" + s.getTel() + "', Email='" + s.getEmail() + "' ;";
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    //Update person data
    public boolean updatePersonIntoDb(Person p) {

        Connection conn = myPool.getConnection();

        Statement stmt = null;

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(this.generateSQLUpdateStatement(p)) == 1)
                return true;
            else
                return false;
        } catch (SQLException ex) { //Handle a double key exception
            System.out.println(ex);
            return false;
        } finally {
            myPool.releaseConnection(conn);
        }
    }

    public boolean updateSupplierIntoDb(Supplier s) {
        Connection conn = myPool.getConnection();

        Statement stmt = null;
        String sql = "UPDATE FORNITORE SET  NomeF='" + s.getNomeF() + "', Tel='" + s.getTel() + "', Email='" + s.getEmail() + "' WHERE PIva='" + s.getpIva() + " ';";

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                return true;
            else
                return false;
        } catch (SQLException ex) { //Handle a double key exception
            System.out.println(ex);
            return false;
        } finally {
            System.out.println("CIAO");
            myPool.releaseConnection(conn);
        }
    }

    private String generateSQLUpdateStatement(Person p) {
        String update = null;
        if (p instanceof Staff) {
            Staff s = (Staff) p;

            update = "UPDATE PERSONALE"
                    + " SET  Nome='" + p.getNome() + "', Cognome='" + p.getCognome() + "', DataN ='" + s.getDataNascita() + "', TypeFlag='" + s.getTipo() + "'"
                    + " WHERE CodF='" + p.getCodiceFiscale() + "';";
        } else if (p instanceof Child) {
            Child c = (Child) p;
            update = "UPDATE BAMBINO"
                    + " SET  Nome='" + p.getNome() + "', Cognome='" + p.getCognome() + "', DataN ='" + c.getDataNascita() + "'"
                    + " WHERE CodF='" + p.getCodiceFiscale() + "';";
        } else if (p instanceof Contact) {
            Contact o = (Contact) p;
            update = "UPDATE ESTERNO"
                    + " SET  Nome='" + p.getNome() + "', Cognome='" + p.getCognome() + "', Cell ='" + o.getCellulare() + "', TypeFlag='" + o.getTipo() + "'"
                    + " WHERE CodF='" + p.getCodiceFiscale() + "';";
        }

        return update;
    }

    //Select children from db so as to send them to clients
    public List<Child> selectChildrenFromDB() throws SQLException {

        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM Bambino;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Child child = new Child();
            child.setCodiceFiscale(rs.getString("codF"));
            child.setNome(rs.getString("Nome"));
            child.setCognome(rs.getString("Cognome"));
            child.setDataNascita(rs.getString("DataN"));
            child.setCodR(rs.getString("codR"));
            child.setCodiceFiscaleGen1(rs.getString("codFGen1"));
            child.setCodiceFiscaleGen2(rs.getString("codFGen2"));
            child.setCodiceFiscalePediatra(rs.getString("codFPed"));
            childrenList.add(child);
        }

        if(childrenList.size()>0)
            return childrenList;
        else
            return null;
    }

    public List<Contact> selectContactsFromDB() throws SQLException {

        List<Contact> contactList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM ESTERNO;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Contact contact = new Contact();
            contact.setCodiceFiscale(rs.getString("codF"));
            contact.setNome(rs.getString("Nome"));
            contact.setCognome(rs.getString("Cognome"));
            contact.setCellulare(rs.getString("Cell"));
            contact.setTipo(rs.getString("TypeFlag"));

            contactList.add(contact);
        }

        if(contactList.size()>0)
            return contactList;
        else
            return null;
    }

    public List<Supplier> selectSuppliersFromDB() throws SQLException {

        List<Supplier> suppliersList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM FORNITORE;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Supplier supplier = new Supplier();

            supplier.setpIva(rs.getString("PIva"));
            supplier.setNomeF(rs.getString("NomeF"));
            supplier.setEmail(rs.getString("Email"));
            supplier.setTel(rs.getString("Tel"));

            suppliersList.add(supplier);
        }

        if(suppliersList.size()>0)
            return suppliersList;
        else
            return null;
    }

    public List<Staff> selectStaffFromDB() throws SQLException {

        List<Staff> staffList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM PERSONALE;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Staff staff = new Staff();
            staff.setCodiceFiscale(rs.getString("codF"));
            staff.setUsername(rs.getString("Username"));
            staff.setNome(rs.getString("Nome"));
            staff.setCognome(rs.getString("Cognome"));
            staff.setDataNascita(rs.getString("DataN"));
            staff.setTipo(rs.getString("TypeFlag"));

            staffList.add(staff);
        }

        if(staffList.size()>0)
            return staffList;
        else
            return null;
    }

    public List<Contact> selectParentsForChild(String childCodF) throws SQLException {
        List<Contact> contactList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT ESTERNO.* FROM  BAMBINO,ESTERNO WHERE BAMBINO.CodF='" + childCodF + "' AND (BAMBINO.CodFGen1=ESTERNO.CodF OR BAMBINO.CodFGen2=ESTERNO.CodF);";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Contact contact = new Contact();
            contact.setCodiceFiscale(rs.getString("CodF"));
            contact.setNome(rs.getString("Nome"));
            contact.setCognome(rs.getString("Cognome"));
            contact.setCellulare(rs.getString("Cell"));
            contact.setTipo(rs.getString("TypeFlag"));

            contactList.add(contact);
        }

        if(contactList.size()>0)
            return contactList;
        else
            return null;
    }

    public boolean deleteFromDb(String subject, String toDelId) {
        Connection conn = myPool.getConnection();

        Statement stmt = null;
        boolean status = false;

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(generateSQLDeleteStatement(subject, toDelId)) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a delete exception
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    private String generateSQLDeleteStatement(String subject, String toDelId) {
        String delete = null;

        if (subject.equals("Contact")) {
            delete = "DELETE FROM ESTERNO WHERE CodF='" + toDelId + "';";
        } else if (subject.equals("Child")) {
            delete = "DELETE FROM BAMBINO WHERE CodF='" + toDelId + "';";
        } else if (subject.equals("Supplier")) {
            delete = "DELETE FROM FORNITORE WHERE PIva='" + toDelId + "';";

        } else if (subject.equals("Staff")) {
            delete = "DELETE FROM PERSONALE WHERE CodF='" + toDelId + "';";

        }//More delete statement

        return delete;
    }

    public boolean insertTripIntoDb(Trip trip) {

        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO GITA"
                + "(NomeG, DataG, Destinazione, Partenza)"
                + " VALUES('" + trip.getNomeGita() + "','" + trip.getData() + "','" + trip.getDestinazione() + "' ,'" + trip.getPartenza() + "')" +
                "ON DUPLICATE KEY UPDATE Destinazione='"+trip.getDestinazione()+"' AND Partenza='"+trip.getPartenza()+"';";
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertBusIntoDb(Bus bus) {

        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO AUTOBUS"
                + "(Targa, NomeA, NomeG, DataG) "
                + " VALUES('" + bus.getTarga() + "','" + bus.getNomeA() + "' ,'" + bus.getNomeG() + "', '" + bus.getDataG() + "')"
                + " ON DUPLICATE KEY UPDATE NomeA='" + bus.getNomeA() + "';";
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            //System.out.println("Double entry in insert");
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertNewStopIntoDb(Stop stop) {

        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO TAPPA"
                + "(Luogo, Targa, NomeG, DataG)"
                + " VALUES('" + stop.getLuogo() + "','" + stop.getTarga() + "','" + stop.getNomeGita() + "' ,'" + stop.getDataGita() + "');";
        try {
            if (stmt.executeUpdate(sql) == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertStopPresencesIntoDb(List<StopPresence> list) {

        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (StopPresence sp : list) {
            String sql = "INSERT INTO PRESENZATAPPA"
                    + " (CodF, NumTappa, Targa, NomeG, DataG)"
                    + " VALUES('" + sp.getCodF() + "','" + sp.getNumTappa() + "' ,'" + sp.getTarga() + "' ,'" + sp.getNomeG() + "' ,'" + sp.getDataG() + "')"
                    + " ON DUPLICATE KEY UPDATE CodF='"+sp.getCodF()+"';";
            try {
                if (stmt.executeUpdate(sql) == 1)
                    status = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                myPool.releaseConnection(conn);
                return status;
            }
        }
        myPool.releaseConnection(conn);
        return status;
    }

    public List<Trip> selectAllTripsFromDb() throws SQLException {

        List<Trip> tripList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM GITA WHERE DataG>=DATE(SYSDATE());";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {

            Trip trip = new Trip();

            trip.setNomeGita(rs.getString("NomeG"));
            trip.setData(rs.getString("DataG"));
            trip.setPartenza(rs.getString("Partenza"));
            trip.setDestinazione(rs.getString("Destinazione"));

            tripList.add(trip);
        }

        if(tripList.size()>0)
            return tripList;
        else
            return null;
    }

    public List<Stop> selectAllStopsFromBus(String nomeG, String dataG, String targa) throws SQLException {

        List<Stop> stopList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM TAPPA WHERE NomeG='" + nomeG + "' AND DataG='" + dataG + "' AND Targa='" + targa + "';";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {

            Stop stop = new Stop();

            stop.setNumeroTappa(Integer.parseInt(rs.getString("NumTappa")));
            stop.setLuogo(rs.getString("Luogo"));
            stop.setTarga(rs.getString("Targa"));
            stop.setNomeGita(rs.getString("NomeG"));
            stop.setDataGita(rs.getString("DataG"));

            stopList.add(stop);
        }

        if(stopList.size()>0)
            return stopList;
        else
            return null;
    }

    public List<Bus> selectAllBusForTripFromDb(String nomeG, String dataG) throws SQLException {

        List<Bus> busList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM AUTOBUS WHERE NomeG='" + nomeG + "' AND DataG='" + dataG + "';";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {

            Bus bus = new Bus();

            bus.setNomeG(rs.getString("NomeG"));
            bus.setDataG(rs.getString("DataG"));
            bus.setTarga(rs.getString("Targa"));
            bus.setNomeA(rs.getString("NomeA"));

            busList.add(bus);
        }

        if(busList.size()>0)
            return busList;
        else
            return null;
    }

    public boolean deleteStopOrBusOrTripFromDb(String subject, String nomeG, String dataG, Integer numTappa, String targa){

        Statement stmt = null;
        boolean status = false;
        String sql=null;

        if (subject.equals("Trip")) {
            sql = "DELETE FROM GITA WHERE NomeG='" +nomeG+ "' AND DataG='" +dataG+ "' ;";

        } else if (subject.equals("Bus")) {
            sql = "DELETE FROM AUTOBUS WHERE NomeG='" +nomeG+ "' AND DataG='" +dataG+ "' AND targa='" +targa+ "';";

        } else if (subject.equals("Stop")) {
            sql = "DELETE FROM TAPPA WHERE NomeG='" +nomeG+ "' AND DataG='" +dataG+ "'AND targa='" +targa+ "' AND NumTappa='" +numTappa+ "' ;";

        }

        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a delete exception
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    public boolean deleteStopPresenceFromDb(StopPresence sp){
        Statement stmt = null;
        boolean status = false;
        String sql=null;

        sql = "DELETE FROM PRESENZATAPPA WHERE NomeG='" +sp.getNomeG()+ "' AND DataG='" +sp.getDataG()+
                "'AND targa='" +sp.getTarga()+ "' AND NumTappa='" +sp.getNumTappa()+ "' AND CodF='"+sp.getCodF()+"' ;";


        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a delete exception
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    //return false in case of double key exception
    public boolean updateTripIntoDb(Trip oldTrip, Trip newTrip){

        boolean status = false;
        Connection conn = myPool.getConnection();

        Statement stmt = null;
        String sql = "UPDATE GITA SET  NomeG='" + newTrip.getNomeGita() + "', DataG='" + newTrip.getData() + "', Destinazione='" + newTrip.getDestinazione() + "', Partenza='"+newTrip.getPartenza()+"'"
                +" WHERE NomeG='" +oldTrip.getNomeGita() + "' AND DataG='"+oldTrip.getData()+"';";

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;

        } catch (SQLException ex) { //Handle a double key exception
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    //return false in case of double key exception
    public boolean updateBusIntoDb(Bus oldBus, Bus newBus){
        boolean status = false;
        Connection conn = myPool.getConnection();

        Statement stmt = null;
        String sql = "UPDATE AUTOBUS SET  NomeG='" + newBus.getNomeG() + "', DataG='" + newBus.getDataG() + "', Targa='" + newBus.getTarga() + "', NomeA='" + newBus.getNomeA() + "' "
                +" WHERE NomeG='" +oldBus.getNomeG() + "' AND DataG='"+oldBus.getDataG()+"' AND Targa='"+oldBus.getTarga()+"';";

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;

        } catch (SQLException ex) { //Handle a double key exception
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertTripSubscriptionsIntoDb(List<BusAssociation> list){

        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (BusAssociation ba: list) {
            String sql = "INSERT INTO ASSEGNAZIONEAUTOBUS"
                    + " (CodF, Targa, NomeG, DataG)"
                    + " VALUES('" + ba.getCodF() + "','" + ba.getTarga() + "' ,'" + ba.getNomeG() + "' ,'" + ba.getDataG() + "')"
                    + " ON DUPLICATE KEY UPDATE CodF='"+ba.getCodF()+"';";
            try {
                if (stmt.executeUpdate(sql) == 1)
                    status = true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                myPool.releaseConnection(conn);
                return status;
            }
        }
        myPool.releaseConnection(conn);
        return status;
    }

    public boolean deleteBusAssociationFromDb(BusAssociation ba){

        Statement stmt = null;
        boolean status = false;
        String sql;

        sql = "DELETE FROM ASSEGNAZIONEAUTOBUS WHERE NomeG='" +ba.getNomeG()+ "' AND DataG='" +ba.getDataG()+
                "'AND targa='" +ba.getTarga()+ "' AND CodF='"+ba.getCodF()+"' ;";


        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a delete exception
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    public List<Child> selectAvailableChildrenForTripFromDb(String tripDate) throws SQLException{

        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM BAMBINO WHERE CodF NOT IN (" +
                "SELECT CodF FROM ASSEGNAZIONEAUTOBUS WHERE DataG='"+tripDate+"');";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Child child = new Child();
            child.setCodiceFiscale(rs.getString("codF"));
            child.setNome(rs.getString("Nome"));
            child.setCognome(rs.getString("Cognome"));
            child.setDataNascita(rs.getString("DataN"));
            child.setCodR(rs.getString("codR"));
            child.setCodiceFiscaleGen1(rs.getString("codFGen1"));
            child.setCodiceFiscaleGen2(rs.getString("codFGen2"));
            child.setCodiceFiscalePediatra(rs.getString("codFPed"));
            childrenList.add(child);
        }

        if(childrenList.size()>0)
            return childrenList;
        else
            return null;
    }

    public List<Child> selectChildrenForBusFromDb(Bus bus) throws SQLException{
        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM BAMBINO as B JOIN ASSEGNAZIONEAUTOBUS as AB ON B.CodF=AB.CodF " +
                "WHERE AB.Targa='"+bus.getTarga()+"' AND AB.NomeG='"+bus.getNomeG()+"' AND AB.DataG='"+bus.getDataG()+"' ;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Child child = new Child();
            child.setCodiceFiscale(rs.getString("codF"));
            child.setNome(rs.getString("Nome"));
            child.setCognome(rs.getString("Cognome"));
            child.setDataNascita(rs.getString("DataN"));
            child.setCodR(rs.getString("codR"));
            child.setCodiceFiscaleGen1(rs.getString("codFGen1"));
            child.setCodiceFiscaleGen2(rs.getString("codFGen2"));
            child.setCodiceFiscalePediatra(rs.getString("codFPed"));
            childrenList.add(child);
        }

        if(childrenList.size()>0)
            return childrenList;
        else
            return null;
    }

    public List<Child> selectMissingChildrenForStopFromDb(Stop stop) throws SQLException{

        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT B.* " +
                "FROM PRESENZABAMBINO as PB, AssegnazioneAutobus as AB, BAMBINO as B " +
                "WHERE B.CodF=AB.CodF AND PB.CodF=AB.CodF AND DATE(PB.DataOra)='"+stop.getDataGita()+"' AND AB.NomeG='"+stop.getNomeGita()+"' AND AB.Targa='"+stop.getTarga()+"' AND AB.DataG='"+stop.getDataGita()+"' "+
                "AND B.CodF NOT IN (SELECT PT.CodF " +
                "FROM PRESENZATAPPA as PT " +
                "WHERE PT.NomeG='"+stop.getNomeGita()+"' AND PT.Targa='"+stop.getTarga()+"' AND PT.DataG='"+stop.getDataGita()+"' AND PT.NumTappa='"+stop.getNumeroTappa()+"');";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Child child = new Child();
            child.setCodiceFiscale(rs.getString("CodF"));
            child.setNome(rs.getString("Nome"));
            child.setCognome(rs.getString("Cognome"));
            child.setDataNascita(rs.getString("DataN"));
            child.setCodR(rs.getString("CodR"));
            child.setCodiceFiscaleGen1(rs.getString("codFGen1"));
            child.setCodiceFiscaleGen2(rs.getString("codFGen2"));
            child.setCodiceFiscalePediatra(rs.getString("codFPed"));
            childrenList.add(child);
        }

        if(childrenList.size()>0)
            return childrenList;
        else
            return null;
    }

    public boolean insertIngredientIntoDb(String nomeI){
        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO INGREDIENTE (NomeI) VALUES('" + nomeI + "') ON DUPLICATE KEY UPDATE NomeI='" + nomeI + "';";
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            //System.out.println("Double entry in insert");
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertDishIntoDb(Dish dish){
        Statement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO PIATTO (NomeP, Tipo) VALUES('" + dish.getNomeP() + "', '"+dish.getTipo().getOrderNum()+"') ON DUPLICATE KEY UPDATE NomeP='" + dish.getNomeP() + "';";
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            //System.out.println("Double entry in insert");
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean deleteDishFromDb(Dish dish){
        Statement stmt = null;
        boolean status = false;
        String sql=null;

        sql = "DELETE FROM PIATTO WHERE NomeP='" +dish.getNomeP()+ "' ;";


        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a delete exception
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    public boolean insertIntoleranceIntoDb(Intolerance intolerance){
        Statement stmt = null;
        boolean status = false;
        String sql;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(intolerance instanceof ChildIntolerance)
            sql = "INSERT INTO INTOLLERANZABAMBINO (CodF, NomeI) VALUES('" + intolerance.getCodF() + "','" + intolerance.getNomeI() + "') ON DUPLICATE KEY UPDATE NomeI='" + intolerance.getNomeI() + "';";
        else if(intolerance instanceof PersonIntolerance)
            sql= "INSERT INTO INTOLLERANZAPERSONALE (CodF, NomeI) VALUES('" + intolerance.getCodF() + "','" + intolerance.getNomeI() + "') ON DUPLICATE KEY UPDATE NomeI='" +intolerance.getNomeI()+ "';";
        else
            return false;

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            //System.out.println("Double entry in insert");
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean deleteIntoleranceFromDb(Intolerance intolerance){
        Statement stmt = null;
        boolean status = false;
        String sql;
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(intolerance instanceof ChildIntolerance)
            sql = "DELETE FROM INTOLLERANZABAMBINO WHERE CodF='" + intolerance.getCodF() + "' AND NomeI='" + intolerance.getNomeI() + "' ;";
        else if(intolerance instanceof PersonIntolerance)
            sql= "DELETE FROM INTOLLERANZAPERSONALE WHERE CodF='" + intolerance.getCodF() + "' AND NomeI='" + intolerance.getNomeI() + "' ;";
        else
            return false;

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            //System.out.println("Double entry in insert");
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

   /*public static void main(String[] args) throws SQLException {
        List<Child> list = null;
        try {
            list= DMLCommandExecutor.getInstance().selectAvailableChildrenForTripFromDb("2018-06-06");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DMLCommandExecutor.getInstance().insertIngredientIntoDb("Mais");
        DMLCommandExecutor.getInstance().insertDishIntoDb(new Dish("Tacos de lechon", Dish.DishTypeFlag.ANTIPASTO));
        DMLCommandExecutor.getInstance().insertIntoleranceIntoDb(new ChildIntolerance("TTTTTTTTTTTTTTTT", "Mais"));
        DMLCommandExecutor.getInstance().insertIntoleranceIntoDb(new PersonIntolerance("LLLLLLLLLLLLLLLL", "Mais"));
        DMLCommandExecutor.getInstance().deleteIntoleranceFromDb(new PersonIntolerance("LLLLLLLLLLLLLLLL", "Mais"));
    }*/

}