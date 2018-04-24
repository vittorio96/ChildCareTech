package test.SocketTest.TestAnagrafica;

import main.Client;
import main.RmiMode;
import main.SessionMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class SocketModeTestAnagrafica {

    private Client c;
    private SessionMode socketMode;

    @BeforeClass
    public void testSetUp() {
        try {
            this.c = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        this.socketMode = new RmiMode(c);
    }

    @AfterClass
    public void testSessionDisconnect() {
        this.socketMode.disconnect();
    }

    @Test
    public void login() throws Exception {
    }

    @Test
    public void insertPersonIntoDb() throws Exception {
    }

    @Test
    public void extractChildrenFromDb() throws Exception {
    }

    @Test
    public void disconnect() throws Exception {
    }

    @Test
    public void insertSupplierIntoDb() throws Exception {
    }

    @Test
    public void updatePersonIntoDb() throws Exception {
    }

    @Test
    public void updateSupplierIntoDb() throws Exception {
    }

    @Test
    public void extractContactsFromDb() throws Exception {
    }

    @Test
    public void extractSuppliersFromDb() throws Exception {
    }

    @Test
    public void extractStaffFromDb() throws Exception {
    }

    @Test
    public void extractParentsForChild() throws Exception {
    }

    @Test
    public void deleteSubjectFromDb() throws Exception {
    }

}