package main.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.NormalClasses.Anagrafica.Child;
import main.NormalClasses.Gite.Bus;
import main.NormalClasses.Gite.Trip;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Gite.StringPropertyBus;
import main.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ControllerGiteAddBus extends AbstractController implements Initializable {
    private final String pattern = "dd/MM/yyyy";

    //Tasti
    @FXML private Button saveButton;

    private static StringPropertyTrip gita;

    private ObservableList<StringPropertyBus> busObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyChild> bambiniObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyBus> autobusTable;
    @FXML private TableColumn<StringPropertyBus, String> busesColumn;

    // Campi
    @FXML private TextField nomeAutotrasportatoreTextField;
    @FXML private TextField targaAutobusTextField;

    @FXML private TableView<StringPropertyChild> bambiniTable;
    @FXML private TableColumn<StringPropertyChild, String> codiceBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> nomeBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> cognomeBambinoColumn;



    public static void setGita(StringPropertyTrip sptrip){
        gita = sptrip;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AbstractController.setCurrentController(this);
        busesColumn.setCellValueFactory(cellData -> cellData.getValue().targaProperty());
        codiceBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        cognomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        autobusTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedBusAndChildren(newValue));
        refreshBusTable(gita);

    }

    private void showRelatedBusAndChildren(StringPropertyBus selectedbus) {
        if(selectedbus!=null) {
            bambiniObservableList.clear();

            List<Child> childrenArrayList = CLIENT.clientExtractChildrenFromBus(new Bus(selectedbus));
            if (childrenArrayList != null) {
                for (Child c : childrenArrayList) {
                    bambiniObservableList.add(new StringPropertyChild(c));
                }
            }
            bambiniTable.setItems(bambiniObservableList);
        }

    }


    private void refreshBusTable(StringPropertyTrip gita){
        if(gita!=null){
            busObservableList.clear();

            List<Bus> autobusArrayList = CLIENT.clientExtractAllBusesFromTrip(gita.getNome(),gita.getData());
            if(autobusArrayList != null){
                for(Bus b : autobusArrayList){
                    busObservableList.add(new StringPropertyBus(b));
                }
            }
            autobusTable.setItems(busObservableList);
        }
    }


    @FXML private void handleGoHomebutton(){
        //use a generic button
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


    @FXML private void handleSaveButton(){
            if(textConstraintsRespected()) {

                String targaAutobus = targaAutobusTextField.getText();
                String nomeAutotrasportatore = nomeAutotrasportatoreTextField.getText();
                //Integer capienza = Integer.parseInt(capienzaTextField.getText());

                StringPropertyTrip selectedTrip = gita;

                Bus autobus = new Bus(targaAutobus, nomeAutotrasportatore, selectedTrip.getNome(), selectedTrip.getData());

                boolean success = CLIENT.clientInsertBusIntoDb(autobus);
                try {
                    if (!success) {
                        createErrorPopup("Errore","Verifica i dati inseriti ");
                    } else {
                       refreshBusTable(gita);
                       createSuccessPopup();
                    }
                } catch (Exception e){
                    //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
                }

            }
            else{
                createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
            }

    }


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
}
