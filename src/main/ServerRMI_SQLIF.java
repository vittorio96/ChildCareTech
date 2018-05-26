package main;
import main.Classes.NormalClasses.Anagrafica.*;
import main.Classes.NormalClasses.Gite.*;
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.NormalClasses.Mensa.Intolerance;
import main.Classes.NormalClasses.Mensa.Menu;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;


public class ServerRMI_SQLIF extends UnicastRemoteObject implements RemoteServerInterface {

    //private Vector users = new Vector();


    public ServerRMI_SQLIF() throws RemoteException{
        super();
    }

    @Override
    public User verifyLogin(String username, String password) throws RemoteException {

        String name = null;
        String psw = null;
        //Execution of the query
        try {

            User verifiedUser = DMLCommandExecutorSQLInjectionFree.getInstance().selectUserForLogin(username, password);
            return verifiedUser;

        } catch (SQLException e) {
            e.getStackTrace();
        }

        return null;
    }

    @Override
    public boolean insertPersonExecution(Person p) {

        return DMLCommandExecutorSQLInjectionFree.getInstance().insertPersonIntoDb(p);

    }

    @Override
    public List<Child> selectAllChildrenExecution() throws RemoteException {

        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectChildrenFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean insertSupplierExecution(Supplier s) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().insertSupplierIntoDb(s);
    }

    @Override
    public boolean updatePersonExecution(Person p) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().updatePersonIntoDb(p);

    }

    @Override
    public boolean updateSupplierExecution(Supplier s) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().updateSupplierIntoDb(s);

    }

    @Override
    public List<Contact> selectAllContactsExecution() throws RemoteException {

        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectContactsFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Supplier> selectAllSuppliersExecution() throws RemoteException {

        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectSuppliersFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Staff> selectAllStaffExecution() throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectStaffFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Contact> selectParentsForChildExecution(String childCodF) throws RemoteException {

        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectParentsForChild(childCodF);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteFromDbExecution(String subject, String toDelId) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().deletePersonFromDb(subject, toDelId);

    }

    @Override
    public boolean insertTripIntoDbExecution(Trip trip) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().insertTripIntoDb(trip);

    }

    @Override
    public boolean insertBusIntoDbExecution(Bus bus) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().insertBusIntoDb(bus);

    }

    @Override
    public List<Trip> selectAllTripsExecution() throws RemoteException {

        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectAllTripsFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Bus> selectAllBusesForTripExecution(String nomeG, String dataG) throws RemoteException {

        try {

            return DMLCommandExecutorSQLInjectionFree.getInstance().selectAllBusesForTripFromDb(nomeG, dataG);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Stop> selectAllStopsForBusExecution(String nomeG, String dataG, String targa) throws RemoteException {
        try {

            return DMLCommandExecutorSQLInjectionFree.getInstance().selectAllStopsFromBus(nomeG, dataG, targa);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertNewStopIntoDbExecution(Stop stop) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().insertNewStopIntoDb(stop);

    }

    @Override
    public boolean insertStopPresencesIntoDbExecution(List<StopPresence> list) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().insertStopPresencesIntoDb(list);

    }

    @Override
    public boolean deleteTripOrBusOrStopFromDbExecution(String subject, String nomeG, String dataG, Integer numTappa, String targa) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().deleteStopOrBusOrTripFromDb(subject, nomeG, dataG, numTappa, targa);

    }

    @Override
    public boolean deleteStopPresenceFromDbExecution(StopPresence sp) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().deleteStopPresenceFromDb(sp);

    }

    @Override
    public boolean updateTripIntoDbExecution(Trip oldTrip, Trip newTrip) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().updateTripIntoDb(oldTrip, newTrip);

    }

    @Override
    public boolean updateBusIntoDbExecution(Bus oldBus, Bus newBus) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().updateBusIntoDb(oldBus, newBus);

    }

    @Override
    public boolean insertBusAssociationsIntoDbExecution(List<BusAssociation> list) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().insertTripSubscriptionsIntoDb(list);

    }

    @Override
    public boolean deleteBusAssociationFromDbExecution(BusAssociation subscription) throws RemoteException {

        return DMLCommandExecutorSQLInjectionFree.getInstance().deleteBusAssociationFromDb(subscription);

    }

    @Override
    public List<Child> selectAvailableChildrenForTripFromDbExecution(String tripDate) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectAvailableChildrenForTripFromDb(tripDate);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertChildDailyPresenceIntoDbExecution(String codF) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().insertChildDailyPresenceIntoDb(codF);
    }

    @Override
    public boolean insertPersonDailyPresenceIntoDbExecution(String codF) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().insertPersonDailyPresenceIntoDb(codF);
    }

    @Override
    public List<Child> selectMissingChildrenForStopFromDbExecution(Stop stop) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectMissingChildrenForStopFromDb(stop);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Child> selectChildrenForBusFromDbExecution(Bus bus) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectChildrenForBusFromDb(bus);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertIngredientIntoDbExecution(String nomeI) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().insertIngredientIntoDb(nomeI);
    }

    @Override
    public boolean insertDishIntoDbExecution(Dish dish) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().insertDishIntoDb(dish);
    }

    @Override
    public boolean deleteDishFromDbExecution(Dish dish) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().deleteDishFromDb(dish);
    }

    @Override
    public boolean insertIntoleranceIntoDbExecution(Intolerance intolerance) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().insertIntoleranceIntoDb(intolerance);
    }

    @Override
    public boolean deleteIntoleranceFromDbExecution(Intolerance intolerance) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().deleteIntoleranceFromDb(intolerance);
    }

    @Override
    public List<String> selectIngredientsFromDbExecution() throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectIngredientsFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Menu> selectMenusFromDbExecution() throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectMenusFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dish> selectDishesFromDbExecution() throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectDishesFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Child> selectIntolerantsChildrenForIngredientFromDbExecution(String nomeI) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectIntolerantsChildrenForIngredientFromDb(nomeI);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dish> selectDishesForMenuFromDbExecution(Menu.MenuTypeFlag codMenu) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectDishesForMenuFromDb(codMenu);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> selectIngredientsForDishFromDbExecution(String nomeP) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectIngredientsForDishFromDb(nomeP);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertIngredientIntoDishIntoDbExecution(String nomeP, String nomeI) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().insertIngredientIntoDishIntoDb(nomeP, nomeI);
    }

    @Override
    public boolean insertDishIntoMenuIntoDbExecution(Menu.MenuTypeFlag codMenu, String nomeP) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().insertDishIntoMenuIntoDb(codMenu, nomeP);
    }

    @Override
    public boolean deleteIngredientFromDishFromDbExecution(String nomeP, String nomeI) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().deleteIngredientFromDishFromDb(nomeP, nomeI);
    }

    @Override
    public boolean deleteDishFromMenuFromDbExecution(Menu.MenuTypeFlag codMenu, String nomeP) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().deleteDishFromMenuFromDb(codMenu, nomeP);
    }

    @Override
    public List<Dish> selectDishesForTypeFromDbExecution(Dish.DishTypeFlag dishType) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectDishesForTypeFromDb(dishType);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Staff> selectIntolerantsWorkersForIngredientFromDbExecution(String nomeI) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectIntolerantsWorkersForIngredientFromDb(nomeI);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> selectUntoleratedIngredientsForPersonFromDbExecution(Person p) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectUntoleratedIngredientsForPersonFromDb(p);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> selectNotUntoleratedIngredientsForPersonFromDbExecution(Person p) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectNotUntoleratedIngredientsForPersonFromDb(p);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Child> selectIntolerantsChildrenForMenuFromDbExecution(Menu.MenuTypeFlag codMenu) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectIntolerantsChildrenForMenuFromDb(codMenu);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Staff> selectIntolerantsWorkersForMenuFromDbExecution(Menu.MenuTypeFlag codMenu) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectIntolerantsWorkersForMenuFromDb(codMenu);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*@Override
    public boolean registerClientToDBNotifications(Client c) throws RemoteException {
        return DMLCommandExecutorSQLInjectionFree.getInstance().subscribeClient(c);
    }*/

    @Override
    public List<String> extractUntoleratedDishesForPersonOnMenuDbExecution(Person p, Menu.MenuTypeFlag menu) throws RemoteException {
        try {
            return DMLCommandExecutorSQLInjectionFree.getInstance().selectUntoleratedDishesForPersonOnMenu(p, menu);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}