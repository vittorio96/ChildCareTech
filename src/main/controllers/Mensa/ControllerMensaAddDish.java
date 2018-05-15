package main.controllers.Mensa;

import javafx.application.Platform;
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
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.StringPropertyClasses.Mensa.StringPropertyIngredient;
import main.controllers.AbstractController;
import main.controllers.PopupController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaAddDish extends AbstractController implements Initializable {

    /*
        Tables
    */
    @FXML private TableView<StringPropertyIngredient> availableIngredientsTable;
    @FXML private TableColumn <StringPropertyIngredient,String> availableIngredients;

    @FXML private TableView<StringPropertyIngredient> ingredientsOnDishTable;
    @FXML private TableColumn <StringPropertyIngredient,String> ingredientsOnDish;


    /*
         GUI objects
    */

    @FXML private ImageView goHomeIV;
    @FXML private TextField searchFieldAvailable;
    @FXML private TextField dishNameTextField;


    /*
         Static
    */

    private static Dish.DishTypeFlag dishType;

    public static void setDishType(Dish.DishTypeFlag dishType) {
        ControllerMensaAddDish.dishType = dishType;
    }

    /*
         Lists
    */

    private List<StringPropertyIngredient> onDishNormalList = new ArrayList<StringPropertyIngredient>();
    private List<StringPropertyIngredient> availableNormalList = new ArrayList<StringPropertyIngredient>();
    private List<String> tempList = new ArrayList<String>();
    private ObservableList<StringPropertyIngredient> availableIngredientsObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyIngredient> ingredientsOnDishObservableList = FXCollections.observableArrayList();
    private SortedList<StringPropertyIngredient> sortedData;
    private FilteredList<StringPropertyIngredient> filteredData;

    /*
         Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerType = new PopupController();
        setColumnassociations();
        refreshIngredientsList();
        setFilter();

    }


    private void setFilter() {
        filteredData = new FilteredList<>(availableIngredientsObservableList, p -> true);
        //Set the filter
        searchFieldAvailable.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ingredient -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (ingredient.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (ingredient.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(availableIngredientsTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        availableIngredientsTable.setItems(sortedData);
    }

    private void setColumnassociations() {
        ingredientsOnDish.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        availableIngredientsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toDish(newValue));
        ingredientsOnDishTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toAvailableIngredients(newValue));
        availableIngredients.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
    }

    private void setItems() {
        ingredientsOnDishObservableList.clear();
        ingredientsOnDishObservableList.addAll(onDishNormalList);
        ingredientsOnDishTable.setItems(ingredientsOnDishObservableList);
        availableIngredientsObservableList.clear();
        availableIngredientsObservableList.addAll(availableNormalList);
        availableIngredientsTable.setItems(availableIngredientsObservableList);
    }

    private void refreshIngredientsList() {
        ingredientsOnDishObservableList.clear();
        availableIngredientsObservableList.clear();
        onDishNormalList.clear();
        availableNormalList.clear();
        tempList= CLIENT.clientExtractIngredientsFromDb();
        if(tempList!=null)
            for(String s: tempList)
                availableNormalList.add(new StringPropertyIngredient(s)  );
        availableIngredientsObservableList.addAll(availableNormalList);
    }

    /*
         Methods
    */

    private void toAvailableIngredients(StringPropertyIngredient selected) {

        Platform.runLater(new Runnable() {
            @Override public void run() {
                if(selected!=null){
                availableNormalList.add(selected);
                int initialsize = onDishNormalList.size();
                boolean done = false;
                for(int i=0; i < initialsize ;i++){
                    if(onDishNormalList.get(i).getNome().equals(selected.getNome())) {
                        onDishNormalList.remove(i);
                        done = true;
                    }
                    if (done==true) break;
                }
                setItems();
                setFilter();
                sort();
            }}});

    }

    private void toDish(StringPropertyIngredient selected) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                if(selected!=null){
                onDishNormalList.add(selected);
                int initialsize = availableNormalList.size();
                boolean done = false;
                for(int i=0; i < initialsize ;i++){
                    if(availableNormalList.get(i).getNome().equals(selected.getNome())) {
                        availableNormalList.remove(i);
                        done = true;
                    }
                    if (done==true) break;
                 }
                    setItems();
                    setFilter();
                }
            }});

    }

    private void sort() {
        availableIngredientsTable.sort();
    }

    public void saveNewDish() {
        if(textConstraintsRespected()){
            int errors = 0;
            String nomeP = dishNameTextField.getText();
            errors += CLIENT.clientInsertDishIntoDb(new Dish(nomeP,dishType)) ? 0:1000;
            for(StringPropertyIngredient ingredient: onDishNormalList){
                errors += CLIENT.clientInsertIngredientIntoDishIntoDb(nomeP,ingredient.getNome())? 0:1;
            }
            if(errors==0) createSuccessPopup();
            else createGenericErrorPopup();
            handleGoHomeButton();
        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }

    }

    private boolean textConstraintsRespected() {
        int errors = 0;
        errors+= textFieldConstraintsRespected(dishNameTextField) ? 0:1;
        errors+= listSizeConstraintsRespected(onDishNormalList) ? 0:1;
        errors+= (dishType != null) ? 0:1;

        return errors == 0;
    }

    /*
         Gui Methods
    */

    @FXML private void handleGoHomeButton(){
        controllerType.close(goHomeIV);
    }


}
