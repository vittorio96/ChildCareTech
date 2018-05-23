package test.SocketTest;

import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Contact;
import main.Classes.NormalClasses.Anagrafica.Person;
import main.Classes.NormalClasses.Anagrafica.Staff;
import main.Classes.NormalClasses.Mensa.*;
import main.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class SocketModeTestMensa {

    /*
        Tables
    */

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
    public void insertIngredientIntoDb() throws Exception {
        assertTrue(socketMode.insertIngredientIntoDb("testIngredient" ));
    }

    @Test
    public void insertDishIntoDb() throws Exception {
        Dish testDish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        assertTrue(socketMode.insertDishIntoDb(testDish));
        socketMode.deleteDishFromDb(testDish);
    }

    @Test
    public void deleteDishFromDb() throws Exception {
        Dish testDish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        socketMode.insertDishIntoDb(testDish);
        assertTrue(socketMode.deleteDishFromDb(testDish));
    }

    @Test
    public void insertInvalidChildIntoleranceDue2Ingredient() throws Exception {
        Intolerance testIntolerance = new ChildIntolerance(getExistingChildCode(),getNonExistingIngredientName());
        assertFalse(socketMode.insertIntoleranceIntoDb(testIntolerance));
        socketMode.deleteSubjectFromDb("Child", getExistingChildCode());
    }

    @Test
    public void insertInvalidChildIntoleranceDue2ChildCode() throws Exception {
        Intolerance testIntolerance = new ChildIntolerance(getNonExistingChildCode(),getExistingIngredientName());
        assertFalse(socketMode.insertIntoleranceIntoDb(testIntolerance));
    }

    @Test
    public void insertValidIntoleranceIntoDb() throws Exception {
        Intolerance testIntolerance = new ChildIntolerance(getExistingChildCode(),getExistingIngredientName());
        assertTrue(socketMode.insertIntoleranceIntoDb(testIntolerance));
        socketMode.deleteSubjectFromDb("Child", getExistingChildCode());
    }

    @Test
    public void deleteIntoleranceFromDb() throws Exception {
        socketMode.deleteIntoleranceFromDb(getExistingChildIntolerance());
    }

    @Test
    public void extractIngredientsFromDb() throws Exception {
        getExistingIngredientName();
        assertNotNull(socketMode.extractIngredientsFromDb());
    }

    @Test
    public void extractMenusFromDb() throws Exception {
        assertNotNull(socketMode.extractMenusFromDb());
    }

    @Test
    public void extractDishesFromDb() throws Exception {
        socketMode.insertDishIntoMenuIntoDb(getExistingMenu(), getExistingIngredientName());
        assertNotNull(socketMode.extractDishesForMenuFromDb(getExistingMenu()));
        socketMode.deleteDishFromMenuFromDb(getExistingMenu(),getExistingIngredientName());
    }

    @Test
    public void extractIntolerantsChildrenForIngredientFromDb() throws Exception {
        socketMode.insertIntoleranceIntoDb(getExistingChildIntolerance());
        assertNotNull(socketMode.extractIntolerantsChildrenForIngredientFromDb(getExistingIngredientName()));
        socketMode.deleteIntoleranceFromDb(getExistingChildIntolerance());
    }

    @Test
    public void extractDishesForMenuFromDb() throws Exception {
        socketMode.insertDishIntoMenuIntoDb(getExistingMenu(),getExistingDish().getNomeP());
        assertNotNull(socketMode.extractDishesForMenuFromDb(getExistingMenu()));
        socketMode.deleteDishFromMenuFromDb(getExistingMenu(),getExistingDish().getNomeP());
    }

    @Test
    public void extractIngredientsForDishFromDb() throws Exception {
        socketMode.insertDishIntoDb(getExistingDish());
        socketMode.insertIngredientIntoDishIntoDb(getExistingDish().getNomeP(),getExistingIngredientName());
        assertNotNull(socketMode.extractIngredientsForDishFromDb(getExistingDish().getNomeP()));
        socketMode.deleteDishFromDb(getExistingDish());
    }

    @Test
    public void insertIngredientIntoDishIntoDb() throws Exception {
        socketMode.insertDishIntoDb(getExistingDish());
        assertTrue(socketMode.insertIngredientIntoDishIntoDb(getExistingDish().getNomeP(),getExistingIngredientName()));
        socketMode.deleteIngredientFromDishFromDb(getExistingDish().getNomeP(),getExistingIngredientName());
        socketMode.deleteDishFromDb(getExistingDish());
    }

    @Test
    public void insertDishIntoMenuIntoDb() throws Exception {
        Dish dish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        socketMode.insertDishIntoDb(dish);
        assertTrue(socketMode.insertDishIntoMenuIntoDb(getExistingMenu(), dish.getNomeP()));
        socketMode.deleteDishFromMenuFromDb(getExistingMenu(), dish.getNomeP());
        socketMode.deleteDishFromDb(dish);
    }

    @Test
    public void deleteIngredientFromDishFromDb() throws Exception {
        socketMode.insertIngredientIntoDishIntoDb(getExistingDish().getNomeP(), getExistingIngredientName());
        assertTrue(socketMode.deleteIngredientFromDishFromDb(getExistingDish().getNomeP(), getExistingIngredientName()));
    }

    @Test
    public void deleteDishFromMenuFromDb() throws Exception {
        socketMode.insertDishIntoMenuIntoDb(getExistingMenu(),getExistingDish().getNomeP());
        assertTrue(socketMode.deleteDishFromMenuFromDb(getExistingMenu(), getExistingDish().getNomeP()));
    }

    @Test
    public void extractDishesForTypeFromDb() throws Exception {
        socketMode.insertDishIntoMenuIntoDb(getExistingMenu(),getExistingDish().getNomeP());
        assertNotNull(socketMode.extractDishesForTypeFromDb(getDishType()));
        socketMode.deleteDishFromMenuFromDb(getExistingMenu(), getExistingDish().getNomeP());
    }

    @Test
    public void extractIntolerantsWorkersForIngredientFromDb() throws Exception {
        Intolerance intolerance= new PersonIntolerance(getExistingStaff().getCodiceFiscale(),getExistingIngredientName());
        boolean success = socketMode.insertIntoleranceIntoDb(intolerance);
        assertNotNull(socketMode.extractIntolerantsWorkersForIngredientFromDb(getExistingIngredientName()));
        socketMode.deleteIntoleranceFromDb(intolerance);
        socketMode.deleteSubjectFromDb("Staff", getExistingStaff().getCodiceFiscale());


    }

    @Test
    public void extractUntoleratedIngredientsForPersonFromDb() throws Exception {
        Intolerance intolerance= new ChildIntolerance(getExistingChildCode(),getExistingIngredientName());
        socketMode.insertIntoleranceIntoDb(intolerance);
        assertNotNull(socketMode.extractUntoleratedIngredientsForPersonFromDb(getExistingChild()));
        socketMode.deleteIntoleranceFromDb(intolerance);
        socketMode.deleteSubjectFromDb("Child", getExistingChildCode());
    }

    @Test
    public void extractNotUntoleratedIngredientsForPersonFromDb() throws Exception {
        Intolerance intolerance= new ChildIntolerance(getExistingChildCode(),getExistingIngredientName());
        socketMode.insertIntoleranceIntoDb(intolerance);
        List<String> list= socketMode.extractNotUntoleratedIngredientsForPersonFromDb(getExistingChild());
        Iterator<String> itr = list.iterator();
        while(itr.hasNext()){
            assertFalse(itr.next().equals(getExistingIngredientName()));
        }
        socketMode.deleteSubjectFromDb("Child", getExistingChildCode());
        socketMode.deleteIntoleranceFromDb(intolerance);
    }

    private Child getExistingChild() {
        Child testChild = new Child();
        testChild.setCodR("TESTCODE01");
        testChild.setCodiceFiscale("CODFISTEST123456");
        testChild.setDataNascita("1995-12-12");
        testChild.setCodiceFiscaleGen1("CODFISGEN1TEST12");
        testChild.setCodiceFiscaleGen2("CODFISGEN2TEST12");
        testChild.setCodiceFiscalePediatra("CODFISPEDRTEST12");
        testChild.setNome("TestName");
        testChild.setCognome("TestCognome");
        Person gen1 =  new Contact("NomeGen1", "CognomeGen1",
                "CODFISGEN1TEST12", "1111111111", Contact.ContactTypeFlag.GENITORE);
        Person gen2 =  new Contact("NomeGen2", "CognomeGen2",
                "CODFISGEN2TEST12", "1111111112", Contact.ContactTypeFlag.GENITORE);
        Person pedr =  new Contact("NomePedr", "CognomePedr",
                "CODFISPEDRTEST12", "1111111113", Contact.ContactTypeFlag.PEDIATRA);
        socketMode.insertPersonIntoDb(gen1);
        socketMode.insertPersonIntoDb(gen2);
        socketMode.insertPersonIntoDb(pedr);
        socketMode.insertPersonIntoDb(testChild);
        return testChild;
    }

    private String getExistingChildCode() {
        return getExistingChild().getCodiceFiscale();
    }

    private String getNonExistingChildCode() {
        return "RANDOMCODE123456";
    }

    private String getExistingIngredientName() {
        socketMode.insertIngredientIntoDb("existingTestIngredient");
        return "existingTestIngredient";
    }
    private String getNonExistingIngredientName() {
        return "nonExistingtestIngredient";
    }

    private Intolerance getExistingChildIntolerance() {
        Intolerance intolerance= new ChildIntolerance(getExistingChild().getCodiceFiscale(),getExistingIngredientName());
        socketMode.insertIntoleranceIntoDb(intolerance);
        return intolerance;
    }

    private Intolerance getExistingStaffIntolerance() {
        Intolerance intolerance= new PersonIntolerance(getExistingStaff().getCodiceFiscale(),getExistingIngredientName());
        socketMode.insertIntoleranceIntoDb(intolerance);
        return intolerance;
    }

    public Menu.MenuTypeFlag getExistingMenu() {
        return Menu.MenuTypeFlag.MONDAY;
    }

    public Dish getExistingDish() {
        Dish dish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        socketMode.insertDishIntoDb(dish);
        return dish;
    }

    public Dish.DishTypeFlag getDishType() {
        return Dish.DishTypeFlag.PRIMO;
    }

    public Person getExistingStaff() {
        Person staff = new Staff("CODFISSTAFTEST12", "NomeStaff", "CognomeStaff", "UsernameStaff", "password", "1990-01-01", User.UserTypeFlag.AMMINISTRATIVO);
        socketMode.insertPersonIntoDb(staff);
        return staff;
    }

}