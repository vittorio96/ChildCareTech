package main.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.NormalClasses.Anagrafica.Contact;
import main.NormalClasses.Anagrafica.Supplier;
import main.StringPropertyClasses.Anagrafica.StringPropertyContact;
import main.StringPropertyClasses.Anagrafica.StringPropertySupplier;
import main.controllers.AbstractController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaManageContact extends AbstractController implements Initializable {

    //main list
    private ObservableList<StringPropertyContact> parentObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyContact> doctorObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertySupplier> supplierObservableList = FXCollections.observableArrayList();

    //Utilities
    private final String pattern = "dd/MM/yyyy";
    private final String inputPattern = "yyyy-MM-dd";
    private String NAME = "contatto";
    private String lastSelection;

    //Pane & Utilities
    @FXML private TabPane tabPane;
    @FXML private ImageView goHomeImageView;

    //Tabella Genitore
    @FXML private TableView<StringPropertyContact> parentTable;
    @FXML private TableColumn<StringPropertyContact, String> parentNameColumn;
    @FXML private TableColumn<StringPropertyContact, String> parentSurnameColumn;

    @FXML private TextField parentCodFisTextField;
    @FXML private TextField parentNameTextField;
    @FXML private TextField parentSurnameTextField;
    @FXML private TextField parentCellphoneTextField;

    //Tabella Pediatra
    @FXML private TableView<StringPropertyContact> doctorTable;
    @FXML private TableColumn<StringPropertyContact, String> doctorNameColumn;
    @FXML private TableColumn<StringPropertyContact, String> doctorSurnameColumn;

    @FXML private TextField doctorCodFisTextField;
    @FXML private TextField doctorNameTextField;
    @FXML private TextField doctorSurnameTextField;
    @FXML private TextField doctorCellphoneTextField;

    //Tabella Fornitore
    @FXML private TableView<StringPropertySupplier> supplierTable;
    @FXML private TableColumn<StringPropertySupplier, String> supplierNameColumn;
    //@FXML private TableColumn<StringPropertySupplier, String> supplierSurnameColumn;

    @FXML private TextField pivaTextField;
    @FXML private TextField supplierNameTextField;
    @FXML private TextField supplierEmailTextField;
    @FXML private TextField supplierCellphoneTextField;

    //Buttons
    @FXML private Button parentSaveChangesButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parentNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        parentSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

        doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        doctorSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

        supplierNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeFProperty());

        showParentDetails(null);
        showDoctorDetails(null);
        showSupplierDetails(null);

        parentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showParentDetails(newValue));
        doctorTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDoctorDetails(newValue));
        supplierTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSupplierDetails(newValue));

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
            if(nv!=null){
                lastSelection = nv.getText().toUpperCase();
            }
        });

        List<Contact> contactArrayList = null;
        contactArrayList = CLIENT.clientExtractContactsFromDb();

        List<Supplier> supplierArrayList = null;
        supplierArrayList = CLIENT.clientExtractSuppliersFromDb();
        if(contactArrayList!= null){
            for(Contact c : contactArrayList){

                if(c.getTipo().equals(Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum())))
                    parentObservableList.add(new StringPropertyContact(c));
                else if(c.getTipo().equals(Integer.toString(Contact.ContactTypeFlag.PEDIATRA.getOrdernum())))
                    doctorObservableList.add(new StringPropertyContact(c));
            }
        }
        if(supplierArrayList != null){
            for(Supplier s : supplierArrayList){
                supplierObservableList.add(new StringPropertySupplier(s));
            }
        }

        doctorTable.setItems(doctorObservableList);
        parentTable.setItems(parentObservableList);
        supplierTable.setItems(supplierObservableList);
    }

    //Parent

    @FXML private void showParentDetails(StringPropertyContact parent) {
        if (parent != null) {
            // Fill the textfields with info from the child object.
            parentNameTextField.setText(parent.getNome());
            parentSurnameTextField.setText(parent.getCognome());
            parentCodFisTextField.setText(parent.getCodiceFiscale());
            parentCellphoneTextField.setText(parent.getCellphone());

        } else {
            // Person is null, remove all the text.
            parentNameTextField.setText("");
            parentSurnameTextField.setText("");
            parentCodFisTextField.setText("");
            parentCellphoneTextField.setText("");
        }
    }

    @FXML private void handleParentSaveChangesButton(){
        if(textConstraintsRespectedForParentUpdate()){
            int selectedIndex = parentTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                StringPropertyContact selectedContact = parentTable.getSelectionModel().getSelectedItem();
                selectedContact.setNome(parentNameTextField.getText());
                selectedContact.setCognome(parentSurnameTextField.getText());
                selectedContact.setCellphone(parentCellphoneTextField.getText());
                boolean success = CLIENT.clientUpdatePersonIntoDb(new Contact(selectedContact));
                parentCodFisTextField.setStyle("-fx-background-color: #F4F4F4;");
                if (!success) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Verifica i dati inseriti ");
                    alert.setContentText("Contatto già esistente o cellulare già in Db");
                    alert.showAndWait();
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
                    alert2.setGraphic(new ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
                    alert2.setTitle("Successo");
                    alert2.setHeaderText("Successo! ");
                    alert2.setContentText("Dati aggiornati correttamente nel database.\n");
                    alert2.showAndWait();
                }
            }
        } else{
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore");
            alert2.setHeaderText("Verifica i dati inseriti ");
            alert2.setContentText("Hai lasciato campi vuoti o con un formato sbagliato");
            alert2.showAndWait();
        }
    }

    @FXML private void handleParentDeleteButton(){
        String NAME = "genitore";
        int selectedIndex = parentTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType buttonTypeOne = new ButtonType("No");
            ButtonType buttonTypeTwo = new ButtonType("Si");
            alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            alert2.setHeaderText("Sei sicuro di voler eliminare?");
            alert2.setContentText("Una volta fatta la cancellazione è impossibile annullarla ");
            alert2.showAndWait();

            if (alert2.getResult() == buttonTypeTwo) {

                StringPropertyContact selectedContact = parentTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb("Contact", selectedContact.getCodiceFiscale());
                if (success) {
                    parentObservableList.remove(selectedContact);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Errore");
                    alert.setContentText("Non è stato possibile cancellare il "+NAME);
                    alert.showAndWait();
                }
            }
        }else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText("Non si è selezionato un "+NAME);
            alert.setContentText("Seleziona un "+NAME+" dalla tabella");
            alert.showAndWait();
        }

    }

    private boolean textConstraintsRespectedForParentUpdate() {
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(parentNameTextField.getText().length() == 0){
            parentNameTextField.setStyle(errorCss);
            errors++;
        }else {
            parentNameTextField.setStyle(normalCss);
        }

        if(parentSurnameTextField.getText().length() == 0){
            parentSurnameTextField.setStyle(errorCss);
            errors++;
        }else {
            parentSurnameTextField.setStyle(normalCss);
        }

        if(parentCellphoneTextField.getText().length() == 0){
            parentCellphoneTextField.setStyle(errorCss);
            errors++;
        }else {
            parentCellphoneTextField.setStyle(normalCss);
        }
        return errors == 0;
    }

    //Doctor

    @FXML private void showDoctorDetails(StringPropertyContact doctor) {
        if (doctor != null) {
            // Fill the textfields with info from the child object.
            doctorNameTextField.setText(doctor.getNome());
            doctorSurnameTextField.setText(doctor.getCognome());
            doctorCodFisTextField.setText(doctor.getCodiceFiscale());
            doctorCellphoneTextField.setText(doctor.getCellphone());

        } else {
            // Person is null, remove all the text.
            doctorNameTextField.setText("");
            doctorSurnameTextField.setText("");
            doctorCodFisTextField.setText("");
            doctorCellphoneTextField.setText("");
        }
    }

    @FXML private void handleDoctorSaveChangesButton(){
        if(textConstraintsRespectedForDoctorUpdate()){
            int selectedIndex = doctorTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                StringPropertyContact selectedContact = doctorTable.getSelectionModel().getSelectedItem();
                selectedContact.setNome(doctorNameTextField.getText());
                selectedContact.setCognome(doctorSurnameTextField.getText());
                selectedContact.setCellphone(doctorCellphoneTextField.getText());
                boolean success = CLIENT.clientUpdatePersonIntoDb(new Contact(selectedContact));
                doctorCodFisTextField.setStyle("-fx-background-color: #F4F4F4;");
                if (!success) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Verifica i dati inseriti ");
                    alert.setContentText("Contatto già esistente o cellulare già in Db");
                    alert.showAndWait();
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
                    alert2.setGraphic(new ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
                    alert2.setTitle("Successo");
                    alert2.setHeaderText("Successo! ");
                    alert2.setContentText("Dati aggiornati correttamente nel database.\n");
                    alert2.showAndWait();
                }
            }
        }else{
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore");
            alert2.setHeaderText("Verifica i dati inseriti ");
            alert2.setContentText("Hai lasciato campi vuoti o con un formato sbagliato");
            alert2.showAndWait();
        }
    }

    @FXML private void handleDoctorDeleteButton(){
        String NAME = "pediatra";
        int selectedIndex = doctorTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType buttonTypeOne = new ButtonType("No");
            ButtonType buttonTypeTwo = new ButtonType("Si");
            alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            alert2.setHeaderText("Sei sicuro di voler eliminare?");
            alert2.setContentText("Una volta fatta la cancellazione è impossibile annullarla ");
            alert2.showAndWait();

            if (alert2.getResult() == buttonTypeTwo) {

                StringPropertyContact selectedContact = doctorTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb("Contact", selectedContact.getCodiceFiscale());
                if (success) {
                    doctorObservableList.remove(selectedContact);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Errore");
                    alert.setContentText("Non è stato possibile cancellare il "+NAME);
                    alert.showAndWait();
                }
            }
        }else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText("Non si è selezionato un "+NAME);
            alert.setContentText("Seleziona un "+NAME+" dalla tabella");
            alert.showAndWait();
        }

    }

    private boolean textConstraintsRespectedForDoctorUpdate() {
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(doctorNameTextField.getText().length() == 0){
            doctorNameTextField.setStyle(errorCss);
            errors++;
        }else {
            doctorNameTextField.setStyle(normalCss);
        }

        if(doctorSurnameTextField.getText().length() == 0){
            doctorSurnameTextField.setStyle(errorCss);
            errors++;
        }else {
            doctorSurnameTextField.setStyle(normalCss);
        }

        if(doctorCellphoneTextField.getText().length() == 0){
            doctorCellphoneTextField.setStyle(errorCss);
            errors++;
        }else {
            doctorCellphoneTextField.setStyle(normalCss);
        }
        return errors == 0;
    }

    //Supplier

    @FXML private void showSupplierDetails(StringPropertySupplier supplier) {
        if (supplier != null) {
            // Fill the textfields with info from the child object.
            supplierNameTextField.setText(supplier.getNomeF());
            supplierCellphoneTextField.setText(supplier.getTel());
            pivaTextField.setText(supplier.getpIva());
            supplierEmailTextField.setText(supplier.getEmail());

        } else {
            // Person is null, remove all the text.
            supplierNameTextField.setText("");
            supplierCellphoneTextField.setText("");
            pivaTextField.setText("");
            supplierEmailTextField.setText("");
        }
    }

    @FXML private void handleSupplierSaveChangesButton(){
        if(textConstraintsRespectedForSupplierUpdate()){
            int selectedIndex = supplierTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                StringPropertySupplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
                selectedSupplier.setNomeF(supplierNameTextField.getText());
                selectedSupplier.setTel(supplierCellphoneTextField.getText());
                selectedSupplier.setEmail(supplierEmailTextField.getText());
                boolean success = CLIENT.clientUpdateSupplierIntoDb(new Supplier(selectedSupplier));
                if (!success) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Verifica i dati inseriti ");
                    alert.setContentText("Contatto già esistente o cellulare già in Db");
                    alert.showAndWait();
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
                    alert2.setGraphic(new ImageView(this.getClass().getResource("/main/resources/images/checkmark.png").toString()));
                    alert2.setTitle("Successo");
                    alert2.setHeaderText("Successo! ");
                    alert2.setContentText("Dati aggiornati correttamente nel database.\n");
                    alert2.showAndWait();
                }
            }
        }else{
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Errore");
            alert2.setHeaderText("Verifica i dati inseriti ");
            alert2.setContentText("Hai lasciato campi vuoti o con un formato sbagliato");
            alert2.showAndWait();
        }
    }

    @FXML private void handleSupplierDeleteButton(){
        String NAME = "fornitore";
        int selectedIndex = supplierTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            ButtonType buttonTypeOne = new ButtonType("No");
            ButtonType buttonTypeTwo = new ButtonType("Si");
            alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            alert2.setHeaderText("Sei sicuro di voler eliminare?");
            alert2.setContentText("Una volta fatta la cancellazione è impossibile annullarla ");
            alert2.showAndWait();

            if (alert2.getResult() == buttonTypeTwo) {

                StringPropertySupplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb("Supplier", selectedSupplier.getpIva());
                if (success) {
                    supplierObservableList.remove(selectedSupplier);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getScene().getStylesheets().add("/main/resources/CSS/dialogAlertError.css");
                    alert.setTitle("Errore");
                    alert.setHeaderText("Errore");
                    alert.setContentText("Non è stato possibile cancellare il "+NAME);
                    alert.showAndWait();
                }
            }
        }else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERRORE");
            alert.setHeaderText("Non si è selezionato un "+NAME);
            alert.setContentText("Seleziona un "+NAME+" dalla tabella");
            alert.showAndWait();
        }

    }

    private boolean textConstraintsRespectedForSupplierUpdate() {
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(supplierNameTextField.getText().length() == 0){
            supplierNameTextField.setStyle(errorCss);
            errors++;
        }else {
            supplierNameTextField.setStyle(normalCss);
        }

        if(supplierCellphoneTextField.getText().length() == 0){
            supplierCellphoneTextField.setStyle(errorCss);
            errors++;
        }else {
            supplierCellphoneTextField.setStyle(normalCss);
        }

        if(supplierEmailTextField.getText().length() == 0){
            supplierEmailTextField.setStyle(errorCss);
            errors++;
        }else {
            supplierEmailTextField.setStyle(normalCss);
        }
        return errors == 0;
    }

    public void handleGoHomebutton() {
        closePopup(goHomeImageView);
    }

}

