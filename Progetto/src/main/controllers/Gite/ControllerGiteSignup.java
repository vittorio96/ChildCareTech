package main.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.NormalClasses.Gite.Bus;
import main.NormalClasses.Gite.Trip;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Gite.StringPropertyBus;
import main.StringPropertyClasses.Gite.StringPropertyTrip;
import main.controllers.AbstractController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGiteSignup extends AbstractController implements Initializable {

    //Tabelle
    private ObservableList<StringPropertyTrip> giteObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyTrip> giteTable;
    @FXML private TableColumn<StringPropertyTrip, String> nomeGitaColumn;
    @FXML private TableColumn<StringPropertyTrip, String> dataGitaColumn;

    private ObservableList<StringPropertyBus> autobusObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyBus> autobusTable;
    @FXML private TableColumn<StringPropertyBus, String> busesColumn;

    //Tabella 4
    private ObservableList<StringPropertyChild> bambiniObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyChild> iscrizioniTable;
    @FXML private TableColumn<StringPropertyChild, String> codiceBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> nomeBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> cognomeBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, Boolean> presenzaBambinoColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AbstractController.setCurrentController(this);
        nomeGitaColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        dataGitaColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());

        busesColumn.setCellValueFactory(cellData -> cellData.getValue().targaProperty());

        giteTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedAutobus(newValue));

        List<Trip> giteArrayList = null;
        giteArrayList = CLIENT.clientExtractAllTripsFromDb();
        if(giteArrayList != null){
            for(Trip t : giteArrayList){
                giteObservableList.add(new StringPropertyTrip(t));
            }

            giteTable.setItems(giteObservableList);
        }


    }

    private void showRelatedAutobus(StringPropertyTrip selectedGita) {
        iscrizioniTable.setItems(null);
        List<Bus> autobusArrayList = new ArrayList<>();
        autobusArrayList = CLIENT.clientExtractAllBusesFromTrip(selectedGita.getNome(),selectedGita.getData());
        if(autobusArrayList != null){
            for(Bus b : autobusArrayList){
                autobusObservableList.add(new StringPropertyBus(b));
            }
        }
        autobusTable.setItems(autobusObservableList);
    }

    @FXML private void handleGoHomebutton(){
    }

}
