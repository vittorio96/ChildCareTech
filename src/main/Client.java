package main;

import main.NormalClasses.Anagrafica.*;
import main.NormalClasses.Gite.*;
import main.NormalClasses.Mensa.Dish;
import main.NormalClasses.Mensa.Intolerance;
import main.NormalClasses.Mensa.Menu;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Anagrafica.StringPropertyContact;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.controllers.Menu.ControllerLogin;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Client extends UnicastRemoteObject implements RemoteClientInterface {

    private ControllerLogin controllerLogin = ControllerLogin.getControllerLogin();
    private static User user;
    private SessionMode session;

    public Client() throws RemoteException, MalformedURLException, NotBoundException { }

    @Override
    public void clientPrint(String s) throws RemoteException{
        //works only when AbstractController is instance of controllerLogin
        controllerLogin.printToOutputField(s);
    }


    /*
        Login
    */

    public static User getUser() {
        return user;
    }

    public static void registerUser(User u) {
        user = u;
    }

    public User clientLoginDataHandler(String username, String password, String choice)  {

        if(choice.equals("RMI")) session = new RmiMode( this);
        else session =  new SocketMode(this);

        user = session.login(username, password);

        return user;
    }

    public SessionMode getSession() {
        return session;
    }

    /*
        Anagrafica
    */

    public boolean clientInsertIntoDb(Person person)  {
        return  session.insertPersonIntoDb(person);
    }

    public boolean clientInsertTripIntodb(Trip t){
        return session.insertTripIntoDb(t);
    }

    public boolean clientInsertBusIntoDb(Bus b){
        return session.insertBusIntoDb(b);
    }

    public boolean clientInsertStopIntoDb(Stop s){
        return session.insertNewStopIntoDb(s);
    }

    public  boolean clientInsertSupplierIntoDb(Supplier supplier){
        return session.insertSupplierIntoDb(supplier);
    }

    public boolean clientUpdatePersonIntoDb(Person person){
        return session.updatePersonIntoDb(person);
    }

    public boolean clientUpdateSupplierIntoDb(Supplier supplier){
        return session.updateSupplierIntoDb(supplier);
    }

    public boolean clientDeleteFromDb(String subject, String Id){
        return session.deleteSubjectFromDb(subject, Id);
    }

    public List<Child> clientExtractChildrenFromBus(Bus bus){ return session.extractChildrenForBusFromDb(bus);}

    public List<Child> clientExtractChildrenFromDb() {
        return  session.extractChildrenFromDb();
    }

    public List<Contact> clientExtractContactsFromDb(){
        return session.extractContactsFromDb();
    }

    public List<Staff> clientExtractStaffFromDb(){
        return session.extractStaffFromDb();
    }

    public List<? extends Person> clientExtractPersonFromDb(String s) {
        if(s.equals(Child.class.getSimpleName())) return clientExtractChildrenFromDb();
        if(s.equals(Staff.class.getSimpleName())) return clientExtractStaffFromDb();
        if(s.equals(Contact.class.getSimpleName())) return clientExtractContactsFromDb();
        return null;
    }

    public List<Supplier> clientExtractSuppliersFromDb(){
        return session.extractSuppliersFromDb();
    }

    public List<Contact> clientExtractParentsFromChild(String codice){
        return session.extractParentsForChild(codice);
    }

    /*
        Gite
    */

    public List<Trip> clientExtractAllTripsFromDb(){
        return session.extractAllTripsFromDb();
    }

    public List<Bus> clientExtractAllBusesFromTrip(String nomeGita, String dataGita){
            return session.extractAllBusesForTrip(nomeGita,dataGita);
        }

    public List<Stop> clientExtractRelatedStopsFromTrip(String nomeGita, String dataGita, String targa){ return session.extractAllStopsFromBus(nomeGita, dataGita, targa);}

    public List<Child> clientExtractEnrollableChildren(String data){
        return session.extractAvailableChildrenForTripFromDb(data);
    }

    public List<Child> clientExtractMissingChildrenForStopFromDb(Stop stop){
        return session.extractMissingChildrenForStopFromDb(stop);
    }

    public boolean clientDeleteTripFromDb(String nomeG, String dataG){
        return session.deleteTripFromDb(nomeG, dataG);
    }

    public boolean clientDeleteBusFromDb(String nome, String dataG,String targa) {
        return session.deleteBusFromDb(nome,dataG,targa);
    }

    public boolean clientEnrollChildren(List<BusAssociation> busAssociations){
        return session.insertBusAssociationsIntoDb(busAssociations);
    }

    public boolean clientDeleteStopFromDb(String nomeG, String dataG, String targa, Integer numTappa){
        return session.deleteStopFromDb(nomeG, dataG, targa, numTappa);
    }

    public boolean clientUpdateBusIntoDb(Bus oldBus, Bus newBus){
        return session.updateBusIntoDb(oldBus,newBus);
    }

    public boolean clientUpdateTripIntoDb(Trip oldTrip, Trip newTrip){
        return session.updateTripIntoDb(oldTrip, newTrip);
    }

    public boolean clientInsertStopPresencesIntoDb(List<StopPresence> list){
        return session.insertStopPresencesIntoDb(list);
    }

    public boolean clientDeleteBusAssociationFromDb(BusAssociation busAssociation){
        return session.deleteBusAssociationFromDb(busAssociation);
    }

    /*
        Mensa
    */

    public boolean clientInsertDishIntoDb(Dish dish){
        return session.insertDishIntoDb(dish);
    }

    public boolean clientInsertIngredientIntoDb(String nomeI){
        return session.insertIngredientIntoDb(nomeI);
    }

    public boolean clientInsertIngredientIntoDishIntoDb(String nomeP, String nomeI){ return session.insertIngredientIntoDishIntoDb(nomeP, nomeI); }

    public boolean clientInsertDishIntoMenuIntoDb(Menu.MenuTypeFlag codMenu, String nomeP){ return session.insertDishIntoMenuIntoDb(codMenu, nomeP); }

    public boolean clientDeleteIngredientFromDishFromDb(String nomeP, String nomeI){ return session.deleteIngredientFromDishFromDb(nomeP, nomeI); }

    public boolean clientDeleteDishFromMenuFromDb(Menu.MenuTypeFlag codMenu, String nomeP){ return session.deleteDishFromMenuFromDb(codMenu, nomeP); }

    public List<Dish> clientExtractDishesForTypeFromDb(Dish.DishTypeFlag dishType){ return session.extractDishesForTypeFromDb(dishType); }

    public List<Staff> clientExtractIntolerantsWorkersForIngredientFromDb(String nomeI){ return session.extractIntolerantsWorkersForIngredientFromDb(nomeI); }

    public List<String> clientExtractIngredientsFromDb(){ return session.extractIngredientsFromDb();}

    public List<String> clientExtractIngredientsForDishFromDb(String nomeP){ return session.extractIngredientsForDishFromDb(nomeP);}

    public List<Dish> clientExtractDishesForMenuFromDb(Menu.MenuTypeFlag codMenu){return session.extractDishesForMenuFromDb(codMenu);}

    public List<Dish> clientExtractDishesFromDb(){ return session.extractDishesFromDb(); }

    public List<String> extractAllergiesForPerson(Person person) { return session.extractUntoleratedIngredientsForPersonFromDb(person); }

    public List<String> extractHarmlessIngredientsForPerson(Person person) { return session.extractNotUntoleratedIngredientsForPersonFromDb(person); }

    public boolean clientInsertIntoleranceIntoDb(Intolerance intolerance){ return  session.insertIntoleranceIntoDb(intolerance);}

    public boolean clientDeleteIntoleranceFromDb(Intolerance intolerance){ return  session.deleteIntoleranceFromDb(intolerance);}


    /*
        Accessi
    */

    public boolean clientChildQRAccess(String codF){
        return session.insertChildDailyPresenceIntoDb(codF);
    }

    public boolean clientStaffQRAccess(String codF){
        return session.insertPersonDailyPresenceIntoDb(codF);
    }



}
