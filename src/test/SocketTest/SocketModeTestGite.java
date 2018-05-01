package test.SocketTest;

import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Contact;
import main.Classes.NormalClasses.Anagrafica.Staff;
import main.Classes.NormalClasses.Gite.*;
import main.*;
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
    public void insertNotExistingTripIntoDb() throws Exception {

        Trip t = generateTrip();
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertTrue(socketMode.insertTripIntoDb(t));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertExistingTripIntoDb() throws Exception {
        Trip t = generateTrip();
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        socketMode.insertTripIntoDb(t);
        assertTrue(socketMode.insertTripIntoDb(t));//On duplicate key update
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertNotExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        socketMode.insertTripIntoDb(t);
        assertTrue(socketMode.insertBusIntoDb(b));

        //House keeping operations
        socketMode.deleteBusFromDb(b.getNomeG(), b.getDataG(), b.getTarga());
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        assertTrue(socketMode.insertBusIntoDb(b));//On duplicate key update

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade
    }

    @Test
    public void insertBusForNotExistingTripIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertFalse(socketMode.insertBusIntoDb(b));
    }

    @Test
    public void insertNotExistingStopIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        assertTrue(socketMode.insertNewStopIntoDb(s));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade is set
    }

    @Test
    public void insertExistingStopIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        socketMode.insertNewStopIntoDb(s);
        assertTrue(socketMode.insertNewStopIntoDb(s));//On duplicate key update

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade is set
    }

    @Test
    public void insertStopForNotExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        socketMode.insertTripIntoDb(t);
        //The bus in not in the db
        assertFalse(socketMode.insertNewStopIntoDb(s));//On duplicate key update

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//On delete cascade is set
    }

    @Test
    public void insertNotExistingStopPresencesIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<Stop> stopList = socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        assertTrue(socketMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertExistingStopPresencesIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<Stop> stopList = socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        socketMode.insertStopPresencesIntoDb(list);
        assertTrue(socketMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertStopPresencesForNotExistingBusIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<Stop> stopList = socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), "0000000", s.getDataGita(), s.getNomeGita()));//Alter bus identifier

        //Test
        assertFalse(socketMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertStopPresencesForNotExistingChildIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<Stop> stopList = socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence("AAAAAAAAAAAAAAAA", s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        socketMode.insertStopPresencesIntoDb(list);
        assertFalse(socketMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void insertStopPresencesForNotExistingTripIntoDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<Stop> stopList = socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), "Nessuno"));

        //Test
        socketMode.insertStopPresencesIntoDb(list);
        assertFalse(socketMode.insertStopPresencesIntoDb(list));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void extractAllTripsFromDb() throws Exception {
        Trip t = generateTrip();
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up

        socketMode.insertTripIntoDb(t);
        assertNotNull(socketMode.extractAllTripsFromDb());
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void extractAllBusesForTrip() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up, on delete cascade

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        assertNotNull(socketMode.extractAllBusesForTrip(t.getNomeGita(), t.getData()));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void extractAllStopsFromBus() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up, on delete cascade

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);
        assertNotNull(socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga()));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }


    @Test
    public void deleteTripFromDb() throws Exception {
        Trip t = generateTrip();
        socketMode.insertTripIntoDb(t);//Overwritten if already in db

        assertTrue(socketMode.deleteTripFromDb(t.getNomeGita(), t.getData()));
    }

    @Test
    public void deleteNotExistingTripFromDb() throws Exception {
        Trip t = generateTrip();
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertFalse(socketMode.deleteTripFromDb(t.getNomeGita(), t.getData()));
    }

    @Test
    public void deleteBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        assertTrue(socketMode.deleteBusFromDb(b.getNomeG(), b.getDataG(), b.getTarga()));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteNotExistingBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        socketMode.insertTripIntoDb(t);

        assertFalse(socketMode.deleteBusFromDb(b.getNomeG(), b.getDataG(), b.getTarga()));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteStopFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<Stop> stopList = socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);//Extract the stop to get the auto increment value of 'NumTappa'

        assertTrue(socketMode.deleteStopFromDb(s.getNomeGita(), s.getDataGita(), s.getTarga(),s.getNumeroTappa()));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteNotExistingStopFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        assertFalse(socketMode.deleteStopFromDb(s.getNomeGita(), s.getDataGita(), s.getTarga(),s.getNumeroTappa()));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void deleteStopPresenceFromDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<Stop> stopList = socketMode.extractAllStopsFromBus(b.getNomeG(), b.getDataG(), b.getTarga());
        s = stopList.get(0);

        List<StopPresence> list = new ArrayList<>();
        list.add(new StopPresence(c1.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        //Test
        socketMode.insertStopPresencesIntoDb(list);
        assertTrue(socketMode.deleteStopPresenceFromDb(list.get(0)));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void deleteNotExistingStopPresenceFromDb() throws Exception {
        //Set up operations
        Child c1 = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c1);


        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean up
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        StopPresence sp = new StopPresence("none", s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita());

        //Test
        assertFalse(socketMode.deleteStopPresenceFromDb(sp));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c1);
    }

    @Test
    public void updateTripIntoDb() throws Exception {
        Trip t = generateTrip();
        Trip t1 = generateTrip();
        t1.setNomeGita("Matera e il suo pane");

        socketMode.insertTripIntoDb(t);

        assertTrue(socketMode.updateTripIntoDb(t, t1));
        //House keeping operations
        socketMode.deleteTripFromDb(t1.getNomeGita(), t1.getData());
    }

    @Test
    public void updateNotExistingTripIntoDb() throws Exception {
        Trip t = generateTrip();
        Trip t1 = generateTrip();
        t1.setNomeGita("Matera e il suo pane");

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Trip t is not into db, initial clean-up

        assertFalse(socketMode.updateTripIntoDb(t, t1));

    }

    @Test
    public void updateBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Bus b1 = generateBusForTrip(t);

        b1.setTarga("HHHJJJV");

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        assertTrue(socketMode.updateBusIntoDb(b,b1));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void updateNotExistingBusIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Bus b1 = generateBusForTrip(t);

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up, on delete cascade

        b1.setTarga("HHHJJJV");

        socketMode.insertTripIntoDb(t);

        assertFalse(socketMode.updateBusIntoDb(b,b1));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
    }

    @Test
    public void insertBusAssociationsIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c);

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        assertTrue(socketMode.insertBusAssociationsIntoDb(list));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void insertExistingBusAssociationsIntoDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c);

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        socketMode.insertBusAssociationsIntoDb(list);
        assertTrue(socketMode.insertBusAssociationsIntoDb(list));//On duplicate key update

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void deleteBusAssociationFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c);

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        socketMode.insertBusAssociationsIntoDb(list);
        assertTrue(socketMode.deleteBusAssociationFromDb(list.get(0)));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void deleteNotExistingBusAssociationFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);

        Child c = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c);

        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));

        assertFalse(socketMode.deleteBusAssociationFromDb(list.get(0)));//New bus association is not into db

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractAvailableChildrenForTripFromDb() throws Exception {

    }

    @Test
    public void insertChildDailyPresenceIntoDb() throws Exception {
        Child c = generateChildWithExistingParents();
        socketMode.insertPersonIntoDb(c);

        assertTrue(socketMode.insertChildDailyPresenceIntoDb(c.getCodiceFiscale()));

        //House keeping operations
        removeChildWithExistingParents(c);
    }

    @Test
    public void insertPersonDailyPresenceIntoDb() throws Exception {
        Staff s = new Staff("ABBAABBAABBAABBA", "Daniele", "Cardone", "daniele", "daniele", "1995-10-03", User.UserTypeFlag.MENSA);

        socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());

        socketMode.insertPersonIntoDb(s);
        assertTrue(socketMode.insertPersonDailyPresenceIntoDb(s.getCodiceFiscale()));
        //House keeping operations
        socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void extractMissingChildrenForStopFromDbTestForNotExistingTrip() throws Exception {

        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up

        assertNull(socketMode.extractMissingChildrenForStopFromDb(s));
    }

    @Test
    public void extractMissingChildrenForStopFromDbTestForNotRegisteredChild() throws Exception {

        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        Child c = generateChildWithExistingParents();

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        socketMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        socketMode.insertPersonIntoDb(c);
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));
        socketMode.insertBusAssociationsIntoDb(list);

        socketMode.insertChildDailyPresenceIntoDb(c.getCodiceFiscale());//The child is present the day of the trip but not at the stop

        assertNotNull(socketMode.extractMissingChildrenForStopFromDb(s));
        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractMissingChildrenForStopFromDbTestForRegisteredChild() throws Exception {

        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Stop s = generateStopForBus(b);
        Child c = generateChildWithExistingParents();

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        socketMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        socketMode.insertPersonIntoDb(c);
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);
        socketMode.insertNewStopIntoDb(s);

        List<BusAssociation> busAssociationsList = new ArrayList<>();
        busAssociationsList.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));
        socketMode.insertBusAssociationsIntoDb(busAssociationsList);

        List<StopPresence> stopPresencesList = new ArrayList<>();
        stopPresencesList.add(new StopPresence(c.getCodiceFiscale(), s.getNumeroTappa(), s.getTarga(), s.getDataGita(), s.getNomeGita()));

        assertNull(socketMode.extractMissingChildrenForStopFromDb(s));//One child is enrolled and it is present the day of the trip and at the bus stop

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractChildrenForExistingBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Child c = generateChildWithExistingParents();

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        socketMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        socketMode.insertPersonIntoDb(c);
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        List<BusAssociation> list = new ArrayList<>();
        list.add(new BusAssociation(c.getCodiceFiscale(), b.getNomeG(), b.getDataG(), b.getTarga()));
        socketMode.insertBusAssociationsIntoDb(list);

        assertNotNull(socketMode.extractChildrenForBusFromDb(b));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractChildrenForNotExistingBusFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Child c = generateChildWithExistingParents();

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        socketMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        socketMode.insertPersonIntoDb(c);
        socketMode.insertTripIntoDb(t);//The bus is not into db

        assertNull(socketMode.extractChildrenForBusFromDb(b));

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
        this.removeChildWithExistingParents(c);
    }

    @Test
    public void extractChildrenForBusWithoutEnrolledChildrenFromDb() throws Exception {
        Trip t = generateTrip();
        Bus b = generateBusForTrip(t);
        Child c = generateChildWithExistingParents();

        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());//Initial clean-up
        socketMode.deleteSubjectFromDb("Child", c.getCodiceFiscale());//Initial clean-up

        socketMode.insertPersonIntoDb(c);
        socketMode.insertTripIntoDb(t);
        socketMode.insertBusIntoDb(b);

        assertNull(socketMode.extractChildrenForBusFromDb(b));//There are not assigned children to the bus

        //House keeping operations
        socketMode.deleteTripFromDb(t.getNomeGita(), t.getData());
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
        socketMode.deleteSubjectFromDb("Contact", c1.getCodiceFiscale());
        socketMode.deleteSubjectFromDb("Contact", c2.getCodiceFiscale());
        socketMode.deleteSubjectFromDb("Contact", c3.getCodiceFiscale());

        socketMode.insertPersonIntoDb(c1);
        socketMode.insertPersonIntoDb(c2);
        socketMode.insertPersonIntoDb(c3);

        return new Child("Riccardo", "Pagnan", "PAGNANPRESIDENTE", "1996-05-05", c3.getCodiceFiscale(), c2.getCodiceFiscale(), c1.getCodiceFiscale());
    }

    private void removeChildWithExistingParents(Child c){
        socketMode.deleteSubjectFromDb("Child",c.getCodiceFiscale() );
        socketMode.deleteSubjectFromDb("Contact", c.getCodiceFiscalePediatra());
        socketMode.deleteSubjectFromDb("Contact", c.getCodiceFiscaleGen1());
        socketMode.deleteSubjectFromDb("Contact", c.getCodiceFiscaleGen2());
    }

}