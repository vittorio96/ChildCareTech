package main.controllers.Gite;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import main.NormalClasses.Gite.Trip;
import main.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractController;

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
    private final String pattern = "dd/MM/yyyy";
    private final String inputPattern = "yyyy-MM-dd";

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
        if(trip!=null){
            tripNameTextField.setText(trip.getNome());
            tripOriginTextField.setText(trip.getPartenza());
            tripDestinationTextField.setText(trip.getDestinazione());
            String dateValue = trip.getData();
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(inputPattern);
            dateOfDepartureDatePicker.setValue(LocalDate.parse(dateValue, DATE_FORMAT));
        }
    }


    @FXML private void handleGoHomebutton(){
        /*Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();*/
        hidePopOver(saveButton);
    }

    @FXML public void handleSaveButton(ActionEvent event) {

        if(textConstraintsRespected()){
            //Creazione Trip
            String nomeGita = tripNameTextField.getText();
            String partenza = tripOriginTextField.getText();
            String dataGita = dateOfDepartureDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String destinazione = tripDestinationTextField.getText();

            Trip newgita = new Trip(nomeGita, partenza, destinazione, dataGita);

            boolean success = CLIENT.clientUpdateTripIntoDb(new Trip(trip), newgita);
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

    private boolean textConstraintsRespected() {
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(tripNameTextField.getText().length() == 0){
            tripNameTextField.setStyle(errorCss);
            errors++;
        }else {
            tripNameTextField.setStyle(normalCss);
        }

        if(tripOriginTextField.getText().length() == 0){
            tripOriginTextField.setStyle(errorCss);
            errors++;
        }else {
            tripOriginTextField.setStyle(normalCss);
        }

        if(dateOfDepartureDatePicker.getValue() == null){
            dateOfDepartureDatePicker.setStyle("-fx-border-color: red ; -fx-focus-color: #81cee9 ;");
            errors++;
        }else {
            dateOfDepartureDatePicker.setStyle("-fx-border-color: transparent ; -fx-focus-color: transparent ;");
        }

        if(tripDestinationTextField.getText().length() == 0){
            tripDestinationTextField.setStyle(errorCss);
            errors++;
        }else {
            tripDestinationTextField.setStyle(normalCss);
        }

        return errors == 0;

    }
}
