package main.client.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.entities.normal_entities.Anagrafica.Child;
import main.entities.normal_entities.Gite.Bus;
import main.entities.string_property_entities.Anagrafica.StringPropertyChild;
import main.entities.string_property_entities.Gite.StringPropertyBus;
import main.entities.string_property_entities.Gite.StringPropertyTrip;
import main.client.controllers.AbstractController;
import main.client.controllers.PopupController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ControllerGiteAddBus extends AbstractController implements Initializable {

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
        setControllerType();
        AbstractController.setCurrentController(this);
        setTextFieldAutocaps(targaAutobusTextField);
        busesColumn.setCellValueFactory(cellData -> cellData.getValue().targaProperty());
        codiceBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        cognomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        autobusTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRelatedBusAndChildren(newValue));
        refreshBusTable(gita);

    }

    protected void setControllerType() {
        controllerType = new PopupController();
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
        controllerType.close(saveButton);
    }


    @FXML private void handleSaveButton(){
            if(textConstraintsRespected()) {
                String targaAutobus = targaAutobusTextField.getText();
                String nomeAutotrasportatore = nomeAutotrasportatoreTextField.getText();
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
                createFieldErrorPopup();
            }

    }


    private boolean textConstraintsRespected() {
        final int LICENSEPLATELENGTH = 7;
        int errors = 0;
        errors+= textFieldConstraintsRespected(nomeAutotrasportatoreTextField)? 0:1;
        errors+= textFieldLengthRespected(targaAutobusTextField, LICENSEPLATELENGTH) ? 0:1;

        return errors == 0;

    }

    @Override
    public void refresh() {

    }
}
