package main;

import main.NormalClasses.Anagrafica.*;
import main.NormalClasses.Gite.*;
import main.NormalClasses.Mensa.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteServerInterface extends Remote{

    //Remote method to check if a user is registered
    public User verifyLogin(String username, String password, RemoteClientInterface client) throws RemoteException;

    //Remote method to execute a person into db
    public boolean insertPersonExecution(Person p) throws RemoteException;

    //Remote method to select all children from db
    public List<Child> selectAllChildrenExecution() throws RemoteException;

    //Remote method to insert supplier
    public boolean insertSupplierExecution(Supplier s) throws RemoteException;

    public boolean updatePersonExecution(Person p) throws RemoteException;

    public boolean updateSupplierExecution(Supplier s) throws RemoteException;

    public List<Contact> selectAllContactsExecution() throws RemoteException;

    public List<Supplier> selectAllSuppliersExecution() throws RemoteException;

    public List<Staff> selectAllStaffExecution() throws RemoteException;

    public List<Contact> selectParentsForChildExecution(String childCodF) throws RemoteException;

    public boolean deleteFromDbExecution(String subject, String toDelId) throws RemoteException;

    public boolean insertTripIntoDbExecution(Trip trip) throws RemoteException;

    public boolean insertBusIntoDbExecution(Bus bus) throws RemoteException;

    public boolean insertNewStopIntoDbExecution(Stop stop) throws RemoteException;

    public boolean insertStopPresencesIntoDbExecution(List<StopPresence> list) throws RemoteException;

    public List<Trip> selectAllTripsExecution() throws RemoteException;

    public List<Bus> selectAllBusesForTripExecution(String nomeG, String dataG) throws RemoteException;

    public List<Stop> selectAllStopsForBusExecution(String nomeG, String dataG, String targa) throws RemoteException;

    public boolean deleteTripOrBusOrStopFromDbExecution(String subject, String nomeG, String dataG, Integer numTappa, String targa) throws RemoteException;

    public boolean deleteStopPresenceFromDbExecution(StopPresence sp) throws RemoteException;

    public boolean updateTripIntoDbExecution(Trip oldTrip, Trip newTrip) throws RemoteException;

    public boolean updateBusIntoDbExecution(Bus oldBus, Bus newBus) throws RemoteException;

    public boolean insertBusAssociationsIntoDbExecution(List<BusAssociation> list) throws RemoteException;

    public boolean deleteBusAssociationFromDbExecution(BusAssociation subscription) throws RemoteException;

    public List<Child> selectAvailableChildrenForTripFromDbExecution(String tripDate) throws RemoteException;

    public boolean insertChildDailyPresenceIntoDbExecution(String codF) throws RemoteException;

    public boolean insertPersonDailyPresenceIntoDbExecution(String codF) throws RemoteException;

    public List<Child> selectMissingChildrenForStopFromDbExecution(Stop stop) throws RemoteException;

    public List<Child> selectChildrenForBusFromDbExecution(Bus bus) throws RemoteException;

    public boolean insertIngredientIntoDbExecution(String nomeI) throws RemoteException;

    public boolean insertDishIntoDbExecution(Dish dish) throws RemoteException;

    public boolean deleteDishFromDbExecution(Dish dish) throws RemoteException;

    public boolean insertIntoleranceIntoDbExecution(Intolerance intolerance) throws RemoteException;

    public boolean deleteIntoleranceFromDbExecution(Intolerance intolerance) throws RemoteException;

    public List<String> selectIngredientsFromDbExecution() throws RemoteException;

    public List<Menu> selectMenusFromDbExecution() throws RemoteException;

    public List<Dish> selectDishesFromDbExecution() throws RemoteException;

    public List<Child> selectIntolerantsChildrenForIngredientFromDbExecution(String nomeI) throws RemoteException;

    public List<Dish> selectDishesForMenuFromDbExecution(Menu.MenuTypeFlag codMenu) throws RemoteException;

    public List<String> selectIngredientsForDishFromDbExecution(String nomeP) throws RemoteException;

    public boolean insertIngredientIntoDishIntoDbExecution(String nomeP, String nomeI) throws RemoteException;//Parameters are the primary keys

    public boolean insertDishIntoMenuIntoDbExecution(Menu.MenuTypeFlag codMenu, String nomeP) throws RemoteException;

    public boolean deleteIngredientFromDishFromDbExecution(String nomeP, String nomeI) throws RemoteException;

    public boolean deleteDishFromMenuFromDbExecution(Menu.MenuTypeFlag codMenu, String nomeP) throws RemoteException;

    public List<Dish> selectDishesForTypeFromDbExecution(Dish.DishTypeFlag dishType) throws RemoteException;

    public List<Staff> selectIntolerantsWorkersForIngredientFromDbExecution(String nomeI) throws RemoteException;

    public List<String> selectUntoleratedIngredientsForPersonFromDbExecution(Person p) throws RemoteException;

    public List<String> selectNotUntoleratedIngredientsForPersonFromDbExecution(Person p) throws RemoteException;

    public List<Child> selectIntolerantsChildrenForMenuFromDbExecution(Menu.MenuTypeFlag codMenu) throws RemoteException;

    public List<Staff> selectIntolerantsWorkersForMenuFromDbExecution(Menu.MenuTypeFlag codMenu) throws RemoteException;
}