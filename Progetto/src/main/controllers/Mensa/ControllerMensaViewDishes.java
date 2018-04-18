package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.NormalClasses.Mensa.Dish;
import main.NormalClasses.Mensa.Menu;
import main.StringPropertyClasses.Mensa.StringPropertyDish;
import main.controllers.AbstractController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaViewDishes extends AbstractController implements Initializable {

    /*
        Static
    */

    private static Dish.DishTypeFlag dishType;
    private static Menu.MenuTypeFlag menuType;

    public static Dish.DishTypeFlag getDishType() {
        return dishType;
    }

    public static void setDishType(Dish.DishTypeFlag dishType) {
        ControllerMensaViewDishes.dishType = dishType;
    }

    public static Menu.MenuTypeFlag getMenuType() {
        return menuType;
    }

    public static void setMenuType(Menu.MenuTypeFlag menuType) {
        ControllerMensaViewDishes.menuType = menuType;
    }

    /*
        Utilities
    */

    private final String addNewDishFXMLPath =  "../../resources/fxml/mensa_addDish.fxml";
    private final int addDishW = 800;
    private final int addDishH = 450;

    /*
        GUI items
    */
    @FXML private ImageView goHomeIV;
    @FXML private ImageView addNewDishIV;
    @FXML private ImageView saveDishIV;
    @FXML private TextField searchField;
    @FXML private TableView<StringPropertyDish> dishesTable;
    @FXML private TableColumn<StringPropertyDish,String> dishesColumn;

    /*
        Initialization
    */

    private ObservableList<StringPropertyDish> dishObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshDishes();
    }

    private void refreshDishes() {
        List<Dish> dishes = CLIENT.clientExtractDishesFromDb();
        if(dishes!=null) for(Dish dish: dishes){
            if(dish.getTipo()==dishType) dishObservableList.add(new StringPropertyDish(dish));
        }
        dishesColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<StringPropertyDish> filteredData = new FilteredList<>(dishObservableList, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getNomeP().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (person.getNomeP().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<StringPropertyDish> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(dishesTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        dishesTable.setItems(sortedData);
    }


    public void goHome() {
        closePopup(goHomeIV);
    }

    public void addNewDish() throws IOException {
        openPopup(addNewDishIV,addNewDishFXMLPath,addDishW,addDishH);

    }

    public void saveNewDish() {
        //TODO savedish to menu
    }
}
