package main;
import main.Classes.NormalClasses.Anagrafica.*;
import main.Classes.NormalClasses.Gite.*;
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.NormalClasses.Mensa.Intolerance;
import main.Classes.NormalClasses.Mensa.Menu;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

//Adapter to reach the RMI Server while working in RMI mode. This class works on client side

public class RmiMode implements SessionMode {

    private RemoteServerInterface server; //Remote server object
    private RemoteClientInterface client; //Local client object

    public RmiMode(RemoteClientInterface client) {

        this.client = client; //Reference to the adaptee client

        try{
            this.server = (RemoteServerInterface) Naming.lookup("rmi://127.0.0.1/stub");
        }
        catch(Exception e){ e.getStackTrace(); }

    }

    @Override
    public User login(String user, String password) {

        User candidateUser;

        try{
            candidateUser = server.verifyLogin(user, password);
            return candidateUser;
        }
        catch(RemoteException e){
            e.getStackTrace();
        }

        return null;
    }

    @Override
    public boolean insertPersonIntoDb(Person p) {
        boolean result = false;
        try {
            result=server.insertPersonExecution(p);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public List<Child> extractChildrenFromDb() {

        List<Child> list = null;
        try {
            list = server.selectAllChildrenExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finally{
            return list;
        }
    }

    @Override
    public void disconnect() {
        //Nothing to do
    }

    @Override
    public boolean insertSupplierIntoDb(Supplier s) {

        try {
            return server.insertSupplierExecution(s);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePersonIntoDb(Person p) {

        try {
            return this.server.updatePersonExecution(p);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSupplierIntoDb(Supplier s) {
        try {
            return this.server.updateSupplierExecution(s);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Contact> extractContactsFromDb() {

        try {
            return server.selectAllContactsExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Supplier> extractSuppliersFromDb() {

        try {
            return server.selectAllSuppliersExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Staff> extractStaffFromDb() {
        try {
            return server.selectAllStaffExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Contact> extractParentsForChildFromDb(String childCodF) {

        try {
            return server.selectParentsForChildExecution(childCodF);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteSubjectFromDb(String subject, String toDelId) {

        try {
            return server.deleteFromDbExecution(subject, toDelId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertTripIntoDb(Trip trip) {

        try {
            return server.insertTripIntoDbExecution(trip);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertBusIntoDb(Bus bus) {

        try {
            return server.insertBusIntoDbExecution(bus);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean insertNewStopIntoDb(Stop stop) {

        try {

            return server.insertNewStopIntoDbExecution(stop);

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertStopPresencesIntoDb(List<StopPresence> list) {

        try {

            return server.insertStopPresencesIntoDbExecution(list);

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Trip> extractAllTripsFromDb() {

        List<Trip> list = null;
        try {
            list = server.selectAllTripsExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finally{
            return list;
        }

    }

    @Override
    public List<Stop> extractAllStopsFromBus(String nomeG, String dataG, String targa) {
        List<Stop> list = null;
        try {
            list = server.selectAllStopsForBusExecution(nomeG, dataG, targa);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finally{
            return list;
        }
    }


    @Override
    public List<Bus> extractAllBusesForTrip(String nomeG, String dataG) {

        try {

            return server.selectAllBusesForTripExecution(nomeG, dataG);

        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteTripFromDb(String nomeG, String dataG) {
        return this.deleteTripOrBusOrStopFromDb("Trip", nomeG, dataG, null, null);
    }

    @Override
    public boolean deleteBusFromDb(String nomeG, String dataG, String targa) {
        return this.deleteTripOrBusOrStopFromDb("Bus", nomeG, dataG, targa, null);
    }

    @Override
    public boolean deleteStopFromDb(String nomeG, String dataG, String targa, Integer numTappa) {
        return this.deleteTripOrBusOrStopFromDb("Stop", nomeG, dataG, targa, numTappa);
    }


    private boolean deleteTripOrBusOrStopFromDb(String subject, String nomeG, String dataG, String targa, Integer numTappa){

        try {

            return server.deleteTripOrBusOrStopFromDbExecution(subject, nomeG, dataG, numTappa, targa);

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStopPresenceFromDb(StopPresence sp) {
        try {
            return server.deleteStopPresenceFromDbExecution(sp);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTripIntoDb(Trip oldTrip, Trip newTrip) {

        try {

            return server.updateTripIntoDbExecution(oldTrip, newTrip);

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateBusIntoDb(Bus oldBus, Bus newBus) {
        try {

            return server.updateBusIntoDbExecution(oldBus, newBus);

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertBusAssociationsIntoDb(List<BusAssociation> list) {
        try {
            return server.insertBusAssociationsIntoDbExecution(list);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBusAssociationFromDb(BusAssociation subscriptionToTrip) {
        try {
            return server.deleteBusAssociationFromDbExecution(subscriptionToTrip);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Child> extractAvailableChildrenForTripFromDb(String tripDate) {
        List<Child> list = null;
        try {
            list = server.selectAvailableChildrenForTripFromDbExecution(tripDate);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finally{
            return list;
        }
    }

    @Override
    public boolean insertChildDailyPresenceIntoDb(String codF) {
        try {
            return server.insertChildDailyPresenceIntoDbExecution(codF);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertPersonDailyPresenceIntoDb(String codF) {
        try {
            return server.insertPersonDailyPresenceIntoDbExecution(codF);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Child> extractMissingChildrenForStopFromDb(Stop stop) {
        List<Child> list = null;
        try {
            list = server.selectMissingChildrenForStopFromDbExecution(stop);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finally{
            return list;
        }
    }

    @Override
    public List<Child> extractChildrenForBusFromDb(Bus bus) {
        List<Child> list = null;
        try {
            list = server.selectChildrenForBusFromDbExecution(bus);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finally{
            return list;
        }
    }

    @Override
    public boolean insertIngredientIntoDb(String nomeI) {
        try {
            return server.insertIngredientIntoDbExecution(nomeI);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertDishIntoDb(Dish dish) {
        try {
            return server.insertDishIntoDbExecution(dish);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteDishFromDb(Dish dish) {
        try {
            return server.deleteDishFromDbExecution(dish);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertIntoleranceIntoDb(Intolerance intolerance) {
        try {
            return server.insertIntoleranceIntoDbExecution(intolerance);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteIntoleranceFromDb(Intolerance intolerance) {
        try {
            return server.deleteIntoleranceFromDbExecution(intolerance);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> extractIngredientsFromDb() {
        try {
            return server.selectIngredientsFromDbExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Menu> extractMenusFromDb() {
        try {
            return server.selectMenusFromDbExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dish> extractDishesFromDb() {
        try {
            return server.selectDishesFromDbExecution();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Child> extractIntolerantsChildrenForIngredientFromDb(String nomeI) {
        try {
            return server.selectIntolerantsChildrenForIngredientFromDbExecution(nomeI);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dish> extractDishesForMenuFromDb(Menu.MenuTypeFlag codMenu) {
        try {
            return server.selectDishesForMenuFromDbExecution(codMenu);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> extractIngredientsForDishFromDb(String nomeP) {
        try {
            return server.selectIngredientsForDishFromDbExecution(nomeP);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertIngredientIntoDishIntoDb(String nomeP, String nomeI) {
        try {
            return server.insertIngredientIntoDishIntoDbExecution(nomeP, nomeI);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertDishIntoMenuIntoDb(Menu.MenuTypeFlag codMenu, String nomeP) {
        try {
            return server.insertDishIntoMenuIntoDbExecution(codMenu, nomeP);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteIngredientFromDishFromDb(String nomeP, String nomeI) {
        try {
            return server.deleteIngredientFromDishFromDbExecution(nomeP, nomeI);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteDishFromMenuFromDb(Menu.MenuTypeFlag codMenu, String nomeP) {
        try {
            return server.deleteDishFromMenuFromDbExecution(codMenu, nomeP);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Dish> extractDishesForTypeFromDb(Dish.DishTypeFlag dishType) {
        try {
            return server.selectDishesForTypeFromDbExecution(dishType);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Staff> extractIntolerantsWorkersForIngredientFromDb(String nomeI) {
        try {
            return server.selectIntolerantsWorkersForIngredientFromDbExecution(nomeI);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> extractUntoleratedIngredientsForPersonFromDb(Person p) {
        try {
            return server.selectUntoleratedIngredientsForPersonFromDbExecution(p);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> extractNotUntoleratedIngredientsForPersonFromDb(Person p) {
        try {
            return server.selectNotUntoleratedIngredientsForPersonFromDbExecution(p);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Child> extractIntolerantsChildrenForMenuFromDb(Menu.MenuTypeFlag codMenu) {
        try {
            return server.selectIntolerantsChildrenForMenuFromDbExecution(codMenu);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Staff> extractIntolerantsWorkersForMenuFromDb(Menu.MenuTypeFlag codMenu) {
        try {
            return server.selectIntolerantsWorkersForMenuFromDbExecution(codMenu);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

   /* @Override
    public boolean registerClientToDBNotifications(Client c) {
        try {
            return server.registerClientToDBNotifications(c);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unsubscribeClientToDBNotifications(Client c) {
        return false;
    }*/

    /*public static void main(String[] args){
        Client c = null;
        try {
            c = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        SessionMode session = new RmiMode(c);

        session.disconnect();
    }*/
}