package test.RMITest;

import main.*;
import main.Classes.NormalClasses.Anagrafica.*;
import main.Classes.NormalClasses.Gite.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RmiModeTestGite {

    private static Client c;
    private static SessionMode rmiMode;

    @BeforeClass
    public static void testSetUp() {

        RMIServerLauncher.main(null);

        try {
            c = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        rmiMode = new RmiMode(c);
    }

    @AfterClass
    public static void testSessionDisconnect() {
        rmiMode.disconnect();
    }


    @Test
    public void insertNotExistingTripIntoDb() throws Exception {

        Trip t = generateTrip();
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertTrue(rmiMode.insertTripIntoDb(t));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertExistingTripIntoDb() throws Exception {
        Trip t = generateTrip();
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        rmiMode.insertTripIntoDb(t);
        assertTrue(rmiMode.insertTripIntoDb(t));//On duplicate key update
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertNotExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        rmiMode.insertTripIntoDb(t);
        assertTrue(rmiMode.insertBusIntoDb(b));

        //House keeping operations
        rmiMode.deleteBusFromDb(b.getNomeG(), b.getDataG(), b.getTarga());
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        assertTrue(rmiMode.insertBusIntoDb(b));//On duplicate key update

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade
    }

    @Test
    public void insertBusForNotExistingTripIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertFalse(rmiMode.insertBusIntoDb(b));
    }

    @Test
    public void insertNotExistingStopIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        assertTrue(rmiMode.insertNewStopIntoDb(s));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade is set
    }

    @Test
    public void insertExistingStopIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        rmiMode.insertNewStopIntoDb(s);
        assertTrue(rmiMode.insertNewStopIntoDb(s));//On duplicate key update

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade is set
    }

    @Test
    public void insertStopForNotExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        rmiMode.insertTripIntoDb(t);
        //The bus in not in the db
        assertFalse(rmiMode.insertNewStopIntoDb(s));//On duplicate key update

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade is set
    }

    @Test
    public void insertNotExistingStopPresencesIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<Stop> stopList = rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        assertTrue(rmiMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertExistingStopPresencesIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<Stop> stopList = rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        rmiMode.insertStopPresencesIntoDb(list);
        assertTrue(rmiMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertStopPresencesForNotExistingBusIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<Stop> stopList = rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), "0000000", s.getDataGita(), s.getNomeGita()));//Alter bus identifier

        //Test
        assertFalse(rmiMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertStopPresencesForNotExistingChildIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<Stop> stopList = rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence("AAAAAAAAAAAAAAAA", s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        rmiMode.insertStopPresencesIntoDb(list);
        assertFalse(rmiMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertStopPresencesForNotExistingTripIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<Stop> stopList = rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), "Nessuno"));

        //Test
        rmiMode.insertStopPresencesIntoDb(list);
        assertFalse(rmiMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void extractAllTripsFromDb() throws Exception {
        Trip t = generateTrip();
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up

        rmiMode.insertTripIntoDb(t);
        assertNotNull(rmiMode.extractAllTripsFromDb());
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void extractAllBusesForTrip() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up, on delete cascade

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        assertNotNull(rmiMode.extractAllBusesForTrip(t.getNomeGita(), t.getData()));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void extractAllStopsFromBus() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up, on delete cascade

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);
        assertNotNull(rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga()));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }


    @Test
    public void deleteTripFromDb() throws Exception {
        Trip t = generateTrip();
        rmiMode.insertTripIntoDb(t);//Overwritten if already in db

        assertTrue(rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData()));
    }

    @Test
    public void deleteNotExistingTripFromDb() throws Exception {
        Trip t = generateTrip();
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertFalse(rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData()));
    }

    @Test
    public void deleteBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        assertTrue(rmiMode.deleteBusFromDb(b.getNomeG(), b.getDataG(), b.getTarga()));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteNotExistingBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        rmiMode.insertTripIntoDb(t);

        assertFalse(rmiMode.deleteBusFromDb(b.getNomeG(), b.getDataG(), b.getTarga()));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteStopFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<Stop> stopList = rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);//Extract the stop to get the auto increment value of 'NumTappa'

        assertTrue(rmiMode.deleteStopFromDb(s.getNomeGita(), s.getDataGita(), s.getTarga(),s.getNumeroTappa()));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteNotExistingStopFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        assertFalse(rmiMode.deleteStopFromDb(s.getNomeGita(), s.getDataGita(), s.getTarga(),s.getNumeroTappa()));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteStopPresenceFromDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<Stop> stopList = rmiMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        rmiMode.insertStopPresencesIntoDb(list);
        assertTrue(rmiMode.deleteStopPresenceFromDb(list.get(0)));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void deleteNotExistingStopPresenceFromDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        StopPresence sp = new StopPresence("none", s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita());

        //Test
        assertFalse(rmiMode.deleteStopPresenceFromDb(sp));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void updateTripIntoDb() throws Exception {
        Trip t = generateTrip();
        Trip t1 = generateTrip();
        t1.setNomeGita("Matera e il suo pane");

        rmiMode.insertTripIntoDb(t);

        assertTrue(rmiMode.updateTripIntoDb(t, t1));
        //House keeping operations
        rmiMode.deleteTripFromDb(t1.getNomeGita(), t1.getData());
    }

    @Test
    public void updateNotExistingTripIntoDb() throws Exception {
        Trip t = generateTrip();
        Trip t1 = generateTrip();
        t1.setNomeGita("Matera e il suo pane");

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Trip t is not into db, initial clean-up

        assertFalse(rmiMode.updateTripIntoDb(t, t1));

    }

    @Test
    public void updateBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Bus b1 = generateBusForTrip(t);

        b1.setTarga("HHHJJJV");

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        assertTrue(rmiMode.updateBusIntoDb(b,b1));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void updateNotExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Bus b1 = generateBusForTrip(t);

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        b1.setTarga("HHHJJJV");

        rmiMode.insertTripIntoDb(t);

        assertFalse(rmiMode.updateBusIntoDb(b,b1));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertBusAssociationsIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c);

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        assertTrue(rmiMode.insertBusAssociationsIntoDb(list));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void insertExistingBusAssociationsIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c);

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        rmiMode.insertBusAssociationsIntoDb(list);
        assertTrue(rmiMode.insertBusAssociationsIntoDb(list));//On duplicate key update

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void deleteBusAssociationFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c);

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        rmiMode.insertBusAssociationsIntoDb(list);
        assertTrue(rmiMode.deleteBusAssociationFromDb(list.get(0)));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void deleteNotExistingBusAssociationFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c);

        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        assertFalse(rmiMode.deleteBusAssociationFromDb(list.get(0)));//New bus association is not into db

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractAvailableChildrenForTripFromDb() throws Exception {

    }

    @Test
    public void insertChildDailyPresenceIntoDb() throws Exception {
        Child c = generateChildWithExistingParents();
        rmiMode.insertPersonIntoDb(c);

        assertTrue(rmiMode.insertChildDailyPresenceIntoDb(c.getCodiceFiscale()));

        //House keeping operations
        removeChildWithExistingParents(c);
    }

    @Test
    public void insertPersonDailyPresenceIntoDb() throws Exception {
        Staff s = new Staff("ABBAABBAABBAABBA", "Daniele", "Cardone", "daniele", "daniele", "1995-10-03", User.UserTypeFlag.MENSA);

        rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());

        rmiMode.insertPersonIntoDb(s);
        assertTrue(rmiMode.insertPersonDailyPresenceIntoDb(s.getCodiceFiscale()));
        //House keeping operations
        rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void extractMissingChildrenForStopFromDbTestForNotExistingTrip() throws Exception {

        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertNull(rmiMode.extractMissingChildrenForStopFromDb(s));
    }

    @Test
    public void extractMissingChildrenForStopFromDbTestForNotRegisteredChild() throws Exception {

        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        Child c = generateChildWithExistingParents();

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        rmiMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        rmiMode.insertPersonIntoDb(c);
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));
        rmiMode.insertBusAssociationsIntoDb(list);

        rmiMode.insertChildDailyPresenceIntoDb(c.getCodiceFiscale());//The child is present the day of the trip but not at the stop

        assertNotNull(rmiMode.extractMissingChildrenForStopFromDb(s));
        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractMissingChildrenForStopFromDbTestForRegisteredChild() throws Exception {

        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        Child c = generateChildWithExistingParents();

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        rmiMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        rmiMode.insertPersonIntoDb(c);
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);
        rmiMode.insertNewStopIntoDb(s);

        List<BusAssociation> busAssociationsList = new ArrayList<>();
        busAssociationsList.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));
        rmiMode.insertBusAssociationsIntoDb(busAssociationsList);

        List<StopPresence> stopPresencesList = new ArrayList<>();
        stopPresencesList.add(new StopPresence(c.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        assertNull(rmiMode.extractMissingChildrenForStopFromDb(s));//One child is enrolled and it is present the day of the trip and at the bus stop

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractChildrenForExistingBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Child c = generateChildWithExistingParents();

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        rmiMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        rmiMode.insertPersonIntoDb(c);
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));
        rmiMode.insertBusAssociationsIntoDb(list);

        assertNotNull(rmiMode.extractChildrenForBusFromDb(b));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractChildrenForNotExistingBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Child c = generateChildWithExistingParents();

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        rmiMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        rmiMode.insertPersonIntoDb(c);
        rmiMode.insertTripIntoDb(t);//The bus is not into db

        assertNull(rmiMode.extractChildrenForBusFromDb(b));

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractChildrenForBusWithoutEnrolledChildrenFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Child c = generateChildWithExistingParents();

        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        rmiMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        rmiMode.insertPersonIntoDb(c);
        rmiMode.insertTripIntoDb(t);
        rmiMode.insertBusIntoDb(b);

        assertNull(rmiMode.extractChildrenForBusFromDb(b));//There are not assigned children to the bus

        //House keeping operations
        rmiMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    //Support methods
    private Trip generateTrip(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        return new Trip("Firenze rinascimentale", dtf.format(localDate), "Milano", "Firenze");
    }

    private Bus generateBusForTrip(Trip t){
        return new Bus("EA765VD", "NikolicBus", t.getNomeGita(), t.getData());
    }

    private Stop generateStopForBus(Bus b){
        return new Stop(b.getNomeG(), b.getDataG(), b.getTarga(), "Bologna");
    }

    private Child generateChildWithExistingParents(){

        Contact c1 = new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.GENITORE);
        Contact c2 = new Contact("Gloria", "Grazioli", "GLORIAGRAZIOLI57", "123496780", Contact.ContactTypeFlag.GENITORE);
        Contact c3 = new Contact("Alberto", "Ferrari", "FERALB1999LKJHGF", "333456780", Contact.ContactTypeFlag.PEDIATRA);

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", c1.getCodiceFiscale());
        rmiMode.deleteSubjectFromDb("Contact", c2.getCodiceFiscale());
        rmiMode.deleteSubjectFromDb("Contact", c3.getCodiceFiscale());

        rmiMode.insertPersonIntoDb(c1);
        rmiMode.insertPersonIntoDb(c2);
        rmiMode.insertPersonIntoDb(c3);

        return new Child("Riccardo", "Pagnan", "PAGNANPRESIDENTE", "1996-05-05", c3.getCodiceFiscale(), c2.getCodiceFiscale(), c1.getCodiceFiscale());
    }

    private void removeChildWithExistingParents(Child c){
        rmiMode.deleteSubjectFromDb("Child",c.getCodiceFiscale() );
        rmiMode.deleteSubjectFromDb("Contact", c.getCodiceFiscalePediatra());
        rmiMode.deleteSubjectFromDb("Contact", c.getCodiceFiscaleGen1());
        rmiMode.deleteSubjectFromDb("Contact", c.getCodiceFiscaleGen2());
    }

}