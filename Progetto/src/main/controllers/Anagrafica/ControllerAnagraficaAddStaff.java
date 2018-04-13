package main.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import main.NormalClasses.Anagrafica.Person;
import main.NormalClasses.Anagrafica.Staff;
import main.User;
import main.controllers.AbstractController;
import main.qrReader.QRGenerator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerAnagraficaAddStaff extends AbstractController implements Initializable {

    private final String pattern = "dd/MM/yyyy";
    private final int CODFISLENGTH = 16;

    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private DatePicker birthdayDatePicker;
    @FXML private ChoiceBox userTypeDropList;

    @FXML private ImageView goHomeImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        codFisTextField.textProperty().addListener((ov, oldValue, newValue) -> {
            codFisTextField.setText(newValue.toUpperCase());
        });

        AbstractController.setCurrentController(this);
        ArrayList elements = new ArrayList<>(Arrays.asList(User.UserTypeFlag.values()));
        userTypeDropList.setItems(FXCollections.observableArrayList(elements));

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
    }

    @FXML protected void handleNextButtonAction(ActionEvent event) throws IOException{

        if(textConstraintsRespected()) {
            String nome = nameTextField.getText();
            String cognome = surnameTextField.getText();
            String codFis = codFisTextField.getText();
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String dataN = birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            User.UserTypeFlag typeFlag =
                    User.UserTypeFlag.valueOf(userTypeDropList.getSelectionModel().getSelectedItem().toString());


            Person staff = new Staff(codFis, nome, cognome, username, password, dataN, typeFlag);

            boolean success = CLIENT.clientInsertIntoDb(staff);
            try {
                if (!success) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Verifica i dati inseriti ");
                    alert.setContentText("Codice FIscale già esistente o username già preso");
                    alert.showAndWait();
                } else {
                    //Genera QR
                    QRGenerator.GenerateQR(staff);

                    Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
                    alert2.setGraphic(new ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
                    alert2.setTitle("Successo");
                    alert2.setHeaderText("Successo! ");
                    alert2.setContentText("Dati inseriti correttamente nel database.\n" + "Sarai ridiretto al menu");
                    alert2.showAndWait();
                }
            } catch (Exception e){
                //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
            }

        }
        else{
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore");
            alert2.setHeaderText("Verifica i dati inseriti ");
            alert2.setContentText("Hai lasciato campi vuoti o con un formato sbagliato");
            alert2.showAndWait();
        }
    }

    private boolean textConstraintsRespected() {
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
            System.out.println(surnameTextField.getText().length());
            surnameTextField.setStyle(errorCss);
            errors++;
        }else {
            System.out.println(surnameTextField.getText().length());
            surnameTextField.setStyle(normalCss);
        }

        if(usernameTextField.getText().length() == 0){
            usernameTextField.setStyle(errorCss);
            errors++;
        }else {
            usernameTextField.setStyle(normalCss);
        }

        if(passwordTextField.getText().length() == 0){
            passwordTextField.setStyle(errorCss);
            errors++;
        }else {
            passwordTextField.setStyle(normalCss);
        }

        if(birthdayDatePicker.getValue() == null){
            birthdayDatePicker.setStyle("-fx-border-color: red ; -fx-focus-color: #81cee9 ;");
            errors++;
        }else {
            birthdayDatePicker.setStyle("-fx-border-color: transparent ; -fx-focus-color: transparent ;");
        }

        if(userTypeDropList.getSelectionModel().isEmpty()){
            userTypeDropList.setStyle("-fx-border-color: red ; -fx-focus-color: #81cee9 ;");
        }else {
            userTypeDropList.setStyle("-fx-border-color: transparent ; -fx-focus-color: transparent ;");
        }

        return errors == 0;

    }

    public void handleGoHomebutton() {
        if(createConfirmationDialog("Sei sicuro di voler chiudere?",
                "I dati inseriti non verranno salvati."))
            closePopup(goHomeImageView);
    }
}
