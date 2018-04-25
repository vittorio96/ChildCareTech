package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Staff;
import main.Classes.NormalClasses.Mensa.Menu;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.controllers.AbstractPopupController;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaTodaysConflicts extends AbstractPopupController implements Initializable {

    /*
        Tables
    */

    @FXML private TableView<StringPropertyStaff> staffTable;
    @FXML private TableColumn<StringPropertyStaff, String> staffNameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> staffSurnameColumn;

    @FXML private TableView<StringPropertyChild> childTable;
    @FXML private TableColumn<StringPropertyChild, String> nameColumn;
    @FXML private TableColumn<StringPropertyChild, String> surnameColumn;

    /*
        GUI objects
    */

    @FXML private ComboBox giorniComboBox;
    @FXML private ImageView closeImageView;

    /*
        Lists & Utils
    */

    private ObservableList<StringPropertyChild> childObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyStaff> staffObservableList = FXCollections.observableArrayList();
    private final Menu.MenuTypeFlag defaultDay = Menu.MenuTypeFlag.MONDAY;

    /*
        Initialization
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTableAssociations();
        selectionMenuSetup();
        refreshPersonTables(defaultDay);

    }

    /*
        Setup Methods
    */

    private void refreshPersonTables(Menu.MenuTypeFlag menu) {
        refreshChildTable(menu);
        refreshStaffTable(menu);
    }

    private void setTableAssociations() {
        setChildTableAssociations();
        setStaffTableAssociations();
    }

    private void setChildTableAssociations() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    private void setStaffTableAssociations() {
        staffNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        staffSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    private void selectionMenuSetup() {
        loadSelectionMenuItems();
        selectionMenuSetDefaultBehaviour();
    }

    private void selectionMenuSetDefaultBehaviour() {
        giorniComboBox.getSelectionModel().selectFirst();
        giorniComboBox.setOnAction(event -> refreshPersonTables(getSelectedMenu()));
    }

    private void loadSelectionMenuItems() {
        ObservableList al = FXCollections.observableArrayList(Arrays.asList(Menu.MenuTypeFlag.values()));
        giorniComboBox.setItems(al);
    }

    /*
        Refresh Tables
    */

    private void refreshStaffTable(Menu.MenuTypeFlag selectedMenu) {
        staffObservableList.clear();
        if(selectedMenu!= null){
            List<Staff> personArrayList = CLIENT.clientExtractIntolerantStaffToMenu(selectedMenu);
            if(personArrayList!=null){
                for(Staff p : personArrayList){
                    staffObservableList.add(new StringPropertyStaff(p));
                }
            }
            staffTable.setItems(staffObservableList);
        }
    }

    private void refreshChildTable(Menu.MenuTypeFlag selectedMenu){
        childObservableList.clear();
        if(selectedMenu!= null){
            List<Child> personArrayList = CLIENT.clientExtractIntolerantChildrenToMenu(selectedMenu);
            if(personArrayList!=null){
                for(Child p : personArrayList){
                    childObservableList.add(new StringPropertyChild(p));
                }
            }
            childTable.setItems(childObservableList);
        }
    }

    /*
        GUI getters
    */

    public Menu.MenuTypeFlag getSelectedMenu() {
        return Menu.MenuTypeFlag.valueOf(giorniComboBox.getValue().toString());
    }

    @FXML public void closeCurrentPopup() {
        close(closeImageView);
    }

}
