package main.client.controllers.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.client.controllers.AbstractController;
import main.client.controllers.StageController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerMainGite extends AbstractController implements Initializable {

    /*
        Buttons & Initialization
    */

    @FXML private Button presenzeButton;
    @FXML private Button addTripButton;
    @FXML private Button addStopButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AbstractController.setCurrentController(this);
        setControllerType();
    }

    protected void setControllerType() {
        controllerType = new StageController();
    }
    /*
        Scene Redirects
    */

    @FXML protected void handleAddTripButtonAction(ActionEvent event) throws IOException {
        changeSceneInPopup(addTripButton, "../../resources/fxml/gite_manageTrip.fxml", 800, 450);
    }

    @FXML protected void handleAddStopButtonAction(ActionEvent event) throws IOException {
        changeSceneInPopup(addStopButton, "../../resources/fxml/gite_addStop.fxml", 800, 450);
    }

    @FXML protected void handlePresenzeButtonAction(ActionEvent event) throws IOException {
        changeSceneInPopup(presenzeButton, "../../resources/fxml/gite_addPresence.fxml", 900, 600);
    }

    @Override
    public void refresh() {

    }

}
