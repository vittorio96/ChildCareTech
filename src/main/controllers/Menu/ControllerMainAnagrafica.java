package main.controllers.Menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.User;
import main.controllers.AbstractController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerMainAnagrafica extends AbstractController implements Initializable {

    /*
        Buttons & Initialization
    */

    @FXML Button addStaffButton;
    @FXML Button addChildButton;
    @FXML Button addContactButton;
    @FXML Button editStaffButton;
    @FXML Button editChildButton;
    @FXML Button editContactButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AbstractController.setCurrentController(this);
    }

    /*
        Scene Redirects
    */

    @FXML protected void handleAddChildButton(ActionEvent event) throws IOException {
        changeSceneInPopup(addChildButton,"../../resources/fxml/anagrafica_addChild.fxml",800,450);
    }

    @FXML protected void handleAddStaffButton(ActionEvent event) throws IOException {
        changeSceneInPopup(addStaffButton,"../../resources/fxml/anagrafica_addStaff.fxml",800,450);
    }

    @FXML protected void handleAddContactButton(ActionEvent event) throws IOException {
        changeSceneInPopup(addContactButton,"../../resources/fxml/anagrafica_addContact.fxml",800,450);
    }

    @FXML protected void handleEditChildButton(ActionEvent event) throws IOException {
        changeSceneInPopup(editChildButton,"../../resources/fxml/anagrafica_manageChild.fxml",800,450);
    }

    @FXML protected void handleEditStaffButton(ActionEvent event) throws IOException {
        changeSceneInPopup(editStaffButton,"../../resources/fxml/anagrafica_manageStaff.fxml",800,450);
    }

    @FXML protected void handleEditContactButton(ActionEvent event) throws IOException {
        changeSceneInPopup(editContactButton,"../../resources/fxml/anagrafica_manageContact.fxml",800,450);
    }
}
