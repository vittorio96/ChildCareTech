package test.RMITest;

import main.entities.normal_entities.Anagrafica.Contact;
import main.entities.normal_entities.Anagrafica.Person;
import main.entities.normal_entities.Anagrafica.Staff;
import main.entities.normal_entities.Mensa.*;
import main.entities.normal_entities.Anagrafica.Child;
import main.client.Client;
import main.client.server_interface.RmiMode;
import main.client.server_interface.SessionMode;
import main.client.User;
import main.server.rmi_server.RMIServerLauncher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class RmiModeTestMensa {

    /*
        Tables
    */

    private static Client c;
    private static SessionMode rmiMode;

    @BeforeClass
    public static void testSetUp() {
        RMIServerLauncher.main(null);
        try {
            c = new Client();
        } catch (Exception e){
            //do nothing
        }
        rmiMode = new RmiMode(c);
    }

    @AfterClass
    public static void testSessionDisconnect() {
        cleanup();
        rmiMode.disconnect();
    }




    @Test
    public void insertIngredientIntoDb() throws Exception {
        assertTrue(rmiMode.insertIngredientIntoDb("nonexistingIngredient"));
    }

    @Test
    public void insertDishIntoDb() throws Exception {
        Dish testDish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        assertTrue(rmiMode.insertDishIntoDb(testDish));
        rmiMode.deleteDishFromDb(testDish);
    }

    @Test
    public void deleteDishFromDb() throws Exception {
        Dish testDish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        rmiMode.insertDishIntoDb(testDish);
        assertTrue(rmiMode.deleteDishFromDb(testDish));
    }

    @Test
    public void insertInvalidChildIntoleranceDue2Ingredient() throws Exception {
        Intolerance testIntolerance = new ChildIntolerance(getExistingChildCode(),getNonExistingIngredientName());
        assertFalse(rmiMode.insertIntoleranceIntoDb(testIntolerance));
        rmiMode.deleteSubjectFromDb("Child", getExistingChildCode());
    }

    @Test
    public void insertInvalidChildIntoleranceDue2ChildCode() throws Exception {
        Intolerance testIntolerance = new ChildIntolerance(getNonExistingChildCode(),getExistingIngredientName());
        assertFalse(rmiMode.insertIntoleranceIntoDb(testIntolerance));
    }

    @Test
    public void insertValidIntoleranceIntoDb() throws Exception {
        Intolerance testIntolerance = new ChildIntolerance(getExistingChildCode(),getExistingIngredientName());
        assertTrue(rmiMode.insertIntoleranceIntoDb(testIntolerance));
        rmiMode.deleteSubjectFromDb("Child", getExistingChildCode());
    }

    @Test
    public void deleteIntoleranceFromDb() throws Exception {
        rmiMode.deleteIntoleranceFromDb(getExistingChildIntolerance());
    }

    @Test
    public void extractIngredientsFromDb() throws Exception {
        getExistingIngredientName();
        assertNotNull(rmiMode.extractIngredientsFromDb());
    }

    @Test
    public void extractMenusFromDb() throws Exception {
        assertNotNull(rmiMode.extractMenusFromDb());
    }

    @Test
    public void extractDishesFromDb() throws Exception {
        rmiMode.insertDishIntoMenuIntoDb(getExistingMenu(), getExistingIngredientName());
        assertNotNull(rmiMode.extractDishesForMenuFromDb(getExistingMenu()));
        rmiMode.deleteDishFromMenuFromDb(getExistingMenu(),getExistingIngredientName());
    }

    @Test
    public void extractIntolerantsChildrenForIngredientFromDb() throws Exception {
        rmiMode.insertIntoleranceIntoDb(getExistingChildIntolerance());
        assertNotNull(rmiMode.extractIntolerantsChildrenForIngredientFromDb(getExistingIngredientName()));
        rmiMode.deleteIntoleranceFromDb(getExistingChildIntolerance());
    }

    @Test
    public void extractDishesForMenuFromDb() throws Exception {
        rmiMode.insertDishIntoMenuIntoDb(getExistingMenu(),getExistingDish().getNomeP());
        assertNotNull(rmiMode.extractDishesForMenuFromDb(getExistingMenu()));
        rmiMode.deleteDishFromMenuFromDb(getExistingMenu(),getExistingDish().getNomeP());
    }

    @Test
    public void extractIngredientsForDishFromDb() throws Exception {
        rmiMode.insertDishIntoDb(getExistingDish());
        rmiMode.insertIngredientIntoDishIntoDb(getExistingDish().getNomeP(),getExistingIngredientName());
        assertNotNull(rmiMode.extractIngredientsForDishFromDb(getExistingDish().getNomeP()));
        rmiMode.deleteDishFromDb(getExistingDish());
    }

    @Test
    public void insertIngredientIntoDishIntoDb() throws Exception {
        rmiMode.insertDishIntoDb(getExistingDish());
        assertTrue(rmiMode.insertIngredientIntoDishIntoDb(getExistingDish().getNomeP(),getExistingIngredientName()));
        rmiMode.deleteIngredientFromDishFromDb(getExistingDish().getNomeP(),getExistingIngredientName());
        rmiMode.deleteDishFromDb(getExistingDish());
    }

    @Test
    public void insertDishIntoMenuIntoDb() throws Exception {
        Dish dish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        rmiMode.insertDishIntoDb(dish);
        assertTrue(rmiMode.insertDishIntoMenuIntoDb(getExistingMenu(), dish.getNomeP()));
        rmiMode.deleteDishFromMenuFromDb(getExistingMenu(), dish.getNomeP());
        rmiMode.deleteDishFromDb(dish);
    }

    @Test
    public void deleteIngredientFromDishFromDb() throws Exception {
        rmiMode.insertIngredientIntoDishIntoDb(getExistingDish().getNomeP(), getExistingIngredientName());
        assertTrue(rmiMode.deleteIngredientFromDishFromDb(getExistingDish().getNomeP(), getExistingIngredientName()));
    }

    @Test
    public void deleteDishFromMenuFromDb() throws Exception {
        rmiMode.insertDishIntoMenuIntoDb(getExistingMenu(),getExistingDish().getNomeP());
        assertTrue(rmiMode.deleteDishFromMenuFromDb(getExistingMenu(), getExistingDish().getNomeP()));
    }

    @Test
    public void extractDishesForTypeFromDb() throws Exception {
        rmiMode.insertDishIntoMenuIntoDb(getExistingMenu(),getExistingDish().getNomeP());
        assertNotNull(rmiMode.extractDishesForTypeFromDb(getDishType()));
        rmiMode.deleteDishFromMenuFromDb(getExistingMenu(), getExistingDish().getNomeP());
    }

    @Test
    public void extractIntolerantsWorkersForIngredientFromDb() throws Exception {
        Intolerance intolerance= new PersonIntolerance(getExistingStaff().getCodiceFiscale(),getExistingIngredientName());
        boolean success = rmiMode.insertIntoleranceIntoDb(intolerance);
        assertNotNull(rmiMode.extractIntolerantsWorkersForIngredientFromDb(getExistingIngredientName()));
        rmiMode.deleteIntoleranceFromDb(intolerance);
        rmiMode.deleteSubjectFromDb("Staff", getExistingStaff().getCodiceFiscale());


    }

    @Test
    public void extractUntoleratedIngredientsForPersonFromDb() throws Exception {
        Intolerance intolerance= new ChildIntolerance(getExistingChildCode(),getExistingIngredientName());
        rmiMode.insertIntoleranceIntoDb(intolerance);
        assertNotNull(rmiMode.extractUntoleratedIngredientsForPersonFromDb(getExistingChild()));
        rmiMode.deleteIntoleranceFromDb(intolerance);
        rmiMode.deleteSubjectFromDb("Child", getExistingChildCode());
    }

    @Test
    public void extractNotUntoleratedIngredientsForPersonFromDb() throws Exception {
        Intolerance intolerance= new ChildIntolerance(getExistingChildCode(),getExistingIngredientName());
        rmiMode.insertIntoleranceIntoDb(intolerance);
        List<String> list= rmiMode.extractNotUntoleratedIngredientsForPersonFromDb(getExistingChild());
        Iterator<String> itr = list.iterator();
        while(itr.hasNext()){
            assertFalse(itr.next().equals(getExistingIngredientName()));
        }
        rmiMode.deleteSubjectFromDb("Child", getExistingChildCode());
        rmiMode.deleteIntoleranceFromDb(intolerance);
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
        rmiMode.insertPersonIntoDb(gen1);
        rmiMode.insertPersonIntoDb(gen2);
        rmiMode.insertPersonIntoDb(pedr);
        rmiMode.insertPersonIntoDb(testChild);
        return testChild;
    }

    private String getExistingChildCode() {
        return getExistingChild().getCodiceFiscale();
    }

    private String getNonExistingChildCode() {
        return "RANDOMCODE123456";
    }

    private String getExistingIngredientName() {
        rmiMode.insertIngredientIntoDb("existingTestIngredient");
        return "existingTestIngredient";
    }
    private String getNonExistingIngredientName() {
        return "nonExistingtestIngredient";
    }

    private Intolerance getExistingChildIntolerance() {
        Intolerance intolerance= new ChildIntolerance(getExistingChild().getCodiceFiscale(),getExistingIngredientName());
        rmiMode.insertIntoleranceIntoDb(intolerance);
        return intolerance;
    }

    private Intolerance getExistingStaffIntolerance() {
        Intolerance intolerance= new PersonIntolerance(getExistingStaff().getCodiceFiscale(),getExistingIngredientName());
        rmiMode.insertIntoleranceIntoDb(intolerance);
        return intolerance;
    }

    public Menu.MenuTypeFlag getExistingMenu() {
        return Menu.MenuTypeFlag.MONDAY;
    }

    public Dish getExistingDish() {
        Dish dish = new Dish("PiattoTest", Dish.DishTypeFlag.PRIMO);
        rmiMode.insertDishIntoDb(dish);
        return dish;
    }

    public Dish.DishTypeFlag getDishType() {
        return Dish.DishTypeFlag.PRIMO;
    }

    public Person getExistingStaff() {
        Person staff = new Staff("CODFISSTAFTEST12", "NomeStaff", "CognomeStaff", "UsernameStaff", "password", "1990-01-01", User.UserTypeFlag.AMMINISTRATIVO);
        rmiMode.insertPersonIntoDb(staff);
        return staff;
    }

    private static void cleanup() {
        rmiMode.deleteSubjectFromDb("Contact","CODFISGEN1TEST12");
        rmiMode.deleteSubjectFromDb("Contact","CODFISGEN2TEST12");
        rmiMode.deleteSubjectFromDb("Contact","CODFISPEDRTEST12");
        rmiMode.deleteSubjectFromDb("Child", "CODFISTEST123456");
    }
}