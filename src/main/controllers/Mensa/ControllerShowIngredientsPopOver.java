package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import main.StringPropertyClasses.Mensa.StringPropertyDish;
import main.StringPropertyClasses.Mensa.StringPropertyIngredient;
import main.controllers.AbstractController;
import main.controllers.AbstractPopOverController;
import main.controllers.AbstractPopupController;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerShowIngredientsPopOver extends AbstractPopOverController implements Initializable {

    /*
        Static
    */

    private static StringPropertyDish dish;

    public static void setDish(StringPropertyDish dish) { ControllerShowIngredientsPopOver.dish = dish; }


    /*
        Initialize
    */

    @FXML private TableView<StringPropertyIngredient> ingredientsOnDishTable;
    @FXML private TableColumn <StringPropertyIngredient,String> ingredientsOnDish;
    private ObservableList<StringPropertyIngredient> ingredientsObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ingredientsOnDish.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        showIngredients();

    }
    private void showIngredients() {
        List<String> ingredients = CLIENT.clientExtractIngredientsForDishFromDb(dish.getNomeP());
        if (ingredients != null) for (String s : ingredients) {
            ingredientsObservableList.add(new StringPropertyIngredient(s));
        }
        ingredientsOnDishTable.setItems(ingredientsObservableList);
    }

}
