package main.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.Classes.NormalClasses.Gite.Bus;
import main.Classes.NormalClasses.Gite.Stop;
import main.Classes.NormalClasses.Gite.Trip;
import main.Classes.StringPropertyClasses.Gite.StringPropertyBus;
import main.Classes.StringPropertyClasses.Gite.StringPropertyStop;
import main.Classes.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractController;
import main.controllers.PopupController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGiteAddStop extends AbstractController implements Initializable {
    //Tasti e campi
    @FXML private Button saveButton;
    @FXML private TextField luogoTextField;
    @FXML private CheckBox doForAllCheckbox;

    private List<Bus> currentBusArrayList;

    //Tabelle
    private ObservableList<StringPropertyTrip> giteObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyTrip> giteTable;
    @FXML private TableColumn<StringPropertyTrip, String> nomeGitaColumn;
    @FXML private TableColumn<StringPropertyTrip, String> dataGitaColumn;

    private ObservableList<StringPropertyBus> busObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyBus> autobusTable;
    @FXML private TableColumn<StringPropertyBus, String> busesColumn;

    private ObservableList<StringPropertyStop> stopObservableList = FXCollections.observableArrayList();

    // Campi
    @FXML private TableView<StringPropertyStop> stopTable;
    @FXML private TableColumn<StringPropertyStop, String> stopNameColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        setColumnAssociations();
        setEventListeners();
        refreshTripTable();

    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    @FXML private void deleteStop(){
        if (giteTable.getSelectionModel().selectedItemProperty().get() != null
                && autobusTable.getSelectionModel().selectedItemProperty().get() != null &
                stopTable.getSelectionModel().selectedItemProperty().get() != null) {

            StringPropertyStop selectedStop = stopTable.getSelectionModel().selectedItemProperty().get();
            boolean success = CLIENT.clientDeleteStopFromDb(selectedStop.getNomeGita(), selectedStop.getDataGita(), selectedStop.getTarga(),selectedStop.getNumeroTappa());
            if (success == true) createSuccessPopup();
            else createErrorPopup("Errore ", "Errore di connessione riprova più tardi");
            showRelatedTappe(selectedStop.getNomeGita(), selectedStop.getDataGita(),selectedStop.getTarga());
        }
    }


    @FXML private void handleSaveButton() {
        int errors = 0;
        if (isTripSelected() && isBusSelected()) {
            if (textConstraintsRespected() && doForAllCheckbox.isSelected()) {

                for (Bus bus : currentBusArrayList) {
                    errors += CLIENT.clientInsertStopIntoDb(new Stop(bus.getNomeG(), bus.getDataG(), bus.getTarga(), luogoTextField.getText())) ? 0 : 1;
                }
                if (errors == 0) {
                    showRelatedTappe(getSelectedBus());
                    createSuccessPopup();
                }
                else createErrorPopup("Si sono verificati degli errori ", "Ci sono stati "
                        + errors + "/" + currentBusArrayList.size() + " errori, ti consigliamo di verificare se è tutto ok, altrimenti riprova a inserire la tappa nuovamente");


            } else if (textConstraintsRespected() && !doForAllCheckbox.isSelected()) {

                StringPropertyBus bus = getSelectedBus();
                boolean success = CLIENT.clientInsertStopIntoDb(getNewStop());
                if (success == true) createSuccessPopup();
                else createGenericErrorPopup();
                showRelatedTappe(bus);

            } else {
                createErrorPopup("Verifica i dati inseriti ", "Hai lasciato campi vuoti o con un formato sbagliato");
            }
        } else {
            createErrorPopup("Scegli un autobus dalla tabella ", "Non hai selezionato nessuna gita e/o autobus, seleziona una dalla tabella");
        }
    }

    private void refreshTripTable() {
        giteObservableList.clear();
        List<Trip> giteArrayList = CLIENT.clientExtractAllTripsFromDb();
        if(giteArrayList != null){
            for(Trip t : giteArrayList){
                giteObservableList.add(new StringPropertyTrip(t));
            }
            giteTable.setItems(giteObservableList);
        }
    }


    protected void setEventListeners() {
        giteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedBuses(newValue));

        autobusTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedTappe(newValue));

    }

    protected void setColumnAssociations() {
        nomeGitaColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        dataGitaColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        busesColumn.setCellValueFactory(cellData -> cellData.getValue().targaProperty());
        stopNameColumn.setCellValueFactory(cellData -> cellData.getValue().luogoProperty());
    }

    private void showRelatedBuses(StringPropertyTrip selectedTrip) {
        if(selectedTrip != null) {
            currentBusArrayList = CLIENT.clientExtractAllBusesFromTrip(selectedTrip.getNome(), selectedTrip.getData());
            busObservableList.clear();
            if (currentBusArrayList != null) {
                for (Bus b : currentBusArrayList) {
                    busObservableList.add(new StringPropertyBus(b));
                }
            }
            stopTable.getItems().clear();
            autobusTable.setItems(busObservableList);
        }
    }

    private void showRelatedTappe(StringPropertyBus selectedBus) {
        if(selectedBus != null){
            stopTable.setItems(null);
            stopObservableList.clear();

            List<Stop> stopArrayList = CLIENT.clientExtractRelatedStopsFromTrip(selectedBus.getNomeG(),selectedBus.getDataG(),selectedBus.getTarga());
            if(stopArrayList != null){
                for(Stop s : stopArrayList){
                    stopObservableList.add(new StringPropertyStop(s));
                }
            }
            stopTable.setItems(stopObservableList);
        }
    }

    private void showRelatedTappe(String nomeG, String dataG, String targa ) {
        stopTable.setItems(null);
        stopObservableList.clear();

        List<Stop> stopArrayList = CLIENT.clientExtractRelatedStopsFromTrip(nomeG, dataG, targa);
        if(stopArrayList != null){
            for(Stop s : stopArrayList){
                stopObservableList.add(new StringPropertyStop(s));
            }
        }
        stopTable.setItems(stopObservableList);
    }

    @FXML private void handleGoHomebutton() throws IOException {
        controllerType.close(saveButton);
    }

    private boolean textConstraintsRespected() {
        return textFieldConstraintsRespected(luogoTextField);
    }

    private Stop getNewStop() {
        StringPropertyBus bus = getSelectedBus();
        Stop newStop = new Stop(bus.getNomeG(), bus.getDataG(), bus.getTarga(), luogoTextField.getText());
        return newStop;
    }

    private StringPropertyBus getSelectedBus() {
        return autobusTable.getSelectionModel().selectedItemProperty().get();
    }

    private boolean isTripSelected() {
        return giteTable.getSelectionModel().selectedItemProperty().get() != null;
    }

    private boolean isBusSelected() {
        return autobusTable.getSelectionModel().selectedItemProperty().get() != null;
    }

    @Override
    public void refresh() {

    }
}

