package main.controllers.Gite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseEvent;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Gite.Bus;
import main.Classes.NormalClasses.Gite.BusAssociation;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.Classes.StringPropertyClasses.Gite.StringPropertyBus;
import main.controllers.AbstractController;
import main.controllers.AbstractPopOverController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerGiteRemoveChildren extends AbstractPopOverController implements Initializable {

    //Buttons
    @FXML private Button genericButton;

    List <BusAssociation> unenrolled = new ArrayList<>();

    // Static
    public static StringPropertyBus selectedBus;
    public static Bus selectedNormalBus;

    public static void setSelectedBus(StringPropertyBus selectedBus) {
        ControllerGiteRemoveChildren.selectedBus = selectedBus;
        ControllerGiteRemoveChildren.selectedNormalBus = new Bus(selectedBus);
    }

    //Tabella Bambini
    private ObservableList<StringPropertyChild> bambiniObservableList = FXCollections.observableArrayList();

    @FXML private TableView<StringPropertyChild> iscrizioniTable;
    @FXML private TableColumn<StringPropertyChild, String> codiceBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> nomeBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, String> cognomeBambinoColumn;
    @FXML private TableColumn<StringPropertyChild, Boolean> presenzaBambinoColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AbstractController.setCurrentController(this);

        iscrizioniTable.setEditable(true);
        codiceBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        cognomeBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        presenzaBambinoColumn.setCellFactory( CheckBoxTableCell.forTableColumn(presenzaBambinoColumn) );
        presenzaBambinoColumn.setCellValueFactory(cellData -> cellData.getValue().booleanStatusProperty());
        refreshChildrenTable();

    }



    private void refreshChildrenTable(){
        if (selectedBus != null) {
            bambiniObservableList.clear();
            List<Child> childrenList = CLIENT.clientExtractChildrenFromBus(selectedNormalBus);
            if(childrenList != null){
                for(Child c : childrenList){
                    bambiniObservableList.add(new StringPropertyChild(c));
                }
            }
            iscrizioniTable.setItems(bambiniObservableList);
        }

    }

    @FXML private void handleGoHomebutton(){
        close(genericButton);
    }


    @FXML public void saveChanges(MouseEvent mouseEvent) {
        int errors = 0;
        unenrolled.clear();
        if(selectedBus != null){
            for (StringPropertyChild c : bambiniObservableList){
                if (c.isBooleanStatus() )  unenrolled.add(new BusAssociation(c.getCodiceFiscale(),selectedBus.getNomeG(), selectedBus.getDataG(),selectedBus.getTarga()));
            }
            Iterator itr = unenrolled.iterator();
            while (itr.hasNext()){
                errors +=  CLIENT.clientDeleteBusAssociationFromDb((BusAssociation) itr.next())? 0:1;
            }
            if (errors == 0) {
                createSuccessPopup();
                refreshChildrenTable();
            } else {
                createErrorPopup("Errore", "Non è stato possibile iscrivere uno o tutti i bambini, riprova più tardi");
            }
        }else {
            createErrorPopup("Errore", "Non hai selezionato una gita è un autobus");
        }

    }
}
