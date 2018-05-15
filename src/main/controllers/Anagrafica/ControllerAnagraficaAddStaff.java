package main.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import main.Classes.NormalClasses.Anagrafica.Staff;
import main.User;
import main.controllers.AbstractController;
import main.controllers.PopupController;
import main.qrReader.QRGenerator;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerAnagraficaAddStaff extends AbstractController implements Initializable {

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
        setControllerType();
        choiceBoxSetup();
        setTextFieldAutocaps(codFisTextField);
        datePickerStandardInitialize(birthdayDatePicker);
    }

    private void setControllerType() {
        controllerType = new PopupController();
    }

    private void choiceBoxSetup() {
        ArrayList elements = new ArrayList<>(Arrays.asList(User.UserTypeFlag.values()));
        userTypeDropList.setItems(FXCollections.observableArrayList(elements));
    }

    @FXML protected void handleNextButtonAction(ActionEvent event) throws IOException{

        if(textConstraintsRespected()) {
            Staff staff = createNewStaff();
            boolean success = CLIENT.clientInsertIntoDb(staff);
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ", "Codice FIscale già esistente o username già preso");
            } else {
                //Genera QR
                QRGenerator.GenerateQR(staff);
                createSuccessPopup();
            }
        }
        else{
            createFieldErrorPopup();
        }
    }

    private boolean textConstraintsRespected() {
        int errors = 0;
        errors+= textFieldLengthRespected(codFisTextField, CODFISLENGTH) ? 0:1;
        errors+= textFieldConstraintsRespected(nameTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(surnameTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(usernameTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(passwordTextField) ? 0:1;
        errors+= datePickerIsDateSelected(birthdayDatePicker)? 0:1;
        errors+= choiceBoxConstraintsRespected(userTypeDropList) ? 0:1;
        return errors == 0;

    }

    private Staff createNewStaff(){
        String nome = nameTextField.getText();
        String cognome = surnameTextField.getText();
        String codFis = codFisTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String dataN = birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN));
        User.UserTypeFlag typeFlag =
                User.UserTypeFlag.valueOf(userTypeDropList.getSelectionModel().getSelectedItem().toString());

        return new Staff(codFis, nome, cognome, username, password, dataN, typeFlag);
    }

    public void handleGoHomebutton() {
        if(createConfirmationDialog("Sei sicuro di voler chiudere?",
                "I dati inseriti non verranno salvati."))
            controllerType.close(goHomeImageView);
    }
}
