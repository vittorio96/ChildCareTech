
package main.controllers.Menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.Client;
import main.User;
import main.controllers.AbstractController;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerLogin extends AbstractController implements Initializable {

    /*
       Static
    */

    private static ControllerLogin controllerLogin;

    public static ControllerLogin getControllerLogin(){
        return controllerLogin;
    }

    /*
       Buttons & Initialization
    */

    @FXML private Button loginButton;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField outputField;
    @FXML private ComboBox connectionModeDropList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Implementing the Initializable interface means that this method
        // will be called when the controllerLogin instance is created

        AbstractController.setCurrentController(this);
        controllerLogin = this;

        ArrayList items = new ArrayList<String>();
        items.add("RMI");
        items.add("Socket");
        ObservableList al = FXCollections.observableArrayList(items);
        connectionModeDropList.setItems(al);
    }

    public ControllerLogin() throws RemoteException, NotBoundException, MalformedURLException {
        super();
    }

    public void registerUser(User u){
        //sets static variable of superclass
        loggedUser = u;
        userTypeFlag = loggedUser.getUserTypeFlag();
    }

    @FXML protected void handleLoginButtonAction( ActionEvent event ) throws IOException {
        String user = usernameField.getText();
        String password = passwordField.getText();


        if(connectionModeDropList.getSelectionModel().isEmpty()) {
            createErrorPopup("Errore", "Scegli un tipo di connessione");

        }else{
            String choice = connectionModeDropList.getSelectionModel().getSelectedItem().toString();
            User userObject = CLIENT.clientLoginDataHandler(user, password, choice);

            if (userObject == null) {
                outputField.setText("Errore");
            } else {
                registerUser(userObject);
                Client.registerUser(userObject);
                //changeScene(loginButton, "../resources/fxml/splashscreen.fxml");
                changeScene(loginButton, "../../resources/fxml/main_menu.fxml");
            }
        }
    }

    @FXML protected void handleConnectionModeButton(ActionEvent event){

    }

    public void printToOutputField(String s){
        outputField.setText(s);
    }

}

