package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.AbstractController;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static java.lang.System.exit;

public class Gui extends Application {

    private static Client c;
    //Creates main window and instantiates Client

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();

        //clientAssociationToController();

        Parent root = fxmlLoader.load(getClass().getResource("resources/fxml/login.fxml"));
        primaryStage.setTitle("Child Care Tech");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();


    }

    /*private void clientAssociationToController() throws RemoteException, MalformedURLException, NotBoundException {
        this.c = new Client();
        AbstractController.registerClient(c);
    }*/

    public static void main(String[] args) throws RemoteException {
        launch(args);
        Gui.c.getSession().disconnect();
        exit(0);
    }
}