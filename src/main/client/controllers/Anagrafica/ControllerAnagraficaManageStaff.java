package main.client.controllers.Anagrafica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import main.entities.normal_entities.Anagrafica.Staff;
import main.entities.string_property_entities.Anagrafica.StringPropertyPerson;
import main.entities.string_property_entities.Anagrafica.StringPropertyStaff;
import main.client.controllers.AbstractController;
import main.client.controllers.PopupController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaManageStaff extends AbstractController implements Initializable {

    //main lists
    private ObservableList<StringPropertyStaff> staffObservableList = FXCollections.observableArrayList();
    private SortedList<StringPropertyStaff> sortedData;
    private FilteredList<StringPropertyStaff> filteredData;

    //Tabella
    @FXML private TableView<StringPropertyStaff> staffTable;
    @FXML private TableColumn<StringPropertyStaff, String> usernameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> nameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> surnameColumn;

    //Staff
    @FXML private TextField usernameTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField codFisTextField;
    @FXML private DatePicker birthdayDatePicker;
    @FXML private TextField searchTextField;


    //Buttons
    @FXML private Button saveChangesButton;
    @FXML private ImageView goHomeImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        setColumnAssociations();
        datePickerStandardInitialize(birthdayDatePicker);
        setEventListeners();
        setFilter();
    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    protected void setEventListeners() {
        showStaffDetails(null);

        staffTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStaffDetails(newValue));
        populateTable();
    }

    protected void setColumnAssociations() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
    }

    private void populateTable() {
        List<Staff> staffArrayList = CLIENT.clientExtractStaffFromDb();
        for(Staff s : staffArrayList){
            staffObservableList.add(new StringPropertyStaff(s));
        }

        staffTable.setItems(staffObservableList);
    }

    @FXML private void showStaffDetails(StringPropertyStaff staff) {
        if (staff != null) {
            // Fill the textfields with info from the child object.
            usernameTextField.setText(staff.getUsername());
            nameTextField.setText(staff.getNome());
            surnameTextField.setText(staff.getCognome());
            codFisTextField.setText(staff.getCodiceFiscale());
            String dateValue = staff.getDataNascita();
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DBDATEPATTERN);
            birthdayDatePicker.setValue(LocalDate.parse(dateValue, DATE_FORMAT));

        } else {
            // Person is null, remove all the text.
            usernameTextField.setText("");
            nameTextField.setText("");
            surnameTextField.setText("");
            codFisTextField.setText("");
        }
    }

    @FXML private void handleGoHomeButton(){
        controllerType.close(saveChangesButton);
    }

    @FXML private void handleDeleteButton(){
        final String WHAT = "utente";
        if (isAStaffRowSelected()) {
            if (createDeleteConfirmationDialog()) {
                StringPropertyStaff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
                boolean success = CLIENT.clientDeleteFromDb(Staff.class.getSimpleName(), selectedStaff.getCodiceFiscale());
                if (success) {
                    staffObservableList.remove(selectedStaff);
                    createSuccessPopup();
                } else { createUnableToDeletePopup(WHAT);}
            }
        }else { createPleaseSelectRowPopup(WHAT); }
    }

    @FXML private void handleSaveChangesButton() {
        if (textConstraintsRespectedForUpdate()) {
            boolean success = CLIENT.clientUpdatePersonIntoDb(getSelectedStaff().toPerson());
            if (!success) {
                createErrorPopup("Verifica i dati inseriti ", "Username già preso");
            } else { createSuccessPopup();}
        } else { createFieldErrorPopup();}
    }

    private boolean textConstraintsRespectedForUpdate() {
        final int CODFISLENGTH = 16;
        int errors = 0;
        errors += textFieldConstraintsRespected(nameTextField) ? 0 : 1;
        errors += textFieldConstraintsRespected(surnameTextField) ? 0 : 1;
        errors += datePickerIsDateSelected(birthdayDatePicker) ? 0 : 1;
        return errors == 0;
    }

    public void handleGoHomebutton() {
        controllerType.close(goHomeImageView);
    }

    public StringPropertyPerson getSelectedStaff() {
        StringPropertyStaff selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        selectedStaff.setNome(nameTextField.getText());
        selectedStaff.setCognome(surnameTextField.getText());
        selectedStaff.setDataNascita(birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN)));
        return selectedStaff;
    }

    private boolean isRowSelected(TableView tableView) {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        return selectedIndex >= 0;
    }

    public boolean isAStaffRowSelected() {
        return isRowSelected(staffTable);
    }

    @Override
    public void refresh() {

    }

    private void setFilter() {
        filteredData = new FilteredList<>(staffObservableList, p -> true);
        //Set the filter
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(staff -> {
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
                }else if (staff.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches code
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(staffTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        staffTable.setItems(sortedData);
    }
}
