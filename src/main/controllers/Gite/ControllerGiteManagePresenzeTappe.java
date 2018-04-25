package main.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Contact;
import main.Classes.NormalClasses.Gite.*;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.Classes.StringPropertyClasses.Gite.StringPropertyBus;
import main.Classes.StringPropertyClasses.Gite.StringPropertyStop;
import main.Classes.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractPopupController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ControllerGiteManagePresenzeTappe extends AbstractPopupController implements Initializable {

    //Utilities
    private final String pattern = "dd/MM/yyyy";
    private final String inputPattern = "yyyy-MM-dd";

    //Tabella 1
    private ObservableList<StringPropertyTrip> giteObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyTrip> giteTable;
    @FXML private TableColumn<StringPropertyTrip, String> nomeGitaColumn;
    @FXML private TableColumn<StringPropertyTrip, String> dataGitaColumn;

    //Tabella 2
    private ObservableList<StringPropertyBus> autobusObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyBus> autobusTable;
    @FXML private TableColumn<StringPropertyBus, String> autobusTappaColumn;

    //Tabella 3
    private ObservableList<StringPropertyStop> tappeObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyStop> tappeTable;
    @FXML private TableColumn<StringPropertyStop, String> tappeColumn;


    //Tabella 4
    private ObservableList<StringPropertyChild> bambiniObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyChild> iscrizioniTable;
    @FXML private TableColumn<StringPropertyChild, String> codiceBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> nomeBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> cognomeBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, Boolean> presenzaBambinoColumn;

    //TextFields
    @FXML private TextField parent1TextField;
    @FXML private TextField parent2TextField;
    @FXML private TextField parent1CellTextField;
    @FXML private TextField parent2CellTextField;

    //Buttons
    @FXML private Button saveButton;
    @FXML private Button goHomeButton;

    @FXML private List<StopPresence> stopPresenceList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataGitaColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        nomeGitaColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());

        autobusTappaColumn.setCellValueFactory(cellData -> cellData.getValue().targaProperty());

        tappeColumn.setCellValueFactory( cellData -> cellData.getValue().luogoProperty() );

        codiceBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        cognomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        presenzaBambinoColumn.setCellFactory( CheckBoxTableCell.forTableColumn(presenzaBambinoColumn) );
        presenzaBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().booleanStatusProperty());

        giteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedAutobus(newValue));

        autobusTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedTappe(newValue));

        tappeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedParticipants(newValue));

        iscrizioniTable.setEditable(true);
        iscrizioniTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showInfo(newValue));

        List<Trip> giteArrayList = null;
        giteArrayList = CLIENT.clientExtractAllTripsFromDb();
        if(giteArrayList  != null){
            for(Trip t : giteArrayList){
                giteObservableList.add(new StringPropertyTrip(t));
            }
        }
        giteTable.setItems(giteObservableList);



    }

    private void showInfo(StringPropertyChild selectedChild) {
        if(selectedChild != null) {
            List<Contact> contacts = CLIENT.clientExtractParentsFromChild(selectedChild.getCodiceFiscale());
            int i = 0;
            for (Contact c : contacts) {
                if (c.getTipo().equals(Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum()))) {
                    if (i == 1) {
                        parent2TextField.setText(c.getNome() + " " + c.getCognome());
                        parent2CellTextField.setText(c.getCellulare());
                        i++;
                    }
                    if (i == 0) {
                        parent1TextField.setText(c.getNome() + " " + c.getCognome());
                        parent1CellTextField.setText(c.getCellulare());
                        i++;
                    } else break;

                }
            }
        }
    }

    private void showRelatedParticipants(StringPropertyStop selectedStop) {
        iscrizioniTable.setItems(null);
        bambiniObservableList.clear();
        if(selectedStop != null) {
            List<Child> bambiniTappeArrayList = CLIENT.clientExtractMissingChildrenForStopFromDb(new Stop(selectedStop));
            if (bambiniTappeArrayList != null) {
                for (Child c : bambiniTappeArrayList) {
                    bambiniObservableList.add(new StringPropertyChild(c));
                }
            }
        }
        iscrizioniTable.setItems(bambiniObservableList);
    }

    private void showRelatedTappe(StringPropertyBus selectedBus) {
        tappeObservableList.clear();
        tappeTable.setItems(null);
        iscrizioniTable.setItems(null);
        bambiniObservableList.clear();

        if(selectedBus != null){
            List<Stop> tappeArrayList = CLIENT.clientExtractRelatedStopsFromTrip(selectedBus.getNomeG(),selectedBus.getDataG(),selectedBus.getTarga());
            if(tappeArrayList != null){
                for(Stop s : tappeArrayList){
                    tappeObservableList.add(new StringPropertyStop(s));
                }
            }
        }
        tappeTable.setItems(tappeObservableList);
    }

    private void showRelatedAutobus(StringPropertyTrip selectedGita) {
        iscrizioniTable.setItems(null);
        tappeTable.setItems(null);
        autobusTable.setItems(null);
        autobusObservableList.clear();
        tappeObservableList.clear();
        bambiniObservableList.clear();
        if(selectedGita != null) {
            List<Bus> autobusArrayList = CLIENT.clientExtractAllBusesFromTrip(selectedGita.getNome(), selectedGita.getData());
            if (autobusArrayList != null) {
                for (Bus b : autobusArrayList) {
                    autobusObservableList.add(new StringPropertyBus(b));
                }
            }
        }
        autobusTable.setItems(autobusObservableList);
    }


    @FXML private void handleGoHomeButton(){
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }



    @FXML public void insertStopPresence(ActionEvent event) {
        StringPropertyStop selectedStop = tappeTable.getSelectionModel().selectedItemProperty().get();
        if(giteTable.getSelectionModel().selectedItemProperty().get() != null &
                autobusTable.getSelectionModel().selectedItemProperty().get() != null &
                    selectedStop != null){

            stopPresenceList.clear();
            if(bambiniObservableList.size() != 0 && giteTable.getSelectionModel().selectedItemProperty().get() != null &
                    autobusTable.getSelectionModel().selectedItemProperty().get() != null){
                for (StringPropertyChild c : bambiniObservableList){
                    if (c.isBooleanStatus() )  stopPresenceList.add(new StopPresence(c.getCodiceFiscale(), selectedStop.getNumeroTappa(), selectedStop.getTarga(), selectedStop.getDataGita(), selectedStop.getNomeGita()));
                }
                boolean success = CLIENT.clientInsertStopPresencesIntoDb(stopPresenceList);
                if (success) {
                    createSuccessPopup();
                    refreshChildTable(selectedStop);
                } else {
                    createErrorPopup("Errore", "Non è stato possibile iscrivere i bambini, riprova più tardi");
                }
            }else {
                createErrorPopup("Errore", "Non hai selezionato una gita è una tappa, gita o autobus");
            }


        }
    }

    private void refreshChildTable(StringPropertyStop selectedStop) {
        iscrizioniTable.setItems(null);
        bambiniObservableList.clear();
        if(selectedStop != null) {
            List<Child> bambiniTappeArrayList = CLIENT.clientExtractMissingChildrenForStopFromDb(new Stop(selectedStop));
            if (bambiniTappeArrayList != null) {
                for (Child c : bambiniTappeArrayList) {
                    bambiniObservableList.add(new StringPropertyChild(c));
                }
            }
        }
        iscrizioniTable.setItems(bambiniObservableList);
    }
}
