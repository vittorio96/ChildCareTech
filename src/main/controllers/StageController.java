package main.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StageController implements ControllerType{

    public void close(Node button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
