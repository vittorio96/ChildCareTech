package main.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class AbstractStageController extends AbstractController{
    public void changeScene(Button button, String fxmlPath) throws IOException {

        Stage stage;
        Parent root;
        stage=(Stage) button.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root, 800, 450);
        stage.setResizable(false);
        root.requestFocus();
        stage.setScene(scene);
        stage.show();

    }

    public void close(Node button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
