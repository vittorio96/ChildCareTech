package main.controllers.Anagrafica;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.*;
import main.NormalClasses.Anagrafica.Contact;
import main.NormalClasses.Anagrafica.Person;
import main.NormalClasses.Anagrafica.Supplier;
import main.controllers.AbstractController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerAnagraficaAddContact extends AbstractController implements Initializable {


    //fornitore
    @FXML private TextField nomeFornitoreTextField;
    @FXML private TextField pivaTextField;
    @FXML private TextField cellphoneSupplierTextField;
    @FXML private TextField emailTextField;

    //Contatti
    @FXML private TextField nameTextField;
    @FXML private TextField cellphoneTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;

    @FXML private TextField nameTextField1;
    @FXML private TextField cellphoneTextField1;
    @FXML private TextField surnameTextField1;
    @FXML private TextField codFisTextField1;
    @FXML private Tab genericContact;

    @FXML private ImageView goHomeImageView;
    @FXML private TabPane tabPane;
    @FXML private String lastSelection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        codFisTextField.textProperty().addListener((ov, oldValue, newValue) -> {
            codFisTextField.setText(newValue.toUpperCase());
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
            if(nv!=null){
                lastSelection = nv.getText().toUpperCase();
            }
        });

        AbstractController.setCurrentController(this);

    }



    @FXML protected void handleFornitoreButtonAction(ActionEvent event) throws IOException{
        //Fornitore
        if(textConstraintsRespectedForSupplier()) {
            String nome = nomeFornitoreTextField.getText();
            String piva = pivaTextField.getText();
            String cell = cellphoneSupplierTextField.getText();
            String email = emailTextField.getText();

            Supplier supplier = new Supplier(piva, nome, cell, email);

            boolean success = false;
            success =CLIENT.clientInsertSupplierIntoDb(supplier);
            try {
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
                    alert2.setContentText("Dati inseriti correttamente nel database.\n" + "Sarai ridiretto al menu");
                    alert2.showAndWait();
                    closePopup(goHomeImageView);
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

    @FXML protected void handleDoctorNextButtonAction(ActionEvent event) throws IOException{
        //Genitore o Pediatra
        if(textConstraintsRespectedForDoctor()) {
            String nome = nameTextField.getText();
            String cognome = surnameTextField.getText();
            String cell = cellphoneTextField.getText();
            String codFis = codFisTextField.getText();
            Contact.ContactTypeFlag tipo = Contact.ContactTypeFlag.valueOf(lastSelection);

            Person contact = new Contact(nome,  cognome, codFis, cell, Integer.toString(tipo.getOrdernum()) );

            boolean success = CLIENT.clientInsertIntoDb(contact);
            try {
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
                    alert2.setContentText("Dati inseriti correttamente nel database.\n" + "Sarai ridiretto al menu");
                    alert2.showAndWait();
                    closePopup(goHomeImageView);
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

    private boolean textConstraintsRespectedForParent() {
        final int CODFISLENGTH = 16;
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(codFisTextField1.getText().length()!= CODFISLENGTH){
            codFisTextField1.setStyle(errorCss);
            errors++;
        }else {
            codFisTextField1.setStyle(normalCss);
        }

        if(cellphoneTextField1.getText().length()== 0){
            cellphoneTextField1.setStyle(errorCss);
            errors++;
        }else {
            cellphoneTextField1.setStyle(normalCss);
        }

        if(nameTextField1.getText().length() == 0){
            nameTextField1.setStyle(errorCss);
            errors++;
        }else {
            nameTextField1.setStyle(normalCss);
        }

        if(surnameTextField1.getText().length() == 0){
            surnameTextField1.setStyle(errorCss);
            errors++;
        }else {
            surnameTextField1.setStyle(normalCss);
        }
        return errors == 0;
    }

    private boolean textConstraintsRespectedForDoctor() {
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

        if(cellphoneTextField.getText().length()== 0){
            cellphoneTextField.setStyle(errorCss);
            errors++;
        }else {
            cellphoneTextField.setStyle(normalCss);
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

    private boolean textConstraintsRespectedForSupplier(){
        final int PIVALENGTH = 11;
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(pivaTextField.getText().length()!= PIVALENGTH){
            pivaTextField.setStyle(errorCss);
            errors++;
        }else {
            pivaTextField.setStyle(normalCss);
        }

        if(nomeFornitoreTextField.getText().length() == 0){
            nomeFornitoreTextField.setStyle(errorCss);
            errors++;
        }else {
            nomeFornitoreTextField.setStyle(normalCss);
        }

        if(cellphoneSupplierTextField.getText().length() == 0){
            cellphoneSupplierTextField.setStyle(errorCss);
            errors++;
        }else {
            cellphoneSupplierTextField.setStyle(normalCss);
        }

        if(emailTextField.getText().length() == 0){
            emailTextField.setStyle(errorCss);
            errors++;
        }else {
            emailTextField.setStyle(normalCss);
        }
        return errors == 0;
    }

    public void handleGoHomebutton(MouseEvent mouseEvent) {
        if(createConfirmationDialog("Sei sicuro di voler chiudere?",
                "I dati inseriti non verranno salvati."))
            closePopup(goHomeImageView);
    }

    public void handleParentNextButtonAction(ActionEvent event) {
        if(textConstraintsRespectedForParent()) {
            String nome = nameTextField1.getText();
            String cognome = surnameTextField1.getText();
            String cell = cellphoneTextField1.getText();
            String codFis = codFisTextField1.getText();
            Contact.ContactTypeFlag tipo = Contact.ContactTypeFlag.valueOf(lastSelection);

            Person contact = new Contact(nome,  cognome, codFis, cell, Integer.toString(tipo.getOrdernum()) );

            boolean success = CLIENT.clientInsertIntoDb(contact);
            try {
                if (!success) {
                    createErrorPopup("Verifica i dati inseriti ","Contatto già esistente o cellulare già in Db");

                } else {
                    createSuccessPopup();
                    closePopup(goHomeImageView);
                }
            } catch (Exception e){
                //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
            }

        }
        else{
            createErrorPopup("Verifica i dati inseriti ", "Hai lasciato campi vuoti o con un formato sbagliato");
        }

    }
}
