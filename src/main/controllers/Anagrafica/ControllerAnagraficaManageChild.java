package main.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.NormalClasses.Anagrafica.Child;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.controllers.AbstractController;
import main.controllers.AbstractPopupController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaManageChild extends AbstractPopupController implements Initializable {

    //main list
    private ObservableList<StringPropertyChild> childObservableList = FXCollections.observableArrayList();

    //Utilities
    private final String pattern = "dd/MM/yyyy";
    private final String inputPattern = "yyyy-MM-dd";

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
    @FXML private Button gohomeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

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

        showChildDetails(null);

        childTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showChildDetails(newValue));

        List<Child> childArrayList = null;
        childArrayList = CLIENT.clientExtractChildrenFromDb();
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
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(inputPattern);
            birthdayDatePicker.setValue(LocalDate.parse(dateValue, DATE_FORMAT));

        } else {
            // Person is null, remove all the text.
            codeTextField.setText("");
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

        int selectedIndex = childTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType buttonTypeOne = new ButtonType("No");
            ButtonType buttonTypeTwo = new ButtonType("Si");
            alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            alert2.setHeaderText("Sei sicuro di voler eliminare?");
            alert2.setContentText("Una volta fatta la cancellazione è impossibile annullarla ");
            alert2.showAndWait();

            if (alert2.getResult() == buttonTypeTwo) {

                StringPropertyChild selectedChild = childTable.getSelectionModel().getSelectedItem();
                System.out.println(this.getClass().getSimpleName());
                boolean success = CLIENT.clientDeleteFromDb("Child", selectedChild.getCodiceFiscale());
                if (success) {
                    childObservableList.remove(selectedChild);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Errore");
                    alert.setContentText("Non è stato possibile cancellare il bambino");
                    alert.showAndWait();
                }
            }
        }else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText("Non si è selezionato un bambino");
            alert.setContentText("Seleziona un bambino dalla tabella");
            alert.showAndWait();
        }
    }

    @FXML private void handleSaveChangesButton(){

        if(textConstraintsRespectedForUpdate()){
            StringPropertyChild selectedChild = childTable.getSelectionModel().getSelectedItem();
            selectedChild.setNome(nameTextField.getText());
            selectedChild.setCognome(surnameTextField.getText());
            selectedChild.setDataNascita(birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            //db update
            boolean success = CLIENT.clientUpdatePersonIntoDb(new Child(selectedChild));
            codFisTextField.setStyle("-fx-background-color: #F4F4F4;");
            if (!success) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                alert.setTitle("Errore");
                alert.setHeaderText("Verifica i dati inseriti ");
                alert.setContentText("Contatto già esistente o cellulare già in Db");
                alert.showAndWait();
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
                alert2.setGraphic(new ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
                alert2.setTitle("Successo");
                alert2.setHeaderText("Successo! ");
                alert2.setContentText("Dati aggiornati correttamente nel database.\n");
                alert2.showAndWait();
            }

        }else{
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

        if(codFisTextField.getText().length()!= CODFISLENGTH){
            codFisTextField.setStyle(errorCss);
            errors++;
        }else {
            codFisTextField.setStyle(normalCss);
        }

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
        return errors == 0;
    }
}
