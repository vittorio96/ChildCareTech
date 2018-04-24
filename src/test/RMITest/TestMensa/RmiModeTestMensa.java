package test.RMITest.TestMensa;

import main.Client;
import main.RmiMode;
import main.SessionMode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class RmiModeTestMensa {

    private Client c;
    private SessionMode rmiMode;

    @BeforeClass
    public void testSetUp() {
        try {
            this.c = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        this. rmiMode = new RmiMode(c);
    }

    @AfterClass
    public void testSessionDisconnect() {
        this.rmiMode.disconnect();
    }

    @Test
    public void insertIngredientIntoDb() throws Exception {
    }

    @Test
    public void insertDishIntoDb() throws Exception {
    }

    @Test
    public void deleteDishFromDb() throws Exception {
    }

    @Test
    public void insertIntoleranceIntoDb() throws Exception {
    }

    @Test
    public void deleteIntoleranceFromDb() throws Exception {
    }

    @Test
    public void extractIngredientsFromDb() throws Exception {
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

}