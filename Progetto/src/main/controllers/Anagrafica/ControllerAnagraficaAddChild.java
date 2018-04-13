package main.controllers.Anagrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import main.*;
import main.NormalClasses.Anagrafica.Child;
import main.NormalClasses.Anagrafica.Contact;
import main.NormalClasses.Anagrafica.Person;
import main.controllers.AbstractController;
import main.qrReader.QRGenerator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ControllerAnagraficaAddChild extends AbstractController implements Initializable {
    private final String pattern = "dd/MM/yyyy";
    private final int CODFISLENGTH = 16;
    private int parent2errors = 0;

    //Bambino
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;
    @FXML private DatePicker birthdayDatePicker;

    //Genitore1
    @FXML private TextField nameParent1TextField;
    @FXML private TextField surnameParent1TextField;
    @FXML private TextField codFisParent1TextField;
    @FXML private TextField cellphoneParent1TextField;

    //Genitore2
    @FXML private TextField nameParent2TextField;
    @FXML private TextField surnameParent2TextField;
    @FXML private TextField codFisParent2TextField;
    @FXML private TextField cellphoneParent2TextField;

    //Pediatra
    @FXML private TextField namePedTextField;
    @FXML private TextField surnamePedTextField;
    @FXML private TextField codFisPedTextField;
    @FXML private TextField cellphonePedTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        codFisTextField.textProperty().addListener((ov, oldValue, newValue) -> {
            codFisTextField.setText(newValue.toUpperCase());
        });

        codFisParent1TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            codFisParent1TextField.setText(newValue.toUpperCase());
        });

        codFisParent2TextField.textProperty().addListener((ov, oldValue, newValue) -> {
            codFisParent2TextField.setText(newValue.toUpperCase());
        });

        codFisPedTextField.textProperty().addListener((ov, oldValue, newValue) -> {
            codFisPedTextField.setText(newValue.toUpperCase());
        });


        /*codFisTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.) { // we only care about loosing focus
                // check condition and apply necessay style
                codFisTextField.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
        });*/

        AbstractController.setCurrentController(this);
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

            //Creazione Genitore 1
            String nomePar1 = nameParent1TextField.getText();
            String cognomePar1 = surnameParent1TextField.getText();
            String codFisPar1 = codFisParent1TextField.getText();
            String cellPar1 = cellphoneParent1TextField.getText();

            //Gen 2
            String codFisPar2 = codFisParent2TextField.getText();

            Person gen1 = new Contact(nomePar1,cognomePar1,codFisPar1,cellPar1, Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum()));
            CLIENT.clientInsertIntoDb(gen1);


            //Creazione Genitore 2 (facoltativo)
            if(codFisParent2TextField.getText().length()!=0 || nameParent2TextField.getText().length()!=0 ||
            surnameParent2TextField.getText().length()!=0 || cellphoneParent2TextField.getText().length()!=0){
                if(parent2CheckErrors()==0) {
                String nomePar2 = nameParent2TextField.getText();
                String cognomePar2 = surnameParent2TextField.getText();
                String cellPar2 = cellphoneParent2TextField.getText();
                Person gen2 = new Contact(nomePar2, cognomePar2, codFisPar2, cellPar2,
                        Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum()));
                CLIENT.clientInsertIntoDb(gen2);
                }
            }
            //Creazione Pediatra
            String nomePed = namePedTextField.getText();
            String cognomePed = surnamePedTextField.getText();
            String codFisPed = codFisPedTextField.getText();
            String cellPed = cellphonePedTextField.getText();

            Person ped = new Contact(nomePed,cognomePed,codFisPed,cellPed, Integer.toString(Contact.ContactTypeFlag.PEDIATRA.getOrdernum()));
            CLIENT.clientInsertIntoDb(ped);

            //Creazione Bambino
            String nome = nameTextField.getText();
            String cognome = surnameTextField.getText();
            String codFis = codFisTextField.getText();
            String dataN = birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Person child = new Child(nome, cognome, codFis, dataN, codFisPed,
                    codFisPar1, codFisPar2);

            boolean success = CLIENT.clientInsertIntoDb(child);
            try {
                if (!success) {
                    createErrorPopup("Errore", "Verifica i dati inseriti");
                } else {
                    //Genera QR
                    QRGenerator.GenerateQR(child);
                    createSuccessPopup();
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

        // Genitore 1

        if(codFisParent1TextField.getText().length()!= CODFISLENGTH){
            codFisParent1TextField.setStyle(errorCss);
            errors++;
        }else {
            codFisParent1TextField.setStyle(normalCss);
        }

        if(nameParent1TextField.getText().length() == 0){
            nameParent1TextField.setStyle(errorCss);
            errors++;
        }else {
            nameParent1TextField.setStyle(normalCss);
        }

        if(surnameParent1TextField.getText().length() == 0){
            surnameParent1TextField.setStyle(errorCss);
            errors++;
        }else {
            surnameParent1TextField.setStyle(normalCss);
        }

        if(cellphoneParent1TextField.getText().length() == 0){
            cellphoneParent1TextField.setStyle(errorCss);
            errors++;
        }else {
            cellphoneParent1TextField.setStyle(normalCss);
        }
        //Genitore2
        //parent2CheckErrors();
        //errors+=parent2errors;
        //Pediatra
        if(codFisPedTextField.getText().length()!= CODFISLENGTH){
            codFisPedTextField.setStyle(errorCss);
            errors++;
        }else {
            codFisPedTextField.setStyle(normalCss);
        }

        if(namePedTextField.getText().length() == 0){
            namePedTextField.setStyle(errorCss);
            errors++;
        }else {
            namePedTextField.setStyle(normalCss);
        }

        if(surnamePedTextField.getText().length() == 0){
            surnamePedTextField.setStyle(errorCss);
            errors++;
        }else {
            surnamePedTextField.setStyle(normalCss);
        }

        if(cellphonePedTextField.getText().length() == 0){
            cellphonePedTextField.setStyle(errorCss);
            errors++;
        }else {
            cellphonePedTextField.setStyle(normalCss);
        }

        return errors == 0;

    }

    private int parent2CheckErrors(){
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        parent2errors = 0;

        if(codFisParent2TextField.getText().length() != CODFISLENGTH) {
            codFisParent2TextField.setStyle(errorCss);
            parent2errors++;
        }else{
            codFisParent2TextField.setStyle(normalCss);
        }

        if(nameParent2TextField.getText().length() == 0){
            nameParent2TextField.setStyle(errorCss);
            parent2errors++;
        }else {
            nameParent2TextField.setStyle(normalCss);
        }

        if(surnameParent2TextField.getText().length() == 0){
            surnameParent2TextField.setStyle(errorCss);
            parent2errors++;
        }
        else {
            surnameParent2TextField.setStyle(normalCss);
        }

        if(cellphoneParent2TextField.getText().length() == 0){
            cellphoneParent2TextField.setStyle(errorCss);
            parent2errors++;
        } else{
            cellphoneParent2TextField.setStyle(normalCss);

        }

        return parent2errors;
    }

}
