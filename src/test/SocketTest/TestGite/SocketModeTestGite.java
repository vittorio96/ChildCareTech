package SocketTest.TestGite;

import main.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class SocketModeTestGite {

    private static Client c;
    private static SessionMode socketMode;

    @BeforeClass
    public static void testSetUp() {

        SocketServer.main(null);

        try {
            c = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        socketMode = new SocketMode(c);
    }

    @AfterClass
    public static void testSessionDisconnect() {
        socketMode.disconnect();
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
    public void extractAllTripsFromDb() throws Exception {
    }

    @Test
    public void extractAllBusesForTrip() throws Exception {
    }

    @Test
    public void extractAllStopsFromBus() throws Exception {
    }

    @Test
    public void insertStopPresencesIntoDb() throws Exception {
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