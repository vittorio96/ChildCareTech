package main.controllers.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.User;
import main.controllers.AbstractController;
import main.controllers.AbstractStageController;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMainMensa extends AbstractStageController implements Initializable {

    /*
        Buttons & Initialization
    */

    @FXML private Button aggiungiAllergieButton;
    @FXML private Button modificaMenuButton;
    @FXML private Button addIngredientButton;
    @FXML private Button conflittiAllergieButton;
    @FXML private Button todaysProblemButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AbstractController.setCurrentController(this);
    }

    /*
        Menu Redirects
    */

    public void modificamenu(ActionEvent event) throws IOException {
       //changeScene(modificaMenuButton,"../../resources/fxml/mensa_menu.fxml");
        changeSceneInPopup(modificaMenuButton,"../../resources/fxml/mensa_menu.fxml" , 800,450);
    }

    public void handleAggiungiAllergieButtonAction() throws IOException {
        changeSceneInPopup(aggiungiAllergieButton,"../../resources/fxml/mensa_addAllergy.fxml" , 800,450);
    }

    public void addNewIngredient() throws IOException {
        changeSceneInPopup(addIngredientButton,"../../resources/fxml/mensa_addIngredient.fxml", 380,380);
    }

    public void vediConflitti() throws IOException {
        changeSceneInPopup(conflittiAllergieButton,"../../resources/fxml/mensa_conflittiAllergie.fxml",800,450);
    }

    public void viewTodaysConflicts() throws IOException {
        changeSceneInPopup(todaysProblemButton,"../../resources/fxml/mensa_viewTodaysConflicts.fxml",800,450);
    }
}

