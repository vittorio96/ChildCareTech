package main.client.controllers.Gite;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.entities.normal_entities.Gite.Bus;
import main.entities.string_property_entities.Gite.StringPropertyBus;
import main.client.controllers.AbstractController;
import main.client.controllers.PopupController;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerGiteEditBus extends AbstractController implements Initializable {

    public static StringPropertyBus bus;
    @FXML private Button saveButton;

    // Campi
    @FXML private TextField nomeAutotrasportatoreTextField;
    @FXML private TextField targaAutobusTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        setTextFieldAutocaps(targaAutobusTextField);
        nomeAutotrasportatoreTextField.setText(bus.getNomeA());
        targaAutobusTextField.setText(bus.getTarga());
    }

    public static void setBus(StringPropertyBus selectedBus) {
        bus = selectedBus;
    }

    @FXML  public void handleSaveButton() {

        if(textConstraintsRespected()) {

            String targaAutobus = targaAutobusTextField.getText();
            String nomeAutotrasportatore = nomeAutotrasportatoreTextField.getText();
            //Integer capienza = Integer.parseInt(capienzaTextField.getText());

            Bus autobus = new Bus(targaAutobus, nomeAutotrasportatore, bus.getNomeG(), bus.getDataG());

            boolean success = CLIENT.clientUpdateBusIntoDb(new Bus(bus), autobus);
            if (!success) {
                createErrorPopup("Errore","Non è stato possibile aggiornare l'autobus ");
            } else {
                createSuccessPopup();
                handleGoHomebutton();

            }

        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }

    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    private boolean textConstraintsRespected() {
        final int LICENSEPLATELENGTH = 7;
        int errors = 0;
        errors+= textFieldLengthRespected(targaAutobusTextField, LICENSEPLATELENGTH) ? 0:1;
        errors+= textFieldConstraintsRespected(nomeAutotrasportatoreTextField) ? 0:1;
        return errors == 0;

    }

    @FXML private void handleGoHomebutton(){
        controllerType.close(saveButton);
    }


    @Override
    public void refresh() {

    }
}
