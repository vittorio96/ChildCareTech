package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.NormalClasses.Anagrafica.Child;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.controllers.AbstractController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaAddAllergen extends AbstractController implements Initializable {

    //main list
    private ObservableList<StringPropertyChild> childObservableList = FXCollections.observableArrayList();

    //Utilities
    private final String pattern = "dd/MM/yyyy";
    private final String inputPattern = "yyyy-MM-dd";

    //Tabella Bambini
    @FXML private TableView<StringPropertyChild> childTable;
    @FXML private TableColumn<StringPropertyChild, String> nameColumn;
    @FXML private TableColumn<StringPropertyChild, String> codeColumn;
    @FXML private TableColumn<StringPropertyChild, String> surnameColumn;

    //Allergeni
    //@FXML private TableView<StringPropertyAllergen> allergiesTable;


    //Buttons
    @FXML private Button saveChangesButton;
    @FXML private Button goHomeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

        childTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showChildAllergies(newValue));

        List<Child> childArrayList = null;
        childArrayList = CLIENT.clientExtractChildrenFromDb();
        for(Child c : childArrayList){
            childObservableList.add(new StringPropertyChild(c));
        }
        childTable.setItems(childObservableList);


    }

    @FXML
    private void showChildAllergies(StringPropertyChild child) {
        if (child != null) {
            // Fill the tableview with the child allergies by quering the database

        } else {
            // clear the table
            //allergiesTable.setItems(null);
        }
    }


    @FXML private void handleGoHomeButton(){
        closePopup(goHomeButton);
    }

    @FXML private void handleSaveChangesButton(){
        Stage stage = (Stage) saveChangesButton.getScene().getWindow();
        stage.close();
    }
}
