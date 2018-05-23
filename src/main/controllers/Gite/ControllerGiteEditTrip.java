package main.controllers.Gite;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.Classes.NormalClasses.Gite.Trip;
import main.Classes.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractController;
import main.controllers.PopOverController;
import main.controllers.PopupController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ControllerGiteEditTrip extends AbstractController implements Initializable {

    //Static
    private static StringPropertyTrip trip;

    //Tasti
    @FXML
    private Button saveButton;

    //Campi
    @FXML private TextField tripNameTextField;
    @FXML private TextField tripOriginTextField;
    @FXML private DatePicker dateOfDepartureDatePicker;
    @FXML private TextField tripDestinationTextField;

    public static void setTrip(StringPropertyTrip trip) {
        ControllerGiteEditTrip.trip = trip;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        datePickerStandardInitialize(dateOfDepartureDatePicker);
        initialDetailsDisplay();
    }

    protected void setControllerType() {
        //controllerType = new PopOverController();
        controllerType = new PopupController();

    }

    private void initialDetailsDisplay() {
        if(trip!=null){
            tripNameTextField.setText(trip.getNome());
            tripOriginTextField.setText(trip.getPartenza());
            tripDestinationTextField.setText(trip.getDestinazione());
            String dateValue = trip.getData();
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DBDATEPATTERN);
            dateOfDepartureDatePicker.setValue(LocalDate.parse(dateValue, DATE_FORMAT));
        }
    }


    @FXML private void handleGoHomebutton(){
        controllerType.close(saveButton);
    }

    @FXML public void handleSaveButton(ActionEvent event) {

        if(textConstraintsRespected()){
            boolean success = CLIENT.clientUpdateTripIntoDb(new Trip(trip), getNewTrip());
            try {
                if (!success) createErrorPopup("Errore di connessione", "Riprova più tardi");
                else {
                    createSuccessPopup();
                    handleGoHomebutton();
                }
            } catch (Exception e){
                //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
            }

        } else{
            createErrorPopup("Verifica i dati inseriti ", "Hai lasciato campi vuoti o con un formato sbagliato");
        }

    }

    private Trip getNewTrip() {
        String nomeGita = tripNameTextField.getText();
        String partenza = tripOriginTextField.getText();
        String dataGita = dateOfDepartureDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN));
        String destinazione = tripDestinationTextField.getText();

        Trip newgita = new Trip(nomeGita, dataGita, partenza, destinazione);
        return newgita;
    }

    private boolean textConstraintsRespected() {

        int errors = 0;
        errors+= textFieldConstraintsRespected(tripNameTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(tripOriginTextField) ? 0:1;
        errors+= textFieldConstraintsRespected(tripDestinationTextField) ? 0:1;
        errors+= datePickerIsDateSelected(dateOfDepartureDatePicker) ? 0:1;

        return errors == 0;

    }

    @Override
    public void refresh() {

    }
}
