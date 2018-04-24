package main.controllers;

import javafx.scene.Node;
import org.controlsfx.control.PopOver;

public class AbstractPopOverController extends AbstractController {

    public void close(Node node){
        PopOver stage = (PopOver) node.getScene().getWindow();
        stage.hide();
    }

}
