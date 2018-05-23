package main.controllers.Gite;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.Classes.NormalClasses.Gite.Bus;
import main.Classes.NormalClasses.Gite.Trip;
import main.controllers.AbstractController;
import main.controllers.PopupController;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ControllerGiteAddTrip extends AbstractController implements Initializable {
    private final String pattern = "dd/MM/yyyy";

    //Tasti
    @FXML private Button saveButton;

    //Campi
    @FXML private TextField tripNameTextField;
    @FXML private TextField tripOriginTextField;
    @FXML private DatePicker dateOfDepartureDatePicker;
    @FXML private TextField tripDestinationTextField;

    @FXML private TextField nomeAutotrasportatoreTextField;
    @FXML private TextField targaAutobusTextField;
    @FXML private TextField capienzaTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        datePickerStandardInitialize(dateOfDepartureDatePicker);

    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    @FXML private void handleGoHomebutton(){
        controllerType.close(saveButton);
    }

    @FXML private void handleSaveButton(){
        if(textConstraintsRespected()) {

            Trip gita = getNewTrip();
            Bus autobus = getNewBus();

            boolean success = CLIENT.clientInsertTripIntodb(gita);
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ","Gita già esistente.\n");
            } else {
                success = CLIENT.clientInsertBusIntoDb(autobus);
                if (!success) {
                    createGenericErrorPopup();
                } else {
                    createSuccessPopup();
                    handleGoHomebutton();
                }
            }
        }
        else{
            createFieldErrorPopup();
        }
    }


    private boolean textConstraintsRespected() {
        final int LICENSEPLATELENGTH = 7;
        int errors = 0;

        errors+= textFieldConstraintsRespected(tripNameTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(tripOriginTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(tripDestinationTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(nomeAutotrasportatoreTextField) ? 0:1;
        errors+= textFieldLengthRespected(targaAutobusTextField, LICENSEPLATELENGTH) ? 0:1;
        errors+= datePickerIsDateSelected(dateOfDepartureDatePicker) ? 0:1;

        return errors == 0;

    }

    /* @requires all fields correctly filled*/
    public Trip getNewTrip() {
        String nomeGita = tripNameTextField.getText();
        String partenza = tripOriginTextField.getText();
        String dataGita = dateOfDepartureDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN));
        String destinazione = tripDestinationTextField.getText();

        return new Trip(nomeGita, dataGita, partenza, destinazione);
    }

    /* @requires all fields correctly filled*/
    private Bus getNewBus(){
        String nomeGita = tripNameTextField.getText();
        String dataGita = dateOfDepartureDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN));
        String targaAutobus = targaAutobusTextField.getText();
        String nomeAutotrasportatore = nomeAutotrasportatoreTextField.getText();
        return new Bus(targaAutobus, nomeAutotrasportatore, nomeGita, dataGita);
    }

    @Override
    public void refresh() {

    }
}
