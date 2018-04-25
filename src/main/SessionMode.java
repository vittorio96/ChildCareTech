package main;

import main.Classes.NormalClasses.Anagrafica.*;
import main.Classes.NormalClasses.Gite.*;
import main.Classes.NormalClasses.Mensa.*;

import java.util.List;

public interface SessionMode {

    //Check if user is registered and return user info
    public User login(String user, String password);

    //Insert person p into db and return status
    public boolean insertPersonIntoDb(Person p);

    //Select all children from db
    public List<Child> extractChildrenFromDb();

    //Disconnect from the server
    public void disconnect();

    //Insert supplier into db
    public boolean insertSupplierIntoDb(Supplier s);

    public boolean updatePersonIntoDb(Person p);

    public boolean updateSupplierIntoDb(Supplier s);

    public List<Contact> extractContactsFromDb();

    public List<Supplier> extractSuppliersFromDb();

    public List<Staff> extractStaffFromDb();

    public List<Contact> extractParentsForChild(String childCodF);

    public boolean deleteSubjectFromDb(String subject, String toDelId);

    public boolean insertTripIntoDb(Trip trip);

    public boolean insertBusIntoDb(Bus bus);

    public List<Trip> extractAllTripsFromDb();

    public List<Stop> extractAllStopsFromBus(String nomeG, String dataG, String targa);

    public List<Bus> extractAllBusesForTrip(String nomeG, String dataG);

    public boolean insertNewStopIntoDb(Stop stop);

    public boolean insertStopPresencesIntoDb(List<StopPresence> list);

    public boolean deleteTripFromDb(String nomeG, String dataG);

    public boolean deleteBusFromDb(String nomeG, String dataG, String targa);

    public boolean deleteStopFromDb(String nomeG, String dataG, String targa, Integer numTappa);

    public boolean updateTripIntoDb(Trip oldTrip, Trip newTrip);

    public boolean updateBusIntoDb(Bus oldBus, Bus newBus);

    public boolean insertBusAssociationsIntoDb(List<BusAssociation> list);

    public boolean deleteBusAssociationFromDb(BusAssociation subscriptionToTrip);

    public List<Child> extractAvailableChildrenForTripFromDb(String tripDate);

    public boolean insertChildDailyPresenceIntoDb(String codF);

    public boolean insertPersonDailyPresenceIntoDb(String codF);

    public List<Child> extractMissingChildrenForStopFromDb(Stop stop);

    public List<Child> extractChildrenForBusFromDb(Bus bus);

    public boolean insertIngredientIntoDb(String nomeI);

    public boolean insertDishIntoDb(Dish dish);

    public boolean deleteDishFromDb(Dish dish);

    public boolean insertIntoleranceIntoDb(Intolerance intolerance);

    public boolean deleteIntoleranceFromDb(Intolerance intolerance);

    public boolean deleteStopPresenceFromDb(StopPresence sp);

    public List<String> extractIngredientsFromDb();

    public List<Menu> extractMenusFromDb();

    public List<Dish> extractDishesFromDb();

    public List<Child> extractIntolerantsChildrenForIngredientFromDb(String nomeI);

    public List<Dish> extractDishesForMenuFromDb(Menu.MenuTypeFlag codMenu);

    public List<String> extractIngredientsForDishFromDb(String nomeP);

    public boolean insertIngredientIntoDishIntoDb(String nomeP, String nomeI);//Parameters are the primary keys

    public boolean insertDishIntoMenuIntoDb(Menu.MenuTypeFlag codMenu, String nomeP);

    public boolean deleteIngredientFromDishFromDb(String nomeP, String nomeI);

    public boolean deleteDishFromMenuFromDb(Menu.MenuTypeFlag codMenu, String nomeP);

    public List<Dish> extractDishesForTypeFromDb(Dish.DishTypeFlag dishType);

    public List<Staff> extractIntolerantsWorkersForIngredientFromDb(String nomeI);

    public List<String> extractUntoleratedIngredientsForPersonFromDb(Person p);

    public List<String> extractNotUntoleratedIngredientsForPersonFromDb(Person p);

    public List<Child> extractIntolerantsChildrenForMenuFromDb(Menu.MenuTypeFlag codMenu);

    public List<Staff> extractIntolerantsWorkersForMenuFromDb(Menu.MenuTypeFlag codMenu);
}