package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.NormalClasses.Anagrafica.Child;
import main.NormalClasses.Anagrafica.Person;
import main.NormalClasses.Anagrafica.Staff;
import main.NormalClasses.Mensa.ChildIntolerance;
import main.NormalClasses.Mensa.Intolerance;
import main.NormalClasses.Mensa.PersonIntolerance;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Anagrafica.StringPropertyPerson;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.StringPropertyClasses.Mensa.StringPropertyDish;
import main.StringPropertyClasses.Mensa.StringPropertyIngredient;
import main.controllers.AbstractController;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaAddAllergen extends AbstractController implements Initializable {

    /*
        Tables
    */

    @FXML private TableView<StringPropertyStaff> staffTable;
    @FXML private TableColumn<StringPropertyStaff, String> staffNameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> staffSurnameColumn;

    @FXML private TableView<StringPropertyChild> childTable;
    @FXML private TableColumn<StringPropertyChild, String> nameColumn;
    @FXML private TableColumn<StringPropertyChild, String> surnameColumn;

    @FXML private TableView<StringPropertyIngredient>  notAllergicToTable;
    @FXML private TableColumn<StringPropertyIngredient,String>  harmlessIngredients;

    @FXML private TableView<StringPropertyIngredient>  allergicToTable;
    @FXML private TableColumn<StringPropertyIngredient,String>  harmfulIngredients;

    /*
        Utilities
    */

    @FXML private ImageView closeIV;
    @FXML private Tab staffTab;
    @FXML private Tab childTab;

    private ObservableList<StringPropertyPerson> childObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyPerson> staffObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyIngredient> notAllergicToObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyIngredient> allergicToObservableList = FXCollections.observableArrayList();

    /*
        Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableAssociations();
        refreshTables();
    }

    private void refreshTables() {
        refreshChildTable();
        refreshStaffTable();
    }

    private void setTableAssociations() {
        setChildTableAssociations();
        setStaffTableAssociations();
        setAllergicToTableAssociations();
        setNotAllergicToTableAssociations();
    }

    private void setChildTableAssociations() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        childTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showChildAllergies(newValue));
    }


    private void setStaffTableAssociations() {
        staffNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        staffSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        staffTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStaffAllergies(newValue));
    }

    private void setAllergicToTableAssociations() {
        harmfulIngredients.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        setAllergicToDoubleClickMethod();
    }


    private void setNotAllergicToTableAssociations() {
        harmlessIngredients.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        setNotAllergicToDoubleClickMethod();
    }

    /*
        Methods
    */

    private void showChildAllergies(StringPropertyChild selectedChild) {
        refreshAllergies(new Child(selectedChild));
    }

    private void showStaffAllergies(StringPropertyStaff selectedStaff) {
        refreshAllergies(new Staff(selectedStaff));
    }

    private void developedNewAllergy(StringPropertyIngredient selectedIngredient){
        Person selectedPerson = getSelectedPerson();
        if(selectedPerson!= null) {
            boolean success = CLIENT.clientInsertIntoleranceIntoDb(createAllergy(selectedIngredient, selectedPerson));
            if(!success) createGenericErrorPopup();
        }
        refreshAllergies(selectedPerson);
    }

    private Intolerance createAllergy(StringPropertyIngredient selectedIngredient, Person selectedPerson) {
        return selectedPerson.createIntolerance(selectedIngredient.getNome());
    }

    private void curedFromAllergy(StringPropertyIngredient selectedAllergy) {
        Person selectedPerson = getSelectedPerson();
        if(selectedPerson!= null) {
            boolean success = CLIENT.clientDeleteIntoleranceFromDb(createAllergy(selectedAllergy, selectedPerson));
            if(!success) createGenericErrorPopup();
        }
        refreshAllergies(selectedPerson);
    }

    /*
        Refresh methods
    */

    private void refreshAllergies(Person person) {
        refreshAllergicToTable(person);
        refreshNotAllergicToTable(person);
    }

    private void refreshAllergicToTable(Person person) {
        allergicToObservableList.clear();
        List<String> allergicToArrayList = CLIENT.extractAllergiesForPerson(person);
        if(allergicToArrayList!=null){
            for(String s : allergicToArrayList){
                allergicToObservableList.add(new StringPropertyIngredient(s));
            }
        }
        allergicToTable.setItems(allergicToObservableList);
    }

    private void refreshNotAllergicToTable(Person person) {
        notAllergicToObservableList.clear();
        List<String> notAllergicToArrayList = CLIENT.extractHarmlessIngredientsForPerson(person);
        if(notAllergicToArrayList!=null){
            for(String s : notAllergicToArrayList){
                notAllergicToObservableList.add(new StringPropertyIngredient(s));
            }
        }
        notAllergicToTable.setItems(notAllergicToObservableList);
    }

    private void refreshStaffTable() {
        personTableRefresh(staffTable,staffObservableList, Staff.class.getSimpleName());
    }

    private void refreshChildTable(){
        personTableRefresh(childTable,childObservableList, Child.class.getSimpleName());
    }

    private void personTableRefresh(TableView table, ObservableList<StringPropertyPerson> observableList, String classN){
        observableList.clear();
        List<? extends Person> personArrayList = CLIENT.clientExtractPersonFromDb(classN);
        if(personArrayList!=null){
            for(Person p : personArrayList){
                observableList.add(p.toStringProperty());
            }
        }
        table.setItems(observableList);
    }

    /*
        Button actions
    */

    @FXML private void handleClosePopup() {
        closePopup(closeIV);
    }

    /*
        Gui getters
    */

    private Person getSelectedPerson() {
        Person selectedPerson = null;
        StringPropertyPerson selectedSPPerson = null;
        if(childTab.isSelected()){
            if(childTable.getSelectionModel().selectedItemProperty().get() != null)
                selectedSPPerson = childTable.getSelectionModel().selectedItemProperty().get();
        }
        else if(staffTab.isSelected()){
            if(staffTable.getSelectionModel().selectedItemProperty().get() != null)
                selectedSPPerson = staffTable.getSelectionModel().selectedItemProperty().get();
        }
        if(selectedSPPerson!= null) selectedPerson = selectedSPPerson.toPerson();
        return selectedPerson;
    }

    private void setAllergicToDoubleClickMethod() {
        allergicToTable.setRowFactory( tv -> {
            TableRow<StringPropertyIngredient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    StringPropertyIngredient selectedIngredient = row.getItem();
                    curedFromAllergy(selectedIngredient);
                }
            });
            return row ;
        });
    }

    private void setNotAllergicToDoubleClickMethod() {
        notAllergicToTable.setRowFactory( tv -> {
            TableRow<StringPropertyIngredient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    StringPropertyIngredient selectedIngredient = row.getItem();
                    developedNewAllergy(selectedIngredient);
                }
            });
            return row ;
        });
    }
}
