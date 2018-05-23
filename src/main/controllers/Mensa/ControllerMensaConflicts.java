package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Staff;
import main.Classes.NormalClasses.Mensa.Dish;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.Classes.StringPropertyClasses.Mensa.StringPropertyDish;
import main.Classes.StringPropertyClasses.Mensa.StringPropertyIngredient;
import main.controllers.AbstractController;
import main.controllers.PopupController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaConflicts extends AbstractController implements Initializable {

    /*
        Tables
    */

    @FXML private TableView<StringPropertyIngredient> ingredientsTable;
    @FXML private TableColumn<StringPropertyIngredient,String> ingredientsColumn;

    @FXML private TableView<StringPropertyStaff> staffTable;
    @FXML private TableColumn<StringPropertyStaff, String> staffNameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> staffSurnameColumn;

    @FXML private TableView<StringPropertyChild> childTable;
    @FXML private TableColumn<StringPropertyChild, String> nameColumn;
    @FXML private TableColumn<StringPropertyChild, String> surnameColumn;

    @FXML private TableView<StringPropertyDish> dishesTable;
    @FXML private TableColumn<StringPropertyDish, String> dishesColumn;

    /*
        Lists
    */

    private ObservableList<StringPropertyChild> childObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyStaff> staffObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyDish> dishObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyIngredient> ingredientsObservableList = FXCollections.observableArrayList();

    @FXML private ImageView closeImageView;

    /*
        Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        setTableAssociations();
        refreshDishTable();
        refreshPersonTables();
    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    private void refreshPersonTables() {
        refreshChildTable(getSelectedIngredient());
        refreshStaffTable(getSelectedIngredient());
    }

    private void setTableAssociations() {
        setChildTableAssociations();
        setStaffTableAssociations();
        setIngredientsTableAssociations();
        setDishesTableAssociations();
    }

    /*
        Table associations
    */

    private void setDishesTableAssociations() {
        dishesColumn.setCellValueFactory(cellData -> cellData.getValue().nomePProperty());
        dishesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showIngredientsOnDish(newValue));
    }


    private void setIngredientsTableAssociations() {
        ingredientsColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        ingredientsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAllergicPeople(newValue));
    }

    private void setChildTableAssociations() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    private void setStaffTableAssociations() {
        staffNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        staffSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    /*
        Refresh
    */

    private void refreshIngredientsTable(String selectedDish) {
        ingredientsObservableList.clear();
        List<String> ingredientsArrayList = CLIENT.clientExtractIngredientsForDishFromDb(selectedDish);
        if(ingredientsArrayList!=null){
            for(String s : ingredientsArrayList){
                ingredientsObservableList.add(new StringPropertyIngredient(s));
            }
        }
        ingredientsTable.setItems(ingredientsObservableList);
    }

    private void refreshStaffTable(String selectedIngredient) {
        staffObservableList.clear();
        if(selectedIngredient!= null){
            List<Staff> personArrayList = CLIENT.clientExtractIntolerantWorkersForIngredientFromDb(selectedIngredient);
            if(personArrayList!=null){
                for(Staff p : personArrayList){
                    staffObservableList.add(new StringPropertyStaff(p));
                }
            }
            staffTable.setItems(staffObservableList);
        }
    }

    private void refreshChildTable(String selectedIngredient){
        childObservableList.clear();
        if(selectedIngredient!= null){
            List<Child> personArrayList = CLIENT.clientExtractIntolerantChildrenForIngredientFromDb(selectedIngredient);
            if(personArrayList!=null){
                for(Child p : personArrayList){
                    childObservableList.add(new StringPropertyChild(p));
                }
            }
            childTable.setItems(childObservableList);
        }
    }

    private void refreshDishTable() {
        dishObservableList.clear();
        List<Dish> dishArrayList = CLIENT.clientExtractDishesFromDb();
        if(dishArrayList!=null){
            for(Dish d : dishArrayList){
                dishObservableList.add(new StringPropertyDish(d));
            }
        }
        dishesTable.setItems(dishObservableList);
    }

    /*
        Methods
    */

    private void showIngredientsOnDish(StringPropertyDish selectedDish) {
        refreshIngredientsTable(selectedDish.getNomeP());
    }

    private void showAllergicPeople(StringPropertyIngredient selectedIngredient) {
        if(selectedIngredient!=null){
            refreshChildTable(selectedIngredient.getNome());
            refreshStaffTable(selectedIngredient.getNome());
        }
    }

    public String getSelectedIngredient() {
        if(ingredientsTable.getSelectionModel().selectedItemProperty().get()!=null)
            return ingredientsTable.getSelectionModel().selectedItemProperty().get().getNome();
        return null;
    }

    /*
        Close
    */

    public void closeCurrentPopup() {
        controllerType.close(closeImageView);
    }

    @Override
    public void refresh() {

    }
}
