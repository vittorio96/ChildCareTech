package main.client.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import main.entities.normal_entities.Anagrafica.Contact;
import main.entities.normal_entities.Anagrafica.Person;
import main.entities.normal_entities.Anagrafica.Supplier;
import main.entities.string_property_entities.Anagrafica.StringPropertyContact;
import main.entities.string_property_entities.Anagrafica.StringPropertySupplier;
import main.client.controllers.AbstractController;
import main.client.controllers.PopupController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaManageContact extends AbstractController implements Initializable {

    /*
        Gui elements
    */

    //main lists
    private ObservableList<StringPropertyContact>   parentObservableList = FXCollections.observableArrayList();
    private SortedList<StringPropertyContact>       parentSortedData;
    private FilteredList<StringPropertyContact>     parentFilteredData;
    private ObservableList<StringPropertyContact>   doctorObservableList = FXCollections.observableArrayList();
    private SortedList<StringPropertyContact>       doctorSortedData;
    private FilteredList<StringPropertyContact>     doctorFilteredData;
    private ObservableList<StringPropertySupplier>  supplierObservableList = FXCollections.observableArrayList();
    private SortedList<StringPropertySupplier>      supplierSortedData;
    private FilteredList<StringPropertySupplier>    supplierFilteredData;

    //Utilities
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

    @FXML private TextField pivaTextField;
    @FXML private TextField supplierNameTextField;
    @FXML private TextField supplierEmailTextField;
    @FXML private TextField supplierCellphoneTextField;

    //Ricerca
    @FXML private TextField searchTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        setColumnAssociations();
        setEventListeners();
        populateTables();
        setFilters();
    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    protected void setEventListeners() {
        showParentDetails(null);
        showDoctorDetails(null);
        showSupplierDetails(null);

        parentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showParentDetails(newValue));
        doctorTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDoctorDetails(newValue));
        supplierTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSupplierDetails(newValue));

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                lastSelection = nv.getText().toUpperCase();
            }
        });
    }


    protected void setColumnAssociations() {
        parentNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        parentSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        doctorSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        supplierNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeFProperty());
    }

    private void populateTables() {
        List<Contact> contactArrayList = CLIENT.clientExtractContactsFromDb();

        List<Supplier> supplierArrayList = CLIENT.clientExtractSuppliersFromDb();
        if (contactArrayList != null) {
            for (Contact c : contactArrayList) {
                if (c.getTipo().equals(Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum())))
                    parentObservableList.add(new StringPropertyContact(c));
                else if (c.getTipo().equals(Integer.toString(Contact.ContactTypeFlag.PEDIATRA.getOrdernum())))
                    doctorObservableList.add(new StringPropertyContact(c));
            }
        }
        if (supplierArrayList != null) {
            for (Supplier s : supplierArrayList) {
                supplierObservableList.add(new StringPropertySupplier(s));
            }
        }

        doctorTable.setItems(doctorObservableList);
        parentTable.setItems(parentObservableList);
        supplierTable.setItems(supplierObservableList);
    }

    private void setFilters() {
        setParentFilter();
        setDoctorFilter();
        setSupplierFilter();
    }

    /*
        Gui methods
    */

    @FXML
    private void handleParentSaveChangesButton() {
        if (textConstraintsRespectedForParentUpdate()) {
            if (isAParentRowSelected()) {
                boolean success = CLIENT.clientUpdatePersonIntoDb(getNewParent());
                parentCodFisTextField.setStyle("-fx-background-color: #F4F4F4;");
                if (!success) {
                    createErrorPopup("Verifica i dati inseriti", "Contatto già esistente o cellulare già in Db");
                } else {
                    createSuccessPopup();
                }
            }
        } else {
            createFieldErrorPopup();
        }
    }

    @FXML
    private void handleSupplierSaveChangesButton() {
        if (textConstraintsRespectedForSupplierUpdate()) {
            if (isASupplierRowSelected()) {
                boolean success = CLIENT.clientUpdateSupplierIntoDb(getNewSupplier());
                if (!success) {
                    createErrorPopup("Verifica i dati inseriti", "Contatto già esistente o cellulare già in Db");
                } else {
                    createSuccessPopup();
                }
            }
        } else {
            createFieldErrorPopup();
        }
    }

    @FXML
    private void handleDoctorSaveChangesButton() {
        if (textConstraintsRespectedForDoctorUpdate()) {
            if (isADoctorRowSelected()) {
                boolean success = CLIENT.clientUpdatePersonIntoDb(getNewDoctor());
                doctorCodFisTextField.setStyle("-fx-background-color: #F4F4F4;");
                if (!success) {
                    createErrorPopup("Verifica i dati inseriti", "Contatto già esistente o cellulare già in Db");
                } else {
                    createSuccessPopup();
                }
            }
        } else {
            createFieldErrorPopup();
        }
    }

    @FXML
    private void handleSupplierDeleteButton() {
        String NAME = "fornitore";
        if (isASupplierRowSelected()) {
            if (createDeleteConfirmationDialog()) {
                StringPropertySupplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb(Supplier.class.getSimpleName(), selectedSupplier.getpIva());
                if (success) {
                    supplierObservableList.remove(selectedSupplier);
                    createSuccessPopup();
                } else {
                    createUnableToDeletePopup(NAME);
                }
            }
        } else {
            createPleaseSelectRowPopup(NAME);
        }
    }

    @FXML
    private void handleDoctorDeleteButton() {
        String NAME = "pediatra";
        if (isADoctorRowSelected()) {
            if (createDeleteConfirmationDialog()) {
                StringPropertyContact selectedContact = doctorTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb(Contact.class.getSimpleName(), selectedContact.getCodiceFiscale());
                if (success) {
                    doctorObservableList.remove(selectedContact);
                    createSuccessPopup();
                } else {
                    createUnableToDeletePopup(NAME);
                }
            }
        } else {
            createPleaseSelectRowPopup(NAME);
        }
    }

    @FXML
    private void handleParentDeleteButton() {
        String NAME = "genitore";
        if (isAParentRowSelected()) {
            if (createDeleteConfirmationDialog()) {
                StringPropertyContact selectedContact = parentTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb(Contact.class.getSimpleName(), selectedContact.getCodiceFiscale());
                if (success) {
                    parentObservableList.remove(selectedContact);
                    createSuccessPopup();
                } else {
                    createUnableToDeletePopup(NAME);
                }
            }
        } else {
            createPleaseSelectRowPopup(NAME);
        }
    }

    /*
        Gui methods
    */

    @FXML
    private void showParentDetails(StringPropertyContact parent) {
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

    @FXML
    private void showDoctorDetails(StringPropertyContact doctor) {
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

    @FXML
    private void showSupplierDetails(StringPropertySupplier supplier) {
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

    /*
        Gui supporting methods
    */

    private boolean textConstraintsRespectedForParentUpdate() {
        int parentErrors = 0;
        parentErrors += textFieldConstraintsRespected(parentNameTextField) ? 0 : 1;
        parentErrors += textFieldConstraintsRespected(parentSurnameTextField) ? 0 : 1;
        parentErrors += textFieldConstraintsRespected(parentCellphoneTextField) ? 0 : 1;
        return parentErrors == 0;
    }

    private boolean textConstraintsRespectedForDoctorUpdate() {
        int doctorErrors = 0;
        doctorErrors += textFieldConstraintsRespected(doctorNameTextField) ? 0 : 1;
        doctorErrors += textFieldConstraintsRespected(doctorSurnameTextField) ? 0 : 1;
        doctorErrors += textFieldConstraintsRespected(doctorCellphoneTextField) ? 0 : 1;
        return doctorErrors == 0;
    }

    private boolean textConstraintsRespectedForSupplierUpdate() {
        int supplierErrors = 0;
        supplierErrors += textFieldConstraintsRespected(supplierNameTextField) ? 0 : 1;
        supplierErrors += textFieldConstraintsRespected(supplierEmailTextField) ? 0 : 1;
        supplierErrors += textFieldConstraintsRespected(supplierCellphoneTextField) ? 0 : 1;
        return supplierErrors == 0;
    }

    private Person getNewDoctor() {
        StringPropertyContact selectedContact = doctorTable.getSelectionModel().getSelectedItem();
        selectedContact.setNome(doctorNameTextField.getText());
        selectedContact.setCognome(doctorSurnameTextField.getText());
        selectedContact.setCellphone(doctorCellphoneTextField.getText());
        return selectedContact.toPerson();
    }

    private Supplier getNewSupplier() {
        StringPropertySupplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        selectedSupplier.setNomeF(supplierNameTextField.getText());
        selectedSupplier.setTel(supplierCellphoneTextField.getText());
        selectedSupplier.setEmail(supplierEmailTextField.getText());
        return new Supplier(selectedSupplier);
    }

    private Person getNewParent() {
        StringPropertyContact selectedContact = parentTable.getSelectionModel().getSelectedItem();
        selectedContact.setNome(parentNameTextField.getText());
        selectedContact.setCognome(parentSurnameTextField.getText());
        selectedContact.setCellphone(parentCellphoneTextField.getText());
        return selectedContact.toPerson();
    }

    private boolean isASupplierRowSelected() {
        return isRowSelected(supplierTable);
    }

    private boolean isAParentRowSelected() {
        return isRowSelected(parentTable);
    }

    private boolean isADoctorRowSelected() {
        return isRowSelected(doctorTable);
    }

    private boolean isRowSelected(TableView tableView) {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        return selectedIndex >= 0;
    }

    private void setParentFilter() {
        parentFilteredData = new FilteredList<>(parentObservableList, p -> true);
        //Set the filter
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            parentFilteredData.setPredicate(staff -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (staff.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (staff.getCognome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        parentSortedData = new SortedList<>(parentFilteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        parentSortedData.comparatorProperty().bind(parentTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        parentTable.setItems(parentSortedData);
    }

    private void setSupplierFilter() {
        supplierFilteredData = new FilteredList<>(supplierObservableList, p -> true);
        //Set the filter
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            supplierFilteredData.setPredicate(supplier -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getNomeF().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        supplierSortedData = new SortedList<>(supplierFilteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        supplierSortedData.comparatorProperty().bind(supplierTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        supplierTable.setItems(supplierSortedData);

    }

    private void setDoctorFilter() {

        doctorFilteredData = new FilteredList<>(doctorObservableList, p -> true);
        //Set the filter
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            doctorFilteredData.setPredicate(doctor -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (doctor.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }else if (doctor.getCognome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        doctorSortedData = new SortedList<>(doctorFilteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        doctorSortedData.comparatorProperty().bind(doctorTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        doctorTable.setItems(doctorSortedData);

    }

    public void handleGoHomebutton() {
        controllerType.close(goHomeImageView);
    }

    @Override
    public void refresh() {

    }
}