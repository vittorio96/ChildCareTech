package main;

import main.Classes.NormalClasses.Anagrafica.*;
import main.Classes.NormalClasses.Gite.*;
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.NormalClasses.Mensa.Intolerance;
import main.Classes.NormalClasses.Mensa.Menu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SocketMode implements SessionMode {

    private final static int PORT=1100;
    private final static String address="localhost";

    private Socket socket;

    private ObjectInputStream socketObjectIn;
    private ObjectOutputStream socketObjectOut;

    private Client client;//Reference to the adaptee client

    public SocketMode(Client client) {
        this.client = client;
        this.setUpConnection();
    }

    private void setUpConnection(){

        try {

            socket = new Socket(address, PORT);//Attesa bloccante

            //Input - output interfaces
            socketObjectOut = new ObjectOutputStream(socket.getOutputStream());
            socketObjectIn = new ObjectInputStream(socket.getInputStream());

            System.out.println("Client connesso");
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            e.printStackTrace();

            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                System.err.println("Socket not closed");
            }
        }

    }

    @Override
    public User login(String user, String password) {

        try {
            socketObjectOut.writeObject(new String("cmd_login"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(user);
            socketObjectOut.flush();
            socketObjectOut.writeObject(password);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        User logUser = null;

        try {
            logUser = (User) socketObjectIn.readObject();//Attesa bloccante
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            return logUser;
        }

    }

    @Override
    public boolean insertPersonIntoDb(Person p) {

        try {
            socketObjectOut.writeObject(new String("cmd_insert"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(p);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean result = false;
        try {
            result = socketObjectIn.readBoolean();//Attesa bloccante
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            return result;
        }

    }

    @Override
    public List<Child> extractChildrenFromDb() {

        try {
            socketObjectOut.writeObject("cmd_selectAllChildren");
            socketObjectOut.flush();
            return (List<Child>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public void disconnect() {

        try {
            socketObjectOut.writeObject(new String("cmd_quit"));
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                socketObjectOut.close();
                socketObjectIn.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean insertSupplierIntoDb(Supplier s) {

        try {
            socketObjectOut.writeObject(new String("cmd_insert_supplier"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(s);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePersonIntoDb(Person p) {

        try {
            socketObjectOut.writeObject(new String("cmd_update_person"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(p);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean updateSupplierIntoDb(Supplier s) {
        try {
            socketObjectOut.writeObject(new String("cmd_update_supplier"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(s);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Contact> extractContactsFromDb() {

        try {
            socketObjectOut.writeObject(new String("cmd_selectAllContacts"));
            socketObjectOut.flush();

            return (List<Contact>)socketObjectIn.readObject();//Attesa Bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Supplier> extractSuppliersFromDb() {

        try {
            socketObjectOut.writeObject(new String("cmd_selectAllSuppliers"));
            socketObjectOut.flush();

            return (List<Supplier>)socketObjectIn.readObject();//Attesa Bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Staff> extractStaffFromDb() {
        try {
            socketObjectOut.writeObject(new String("cmd_selectAllStaff"));
            socketObjectOut.flush();

            return (List<Staff>)socketObjectIn.readObject();//Attesa Bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Contact> extractParentsForChildFromDb(String childCodF) {
        try {
            socketObjectOut.writeObject(new String("cmd_selectParentsForChild"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(childCodF);
            socketObjectOut.flush();

            return (List<Contact>)socketObjectIn.readObject();//Attesa Bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteSubjectFromDb(String subject, String toDelId) {

        try {
            socketObjectOut.writeObject(new String("cmd_delete"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(subject);
            socketObjectOut.flush();
            socketObjectOut.writeObject(toDelId);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertTripIntoDb(Trip trip) {
        try {
            socketObjectOut.writeObject(new String("cmd_insert_trip"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(trip);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertBusIntoDb(Bus bus) {
        try {
            socketObjectOut.writeObject(new String("cmd_insert_bus"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(bus);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertNewStopIntoDb(Stop stop) {
        try {
            socketObjectOut.writeObject(new String("cmd_insert_stop"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(stop);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Trip> extractAllTripsFromDb() {

        try {
            socketObjectOut.writeObject("cmd_selectAllTrips");
            socketObjectOut.flush();
            return (List<Trip>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }

    }

    @Override
    public List<Bus> extractAllBusesForTrip(String nomeG, String dataG) {

        try {
            socketObjectOut.writeObject("cmd_selectAllBusesForTrip");
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeG);
            socketObjectOut.flush();
            socketObjectOut.writeObject(dataG);
            socketObjectOut.flush();

            return (List<Bus>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Stop> extractAllStopsFromBus(String nomeG, String dataG, String targa) {
        try {
            socketObjectOut.writeObject("cmd_selectAllStopsForBus");
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeG);
            socketObjectOut.flush();
            socketObjectOut.writeObject(dataG);
            socketObjectOut.flush();
            socketObjectOut.writeObject(targa);
            socketObjectOut.flush();

            return (List<Stop>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean insertStopPresencesIntoDb(List<StopPresence> list) {
        try {
            socketObjectOut.writeObject(new String("cmd_insert_stop_presences"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
            socketObjectOut.writeObject(new String("cmd_multiDeleteForTrip"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(subject);
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeG);
            socketObjectOut.flush();
            socketObjectOut.writeObject(dataG);
            socketObjectOut.flush();
            socketObjectOut.writeObject(targa);
            socketObjectOut.flush();
            socketObjectOut.writeObject(numTappa);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStopPresenceFromDb(StopPresence sp) {
        try {
            socketObjectOut.writeObject(new String("cmd_deleteStopPresence"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(sp);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTripIntoDb(Trip oldTrip, Trip newTrip) {
        try {
            socketObjectOut.writeObject(new String("cmd_update_trip"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(oldTrip);
            socketObjectOut.flush();
            socketObjectOut.writeObject(newTrip);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateBusIntoDb(Bus oldBus, Bus newBus) {
        try {
            socketObjectOut.writeObject(new String("cmd_update_bus"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(oldBus);
            socketObjectOut.flush();
            socketObjectOut.writeObject(newBus);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertBusAssociationsIntoDb(List<BusAssociation> list) {
        try {
            socketObjectOut.writeObject(new String("cmd_insert_busAssociations"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBusAssociationFromDb(BusAssociation subscriptionToTrip) {
        try {
            socketObjectOut.writeObject(new String("cmd_deleteBusAssociation"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(subscriptionToTrip);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Child> extractAvailableChildrenForTripFromDb(String tripDate) {
        try {
            socketObjectOut.writeObject("cmd_selectAvailableChildrenForTrip");
            socketObjectOut.flush();
            socketObjectOut.writeObject(tripDate);
            socketObjectOut.flush();

            return (List<Child>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean insertChildDailyPresenceIntoDb(String codF) {
        try {
            socketObjectOut.writeObject(new String("cmd_insertChildDailyPresence"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(codF);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertPersonDailyPresenceIntoDb(String codF) {
        try {
            socketObjectOut.writeObject(new String("cmd_insertPersonDailyPresence"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(codF);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Child> extractMissingChildrenForStopFromDb(Stop stop) {
        try {
            socketObjectOut.writeObject("cmd_selectMissingChildrenForStop");
            socketObjectOut.flush();
            socketObjectOut.writeObject(stop);
            socketObjectOut.flush();

            return (List<Child>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Child> extractChildrenForBusFromDb(Bus bus) {
        try {
            socketObjectOut.writeObject("cmd_selectChildrenForBus");
            socketObjectOut.flush();
            socketObjectOut.writeObject(bus);
            socketObjectOut.flush();

            return (List<Child>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean insertIngredientIntoDb(String nomeI) {
        try {
            socketObjectOut.writeObject(new String("cmd_insertIngredient"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeI);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertDishIntoDb(Dish dish) {
        try {
            socketObjectOut.writeObject(new String("cmd_insertDish"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(dish);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteDishFromDb(Dish dish) {
        try {
            socketObjectOut.writeObject(new String("cmd_deleteDish"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(dish);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertIntoleranceIntoDb(Intolerance intolerance) {
        try {
            socketObjectOut.writeObject(new String("cmd_insertIntolerance"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(intolerance);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteIntoleranceFromDb(Intolerance intolerance) {
        try {
            socketObjectOut.writeObject(new String("cmd_deleteIntolerance"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(intolerance);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> extractIngredientsFromDb() {
        try {
            socketObjectOut.writeObject("cmd_selectIngredients");
            socketObjectOut.flush();

            return (List<String>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Menu> extractMenusFromDb() {
        try {
            socketObjectOut.writeObject("cmd_selectMenus");
            socketObjectOut.flush();

            return (List<Menu>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Dish> extractDishesFromDb() {
        try {
            socketObjectOut.writeObject("cmd_selectDishes");
            socketObjectOut.flush();

            return (List<Dish>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Child> extractIntolerantsChildrenForIngredientFromDb(String nomeI) {
        try {
            socketObjectOut.writeObject("cmd_selectIntolerantsChildren");
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeI);
            socketObjectOut.flush();

            return (List<Child>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Dish> extractDishesForMenuFromDb(Menu.MenuTypeFlag codMenu) {
        try {
            socketObjectOut.writeObject("cmd_selectDishesForMenu");
            socketObjectOut.flush();
            socketObjectOut.writeObject(codMenu);
            socketObjectOut.flush();

            return (List<Dish>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<String> extractIngredientsForDishFromDb(String nomeP) {
        try {
            socketObjectOut.writeObject("cmd_selectIngredientsForDish");
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeP);
            socketObjectOut.flush();

            return (List<String>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean insertIngredientIntoDishIntoDb(String nomeP, String nomeI) {
        try {
            socketObjectOut.writeObject(new String("cmd_insertIngredientIntoDish"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeP);
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeI);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean insertDishIntoMenuIntoDb(Menu.MenuTypeFlag codMenu, String nomeP) {
        try {
            socketObjectOut.writeObject(new String("cmd_insertDishIntoMenu"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(codMenu);
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeP);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteIngredientFromDishFromDb(String nomeP, String nomeI) {
        try {
            socketObjectOut.writeObject(new String("cmd_deleteIngredientFromDish"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeP);
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeI);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteDishFromMenuFromDb(Menu.MenuTypeFlag codMenu, String nomeP) {
        try {
            socketObjectOut.writeObject(new String("cmd_deleteDishFromMenu"));
            socketObjectOut.flush();
            socketObjectOut.writeObject(codMenu);
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeP);
            socketObjectOut.flush();

            return socketObjectIn.readBoolean();//Attesa bloccante

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Dish> extractDishesForTypeFromDb(Dish.DishTypeFlag dishType) {
        try {
            socketObjectOut.writeObject("cmd_selectDishesForType");
            socketObjectOut.flush();
            socketObjectOut.writeObject(dishType);
            socketObjectOut.flush();

            return (List<Dish>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Staff> extractIntolerantsWorkersForIngredientFromDb(String nomeI) {
        try {
            socketObjectOut.writeObject("cmd_selectIntolerantsWorkers");
            socketObjectOut.flush();
            socketObjectOut.writeObject(nomeI);
            socketObjectOut.flush();

            return (List<Staff>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<String> extractUntoleratedIngredientsForPersonFromDb(Person p) {
        try {
            socketObjectOut.writeObject("cmd_selectUntoleratedIngredientsForPerson");
            socketObjectOut.flush();
            socketObjectOut.writeObject(p);
            socketObjectOut.flush();

            return (List<String>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<String> extractNotUntoleratedIngredientsForPersonFromDb(Person p) {
        try {
            socketObjectOut.writeObject("cmd_selectNotUntoleratedIngredientsForPerson");
            socketObjectOut.flush();
            socketObjectOut.writeObject(p);
            socketObjectOut.flush();

            return (List<String>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Child> extractIntolerantsChildrenForMenuFromDb(Menu.MenuTypeFlag codMenu) {
        try {
            socketObjectOut.writeObject("cmd_selectIntolerantsChildrenForMenu");
            socketObjectOut.flush();
            socketObjectOut.writeObject(codMenu);
            socketObjectOut.flush();

            return (List<Child>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<Staff> extractIntolerantsWorkersForMenuFromDb(Menu.MenuTypeFlag codMenu) {
        try {
            socketObjectOut.writeObject("cmd_selectIntolerantsWorkersForMenu");
            socketObjectOut.flush();
            socketObjectOut.writeObject(codMenu);
            socketObjectOut.flush();

            return (List<Staff>)socketObjectIn.readObject();//Attesa bloccante

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public boolean registerClientToDBNotifications(Client c) {
        return false;
    }

    @Override
    public boolean unsubscribeClientToDBNotifications(Client c) {
        return false;
    }

    //Main method to support testing without GUI
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
         SessionMode session = new SocketMode(c);

         session.disconnect();
     }*/
}