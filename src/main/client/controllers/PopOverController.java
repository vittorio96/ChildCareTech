package main.client.controllers;

import javafx.scene.Node;
import org.controlsfx.control.PopOver;

public class PopOverController implements ControllerType {

    public void close(Node node){
        PopOver stage = (PopOver) node.getScene().getWindow();
        stage.hide();
    }

}
