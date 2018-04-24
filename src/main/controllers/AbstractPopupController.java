package main.controllers;

import javafx.scene.Node;
import javafx.stage.Stage;

public class AbstractPopupController extends AbstractController {

    public void close(Node button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

}
