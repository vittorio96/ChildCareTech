package main.client.controllers.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.client.User;
import main.client.controllers.AbstractController;
import main.client.controllers.StageController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMainMenu extends AbstractController implements Initializable {

    /*
        Buttons & Initialization
    */

    @FXML private TextField loggedUserDataDisplay;
    @FXML private AnchorPane lateralPane;
    @FXML private Pane adminPane;
    @FXML private Pane chefPane;
    @FXML private Pane teacherPane;


    @FXML protected Button giteButton;
    @FXML protected Button mensaButton;
    @FXML protected Button anagraficaButton;
    @FXML protected Button accessiButton;
    @FXML protected Button logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AbstractController.setCurrentController(this);
        setControllerType();
        loggedUserDataDisplay.setText(loggedUser.getUsername().toUpperCase());
        setEnabledItems();
    }

    protected void setControllerType() {
        controllerType = new StageController();
    }

    /*
        Menu Redirects
    */

    @FXML protected void handleMensaButtonAction(ActionEvent event) throws IOException {
        changeMenu("../../resources/fxml/main_mensa.fxml",lateralPane);
    }

    @FXML protected void handleGiteButtonAction(ActionEvent event) throws IOException {
        changeMenu("../../resources/fxml/main_gite.fxml",lateralPane);
    }

    @FXML protected void handleAnagraficaButtonAction(ActionEvent event) throws IOException {
        changeMenu("../../resources/fxml/main_anagrafica.fxml",lateralPane);
    }

    @FXML protected void handleAccessiButtonAction(ActionEvent event) throws IOException {
        changeMenu("../../resources/fxml/main_accessi.fxml",lateralPane);
    }
    
    @FXML protected void handleLogoutButtonAction(ActionEvent event) throws IOException {
        CLIENT.getSession().disconnect();
        changeScene(logoutButton,"../../resources/fxml/login.fxml");
    }

    @Override
    public void refresh() {

    }



    private void setEnabledItems() {

        if(userTypeFlag == User.UserTypeFlag.MENSA) {
            chefPane.setVisible(true);
            adminPane.setVisible(false);
            teacherPane.setVisible(false);
            giteButton.setDisable(true);
            anagraficaButton.setDisable(true);
        }else if(userTypeFlag == User.UserTypeFlag.SUPERVISORE){
            teacherPane.setVisible(true);
            chefPane.setVisible(false);
            adminPane.setVisible(false);
            mensaButton.setDisable(true);
        }else {
            adminPane.setVisible(true);
            chefPane.setVisible(false);
            teacherPane.setVisible(false);

        }

    }


}
