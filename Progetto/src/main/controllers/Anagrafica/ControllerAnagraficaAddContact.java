package main.controllers.Anagrafica;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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

    @FXML private TextField loggedUserDataDisplay;
    @FXML private Button giteButton;
    @FXML private Button mensaButton;
    @FXML private Button anagraficaButton;
    @FXML private Button logoutButton;
    @FXML private ComboBox choiceDropList;
    @FXML private Pane contactEsternoPane;
    @FXML private Pane fornitorePane;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loggedUserDataDisplay.setText(loggedUser.getUsername().toUpperCase());

        codFisTextField.textProperty().addListener((ov, oldValue, newValue) -> {
            codFisTextField.setText(newValue.toUpperCase());
        });

        setEnabledItems();
        AbstractController.setCurrentController(this);

        ArrayList items = new ArrayList<String>();
        items.addAll(Arrays.asList(Contact.ContactTypeFlag.values()));
        items.add("FORNITORE");
        ObservableList al = FXCollections.observableArrayList(items);
        choiceDropList.setItems(al);

        fornitorePane.setVisible(false);
        contactEsternoPane.setVisible(false);
    }

    public void setEnabledItems() {

        if(userTypeFlag== User.UserTypeFlag.MENSA) {
            giteButton.setDisable(true);
            anagraficaButton.setDisable(true);
        }
        if(userTypeFlag== User.UserTypeFlag.SUPERVISORE){
            mensaButton.setDisable(true);
        }
    }

    @FXML protected void handleMensaButtonAction(ActionEvent event) throws IOException {
        changeScene(mensaButton,"../../resources/fxml/mainmensa.fxml");
    }

    @FXML protected void handleGiteButtonAction(ActionEvent event) throws IOException {
        changeScene(giteButton,"../../resources/fxml/maingite.fxml");
    }

    @FXML protected void handleAnagraficaButtonAction(ActionEvent event) throws IOException {
        changeScene(logoutButton,"../../resources/fxml/mainanagrafica.fxml");
    }

    @FXML protected void handleLogoutButtonAction(ActionEvent event) throws IOException {
        changeScene(logoutButton,"../../resources/fxml/login.fxml");
    }

    @FXML protected void handleSelectedOption() throws IOException{

        if(!choiceDropList.getSelectionModel().isEmpty()) {
           if(choiceDropList.getSelectionModel().getSelectedItem().toString().equals("FORNITORE")){

               contactEsternoPane.setVisible(false);
               fornitorePane.setVisible(true);

           } else {
               fornitorePane.setVisible(false);
               contactEsternoPane.setVisible(true);
           }
        }

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
                    changeScene(logoutButton, "../../resources/fxml/mainanagrafica.fxml");
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

    @FXML protected void handleNextButtonAction(ActionEvent event) throws IOException{
        //Genitore o Pediatra
        if(textConstraintsRespectedForContact()) {
            String nome = nameTextField.getText();
            String cognome = surnameTextField.getText();
            String cell = cellphoneTextField.getText();
            String codFis = codFisTextField.getText();
            String selection = choiceDropList.getSelectionModel().getSelectedItem().toString();
            System.out.println(selection);
            Contact.ContactTypeFlag tipo =
                    Contact.ContactTypeFlag.valueOf(selection);

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
                    changeScene(logoutButton, "../../resources/fxml/mainanagrafica.fxml");
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

    private boolean textConstraintsRespectedForContact() {
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

}
