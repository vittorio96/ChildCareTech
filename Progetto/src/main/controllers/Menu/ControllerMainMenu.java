package main.controllers.Menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.User;
import main.controllers.AbstractController;
import org.controlsfx.control.PopOver;

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
        loggedUserDataDisplay.setText(loggedUser.getUsername().toUpperCase());
        setEnabledItems();
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

    @FXML protected void handleLogoutButtonAction(ActionEvent event) throws IOException {
        changeScene(logoutButton,"../../resources/fxml/login.fxml");
    }

    @FXML protected void handleAccessiButtonAction(ActionEvent event) throws IOException {
        changeMenu("../../resources/fxml/main_accessi.fxml",lateralPane);
    }

    private void setEnabledItems() {

        if(userTypeFlag == User.UserTypeFlag.MENSA) {
            adminPane.setVisible(false);
            teacherPane.setVisible(false);
            chefPane.setVisible(true);
            giteButton.setDisable(true);
            anagraficaButton.setDisable(true);
        }
        if(userTypeFlag== User.UserTypeFlag.SUPERVISORE){
            chefPane.setVisible(false);
            adminPane.setVisible(false);
            teacherPane.setVisible(true);
            mensaButton.setDisable(true);
        }else {
            chefPane.setVisible(false);
            teacherPane.setVisible(false);
            adminPane.setVisible(true);
        }

    }


}
