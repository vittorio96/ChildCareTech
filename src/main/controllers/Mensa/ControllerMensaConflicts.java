package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import main.NormalClasses.Anagrafica.Child;
import main.NormalClasses.Anagrafica.Person;
import main.NormalClasses.Anagrafica.Staff;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Anagrafica.StringPropertyPerson;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.StringPropertyClasses.Mensa.StringPropertyIngredient;
import main.controllers.AbstractController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaConflicts extends AbstractController implements Initializable {

    @FXML private TableView<StringPropertyIngredient> ingredientsTable;
    @FXML private TableColumn<StringPropertyIngredient,String> ingredientsColumn;

    @FXML private TableView<StringPropertyStaff> staffTable;
    @FXML private TableColumn<StringPropertyStaff, String> staffNameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> staffSurnameColumn;

    @FXML private TableView<StringPropertyChild> childTable;
    @FXML private TableColumn<StringPropertyChild, String> nameColumn;
    @FXML private TableColumn<StringPropertyChild, String> surnameColumn;

    @FXML private ImageView goHomeIV;
    private ObservableList<StringPropertyChild> childObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyStaff> staffObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyIngredient> ingredientsObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableAssociations();
        refreshIngredientsTable();
        refreshTables();
    }

    private void refreshTables() {
        refreshChildTable(getSelectedIngredient());
        refreshStaffTable(getSelectedIngredient());
    }

    private void setTableAssociations() {
        setChildTableAssociations();
        setStaffTableAssociations();
        setIngredientsTableAssociations();
    }

    private void setIngredientsTableAssociations() {
        ingredientsColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        ingredientsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showAllergicPeople(newValue));
    }

    private void showAllergicPeople(StringPropertyIngredient selectedIngredient) {
        refreshChildTable(selectedIngredient.getNome());
        refreshStaffTable(selectedIngredient.getNome());
    }

    private void setChildTableAssociations() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }



    private void setStaffTableAssociations() {
        staffNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        staffSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    private void refreshIngredientsTable() {
        ingredientsObservableList.clear();
        List<String> ingredientsArrayList = CLIENT.clientExtractIngredientsFromDb();
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

    public String getSelectedIngredient() {
        if(ingredientsTable.getSelectionModel().selectedItemProperty().get()!=null)
            return ingredientsTable.getSelectionModel().selectedItemProperty().get().getNome();
        return null;
    }

    public void goHome() {
        closePopup(goHomeIV);
    }
}
