package main;

import main.Classes.NormalClasses.Anagrafica.*;
import main.Classes.NormalClasses.Gite.*;
import main.Classes.NormalClasses.Mensa.*;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DMLCommandExecutorSQLInjectionFree {

    private static DMLCommandExecutor instance = new DMLCommandExecutor();//Singleton

    public static synchronized DMLCommandExecutor getInstance() {
        return instance;
    }

    private ConnectionPool myPool;
    private ArrayList<Client> subscribers = new ArrayList<>();


    public DMLCommandExecutorSQLInjectionFree() {

        this.myPool = new ConnectionPool();
    }

    public boolean insertChildDailyPresenceIntoDb(String codF){
        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "INSERT INTO PRESENZABAMBINO (CodF, DataOra) VALUES( ?, ? );";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, codF);
            stmt.setString(2, "sysdate()");
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "INSERT INTO PRESENZAPERSONALE (CodF, DataOra) VALUES( ?, ? );";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, codF);
            stmt.setString(2, "sysdate()");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println(ex);
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

        String sql = "SELECT * FROM Personale WHERE Personale.Username= ?;";

        Connection conn = myPool.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);

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

        try {
            if (generateSQLInsertStatementAndExecute(conn, p))
                status = true;
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println("Not possible insert of new person");
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    //Generate a SQL statement for insertPersonIntoDb method
    private boolean generateSQLInsertStatementAndExecute(Connection conn, Person p) throws SQLException {//S
        String query = null;
        PreparedStatement stmt = null;

        if (p instanceof Staff) {
            Staff s = (Staff) p;

            query = "INSERT INTO PERSONALE"
                    + "(CodF, Nome, Cognome, Username, Password, DataN, TypeFlag) " +
                    "VALUES(?,?,?,?,?,?,?);";

            stmt = conn.prepareStatement(query);

            stmt.setString(1,s.getCodiceFiscale());
            stmt.setString(2,s.getNome());
            stmt.setString(3,s.getCognome());
            stmt.setString(4,s.getUsername());
            stmt.setString(5,s.getPassword());
            stmt.setString(6,s.getDataNascita());
            stmt.setString(7,s.getTipo());

        } else if (p instanceof Child) {
            Child c = (Child) p;
            query = "INSERT INTO BAMBINO"
                    + "(CodF, Nome, Cognome, DataN, CodFGen1, CodFGen2, CodFPed) " +
                    "VALUES(?,?,?,?,?,?,?);";

            stmt = conn.prepareStatement(query);

            stmt.setString(1,c.getCodiceFiscale());
            stmt.setString(2,c.getNome());
            stmt.setString(3,c.getCognome());
            stmt.setString(4,c.getDataNascita());
            stmt.setString(5,c.getCodiceFiscaleGen1());
            stmt.setString(6,c.getCodiceFiscaleGen2());
            stmt.setString(7,c.getCodiceFiscalePediatra());

        } else if (p instanceof Contact) {
            Contact o = (Contact) p;

            query = "INSERT INTO ESTERNO"
                    + "(CodF, Nome, Cognome, Cell, TypeFlag) " +
                    "VALUES(?,?,?,?,?);";

            stmt = conn.prepareStatement(query);

            stmt.setString(1,o.getCodiceFiscale());
            stmt.setString(2,o.getNome());
            stmt.setString(3,o.getCognome());
            stmt.setString(4,o.getCellulare());
            stmt.setString(5,o.getTipo());
        }

        return stmt.executeUpdate(query) == 1;
    }

    //Insert a supplier into db
    public boolean insertSupplierIntoDb(Supplier s) {

        Connection conn = myPool.getConnection();
        boolean status = false;
        PreparedStatement stmt = null;
        String sql = "INSERT INTO FORNITORE"
                + " (PIva, NomeF, Tel, Email)"
                + " VALUES(?,?,?,?)"
                + " ON DUPLICATE KEY UPDATE NomeF= ? , Tel=? , Email= ? ;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,s.getpIva());
            stmt.setString(2,s.getNomeF());
            stmt.setString(3,s.getTel());
            stmt.setString(4,s.getEmail());

            stmt.setString(5,s.getNomeF());
            stmt.setString(6,s.getTel());
            stmt.setString(7,s.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println("Not possible insert of new supplier into db");
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    //Update person data
    public boolean updatePersonIntoDb(Person p) {

        Connection conn = myPool.getConnection();

        try {
            if (generateSQLUpdateAndExecuteStatement(conn, p) == true)
                return true;
            else
                return false;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        } finally {
            myPool.releaseConnection(conn);
        }
    }

    public boolean updateSupplierIntoDb(Supplier s) {
        Connection conn = myPool.getConnection();

        PreparedStatement stmt = null;
        String sql = "UPDATE FORNITORE SET  NomeF= ?, Tel=?, Email= ? WHERE PIva= ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,s.getNomeF());
            stmt.setString(2,s.getTel());
            stmt.setString(3,s.getEmail());
            stmt.setString(4,s.getpIva());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                return true;
            else
                return false;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        } finally {
            myPool.releaseConnection(conn);
        }
    }

    private boolean generateSQLUpdateAndExecuteStatement(Connection conn, Person p) throws SQLException {
        String update = null;
        PreparedStatement stmt = null;
        if (p instanceof Staff) {
            Staff s = (Staff) p;

            update = "UPDATE PERSONALE SET  Nome= ? , Cognome= ?, DataN = ?, TypeFlag= ? WHERE CodF= ?;";
            stmt = conn.prepareStatement(update);

            stmt.setString(1,s.getNome());
            stmt.setString(2,s.getCognome());
            stmt.setString(3,s.getDataNascita());
            stmt.setString(4,s.getTipo());
            stmt.setString(5,s.getCodiceFiscale());
        } else if (p instanceof Child) {
            Child c = (Child) p;
            update = "UPDATE BAMBINO SET  Nome= ?, Cognome= ?, DataN = ? WHERE CodF= ?;";
            stmt = conn.prepareStatement(update);

            stmt.setString(1,c.getNome());
            stmt.setString(2,c.getCognome());
            stmt.setString(3,c.getDataNascita());
            stmt.setString(4,c.getCodiceFiscale());

        } else if (p instanceof Contact) {
            Contact o = (Contact) p;
            update = "UPDATE ESTERNO SET  Nome=?, Cognome=?, Cell =?, TypeFlag= ? WHERE CodF=?;";
            stmt = conn.prepareStatement(update);

            stmt.setString(1,o.getNome());
            stmt.setString(2,o.getCognome());
            stmt.setString(3,o.getCellulare());
            stmt.setString(4,o.getTipo());
            stmt.setString(5,o.getCodiceFiscale());
        }

        return stmt.executeUpdate(update) == 1;
    }

    //Select children from db so as to send them to clients
    public List<Child> selectChildrenFromDB() throws SQLException {

        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM Bambino ORDER BY Cognome;";
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

        String sql = "SELECT * FROM ESTERNO ORDER BY Cognome;";
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

        String sql = "SELECT * FROM FORNITORE ORDER BY NomeF;";
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

        String sql = "SELECT * FROM PERSONALE ORDER BY Cognome;";
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

    public boolean deletePersonFromDb(String subject, String toDelId) {
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

        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        String sql = "INSERT INTO GITA"
                + "(NomeG, DataG, Destinazione, Partenza)"
                + " VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE Destinazione= ?, Partenza= ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,trip.getNomeGita());
            stmt.setString(2,trip.getData());
            stmt.setString(3,trip.getDestinazione());
            stmt.setString(4,trip.getPartenza());
            stmt.setString(5,trip.getDestinazione());
            stmt.setString(6,trip.getPartenza());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println("Not possible insert of new trip");
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertBusIntoDb(Bus bus) {

        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "INSERT INTO AUTOBUS (Targa, NomeA, NomeG, DataG) VALUES(?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE NomeA=?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,bus.getTarga());
            stmt.setString(2,bus.getNomeA());
            stmt.setString(3,bus.getNomeG());
            stmt.setString(4,bus.getDataG());
            stmt.setString(5,bus.getNomeA());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            System.out.println("Not possible insert of new bus");
            System.out.println(ex);
            //ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertNewStopIntoDb(Stop stop) {

        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        String sql = "INSERT INTO TAPPA (Luogo, Targa, NomeG, DataG) VALUES(?,?,?,?);";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,stop.getLuogo());
            stmt.setString(2,stop.getTarga());
            stmt.setString(3,stop.getNomeGita());
            stmt.setString(4,stop.getDataGita());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1) {
                status = true;
            }
        } catch (SQLException ex) {
            System.out.println("Not possible insert of new stop");
            System.out.println(ex);
            //ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertStopPresencesIntoDb(List<StopPresence> list) {

        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        if(list == null)
            return false;

        for (StopPresence sp : list) {
            String sql = "INSERT INTO PRESENZATAPPA"
                    + " (CodF, NumTappa, Targa, NomeG, DataG)"
                    + " VALUES(?,?,?,?,?)ON DUPLICATE KEY UPDATE CodF=?;";
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1,sp.getCodF());
                stmt.setString(2,Integer.toString(sp.getNumTappa()));
                stmt.setString(3,sp.getTarga());
                stmt.setString(4,sp.getNomeG());
                stmt.setString(5,sp.getDataG());
                stmt.setString(6,sp.getCodF());
                if (stmt.executeUpdate(sql) == 1)
                    status = true;
            } catch (SQLException ex) {
                //ex.printStackTrace();
                System.out.println("Not possible insert of new stop presence");
                System.out.println(ex);
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

        String sql = "SELECT * FROM GITA WHERE DataG>=DATE(SYSDATE()) ORDER BY DataG;";
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

        String sql = "SELECT * FROM TAPPA WHERE NomeG='" + nomeG + "' AND DataG='" + dataG + "' AND Targa='" + targa + "' ORDER BY NumTappa;";
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

    public List<Bus> selectAllBusesForTripFromDb(String nomeG, String dataG) throws SQLException {

        List<Bus> busList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM AUTOBUS WHERE NomeG='" + nomeG + "' AND DataG='" + dataG + "' ORDER BY NomeA;";
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

        PreparedStatement stmt = null;
        boolean status = false;
        String sql=null;
        Connection conn = myPool.getConnection();
        try{
            if (subject.equals("Trip")) {
                sql = "DELETE FROM GITA WHERE NomeG= ? AND DataG= ? ;";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1,nomeG);
                stmt.setString(2,dataG);

            } else if (subject.equals("Bus")) {
                sql = "DELETE FROM AUTOBUS WHERE NomeG= ? AND DataG= ? AND targa= ?;";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1,nomeG);
                stmt.setString(2,dataG);
                stmt.setString(3,targa);


            } else if (subject.equals("Stop")) {
                sql = "DELETE FROM TAPPA WHERE NomeG= ? AND DataG= ? AND targa= ? AND NumTappa= ? ;";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1,nomeG);
                stmt.setString(2,dataG);
                stmt.setString(3,targa);
                stmt.setString(4,Integer.toString(numTappa));




            }

            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    public boolean deleteStopPresenceFromDb(StopPresence sp){
        PreparedStatement stmt = null;
        boolean status = false;
        String sql=null;

        sql = "DELETE FROM PRESENZATAPPA WHERE NomeG='" +sp.getNomeG()+ "' AND DataG='" +sp.getDataG()+
                "'AND targa='" +sp.getTarga()+ "' AND NumTappa='" +sp.getNumTappa()+ "' AND CodF='"+sp.getCodF()+"' ;";


        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
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

        PreparedStatement stmt = null;
        String sql = "UPDATE GITA SET  NomeG= ?, DataG= ?, Destinazione= ?, Partenza= ?"
                +" WHERE NomeG= ? AND DataG= ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,newTrip.getNomeGita());
            stmt.setString(2,newTrip.getData());
            stmt.setString(3,newTrip.getDestinazione());
            stmt.setString(4,newTrip.getPartenza());

            stmt.setString(5,oldTrip.getNomeGita());
            stmt.setString(6,oldTrip.getData());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;

        } catch (SQLException ex) {
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

        PreparedStatement stmt = null;
        String sql = "UPDATE AUTOBUS SET  NomeG= ?, DataG= ?, Targa= ?, NomeA= ?" +
                " WHERE NomeG= ? AND DataG= ? AND Targa=?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,newBus.getNomeG());
            stmt.setString(2,newBus.getDataG());
            stmt.setString(3,newBus.getTarga());
            stmt.setString(4,newBus.getNomeA());
            stmt.setString(5,oldBus.getDataG());
            stmt.setString(6,oldBus.getTarga());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertTripSubscriptionsIntoDb(List<BusAssociation> list){

        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        try {

            for (BusAssociation ba: list) {
                String sql = "INSERT INTO ASSEGNAZIONEAUTOBUS"
                        + " (CodF, Targa, NomeG, DataG)"
                        + " VALUES(?,?,?,?)"
                        + " ON DUPLICATE KEY UPDATE CodF=?;";

                stmt = conn.prepareStatement(sql);
                stmt.setString(1,ba.getCodF());
                stmt.setString(2,ba.getTarga());
                stmt.setString(3,ba.getNomeG());
                stmt.setString(4,ba.getDataG());
                stmt.setString(5,ba.getCodF());

            }
                if (stmt.executeUpdate() == 1)
                    status = true;
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println(ex);
        }finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }
    //TODO k
    public boolean deleteBusAssociationFromDb(BusAssociation ba){

        PreparedStatement stmt = null;
        boolean status = false;
        String sql;

        sql = "DELETE FROM ASSEGNAZIONEAUTOBUS WHERE NomeG= ? AND DataG= ?"+
                "AND targa= ? AND CodF= ? ;";


        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,ba.getNomeG());
            stmt.setString(2,ba.getDataG());
            stmt.setString(3,ba.getTarga());
            stmt.setString(4,ba.getCodF());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    public List<Child> selectAvailableChildrenForTripFromDb(String tripDate) throws SQLException{

        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT * FROM BAMBINO WHERE CodF NOT IN (" +
                "SELECT CodF FROM ASSEGNAZIONEAUTOBUS WHERE DataG=?) ORDER BY Cognome;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tripDate);
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
    //TODO sistemare

    public List<Child> selectChildrenForBusFromDb(Bus bus) throws SQLException{
        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT * FROM BAMBINO as B JOIN ASSEGNAZIONEAUTOBUS as AB ON B.CodF=AB.CodF " +
                "WHERE AB.Targa= ? AND AB.NomeG= ? AND AB.DataG= ? ORDER BY B.Cognome;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,bus.getTarga());
            stmt.setString(2,bus.getNomeG());
            stmt.setString(3,bus.getDataG());
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
        PreparedStatement stmt;

        String sql = "SELECT B.* " +
                "FROM PRESENZABAMBINO as PB, AssegnazioneAutobus as AB, BAMBINO as B " +
                "WHERE B.CodF=AB.CodF AND PB.CodF=AB.CodF AND DATE(PB.DataOra)= ? AND AB.NomeG= ? AND AB.Targa= ? " +
                "AND AB.DataG=? AND B.CodF NOT IN (SELECT PT.CodF FROM PRESENZATAPPA as PT " +
                "WHERE PT.NomeG= ? AND PT.Targa= ? AND PT.DataG= ? AND PT.NumTappa= ?) ORDER BY B.Cognome;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,stop.getDataGita());
            stmt.setString(2,stop.getNomeGita());
            stmt.setString(3,stop.getTarga());
            stmt.setString(4,stop.getDataGita());

            stmt.setString(5,stop.getNomeGita());
            stmt.setString(6,stop.getTarga());
            stmt.setString(7,stop.getDataGita());
            stmt.setString(8,Integer.toString(stop.getNumeroTappa()));
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
        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "INSERT INTO INGREDIENTE (NomeI) VALUES(?) ON DUPLICATE KEY UPDATE NomeI= ?;";


        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,nomeI);
            stmt.setString(2,nomeI);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt.executeUpdate(sql) == 1){
                status = true;
                //notifyUpdates();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            //ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertDishIntoDb(Dish dish){
        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "INSERT INTO PIATTO (NomeP, Tipo) VALUES(?, ?) ON DUPLICATE KEY UPDATE NomeP= ?;";


        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,dish.getNomeP());
            stmt.setString(2, Integer.toString(dish.getTipoPiatto().getOrderNum()));
            stmt.setString(3,dish.getNomeP());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            System.out.println(ex);
            //ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean deleteDishFromDb(Dish dish){
        PreparedStatement stmt = null;
        boolean status = false;
        String sql=null;

        sql = "DELETE FROM PIATTO WHERE NomeP= ? ;";


        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,dish.getNomeP());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }

    }

    public boolean insertIntoleranceIntoDb(Intolerance intolerance){
        PreparedStatement stmt = null;
        boolean status = false;
        String sql = null;
        Connection conn = myPool.getConnection();

        try {

            if(intolerance instanceof ChildIntolerance)
                sql = "INSERT INTO INTOLLERANZABAMBINO (CodF, NomeI) VALUES(?, ?) ON DUPLICATE KEY UPDATE NomeI= ?;";
            else if(intolerance instanceof PersonIntolerance)
                sql= "INSERT INTO INTOLLERANZAPERSONALE (CodF, NomeI) VALUES(?, ?) ON DUPLICATE KEY UPDATE NomeI= ?;";
            else
                return false;

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, intolerance.getCodF());
            stmt.setString(2, intolerance.getNomeI());
            stmt.setString(3, intolerance.getNomeI());


        } catch (SQLException e) {
            e.printStackTrace();
        }



        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean deleteIntoleranceFromDb(Intolerance intolerance){
        PreparedStatement stmt = null;
        boolean status = false;
        String sql = null;
        Connection conn = myPool.getConnection();
        try {

            if(intolerance instanceof ChildIntolerance) {
                sql = "DELETE FROM INTOLLERANZABAMBINO WHERE CodF= ? AND NomeI= ? ;";
            }
            else if(intolerance instanceof PersonIntolerance)
                sql= "DELETE FROM INTOLLERANZAPERSONALE WHERE CodF= ?  AND NomeI= ? ;";
            else
                return false;

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, intolerance.getCodF());
            stmt.setString(2, intolerance.getNomeI());


        } catch (SQLException e) {
            e.printStackTrace();
        }




        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public List<String> selectIngredientsFromDb() throws SQLException{
        List<String> ingredientsList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM INGREDIENTE ORDER BY NomeI";

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
            String nomeI = rs.getString("nomeI");
            ingredientsList.add(nomeI);
        }

        if(ingredientsList.size()>0)
            return ingredientsList;
        else
            return null;
    }

    public List<Menu> selectMenusFromDb() throws SQLException{
        List<Menu> menusList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM MENU ORDER BY CodGiorno";

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
            Menu menu = new Menu();
            menu.setNomeM(rs.getString("NomeM"));
            menu.setCodMenu(Menu.MenuTypeFlag.values()[rs.getInt("CodGiorno")]);
            menusList.add(menu);
        }

        if(menusList.size()>0)
            return menusList;
        else
            return null;
    }

    public List<Dish> selectDishesFromDb() throws SQLException{
        List<Dish> dishesList = new ArrayList<>();
        ResultSet rs;
        Statement stmt;

        String sql = "SELECT * FROM PIATTO ORDER BY NomeP";

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
            Dish dish = new Dish();
            dish.setNomeP(rs.getString("NomeP"));
            dish.setTipoPiatto(Dish.DishTypeFlag.values()[rs.getInt("Tipo")]);
            dishesList.add(dish);
        }

        if(dishesList.size()>0)
            return dishesList;
        else
            return null;
    }

    public List<Child> selectIntolerantsChildrenForIngredientFromDb(String nomeI) throws SQLException{

        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT B.* FROM BAMBINO B JOIN INTOLLERANZABAMBINO IB ON B.CodF = IB.CodF WHERE IB.NomeI = ? ORDER BY B.Cognome;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeI);
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
            childrenList.add(child);
        }

        if(childrenList.size()>0)
            return childrenList;
        else
            return null;
    }

    public List<Staff> selectIntolerantsWorkersForMenuFromDb(Menu.MenuTypeFlag codMenu) throws SQLException{

        List<Staff> staffList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT P.* " +
                "FROM PERSONALE P, INTOLLERANZAPERSONALE IP, COMPOSIZIONEPIATTO CP, COMPOSIZIONEMENU CM " +
                "WHERE P.CodF=IP.CodF AND IP.NomeI=CP.NomeI AND CP.NomeP=CM.NomeP AND CM.CodGiorno= ? ORDER BY P.Cognome;";

        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Integer.toString(codMenu.getOrderNum()));
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println(ex);
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Staff staff = new Staff();
            staff.setCodiceFiscale(rs.getString("CodF"));
            staff.setNome(rs.getString("Nome"));
            staff.setCognome(rs.getString("Cognome"));
            staff.setDataNascita(rs.getString("DataN"));

            staffList.add(staff);
        }

        if(staffList.size()>0)
            return staffList;
        else
            return null;
    }

    public List<Child> selectIntolerantsChildrenForMenuFromDb(Menu.MenuTypeFlag codMenu) throws SQLException{

        List<Child> childrenList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT B.* " +
                "FROM BAMBINO B, INTOLLERANZABAMBINO IB, COMPOSIZIONEPIATTO CP, COMPOSIZIONEMENU CM " +
                "WHERE B.CodF=IB.CodF AND IB.NomeI=CP.NomeI AND CP.NomeP=CM.NomeP AND CM.CodGiorno= ? ORDER BY B.Cognome;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Integer.toString(codMenu.getOrderNum()));
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
            child.setCodR(rs.getString("codR"));
            childrenList.add(child);
        }

        if(childrenList.size()>0)
            return childrenList;
        else
            return null;
    }

    public List<Dish> selectDishesForMenuFromDb(Menu.MenuTypeFlag codMenu) throws SQLException{
        List<Dish> dishesList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT * FROM PIATTO as P JOIN  COMPOSIZIONEMENU as CM ON P.NomeP = CM.NomeP WHERE CM.CodGiorno= ? ORDER BY P.Tipo";

        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Integer.toString(codMenu.getOrderNum()));
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Dish dish = new Dish();
            dish.setNomeP(rs.getString("NomeP"));
            dish.setTipoPiatto(Dish.DishTypeFlag.values()[rs.getInt("Tipo")]);
            dishesList.add(dish);
        }

        if(dishesList.size()>0)
            return dishesList;
        else
            return null;
    }

    public List<String> selectIngredientsForDishFromDb(String nomeP) throws SQLException{
        List<String> ingredientsList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT I.* FROM INGREDIENTE I JOIN COMPOSIZIONEPIATTO AS CP ON I.NomeI=CP.NomeI WHERE CP.NomeP= ? ORDER BY I.NomeI;";

        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeP);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            String nomeI = rs.getString("nomeI");
            ingredientsList.add(nomeI);
        }

        if(ingredientsList.size()>0)
            return ingredientsList;
        else
            return null;
    }

    public boolean insertDishIntoMenuIntoDb(Menu.MenuTypeFlag codMenu, String nomeP){

        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "INSERT INTO COMPOSIZIONEMENU (NomeP, CodGiorno) VALUES(?,?) ON DUPLICATE KEY UPDATE NomeP= ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeP);
            stmt.setString(2, Integer.toString(codMenu.getOrderNum()));
            stmt.setString(3, nomeP);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            //ex.printStackTrace();
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean insertIngredientIntoDishIntoDb(String nomeP, String nomeI){

        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "INSERT INTO COMPOSIZIONEPIATTO (NomeI, NomeP) VALUES(?,?) ON DUPLICATE KEY UPDATE NomeP=?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,nomeI);
            stmt.setString(2,nomeP);
            stmt.setString(3,nomeP);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            //ex.printStackTrace();
            System.out.println(ex);
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean deleteDishFromMenuFromDb(Menu.MenuTypeFlag codMenu, String nomeP){
        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();
        String sql = "DELETE FROM COMPOSIZIONEMENU WHERE NomeP=? AND CodGiorno= ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeP);
            stmt.setString(2, Integer.toString(codMenu.getOrderNum()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            System.out.println(ex);
            //ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public boolean deleteIngredientFromDishFromDb(String nomeP, String nomeI){
        PreparedStatement stmt = null;
        boolean status = false;
        Connection conn = myPool.getConnection();

        String sql = "DELETE FROM COMPOSIZIONEPIATTO WHERE NomeP= ? AND NomeI= ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeP);
            stmt.setString(2, nomeI);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (stmt.executeUpdate(sql) == 1)
                status = true;
        } catch (SQLException ex) { //Handle a double key exception
            ex.printStackTrace();
        } finally {
            myPool.releaseConnection(conn);
            return status;
        }
    }

    public List<Dish> selectDishesForTypeFromDb(Dish.DishTypeFlag dishType) throws SQLException{
        List<Dish> dishesList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT * FROM PIATTO WHERE Tipo=? ORDER BY NomeP";

        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, Integer.toString(dishType.getOrderNum()));
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Dish dish = new Dish();
            dish.setNomeP(rs.getString("NomeP"));
            dish.setTipoPiatto(Dish.DishTypeFlag.values()[rs.getInt("Tipo")]);
            dishesList.add(dish);
        }

        if(dishesList.size()>0)
            return dishesList;
        else
            return null;
    }

    public List<Staff> selectIntolerantsWorkersForIngredientFromDb(String nomeI) throws SQLException{

        List<Staff> peopleList = new ArrayList<>();
        ResultSet rs;
        PreparedStatement stmt;

        String sql = "SELECT P.* FROM PERSONALE P JOIN INTOLLERANZAPERSONALE IP ON P.CodF = IP.CodF WHERE IP.NomeI = ? ORDER BY P.Cognome;";
        Connection conn = myPool.getConnection();

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeI);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            Staff staffMember = new Staff();
            staffMember.setCodiceFiscale(rs.getString("CodF"));
            staffMember.setNome(rs.getString("Nome"));
            staffMember.setCognome(rs.getString("Cognome"));
            staffMember.setTipo(rs.getString("TypeFlag"));
            peopleList.add(staffMember);
        }

        if(peopleList.size()>0)
            return peopleList;
        else
            return null;
    }



    public List<String> selectUntoleratedIngredientsForPersonFromDb(Person p) throws SQLException{
        List<String> ingredientsList = new ArrayList<>();
        ResultSet rs;
        Connection conn = myPool.getConnection();
        PreparedStatement stmt;
        String sql;

        if(p instanceof Child) {
            sql = "SELECT NomeI FROM INTOLLERANZABAMBINO WHERE CodF = ? ORDER BY NomeI;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getCodiceFiscale());
        }
        else if(p instanceof Staff) {
            sql = "SELECT NomeI FROM INTOLLERANZAPERSONALE WHERE CodF = ? ORDER BY NomeI;";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getCodiceFiscale());
        }
        else
            return null;



        try {

            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            String nomeI = rs.getString("NomeI");
            ingredientsList.add(nomeI);
        }

        if(ingredientsList.size()>0)
            return ingredientsList;
        else
            return null;
    }

    public List<String> selectNotUntoleratedIngredientsForPersonFromDb(Person p) throws SQLException{
        List<String> ingredientsList = new ArrayList<>();
        ResultSet rs;
        Connection conn = myPool.getConnection();
        PreparedStatement stmt;
        String sql;

        if(p instanceof Child){
            sql = "SELECT I.* FROM INGREDIENTE I WHERE I.NomeI NOT IN ( " +
                    "SELECT IB.NomeI FROM INTOLLERANZABAMBINO IB WHERE IB.CodF = ?) ORDER BY I.NomeI;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getCodiceFiscale());

        }
        else if(p instanceof Staff) {
            sql = "SELECT I.* FROM INGREDIENTE I WHERE I.NomeI NOT IN (" +
                    "SELECT IP.NomeI FROM INTOLLERANZAPERSONALE IP WHERE IP.CodF = ?) ORDER BY I.NomeI;";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getCodiceFiscale());
        }
        else
            return null;

        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            String nomeI = rs.getString("NomeI");
            ingredientsList.add(nomeI);
        }

        if(ingredientsList.size()>0)
            return ingredientsList;
        else
            return null;
    }

    /*
    public boolean subscribeClient(Client c) {
        subscribers.add(c);
        return subscribers.indexOf(c) > 0;
    }

    private void notifyUpdates(){
        for(Client c: subscribers){
            try {
                c.update();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    */


   /*public static void main(String[] args) throws SQLException {
        List<Child> list = null;
        try {
            list= DMLCommandExecutor.getInstance().selectAvailableChildrenForTripFromDb("2018-06-06");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }*/

    public List<String> selectUntoleratedDishesForPersonOnMenu(Person p, Menu.MenuTypeFlag menu) throws SQLException{
        List<String> dishesList = new ArrayList<>();
        ResultSet rs;
        Connection conn = myPool.getConnection();
        PreparedStatement stmt;
        String sql;

        if(p instanceof Child){
            sql = "SELECT M.NomeP FROM ComposizioneMenu M, ComposizionePiatto P WHERE M.NomeP=P.NomeP " +
                    "and M.CodGiorno = ? and P.NomeI in (SELECT NomeI from IntolleranzaBambino where CodF = ?);";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, menu.getOrderNum());
            stmt.setString(2, p.getCodiceFiscale());

        }
        else if(p instanceof Staff) {
            sql = "SELECT M.NomeP FROM ComposizioneMenu M, ComposizionePiatto P WHERE M.NomeP=P.NomeP " +
                    "and M.CodGiorno = ? and P.NomeI in (SELECT NomeI from IntolleranzaPersonale where CodF = ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, menu.getOrderNum());
            stmt.setString(2, p.getCodiceFiscale());
        }
        else
            return null;

        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            myPool.releaseConnection(conn);
        }

        //Extract data from result set
        while (rs.next()) {
            String nomeP = rs.getString("NomeP");
            dishesList.add(nomeP);
        }

        if(dishesList.size()>0)
            return dishesList;
        else
            return null;
    }

}
