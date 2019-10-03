package main.client.controllers.Gite;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.entities.string_property_entities.Gite.StringPropertyTrip;
import main.client.controllers.AbstractController;
import main.client.controllers.PopOverController;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerGiteLookupCorrectBus extends AbstractController implements Initializable {

    /*
        Static
    */

    private static StringPropertyTrip trip;

    public static void setTrip(StringPropertyTrip trip) {
        ControllerGiteLookupCorrectBus.trip = trip;
    }


    /*
        Initialize
    */

    @FXML TextField textField;
    @FXML Button button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();


    }

    protected void setControllerType() {
        controllerType = new PopOverController();
    }

    @FXML
    private void handleButtonAction() {
        String targa = null;
        if(textField.getText()!=null) {
            targa = CLIENT.clientgetCorrectBus(textField.getText(), trip.getNome(), trip.getData());
        }
        if (targa == null) createErrorPopup("Ooops", "Il bambino ha sbagliato gita, " +
                "non risulta assegnato ad alcun autobus ");
        else createCustomSuccessPopup("L'abbiamo trovato!","il bambino risulta assegnato al autobus con targa: "+targa+ " \n");
    }

    @Override
    public void refresh() {

    }


}
