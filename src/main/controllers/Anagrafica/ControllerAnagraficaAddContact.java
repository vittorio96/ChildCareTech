package main.controllers.Anagrafica;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.Classes.NormalClasses.Anagrafica.Contact;
import main.Classes.NormalClasses.Anagrafica.Person;
import main.Classes.NormalClasses.Anagrafica.Supplier;
import main.controllers.AbstractController;
import main.controllers.AbstractPopupController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAnagraficaAddContact extends AbstractPopupController implements Initializable {

    /*
        Gui elements
    */

    @FXML private TextField nomeFornitoreTextField;
    @FXML private TextField pivaTextField;
    @FXML private TextField cellphoneSupplierTextField;
    @FXML private TextField emailTextField;


    @FXML private TextField nameTextField;
    @FXML private TextField cellphoneTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;


    @FXML private TextField nameTextField1;
    @FXML private TextField cellphoneTextField1;
    @FXML private TextField surnameTextField1;
    @FXML private TextField codFisTextField1;


    @FXML private ImageView goHomeImageView;
    @FXML private TabPane tabPane;
          private String lastSelection;
          private final int CODFISLENGTH = 16;

    /*
        Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTextFieldAutocaps(codFisTextField);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
            if(nv!=null){
                lastSelection = nv.getText().toUpperCase();
            }
        });

        AbstractController.setCurrentController(this);

    }

    /*
        Gui methods
    */

    @FXML protected void handleParentNextButtonAction(ActionEvent event) {
        if(textConstraintsRespectedForParent()) {
            boolean success = CLIENT.clientInsertIntoDb(getNewParent());
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ",
                        "Contatto già esistente o cellulare già in Db");
            } else {
                createSuccessPopup();
                close(goHomeImageView);
            }
        }
        else{ createFieldErrorPopup(); }

    }

    @FXML protected void handleFornitoreButtonAction(ActionEvent event) throws IOException{

        if(textConstraintsRespectedForSupplier()) {
            boolean success = CLIENT.clientInsertSupplierIntoDb(getNewSupplier());
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ",
                        "Contatto già esistente o cellulare già in Db");
            } else {
                createSuccessPopup();
                close(goHomeImageView);
            }
        }
        else{ createFieldErrorPopup(); }
    }

    @FXML protected void handleDoctorNextButtonAction(ActionEvent event) throws IOException{

        if(textConstraintsRespectedForDoctor()) {

            boolean success = CLIENT.clientInsertIntoDb(getNewDoctor());
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ",
                        "Contatto già esistente o cellulare già in Db");
            } else {
                createSuccessPopup();
            }
        }
        else{ createFieldErrorPopup(); }

    }

    /*
        Gui support methods
    */

    private boolean textConstraintsRespectedForParent() {
        int parentErrors = 0;
        parentErrors+= textFieldLengthRespected(codFisTextField1, CODFISLENGTH) ? 0:1;
        parentErrors+= textFieldConstraintsRespected(nameTextField1) ? 0:1;
        parentErrors+= textFieldConstraintsRespected(surnameTextField1) ? 0:1;
        parentErrors+= textFieldConstraintsRespected(cellphoneTextField1) ? 0:1;
        return parentErrors == 0;
    }

    private boolean textConstraintsRespectedForDoctor() {
        int doctorErrors = 0;
        doctorErrors+= textFieldLengthRespected(codFisTextField, CODFISLENGTH) ? 0:1;
        doctorErrors+= textFieldConstraintsRespected(nameTextField) ? 0:1;
        doctorErrors+= textFieldConstraintsRespected(surnameTextField) ? 0:1;
        doctorErrors+= textFieldConstraintsRespected(cellphoneTextField) ? 0:1;
        return doctorErrors == 0;
    }


    private boolean textConstraintsRespectedForSupplier(){
        final int PIVALENGTH = 11;
        int supplierErrors = 0;
        supplierErrors+= textFieldLengthRespected(pivaTextField, PIVALENGTH) ? 0:1;
        supplierErrors+= textFieldConstraintsRespected(nomeFornitoreTextField) ? 0:1;
        supplierErrors+= textFieldConstraintsRespected(cellphoneSupplierTextField) ? 0:1;
        supplierErrors+= textFieldConstraintsRespected(emailTextField) ? 0:1;
        return supplierErrors == 0;
    }

    public void handleGoHomebutton(MouseEvent mouseEvent) {
        if(createConfirmationDialog("Sei sicuro di voler chiudere?",
                "I dati inseriti non verranno salvati."))
            close(goHomeImageView);
    }

    /*
        Private methods
    */

    public Person getNewParent() {
        String nome = nameTextField1.getText();
        String cognome = surnameTextField1.getText();
        String cell = cellphoneTextField1.getText();
        String codFis = codFisTextField1.getText();
        Contact.ContactTypeFlag tipo = Contact.ContactTypeFlag.valueOf(lastSelection);

        Person contact = new Contact(nome,  cognome, codFis, cell, tipo);
        return contact;
    }

    public Supplier getNewSupplier() {
        String nome = nomeFornitoreTextField.getText();
        String piva = pivaTextField.getText();
        String cell = cellphoneSupplierTextField.getText();
        String email = emailTextField.getText();

        Supplier supplier = new Supplier(piva, nome, cell, email);
        return supplier;
    }

    public Person getNewDoctor() {
        String nome = nameTextField.getText();
        String cognome = surnameTextField.getText();
        String cell = cellphoneTextField.getText();
        String codFis = codFisTextField.getText();
        Contact.ContactTypeFlag tipo = Contact.ContactTypeFlag.valueOf(lastSelection);

        Person newDoctor = new Contact(nome,  cognome, codFis, cell, Integer.toString(tipo.getOrdernum()) );
        return newDoctor;
    }
}
