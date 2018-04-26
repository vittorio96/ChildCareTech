package main.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Classes.NormalClasses.Anagrafica.Staff;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyPerson;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.controllers.AbstractPopupController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaManageStaff extends AbstractPopupController implements Initializable {

    //main list
    private ObservableList<StringPropertyStaff> staffObservableList = FXCollections.observableArrayList();

    //Tabella
    @FXML private TableView<StringPropertyStaff> staffTable;
    @FXML private TableColumn<StringPropertyStaff, String> usernameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> nameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> surnameColumn;

    //Staff
    @FXML private TextField usernameTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;
    @FXML private DatePicker birthdayDatePicker;

    //Buttons
    @FXML private Button saveChangesButton;
    @FXML private Button deleteButton;
    @FXML private ImageView goHomeImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        datePickerStandardInitialize(birthdayDatePicker);

        showStaffDetails(null);

        staffTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStaffDetails(newValue));

        List<Staff> staffArrayList = CLIENT.clientExtractStaffFromDb();
        for(Staff s : staffArrayList){
            staffObservableList.add(new StringPropertyStaff(s));
        }

        staffTable.setItems(staffObservableList);


    }

    @FXML private void showStaffDetails(StringPropertyStaff staff) {
        if (staff != null) {
            // Fill the textfields with info from the child object.
            usernameTextField.setText(staff.getUsername());
            nameTextField.setText(staff.getNome());
            surnameTextField.setText(staff.getCognome());
            codFisTextField.setText(staff.getCodiceFiscale());
            String dateValue = staff.getDataNascita();
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DBDATEPATTERN);
            birthdayDatePicker.setValue(LocalDate.parse(dateValue, DATE_FORMAT));

        } else {
            // Person is null, remove all the text.
            usernameTextField.setText("");
            nameTextField.setText("");
            surnameTextField.setText("");
            codFisTextField.setText("");
        }
    }

    @FXML private void handleGoHomeButton(){
        Stage stage = (Stage) saveChangesButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void handleDeleteButton(){

        int selectedIndex = staffTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            if (createDeleteConfirmationDialog()) {

                StringPropertyStaff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb("Staff", selectedStaff.getCodiceFiscale());
                if (success) {
                    staffObservableList.remove(selectedStaff);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Errore");
                    alert.setContentText("Non è stato possibile cancellare l'utente");
                    alert.showAndWait();
                }
            }
        }else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText("Non si è selezionato un utente");
            alert.setContentText("Seleziona un utente dalla tabella");
            alert.showAndWait();
        }
    }

    @FXML private void handleSaveChangesButton() {
        if (textConstraintsRespectedForUpdate()) {
            boolean success = CLIENT.clientUpdatePersonIntoDb(getSelectedStaff().toPerson());
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ", "Username già preso");
            } else { createSuccessPopup();}
        } else { createFieldErrorPopup();}
    }

    private boolean textConstraintsRespectedForUpdate() {
        final int CODFISLENGTH = 16;
        int errors = 0;
        errors += textFieldConstraintsRespected(nameTextField) ? 0 : 1;
        errors += textFieldLengthRespected(codFisTextField, CODFISLENGTH) ? 0 : 1;
        errors += textFieldConstraintsRespected(surnameTextField) ? 0 : 1;
        errors += datePickerIsDateSelected(birthdayDatePicker) ? 0 : 1;
        return errors == 0;
    }

    public void handleGoHomebutton() {
        close(goHomeImageView);
    }

    public StringPropertyPerson getSelectedStaff() {
        StringPropertyStaff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        selectedStaff.setNome(nameTextField.getText());
        selectedStaff.setCognome(surnameTextField.getText());
        selectedStaff.setDataNascita(birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN)));
        return selectedStaff;
    }
}
