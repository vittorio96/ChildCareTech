package main.client.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;

public class StageController implements ControllerType{

    public void close(Node button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
