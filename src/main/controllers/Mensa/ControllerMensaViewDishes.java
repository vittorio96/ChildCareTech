package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.NormalClasses.Mensa.Menu;
import main.Classes.StringPropertyClasses.Mensa.StringPropertyDish;
import main.controllers.AbstractController;
import main.controllers.PopupController;
import org.controlsfx.control.PopOver;

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

    public static void setDishType(Dish.DishTypeFlag dishType) {
        ControllerMensaViewDishes.dishType = dishType;
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
    private final String viewIngredientsFxmlPath = "../../resources/fxml/mensa_viewIngredientsPopOver.fxml";

    /*
        Initialization
    */

    private ObservableList<StringPropertyDish> dishObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        refreshDishes();
        dishesTable.setRowFactory( tv -> {
            TableRow<StringPropertyDish> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    StringPropertyDish selected = row.getItem();
                    ControllerShowIngredientsPopOver.setDish(selected);
                    try {
                        openPopOver(viewIngredientsFxmlPath, PopOver.ArrowLocation.LEFT_CENTER, row);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
    }

    private void setControllerType() {
        controllerType = new PopupController();
    }


    private void refreshDishes() {
        dishObservableList.clear();
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
        controllerType.close(goHomeIV);
    }

    public void addNewDish() throws IOException {
        ControllerMensaAddDish.setDishType(dishType);
        openPopup(addNewDishIV,addNewDishFXMLPath,addDishW,addDishH);
        refreshDishes();
    }

    public StringPropertyDish getSelectedDish() {
        StringPropertyDish selectedDish = null;
        if(dishesTable.getSelectionModel().selectedItemProperty().get() != null)
            selectedDish = dishesTable.getSelectionModel().selectedItemProperty().get();
        return selectedDish;

    }

    public void saveDishIntoMenu() {
        if(getSelectedDish()!=null){
            boolean success= CLIENT.clientInsertDishIntoMenuIntoDb(menuType,getSelectedDish().getNomeP());
            if (success) {
                createSuccessPopup();
                goHome();
            }
            else createGenericErrorPopup();
        } else createErrorPopup("Errore", "Seleziona un piatto dalla tabella!");
    }

    public void deleteDish() {
        if(getSelectedDish()!=null){
            boolean success= CLIENT.clientDeleteDishFromDb(new Dish(getSelectedDish()));
            if (success) {
                createSuccessPopup();
                refreshDishes();
            }
            else createGenericErrorPopup();
        } else createErrorPopup("Errore", "Seleziona un piatto dalla tabella!");
    }
}
