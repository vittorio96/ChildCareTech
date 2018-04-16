package main.controllers.Mensa;

import javafx.fxml.Initializable;
import main.NormalClasses.Mensa.Dish;
import main.controllers.AbstractController;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMensaViewDishes extends AbstractController implements Initializable {

    /*
        Static
    */

    private static Dish.DishTypeFlag dishType;


    public static Dish.DishTypeFlag getDishType() {
        return dishType;
    }

    public static void setDishType(Dish.DishTypeFlag dishType) {
        ControllerMensaViewDishes.dishType = dishType;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
