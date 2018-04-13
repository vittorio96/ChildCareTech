package main.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.NormalClasses.Gite.Bus;
import main.NormalClasses.Gite.Stop;
import main.NormalClasses.Gite.Trip;
import main.StringPropertyClasses.Gite.StringPropertyBus;
import main.StringPropertyClasses.Gite.StringPropertyStop;
import main.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractController;

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

        AbstractController.setCurrentController(this);
        nomeGitaColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        dataGitaColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());

        busesColumn.setCellValueFactory(cellData -> cellData.getValue().targaProperty());

        stopNameColumn.setCellValueFactory(cellData -> cellData.getValue().luogoProperty());

        giteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedBuses(newValue));

        autobusTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedTappe(newValue.getNomeG(), newValue.getDataG(), newValue.getTarga()));


        List<Trip> giteArrayList = CLIENT.clientExtractAllTripsFromDb();
        if(giteArrayList != null){
            for(Trip t : giteArrayList){
                giteObservableList.add(new StringPropertyTrip(t));
            }
            giteTable.setItems(giteObservableList);
        }


    }

    private void showRelatedBuses(StringPropertyTrip selectedTrip) {
        currentBusArrayList = CLIENT.clientExtractAllBusesFromTrip(selectedTrip.getNome(), selectedTrip.getData());
        busObservableList.clear();
        if(currentBusArrayList != null){
            for(Bus b : currentBusArrayList){
                busObservableList.add(new StringPropertyBus(b));
            }
        }
        stopTable.getItems().clear();
        autobusTable.setItems(busObservableList);
    }

    private void showRelatedTappe(StringPropertyBus selectedBus) {

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
        //using a generic button, makes no difference
        closePopup(saveButton);
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
        if (giteTable.getSelectionModel().selectedItemProperty().get() != null
                && autobusTable.getSelectionModel().selectedItemProperty().get() != null) {
            if (textConstraintsRespected() && doForAllCheckbox.isSelected()) {

                for (Bus bus : currentBusArrayList) {
                    errors += CLIENT.clientInsertStopIntoDb(new Stop(bus.getNomeG(), bus.getDataG(), bus.getTarga(), luogoTextField.getText())) ? 0 : 1;
                }
                if (errors == 0) {
                    showRelatedTappe(autobusTable.getSelectionModel().selectedItemProperty().get());
                    createSuccessPopup();

                }
                else createErrorPopup("Si sono verificati degli errori ", "Ci sono stati "
                        + errors + "/" + currentBusArrayList.size() + " errori, ti consigliamo di verificare se è tutto ok, altrimenti riprova a inserire la tappa nuovamente");


            } else if (textConstraintsRespected() && !doForAllCheckbox.isSelected()) {

                StringPropertyBus bus = autobusTable.getSelectionModel().selectedItemProperty().get();
                boolean success = CLIENT.clientInsertStopIntoDb(new Stop(bus.getNomeG(), bus.getDataG(), bus.getTarga(), luogoTextField.getText()));
                if (success == true) createSuccessPopup();
                else createErrorPopup("Errore ", "Errore di connessione riprova più tardi");

                showRelatedTappe(bus);

            } else {
                createErrorPopup("Verifica i dati inseriti ", "Hai lasciato campi vuoti o con un formato sbagliato");
            }
        } else {
            createErrorPopup("Scegli un autobus dalla tabella ", "Non hai selezionato nessuna gita e/o autobus, seleziona una dalla tabella");
        }
    }

    private boolean textConstraintsRespected() {
        return luogoTextField.getText()!=null;
    }
}

