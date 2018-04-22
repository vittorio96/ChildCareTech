package main.controllers.Mensa;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.NormalClasses.Mensa.Dish;
import main.NormalClasses.Mensa.Menu;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.StringPropertyClasses.Mensa.StringPropertyDish;
import main.StringPropertyClasses.Mensa.StringPropertyIngredient;
import main.controllers.AbstractController;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaAddDish extends AbstractController implements Initializable {

    /*
         Stuff
    */

    private static Dish.DishTypeFlag dishType;

    public static void setDishType(Dish.DishTypeFlag dishType) {
        ControllerMensaAddDish.dishType = dishType;
    }

    @FXML private ImageView goHomeIV;
    @FXML private Button refreshButton;
    @FXML private ImageView saveNewDishIV;
    @FXML private ImageView addNewIngredient;
    @FXML private TextField searchFieldAvailable;
    @FXML private TextField dishNameTextField;
    @FXML private TableView<StringPropertyIngredient> availableIngredientsTable;
    @FXML private TableView<StringPropertyIngredient> ingredientsOnDishTable;
    @FXML private TableColumn <StringPropertyIngredient,String> availableIngredients;
    @FXML private TableColumn <StringPropertyIngredient,String> ingredientsOnDish;
    private List<StringPropertyIngredient> onDishNormalList = new ArrayList<StringPropertyIngredient>();
    private List<StringPropertyIngredient> availableNormalList = new ArrayList<StringPropertyIngredient>();
    private List<String> tempList = new ArrayList<String>();
    private ObservableList<StringPropertyIngredient> availableIngredientsObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyIngredient> ingredientsOnDishObservableList = FXCollections.observableArrayList();
    private SortedList<StringPropertyIngredient> sortedData;
    private FilteredList<StringPropertyIngredient> filteredData;
    private static boolean newIng;
    private static String lastIng;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        newIng = false;//from caps to normal
    }

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

    private void setItems() {
        ingredientsOnDishObservableList.clear();
        ingredientsOnDishObservableList.addAll(onDishNormalList);
        ingredientsOnDishTable.setItems(ingredientsOnDishObservableList);
        availableIngredientsObservableList.clear();
        availableIngredientsObservableList.addAll(availableNormalList);
        availableIngredientsTable.setItems(availableIngredientsObservableList);
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
            handleGoHomebutton();
        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }

    }

    public static void setNewIng(boolean newIng) {
        ControllerMensaAddDish.newIng = newIng;
    }

    public static void setLastIng(String lastIng) {
        ControllerMensaAddDish.lastIng = lastIng;
    }

    public void addNewIngredient(MouseEvent mouseEvent) throws IOException {
        newIng = false;
        openPopOver("../../resources/fxml/mensa_addIngredient.fxml", PopOver.ArrowLocation.BOTTOM_CENTER, addNewIngredient);
        if(newIng){
            getNewIngredient();
        }
    }

    @FXML private void handleGoHomebutton(){
        closePopup(goHomeIV);
    }

    private boolean textConstraintsRespected() {
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(dishNameTextField.getText().length() == 0){
            dishNameTextField.setStyle(errorCss);
            errors++;
        }else {
            dishNameTextField.setStyle(normalCss);
        }

        if(onDishNormalList.size()<1){
            errors++;
        }

        if(dishType == null){
            errors++;
        }

        return errors == 0;

    }

    public void getNewIngredient() {
        searchFieldAvailable.setText("");
        availableNormalList.add(new StringPropertyIngredient(lastIng));
        availableIngredientsObservableList.clear();
        availableIngredientsObservableList.addAll(availableNormalList);
        availableIngredientsTable.setItems(availableIngredientsObservableList);
    }
}
