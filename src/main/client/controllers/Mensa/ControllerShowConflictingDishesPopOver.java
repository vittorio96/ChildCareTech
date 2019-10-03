package main.client.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.entities.normal_entities.Anagrafica.Person;
import main.entities.normal_entities.Mensa.Menu;
import main.entities.string_property_entities.Mensa.StringPropertyIngredient;
import main.client.controllers.AbstractController;
import main.client.controllers.PopOverController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerShowConflictingDishesPopOver extends AbstractController implements Initializable {

    /*
        Static
    */

        private static Person p;
        private static Menu.MenuTypeFlag menu;

        public static void setVariables(Person p, Menu.MenuTypeFlag menu) {
            main.client.controllers.Mensa.ControllerShowConflictingDishesPopOver.p = p;
            main.client.controllers.Mensa.ControllerShowConflictingDishesPopOver.menu = menu;
        }


    /*
        Initialize
    */

        @FXML
        private TableView<StringPropertyIngredient> conflictingDishesTable;
        @FXML private TableColumn<StringPropertyIngredient,String> dishesColumn;
        private ObservableList<StringPropertyIngredient> dishesObservableList = FXCollections.observableArrayList();

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            setControllerType();
            setColumnAssociations();
            showIngredients();

        }


        protected void setColumnAssociations() {
            dishesColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        }

        protected void setControllerType() {
            controllerType = new PopOverController();
        }

        private void showIngredients() {
            List<String> ingredients = CLIENT.clientExtractConflictingDishesFromMenu(p, menu);
            if (ingredients != null) for (String s : ingredients) {
                dishesObservableList.add(new StringPropertyIngredient(s));
            }
            conflictingDishesTable.setItems(dishesObservableList);
        }

        @Override
        public void refresh() {

        }


}
