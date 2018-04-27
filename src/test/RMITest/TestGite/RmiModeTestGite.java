package RMITest.TestGite;

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

public class RmiModeTestGite {

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

    @Test
    public void insertTripIntoDb() throws Exception {
    }

    @Test
    public void insertBusIntoDb() throws Exception {
    }

    @Test
    public void insertNewStopIntoDb() throws Exception {
    }

    @Test
    public void insertStopPresencesIntoDb() throws Exception {
    }

    @Test
    public void extractAllTripsFromDb() throws Exception {
    }

    @Test
    public void extractAllStopsFromBus() throws Exception {
    }

    @Test
    public void extractAllBusesForTrip() throws Exception {
    }

    @Test
    public void deleteTripFromDb() throws Exception {
    }

    @Test
    public void deleteBusFromDb() throws Exception {
    }

    @Test
    public void deleteStopFromDb() throws Exception {
    }

    @Test
    public void deleteStopPresenceFromDb() throws Exception {
    }

    @Test
    public void updateTripIntoDb() throws Exception {
    }

    @Test
    public void updateBusIntoDb() throws Exception {
    }

    @Test
    public void insertBusAssociationsIntoDb() throws Exception {
    }

    @Test
    public void deleteBusAssociationFromDb() throws Exception {
    }

    @Test
    public void extractAvailableChildrenForTripFromDb() throws Exception {
    }

    @Test
    public void insertChildDailyPresenceIntoDb() throws Exception {
    }

    @Test
    public void insertPersonDailyPresenceIntoDb() throws Exception {
    }

    @Test
    public void extractMissingChildrenForStopFromDb() throws Exception {
    }

    @Test
    public void extractChildrenForBusFromDb() throws Exception {
    }

}