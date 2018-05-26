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
import java.sql.SQLException;
import java.util.List;

public class ClientHandler implements Runnable{

    private Socket socket;
    private SocketServer mainSocketServer;

    private ObjectInputStream socketObjectIn;
    private ObjectOutputStream socketObjectOut;

    public ClientHandler(Socket socket, SocketServer mainServer) {
        this.socket = socket;
        this.mainSocketServer = mainServer;
    }


    @Override
    public void run() {

        System.out.println("Connection with user established");
        try {
            socketObjectOut = new ObjectOutputStream(socket.getOutputStream());
            socketObjectIn = new ObjectInputStream(socket.getInputStream());
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            return ;
        }

        //Command management
        String command = "cmd_quit";

        do {
            try {
                command = (String) socketObjectIn.readObject();//Attesa bloccante
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (command) {

                case "cmd_login": this.verifyLogin();
                    break;

                case "cmd_insert": this.insertPersonExecution();
                    break;

                case "cmd_selectAllChildren": this.selectAllChildrenExecution();
                    break;

                case "cmd_insert_supplier": this.insertSupplierExecution();
                    break;

                case "cmd_update_person": this.updatePersonExecution();
                    break;

                case "cmd_update_supplier": this.updateSupplierExecution();
                    break;

                case "cmd_selectAllContacts": this.selectAllContactsExecution();
                    break;

                case "cmd_selectAllSuppliers": this.selectAllSuppliersExecution();
                    break;

                case "cmd_selectAllStaff": this.selectAllStaffExecution();
                    break;

                case "cmd_selectParentsForChild": this.selectParentsForChildExecution();
                    break;

                case "cmd_selectAllTrips": this.selectAllTripsExecution();
                    break;

                case "cmd_selectAllBusesForTrip": this.selectAllBusesForTripExecution();
                    break;

                case "cmd_selectAllStopsForBus": this.selectAllStopsForBusExecution();
                    break;

                case "cmd_insert_trip": this.insertTripIntoDbExecution();
                    break;

                case "cmd_insert_bus": this.insertBusIntoDbExecution();
                    break;

                case "cmd_insert_stop": this.insertNewStopIntoDbExecution();
                    break;

                case "cmd_insert_busAssociations": this.insertBusAssociationsIntoDbExecution();
                    break;

                case "cmd_update_bus": this.updateBusIntoDbExecution();
                    break;

                case "cmd_update_trip": this.updateTripIntoDbExecution();
                    break;

                case "cmd_insert_stop_presences": this.insertStopPresencesIntoDbExecution();
                    break;

                case "cmd_delete": this.deleteFromDbExecution();
                    break;

                case "cmd_multiDeleteForTrip": this.deleteTripOrBusOrStopFromDbExecution();
                    break;

                case "cmd_deleteStopPresence": this.deleteStopPresenceFromDbExecution();
                    break;

                case "cmd_deleteBusAssociation": this.deleteBusAssociationFromDbExecution();
                    break;

                case "cmd_selectAvailableChildrenForTrip": this.selectAvailableChildrenForTripExecution();
                    break;

                case "cmd_insertChildDailyPresence": this.insertChildDailyPresenceIntoDbExecution();
                    break;

                case "cmd_insertPersonDailyPresence": this.insertPersonDailyPresenceIntoDbExecution();
                    break;

                case "cmd_selectMissingChildrenForStop": this.selectMissingChildrenForStopFromDbExecution();
                    break;

                case "cmd_selectChildrenForBus": this.selectChildrenForBusFromDbExecution();
                    break;

                case "cmd_insertIngredient": this.insertIngredientIntoDbExecution();
                    break;

                case "cmd_insertDish": this.insertDishIntoDbExecution();
                    break;

                case "cmd_insertIntolerance": this.insertIntoleranceIntoDbExecution();
                    break;

                case "cmd_deleteDish": this.deleteDishFromDbExecution();
                    break;

                case "cmd_deleteIntolerance": this.deleteIntoleranceFromDbExecution();
                    break;

                case "cmd_selectIngredients": this.selectIngredientsFromDbExecution();
                    break;

                case "cmd_selectMenus": this.selectMenusFromDbExecution();
                    break;

                case "cmd_selectDishes": this.selectDishesFromDbExecution();
                    break;

                case "cmd_selectIntolerantsChildren": this.selectIntolerantsChildrenForIngredientFromDbExecution();
                    break;

                case "cmd_selectDishesForMenu": this.selectDishesForMenuFromDbExecution();
                    break;

                case "cmd_selectIngredientsForDish": this.selectIngredientsForDishFromDbExecution();
                    break;

                case "cmd_insertIngredientIntoDish": this.insertIngredientIntoDishIntoDbExecution();
                    break;

                case "cmd_insertDishIntoMenu": this.insertDishIntoMenuIntoDbExecution();
                    break;

                case "cmd_deleteIngredientFromDish": this.deleteIngredientFromDishFromDbExecution();
                    break;

                case "cmd_deleteDishFromMenu": this.deleteDishFromMenuFromDbExecution();
                    break;

                case "cmd_selectDishesForType": this.selectDishesForTypeFromDbExecution();
                    break;

                case "cmd_selectIntolerantsWorkers": this.selectIntolerantsWorkersForIngredientFromDbExecution();
                    break;

                case "cmd_selectUntoleratedIngredientsForPerson": this.selectUntoleratedIngredientsForPersonFromDbExecution();
                    break;

                case "cmd_selectNotUntoleratedIngredientsForPerson": this.selectNotUntoleratedIngredientsForPersonFromDbExecution();
                    break;

                case "cmd_selectIntolerantsChildrenForMenu": this.selectIntolerantsChildrenForMenuFromDbExecution();
                    break;

                case "cmd_selectIntolerantsWorkersForMenu": this.selectIntolerantsWorkersForMenuFromDbExecution();
                    break;

                case "cmd_selectUntoleratedDishesFromMenuFromPerson": this.extractUntoleratedDishesForPersonOnMenuDbExecution();
                    break;

                case "cmd_quit": this.closeConnection();
                    break;
            }

        }while(!command.equals("cmd_quit"));
    }


    private void selectIntolerantsWorkersForMenuFromDbExecution() {
        Menu.MenuTypeFlag codMenu=null;

        try {
            codMenu = (Menu.MenuTypeFlag) socketObjectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Staff> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectIntolerantsWorkersForMenuFromDb(codMenu);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectIntolerantsChildrenForMenuFromDbExecution() {
        Menu.MenuTypeFlag codMenu=null;

        try {
            codMenu = (Menu.MenuTypeFlag) socketObjectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Child> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectIntolerantsChildrenForMenuFromDb(codMenu);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectNotUntoleratedIngredientsForPersonFromDbExecution() {
        Person p=null;

        try {
            p = (Person)socketObjectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectNotUntoleratedIngredientsForPersonFromDb(p);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectUntoleratedIngredientsForPersonFromDbExecution() {
        Person p=null;

        try {
            p = (Person)socketObjectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectUntoleratedIngredientsForPersonFromDb(p);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectIntolerantsWorkersForIngredientFromDbExecution() {
        String nomeI=null;

        try {
            nomeI = (String)socketObjectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Staff> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectIntolerantsWorkersForIngredientFromDb(nomeI);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectDishesForTypeFromDbExecution() {
        Dish.DishTypeFlag dishType=null;

        try {
            dishType = (Dish.DishTypeFlag) socketObjectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Dish> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectDishesForTypeFromDb(dishType);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteDishFromMenuFromDbExecution() {
        try {
            Menu.MenuTypeFlag codMenu = (Menu.MenuTypeFlag) socketObjectIn.readObject();//Attesa bloccante
            String nomeP = (String) socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().deleteDishFromMenuFromDb(codMenu, nomeP);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteIngredientFromDishFromDbExecution() {
        try {
            String nomeP = (String) socketObjectIn.readObject();//Attesa bloccante
            String nomeI = (String) socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().deleteIngredientFromDishFromDb(nomeP, nomeI);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertDishIntoMenuIntoDbExecution() {
        try {
            Menu.MenuTypeFlag codMenu = (Menu.MenuTypeFlag) socketObjectIn.readObject();//Attesa bloccante
            String nomeP = (String) socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().insertDishIntoMenuIntoDb(codMenu, nomeP);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertIngredientIntoDishIntoDbExecution() {
        try {
            String nomeP = (String) socketObjectIn.readObject();//Attesa bloccante
            String nomeI = (String) socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().insertIngredientIntoDishIntoDb(nomeP, nomeI);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void selectIngredientsForDishFromDbExecution() {

        String nomeP=null;

        try {
            nomeP = (String)socketObjectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectIngredientsForDishFromDb(nomeP);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectDishesForMenuFromDbExecution() {

        Menu.MenuTypeFlag codMenu=null;

        try {
            codMenu = (Menu.MenuTypeFlag) socketObjectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Dish> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectDishesForMenuFromDb(codMenu);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectIntolerantsChildrenForIngredientFromDbExecution() {
        String nomeI=null;

        try {
            nomeI = (String)socketObjectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Child> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectIntolerantsChildrenForIngredientFromDb(nomeI);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectMenusFromDbExecution() {
        List<Menu> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectMenusFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socketObjectOut.writeObject(list);
                socketObjectOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectDishesFromDbExecution() {
        List<Dish> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectDishesFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socketObjectOut.writeObject(list);
                socketObjectOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectIngredientsFromDbExecution() {
        List<String> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectIngredientsFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socketObjectOut.writeObject(list);
                socketObjectOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteIntoleranceFromDbExecution() {
        try {
            Intolerance intolerance= (Intolerance)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().deleteIntoleranceFromDb(intolerance);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteDishFromDbExecution() {
        try {
            Dish dish= (Dish)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().deleteDishFromDb(dish);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoleranceIntoDbExecution() {
        try {
            Intolerance intolerance= (Intolerance)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertIntoleranceIntoDb(intolerance);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertDishIntoDbExecution() {
        try {
            Dish dish= (Dish)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertDishIntoDb(dish);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertIngredientIntoDbExecution() {
        try {
            String nomeI= (String)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertIngredientIntoDb(nomeI);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void selectChildrenForBusFromDbExecution() {
        Bus bus=null;

        try {
            bus = (Bus)socketObjectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Child> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectChildrenForBusFromDb(bus);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectMissingChildrenForStopFromDbExecution() {
        Stop stop=null;

        try {
            stop = (Stop)socketObjectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Child> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectMissingChildrenForStopFromDb(stop);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertPersonDailyPresenceIntoDbExecution() {
        try {
            String codF = (String)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertPersonDailyPresenceIntoDb(codF);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertChildDailyPresenceIntoDbExecution() {
        try {
            String codF = (String)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertChildDailyPresenceIntoDb(codF);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void selectAvailableChildrenForTripExecution() {

        String tripDate=null;

        try {
            tripDate = (String)socketObjectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Child> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectAvailableChildrenForTripFromDb(tripDate);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteBusAssociationFromDbExecution() {
        BusAssociation subscription;

        try {
            subscription = (BusAssociation) socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().deleteBusAssociationFromDb(subscription);
            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertBusAssociationsIntoDbExecution() {
        try {
            List<BusAssociation> list = (List<BusAssociation>)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertTripSubscriptionsIntoDb(list);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void updateTripIntoDbExecution() {

        Trip oldTrip, newTrip;
        try {
            oldTrip = (Trip)socketObjectIn.readObject();//Attesa bloccante
            newTrip = (Trip)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().updateTripIntoDb(oldTrip, newTrip);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void updateBusIntoDbExecution() {
        Bus oldBus, newBus;
        try {
            oldBus = (Bus)socketObjectIn.readObject();//Attesa bloccante
            newBus = (Bus)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().updateBusIntoDb(oldBus, newBus);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void deleteStopPresenceFromDbExecution() {

        StopPresence sp;

        try {
            sp = (StopPresence) socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().deleteStopPresenceFromDb(sp);
            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void deleteTripOrBusOrStopFromDbExecution() {

        String subject, nomeG, dataG, targa;
        Integer numTappa;
        try {
            subject = (String)socketObjectIn.readObject();//Attesa bloccante
            nomeG = (String)socketObjectIn.readObject();//Attesa bloccante
            dataG = (String)socketObjectIn.readObject();//Attesa bloccante
            targa = (String)socketObjectIn.readObject();//Attesa bloccante
            numTappa = (Integer)socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().deleteStopOrBusOrTripFromDb(subject, nomeG, dataG, numTappa, targa);
            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void insertStopPresencesIntoDbExecution() {

        try {
            List<StopPresence> list = (List<StopPresence>)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertStopPresencesIntoDb(list);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void insertNewStopIntoDbExecution() {
        try {
            Stop stop = (Stop)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertNewStopIntoDb(stop);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void selectAllBusesForTripExecution() {

        List<Bus> list = null;
        String nomeG=null; String dataG=null;

        try {
            nomeG = (String)socketObjectIn.readObject();//Attesa bloccante
            dataG = (String)socketObjectIn.readObject();//Attesa bloccante
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            list = DMLCommandExecutor.getInstance().selectAllBusesForTripFromDb(nomeG, dataG);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void selectAllStopsForBusExecution() {

        List<Stop> list = null;
        String nomeG=null; String dataG=null;  String targa=null;

        try {
            nomeG = (String)socketObjectIn.readObject();//Attesa bloccante
            dataG = (String)socketObjectIn.readObject();//Attesa bloccante
            targa = (String)socketObjectIn.readObject();//Attesa bloccante
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            list = DMLCommandExecutor.getInstance().selectAllStopsFromBus(nomeG, dataG,targa);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void selectAllTripsExecution() {

        List<Trip> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectAllTripsFromDb();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void insertBusIntoDbExecution() {
        try {
            Bus bus = (Bus)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertBusIntoDb(bus);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void insertTripIntoDbExecution() {

        try {
            Trip trip = (Trip)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertTripIntoDb(trip);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void selectParentsForChildExecution() {
        List<Contact> list = null;
        String childCodF = null;

        try {
            childCodF = (String)socketObjectIn.readObject();//Attesa bloccante
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            list = DMLCommandExecutor.getInstance().selectParentsForChild(childCodF);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socketObjectOut.writeObject(list);
                socketObjectOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void deleteFromDbExecution() {

        String subject, toDelId;
        try {
            subject = (String)socketObjectIn.readObject();//Attesa bloccante
            toDelId = (String)socketObjectIn.readObject();//Attesa bloccante

            boolean status = DMLCommandExecutor.getInstance().deletePersonFromDb(subject, toDelId);
            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void selectAllChildrenExecution() {

        List<Child> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectChildrenFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
        //Send the list to client
        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void selectAllSuppliersExecution() {

        List<Supplier> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectSuppliersFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socketObjectOut.writeObject(list);
                socketObjectOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void selectAllStaffExecution() {
        List<Staff> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectStaffFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socketObjectOut.writeObject(list);
                socketObjectOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void selectAllContactsExecution() {

        List<Contact> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectContactsFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socketObjectOut.writeObject(list);
                socketObjectOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void updatePersonExecution() {

        Person p = null;
        try {
            p = (Person)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().updatePersonIntoDb(p);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void updateSupplierExecution() {
        Supplier s = null;
        try {
            s = (Supplier)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().updateSupplierIntoDb(s);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void insertSupplierExecution() {

        try {
            Supplier supplier = (Supplier)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertSupplierIntoDb(supplier);

            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void verifyLogin(){

        String username= null; String password = null;

        try {
            username = (String)socketObjectIn.readObject();
            password = (String)socketObjectIn.readObject();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        User user;

        try {

            user = DMLCommandExecutor.getInstance().selectUserForLogin(username, password);
            socketObjectOut.writeObject(user);
            socketObjectOut.flush();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in socket login");
        }
    }


    private void insertPersonExecution(){

        try {
            Person p =(Person)socketObjectIn.readObject();//Attesa bloccante
            boolean status = DMLCommandExecutor.getInstance().insertPersonIntoDb(p);
            socketObjectOut.writeBoolean(status);
            socketObjectOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void closeConnection(){

        try{
            socketObjectOut.close();
            socketObjectIn.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractUntoleratedDishesForPersonOnMenuDbExecution() {
        Person p = null;
        Menu.MenuTypeFlag menu = null;
        try {
            p = (Person) socketObjectIn.readObject();//Attesa bloccante
            menu = (Menu.MenuTypeFlag) socketObjectIn.readObject();//Attesa bloccante
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<String> list = null;
        try {
            list = DMLCommandExecutor.getInstance().selectUntoleratedDishesForPersonOnMenu(p, menu);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        try {
            socketObjectOut.writeObject(list);
            socketObjectOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}