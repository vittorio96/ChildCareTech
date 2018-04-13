package main.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.NormalClasses.Anagrafica.Staff;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.controllers.AbstractController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaManageStaff extends AbstractController implements Initializable {

    //main list
    private ObservableList<StringPropertyStaff> staffObservableList = FXCollections.observableArrayList();

    //Utilities
    private final String pattern = "dd/MM/yyyy";
    private final String inputPattern = "yyyy-MM-dd";

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
    @FXML private Button gohomeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        birthdayDatePicker.setShowWeekNumbers(false);
        birthdayDatePicker.setPromptText("gg/mm/aaaa");
        birthdayDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

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
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(inputPattern);
            birthdayDatePicker.setValue(LocalDate.parse(dateValue, DATE_FORMAT));

        } else {
            // Person is null, remove all the text.
            usernameTextField.setText("");
            nameTextField.setText("");
            surnameTextField.setText("");
            codFisTextField.setText("");
        }
    }

    @FXML private void handleGoHomebutton(){
        Stage stage = (Stage) saveChangesButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void handleDeleteButton(){

        int selectedIndex = staffTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType buttonTypeOne = new ButtonType("No");
            ButtonType buttonTypeTwo = new ButtonType("Si");
            alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            alert2.setHeaderText("Sei sicuro di voler eliminare?");
            alert2.setContentText("Una volta fatta la cancellazione è impossibile annullarla ");
            alert2.showAndWait();

            if (alert2.getResult() == buttonTypeTwo) {

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
            StringPropertyStaff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
            selectedStaff.setNome(nameTextField.getText());
            selectedStaff.setCognome(surnameTextField.getText());
            selectedStaff.setDataNascita(birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            //db update
            boolean success = CLIENT.clientUpdatePersonIntoDb(new Staff(selectedStaff));
            if (!success) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                alert.setTitle("Errore");
                alert.setHeaderText("Verifica i dati inseriti ");
                alert.setContentText("Username già preso");
                alert.showAndWait();
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
                alert2.setGraphic(new ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
                alert2.setTitle("Successo");
                alert2.setHeaderText("Successo! ");
                alert2.setContentText("Dati aggiornati correttamente nel database.\n");
                alert2.showAndWait();
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore");
            alert2.setHeaderText("Verifica i dati inseriti ");
            alert2.setContentText("Hai lasciato campi vuoti o con un formato sbagliato");
            alert2.showAndWait();
        }
    }

    private boolean textConstraintsRespectedForUpdate() {
        final int CODFISLENGTH = 16;
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(nameTextField.getText().length() == 0){
            nameTextField.setStyle(errorCss);
            errors++;
        }else {
            nameTextField.setStyle(normalCss);
        }

        if(surnameTextField.getText().length() == 0){
            surnameTextField.setStyle(errorCss);
            errors++;
        }else {
            surnameTextField.setStyle(normalCss);
        }

        if(birthdayDatePicker.getValue() == null){
            birthdayDatePicker.setStyle("-fx-border-color: red ; -fx-focus-color: #81cee9 ;");
            errors++;
        }else {
            birthdayDatePicker.setStyle("-fx-border-color: transparent ; -fx-focus-color: transparent ;");
        }
        return errors == 0;
    }
}
