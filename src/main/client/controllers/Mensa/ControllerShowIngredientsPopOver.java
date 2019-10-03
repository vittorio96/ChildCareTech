package main.client.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.entities.string_property_entities.Mensa.StringPropertyDish;
import main.entities.string_property_entities.Mensa.StringPropertyIngredient;
import main.client.controllers.AbstractController;
import main.client.controllers.PopOverController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerShowIngredientsPopOver extends AbstractController implements Initializable {

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
        setControllerType();
        setColumnAssociations();
        showIngredients();

    }


    protected void setColumnAssociations() {
        ingredientsOnDish.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
    }

    protected void setControllerType() {
        controllerType = new PopOverController();
    }

    private void showIngredients() {
        List<String> ingredients = CLIENT.clientExtractIngredientsForDishFromDb(dish.getNomeP());
        if (ingredients != null) for (String s : ingredients) {
            ingredientsObservableList.add(new StringPropertyIngredient(s));
        }
        ingredientsOnDishTable.setItems(ingredientsObservableList);
    }

    @Override
    public void refresh() {

    }

}
