package main;

import java.sql.*;
import java.util.List;
import java.util.Vector;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.parseBoolean;

public class ConnectionPool {

    private List<Connection> connectionList = new Vector<>();

    private List<Boolean> busyList = new Vector<>();

    // private List<String> activeUserList = new Vector<>();


    private static final int MINSIZE = 2;

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/ChildCareTech";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "provafinale";

    //Populate the pool
    public ConnectionPool(){

        try {
            Class.forName("com.mysql.jdbc.Driver");

            for (int i = 0; i < MINSIZE; i++) {
                connectionList.add(DriverManager.getConnection(DB_URL, USER, PASS));
                busyList.add(new Boolean("False"));
            }
        }
        catch(Exception e){
            System.out.println("Error in creating connection pool");
            e.printStackTrace();
        }
    }


    //Assign a connection to the user 'who'
    public synchronized Connection getConnection(){

        for(int i = 0; i<connectionList.size(); i++){
            if(busyList.get(i).booleanValue()==FALSE){
                busyList.set(i, new Boolean("True"));
                return connectionList.get(i);
            }
        }
        //There are no available connections
        try {
            Connection newConn = DriverManager.getConnection(DB_URL,USER,PASS);
            connectionList.add(newConn);
            busyList.add(new Boolean("True"));
            return newConn;

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



    //Sign a connection as free
    public synchronized void releaseConnection(Connection givenC){

        for (int i = 0; i<connectionList.size(); i++) {
            if (connectionList.get(i) == givenC){
                busyList.set(i, new Boolean("False"));
            }
        }
    }

    public synchronized void printStatusConnection(){
        String result = "";
        for (int i = 0; i < connectionList.size(); i++) {
            result = "Conn. " + i + ": " + busyList.get(i).booleanValue();
            System.out.println(result);
        }
    }

    public static void main(String[] args){
        ConnectionPool myPool = new ConnectionPool();
        myPool.printStatusConnection();
        Connection c1 =  myPool.getConnection();
        myPool.getConnection();
        myPool.printStatusConnection();
        myPool.getConnection();
        myPool.getConnection();
        myPool.printStatusConnection();
        myPool.releaseConnection(c1);
        myPool.printStatusConnection();

    }

}