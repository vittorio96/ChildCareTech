package test.RMITest.TestAnagrafica;

import main.Client;
import main.RmiMode;
import main.SessionMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

class RmiModeTestAnagrafica {

    private Client c;
    private SessionMode rmiMode;

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
        this. rmiMode = new RmiMode(c);
    }

    @AfterClass
    public void testSessionDisconnect() {
        this.rmiMode.disconnect();
    }

    RmiModeTestAnagrafica() throws RemoteException, NotBoundException, MalformedURLException {
    }

    @Test
    void login(){
    }

    @Test
    void insertPersonIntoDb() {
    }

    @Test
    void extractChildrenFromDb() {
    }

    @Test
    void disconnect() {
    }

    @Test
    void insertSupplierIntoDb() {
    }

    @Test
    void updatePersonIntoDb() {
    }

    @Test
    void updateSupplierIntoDb() {
    }

    @Test
    void extractContactsFromDb() {
    }

    @Test
    void extractSuppliersFromDb() {
    }

    @Test
    void extractStaffFromDb() {
    }

    @Test
    void extractParentsForChild() {
    }

    @Test
    void deleteSubjectFromDb() {
    }

}