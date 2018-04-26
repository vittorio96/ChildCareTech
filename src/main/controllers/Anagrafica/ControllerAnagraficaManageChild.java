package main.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyPerson;
import main.controllers.AbstractPopupController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaManageChild extends AbstractPopupController implements Initializable {

    //main list
    private ObservableList<StringPropertyChild> childObservableList = FXCollections.observableArrayList();

    //Tabella
    @FXML private TableView<StringPropertyChild> childTable;
    @FXML private TableColumn<StringPropertyChild, String> nameColumn;
    @FXML private TableColumn<StringPropertyChild, String> codeColumn;
    @FXML private TableColumn<StringPropertyChild, String> surnameColumn;

    //Bambino
    @FXML private TextField codeTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;
    @FXML private DatePicker birthdayDatePicker;

    //Buttons
    @FXML private Button saveChangesButton;
    @FXML private Button deleteButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumnAssociations();
        datePickerStandardInitialize(birthdayDatePicker);
        setEventListeners();
        populateTable();

    }

    protected void setColumnAssociations() {
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    protected void setEventListeners() {
        showChildDetails(null);
        childTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showChildDetails(newValue));
    }

    private void populateTable() {
        List<Child> childArrayList = CLIENT.clientExtractChildrenFromDb();
        for(Child c : childArrayList){
            childObservableList.add(new StringPropertyChild(c));
        }
        childTable.setItems(childObservableList);
    }

    @FXML
    private void showChildDetails(StringPropertyChild child) {
        if (child != null) {
            // Fill the textfields with info from the child object.
            codeTextField.setText(child.getCodR());
            nameTextField.setText(child.getNome());
            surnameTextField.setText(child.getCognome());
            codFisTextField.setText(child.getCodiceFiscale());
            String dateValue = child.getDataNascita();
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DBDATEPATTERN);
            birthdayDatePicker.setValue(LocalDate.parse(dateValue, DATE_FORMAT));

        } else {
            // Person is null, remove all the text.
            codeTextField.setText("");
            nameTextField.setText("");
            surnameTextField.setText("");
            codFisTextField.setText("");
        }
    }

    @FXML private void handleDeleteButton(){

        if (isAChildSelected()) {
            if (createDeleteConfirmationDialog()) {
                StringPropertyChild selectedChild = childTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb(Child.class.getSimpleName(), selectedChild.getCodiceFiscale());
                if (success) {
                    childObservableList.remove(selectedChild);
                    createSuccessPopup();
                } else { createUnableToDeletePopup("bambino"); }
            }
        }else { createPleaseSelectRowPopup("bambino"); }
    }

    @FXML private void handleSaveChangesButton(){

        if(textConstraintsRespectedForUpdate()){

            boolean success = CLIENT.clientUpdatePersonIntoDb(getSelectedChild().toPerson());
            codFisTextField.setStyle("-fx-background-color: #F4F4F4;");
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ", "Contatto già esistente o cellulare già in Db");
            } else { createSuccessPopup(); }

        }else{ createFieldErrorPopup(); }
    }

    @FXML private void handleGoHomebutton(){
        close(saveChangesButton);
    }

    private boolean textConstraintsRespectedForUpdate() {
        return childErrors() == 0;
    }

    private int childErrors() {
        int childErrors = 0;
        childErrors+= textFieldConstraintsRespected(nameTextField) ? 0:1;
        childErrors+= textFieldConstraintsRespected(surnameTextField) ? 0:1;
        childErrors+= datePickerIsDateSelected(birthdayDatePicker) ? 0:1;
        return childErrors;
    }


    public boolean isAChildSelected() {
        int selectedIndex = childTable.getSelectionModel().getSelectedIndex();
        return selectedIndex >=0 ;
    }

    public StringPropertyPerson getSelectedChild() {
        StringPropertyChild selectedChild = childTable.getSelectionModel().getSelectedItem();
        selectedChild.setNome(nameTextField.getText());
        selectedChild.setCognome(surnameTextField.getText());
        selectedChild.setDataNascita(birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN)));
        return selectedChild;
    }
}
