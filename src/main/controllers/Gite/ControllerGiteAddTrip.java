package main.controllers.Gite;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.NormalClasses.Gite.Bus;
import main.NormalClasses.Gite.Trip;
import main.controllers.AbstractController;
import main.controllers.AbstractPopupController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ControllerGiteAddTrip extends AbstractPopupController implements Initializable {
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

        AbstractController.setCurrentController(this);

        dateOfDepartureDatePicker.setShowWeekNumbers(false);
        dateOfDepartureDatePicker.setPromptText("gg/mm/aaaa");
        dateOfDepartureDatePicker.setConverter(new StringConverter<LocalDate>() {
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

    @FXML private void handleGoHomebutton(){
        //use a generic button
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void handleSaveButton(){
        if(textConstraintsRespected()) {

            //Creazione Trip
            String nomeGita = tripNameTextField.getText();
            String partenza = tripOriginTextField.getText();
            String dataGita = dateOfDepartureDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String destinazione = tripDestinationTextField.getText();

            String targaAutobus = targaAutobusTextField.getText();
            String nomeAutotrasportatore = nomeAutotrasportatoreTextField.getText();

            Trip gita = new Trip(nomeGita, dataGita, partenza, destinazione);
            Bus autobus = new Bus(targaAutobus, nomeAutotrasportatore, nomeGita, dataGita);

            boolean success = CLIENT.clientInsertTripIntodb(gita);
            try {
                if (!success) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Verifica i dati inseriti ");
                    alert.setContentText("Gita già esistente.\n");
                    alert.showAndWait();

                } else {
                    success = CLIENT.clientInsertBusIntoDb(autobus);
                    if (!success) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                        alert.setTitle("Errore");
                        alert.setHeaderText("Verifica i dati inseriti ");
                        alert.showAndWait();
                    } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
                    alert2.setGraphic(new javafx.scene.image.ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
                    alert2.setTitle("Successo");
                    alert2.setHeaderText("Successo! ");
                    alert2.setContentText("Dati inseriti correttamente nel database.\n");
                    alert2.showAndWait();
                    handleGoHomebutton();
                    }
                }
            } catch (Exception e){
                //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
            }

        }
        else{
            createErrorPopup("Verifica i dati inseriti ", "Hai lasciato campi vuoti o con un formato sbagliato");
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore");
            alert2.setHeaderText("Verifica i dati inseriti ");
            alert2.setContentText("Hai lasciato campi vuoti o con un formato sbagliato");
            alert2.showAndWait();
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
        errors+= datePickerDateSelected(dateOfDepartureDatePicker) ? 0:1;

        return errors == 0;

    }
}
