package RMITest.TestMensa;

import main.Client;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Mensa.ChildIntolerance;
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.NormalClasses.Mensa.Intolerance;
import main.RMIServerLauncher;
import main.RmiMode;
import main.SessionMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
        rmiMode.disconnect();
    }

    @Test
    public void insertIngredientIntoDb() throws Exception {
        assertTrue(rmiMode.insertIngredientIntoDb("testIngredient" ));
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
    public void insertInvalidChildIntoleranceIntoDb() throws Exception {
        insertInvalidChildIntoleranceDue2Ingredient();
        insertInvalidChildIntoleranceDue2ChildCode();
    }

    @Test
    public void insertInvalidChildIntoleranceDue2Ingredient() throws Exception {
        Intolerance testIntolerance = new ChildIntolerance(getExistingChildCode(),getNonExistingIngredientName());
        assertFalse(rmiMode.insertIntoleranceIntoDb(testIntolerance));
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

    }

    @Test
    public void extractDishesFromDb() throws Exception {
    }

    @Test
    public void extractIntolerantsChildrenForIngredientFromDb() throws Exception {
    }

    @Test
    public void extractDishesForMenuFromDb() throws Exception {
    }

    @Test
    public void extractIngredientsForDishFromDb() throws Exception {
    }

    @Test
    public void insertIngredientIntoDishIntoDb() throws Exception {
    }

    @Test
    public void insertDishIntoMenuIntoDb() throws Exception {
    }

    @Test
    public void deleteIngredientFromDishFromDb() throws Exception {
    }

    @Test
    public void deleteDishFromMenuFromDb() throws Exception {
    }

    @Test
    public void extractDishesForTypeFromDb() throws Exception {
    }

    @Test
    public void extractIntolerantsWorkersForIngredientFromDb() throws Exception {
    }

    @Test
    public void extractUntoleratedIngredientsForPersonFromDb() throws Exception {

    }

    @Test
    public void extractNotUntoleratedIngredientsForPersonFromDb() throws Exception {

    }

    private String getExistingChildCode() {
        Child testChild = new Child();
        testChild.setCodR("TESTCODE01");
        testChild.setCodiceFiscale("CODFISTEST123456");
        testChild.setDataNascita("1995-12-12");
        testChild.setCodiceFiscaleGen1("CODFISGEN1TEST12");
        testChild.setCodiceFiscaleGen2("CODFISGEN2TEST12");
        testChild.setNome("TestName");
        testChild.setCognome("TestCognome");
        rmiMode.insertPersonIntoDb(testChild);
        return "CODFISTEST123456";
    }

    private String getNonExistingChildCode() {
        return "RANDOMCODE123456";
    }

    private String getExistingIngredientName() {
        rmiMode.insertIngredientIntoDb("existingTestIngredient");
        return "existingTestIngredient";
    }
    private String getNonExistingIngredientName() {
        return "testIngredient";
    }

    private Intolerance getExistingChildIntolerance() {
        Intolerance intolerance= new ChildIntolerance(getExistingChildCode(),getExistingIngredientName());
        rmiMode.insertIntoleranceIntoDb(intolerance);
        return intolerance;
    }
}