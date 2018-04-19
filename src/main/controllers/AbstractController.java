package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Client;
import main.Gui;
import main.User;
import org.controlsfx.control.PopOver;


import java.io.IOException;

public abstract class AbstractController {
    //abstract to avoid instantiation

    protected static Client CLIENT;
    public static AbstractController currentController;
    protected static User loggedUser;
    protected static User.UserTypeFlag userTypeFlag;

    public static void registerClient(Client c){
        CLIENT = c;
        System.out.println(CLIENT);
    }

    public static void setCurrentController(AbstractController controller){
        currentController = controller;
    }

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

    public void changeScene(Node node, String fxmlPath, int w, int h) throws IOException {

        Stage stage;
        Parent root;
        stage=(Stage) node.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root, w, h);
        stage.setResizable(false);
        root.requestFocus();
        stage.setScene(scene);
        stage.show();

    }

    public void changeSceneInPopup(Button button, String fxmlPath, int dimW, int dimH) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage;
        primaryStage =(Stage) button.getScene().getWindow();
        dialogStage.initOwner(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root,dimW,dimH);
        dialogStage.setScene(scene);
        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }

    public void openPopup(Node node, String fxmlPath, int dimW, int dimH) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage;
        primaryStage =(Stage) node.getScene().getWindow();
        dialogStage.initOwner(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root,dimW,dimH);
        dialogStage.setScene(scene);
        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }

    public void goHome(Button button) throws IOException {
        changeScene(button,"resources/fxml/splashscreen.fxml");
    }

    public void createErrorPopup(String headerText, String contentText){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("Errore");
        alert2.setHeaderText(headerText);
        alert2.setContentText(contentText);
        alert2.showAndWait();
    }

    public void createSuccessPopup(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
        alert2.setGraphic(new javafx.scene.image.ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
        alert2.setTitle("Successo");
        alert2.setHeaderText("Successo!");
        alert2.setContentText("Dati inseriti correttamente nel database.\n");
        alert2.showAndWait();
    }

    public void createInfoPopup(String contentText){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("Informazioni");
        alert2.setHeaderText("Come utilizzare la finestra");
        alert2.setContentText(contentText);
        alert2.showAndWait();
    }

    public void closePopup(Node button){
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    public void openPopOver(String fxmlPath, PopOver.ArrowLocation arrowLocation, Node node) throws IOException {
        PopOver popOver = new PopOver();
        popOver.setArrowLocation(arrowLocation);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.setAnimated(true);
        AnchorPane p = FXMLLoader.load(getClass().getResource(fxmlPath));
        popOver.setContentNode(p);

        popOver.show(node);
    }

    public void hidePopOver(Node node){
        PopOver stage = (PopOver) node.getScene().getWindow();
        stage.hide();
    }

    public void changeMenu(String fxmlPath, AnchorPane lateralPane) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(fxmlPath));
        lateralPane.getChildren().setAll(anchorPane);
    }

    public boolean createConfirmationDialog(String headerText, String contentText){
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType buttonTypeOne = new ButtonType("No");
        ButtonType buttonTypeTwo = new ButtonType("Si");
        alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        alert2.setHeaderText(headerText);
        alert2.setContentText(contentText);
        alert2.showAndWait();

        return alert2.getResult() == buttonTypeTwo;
    }

    public boolean isUserSure(){
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType buttonTypeOne = new ButtonType("No");
        ButtonType buttonTypeTwo = new ButtonType("Si");
        alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        alert2.setHeaderText("Sei sicuro ?");
        alert2.setContentText("Una volta fatta questa azione non si può più tornare indietro");
        alert2.showAndWait();

        return alert2.getResult() == buttonTypeTwo;
    }



}
