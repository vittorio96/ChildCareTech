package main;

import main.NormalClasses.Anagrafica.*;
import main.NormalClasses.Gite.*;
import main.NormalClasses.Mensa.Dish;
import main.NormalClasses.Mensa.Menu;
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

    public List<String> clientExtractIngredientsFromDb(){ return session.extractIngredientsFromDb();}

    public List<Dish> clientExtractDishesForMenuFromDb(Menu.MenuTypeFlag codMenu){return session.extractDishesForMenuFromDb(codMenu);}

    public List<Dish> clientExtractDishesFromDb(){ return session.extractDishesFromDb(); }

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
