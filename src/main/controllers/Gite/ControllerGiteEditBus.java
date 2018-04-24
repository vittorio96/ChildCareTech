package main.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.NormalClasses.Anagrafica.Child;
import main.NormalClasses.Gite.Bus;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Gite.StringPropertyBus;
import main.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractController;
import main.controllers.AbstractPopOverController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGiteEditBus extends AbstractPopOverController implements Initializable {

    public static StringPropertyBus bus;
    @FXML private Button saveButton;

    // Campi
    @FXML private TextField nomeAutotrasportatoreTextField;
    @FXML private TextField targaAutobusTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            try {
                if (!success) {
                    createErrorPopup("Errore","Non è stato possibile aggiornare l'autobus ");
                } else {
                    createSuccessPopup();
                    handleGoHomebutton();

                }
            } catch (Exception e){
                //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
            }

        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }

    }

    /*private void refreshBusTable(){
        if(bus!=null){
            busObservableList.clear();

            List<Child> childArrayList = CLIENT.clientExtractChildrenFromBus(normalBus);
            if(childArrayList != null){
                for(Child c : childArrayList){
                    cObservableList.add(new StringPropertyBus(b));
                }
            }
            c.setItems(busObservableList);
        }
    }*/

    private boolean textConstraintsRespected() {
        final int LICENSEPLATELENGTH = 7;
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(nomeAutotrasportatoreTextField.getText().length() == 0){
            nomeAutotrasportatoreTextField.setStyle(errorCss);
            errors++;
        }else {
            nomeAutotrasportatoreTextField.setStyle(normalCss);
        }

        if(targaAutobusTextField.getText().length()!= LICENSEPLATELENGTH){
            targaAutobusTextField.setStyle(errorCss);
            errors++;
        }else {
            targaAutobusTextField.setStyle(normalCss);
        }

        /*if(capienzaTextField.getText().length() == 0){
            capienzaTextField.setStyle(errorCss);
            errors++;
        }else {
            capienzaTextField.setStyle(normalCss);
            try{
                Integer.parseInt(capienzaTextField.getText());
            }catch (NumberFormatException e){
                capienzaTextField.setStyle(errorCss);
                errors++;
            }
        }*/

        return errors == 0;

    }

    @FXML private void handleGoHomebutton(){
        close(saveButton);
    }


    public void addNewIngredient(MouseEvent mouseEvent) {
    }
}
