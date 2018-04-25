package main.controllers.Anagrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Contact;
import main.Classes.NormalClasses.Anagrafica.Person;
import main.controllers.AbstractController;
import main.controllers.AbstractPopupController;
import main.qrReader.QRGenerator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ControllerAnagraficaAddChild extends AbstractPopupController implements Initializable {

    /*
        Gui elements
    */

    private final int CODFISLENGTH = 16;
    @FXML ImageView goHomeImageView;

    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;
    @FXML private DatePicker birthdayDatePicker;


    @FXML private TextField nameParent1TextField;
    @FXML private TextField surnameParent1TextField;
    @FXML private TextField codFisParent1TextField;
    @FXML private TextField cellphoneParent1TextField;


    @FXML private TextField nameParent2TextField;
    @FXML private TextField surnameParent2TextField;
    @FXML private TextField codFisParent2TextField;
    @FXML private TextField cellphoneParent2TextField;


    @FXML private TextField namePedTextField;
    @FXML private TextField surnamePedTextField;
    @FXML private TextField codFisPedTextField;
    @FXML private TextField cellphonePedTextField;

    /*
        Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AbstractController.setCurrentController(this);
        setToCapsTextFieldTransform();
        datePickerStandardInitialize(birthdayDatePicker);

    }

    /*
        Gui methods
    */

    private void setToCapsTextFieldTransform() {
        setTextFieldAutocaps(codFisTextField);
        setTextFieldAutocaps(codFisParent1TextField);
        setTextFieldAutocaps(codFisParent2TextField);
        setTextFieldAutocaps(codFisPedTextField);
    }

    public void handleGoHomebutton() {
        if(createConfirmationDialog("Sei sicuro di voler chiudere?",
                "I dati inseriti non verranno salvati."))
            close(goHomeImageView);
    }


    @FXML protected void handleNextButtonAction(ActionEvent event) throws IOException{

        if(textConstraintsRespected()) {

            Contact gen1 = getNewParent1();
            CLIENT.clientInsertIntoDb(gen1);

            Contact gen2 = null;
            if(parent2NotEmpty())
                if(parent2Errors()==0) {
                    gen2 = getNewParent2();
                    CLIENT.clientInsertIntoDb(gen2);
                }

            Contact ped = getNewDoctor();
            CLIENT.clientInsertIntoDb(ped);

            Child child = getNewChild(gen1,gen2,ped);

            boolean success = CLIENT.clientInsertIntoDb(child);
            if (!success) {
                createErrorPopup("Errore", "Verifica i dati inseriti");
            } else {
                //Genera QR image
                QRGenerator.GenerateQR(child);
                createSuccessPopup();
                close(goHomeImageView);
            }

        }
        else{
            createFieldErrorPopup();
        }

    }

    /*
        Support methods
    */

    private boolean textConstraintsRespected() {

        return ( childErrors() + parent1Errors() + doctorErrors() ) == 0;

    }

    private int doctorErrors() {
        int doctorErrors = 0;
        doctorErrors+= textFieldLengthRespected(codFisPedTextField, CODFISLENGTH) ? 0:1;
        doctorErrors+= textFieldConstraintsRespected(namePedTextField) ? 0:1;
        doctorErrors+= textFieldConstraintsRespected(surnamePedTextField) ? 0:1;
        doctorErrors+= textFieldConstraintsRespected(cellphonePedTextField) ? 0:1;
        return doctorErrors;
    }

    private int parent1Errors() {
        int parent1Errors = 0;
        parent1Errors+= textFieldLengthRespected(codFisParent1TextField, CODFISLENGTH) ? 0:1;
        parent1Errors+= textFieldConstraintsRespected(nameParent1TextField) ? 0:1;
        parent1Errors+= textFieldConstraintsRespected(surnameParent1TextField) ? 0:1;
        parent1Errors+= textFieldConstraintsRespected(cellphoneParent1TextField) ? 0:1;
        return parent1Errors;
    }

    private int parent2Errors(){
        int parent2Errors = 0;
        parent2Errors+= textFieldLengthRespected(codFisParent2TextField, CODFISLENGTH) ? 0:1;
        parent2Errors+= textFieldConstraintsRespected(nameParent2TextField) ? 0:1;
        parent2Errors+= textFieldConstraintsRespected(surnameParent2TextField) ? 0:1;
        parent2Errors+= textFieldConstraintsRespected(cellphoneParent2TextField) ? 0:1;
        return parent2Errors;
    }

    private int childErrors() {
        int childErrors = 0;
        childErrors+= textFieldLengthRespected(codFisTextField, CODFISLENGTH) ? 0:1;
        childErrors+= textFieldConstraintsRespected(nameTextField) ? 0:1;
        childErrors+= textFieldConstraintsRespected(surnameTextField) ? 0:1;
        childErrors+= datePickerIsDateSelected(birthdayDatePicker) ? 0:1;
        return childErrors;
    }

    private Contact getNewParent1() {
        String nomePar1 = nameParent1TextField.getText();
        String cognomePar1 = surnameParent1TextField.getText();
        String codFisPar1 = codFisParent1TextField.getText();
        String cellPar1 = cellphoneParent1TextField.getText();
        Contact gen1 = new Contact(nomePar1,cognomePar1,codFisPar1,cellPar1,
                Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum()));
        return gen1;
    }

    private Contact getNewParent2() {
        String codFisPar2 = codFisParent2TextField.getText();
        String nomePar2 = nameParent2TextField.getText();
        String cognomePar2 = surnameParent2TextField.getText();
        String cellPar2 = cellphoneParent2TextField.getText();
        Contact gen2 = new Contact(nomePar2, cognomePar2, codFisPar2, cellPar2,
                Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum()));
        return gen2;
    }

    private Contact getNewDoctor() {
        String nomePed = namePedTextField.getText();
        String cognomePed = surnamePedTextField.getText();
        String codFisPed = codFisPedTextField.getText();
        String cellPed = cellphonePedTextField.getText();
        Contact ped = new Contact(nomePed,cognomePed,codFisPed,cellPed,
                Integer.toString(Contact.ContactTypeFlag.PEDIATRA.getOrdernum()));
        return ped;
    }

    private Child getNewChild(Contact gen1, Contact gen2, Contact ped) {
        String nome = nameTextField.getText();
        String cognome = surnameTextField.getText();
        String codFis = codFisTextField.getText();
        String dataN = birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN));
        Child child = new Child(nome, cognome, codFis, dataN, ped.getCodiceFiscale(),
                gen1.getCodiceFiscale(), gen2.getCodiceFiscale());
        return child;

    }

    private boolean parent2NotEmpty() {
        return codFisParent2TextField.getText().length()!=0 || nameParent2TextField.getText().length()!=0 ||
                surnameParent2TextField.getText().length()!=0 || cellphoneParent2TextField.getText().length()!=0;
    }
}
