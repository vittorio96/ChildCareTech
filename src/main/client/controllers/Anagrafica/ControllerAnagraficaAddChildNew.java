package main.client.controllers.Anagrafica;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import main.entities.normal_entities.Anagrafica.Child;
import main.entities.normal_entities.Anagrafica.Contact;
import main.entities.string_property_entities.Anagrafica.StringPropertyContact;
import main.client.controllers.AbstractController;
import main.client.controllers.PopupController;
import main.client.qrReader.QRGenerator;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAnagraficaAddChildNew extends AbstractController implements Initializable {


    /*******     Gui elements    *******/


    /*              Child                */

    @FXML private TextField   nameTextField;
    @FXML private TextField   surnameTextField;
    @FXML private TextField   codFisTextField;
    @FXML private DatePicker  birthdayDatePicker;

    /*             Parents               */

    @FXML private TextField   searchFieldAvailableParents;

    @FXML private TableView  <StringPropertyContact>          availableParentsTable;
    @FXML private TableColumn<StringPropertyContact, String>  availableParentsCodFisColumn;
    @FXML private TableColumn<StringPropertyContact, String>  availableParentsNameColumn;
    @FXML private TableColumn<StringPropertyContact, String>  availableParentsSurnameColumn;

    @FXML private TableView  <StringPropertyContact>          chosenParentsTable;
    @FXML private TableColumn<StringPropertyContact, String>  chosenParentsCodFisColumn;
    @FXML private TableColumn<StringPropertyContact, String>  chosenParentsNameColumn;
    @FXML private TableColumn<StringPropertyContact, String>  chosenParentsSurnameColumn;

    /*          Parents Lists            */

    private List<StringPropertyContact>           chosenParentsNormalList    = new ArrayList<StringPropertyContact>();
    private List<StringPropertyContact>           availableParentsNormalList = new ArrayList<StringPropertyContact>();
    private ObservableList<StringPropertyContact> availableParentsObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyContact> chosenParentsObservableList    = FXCollections.observableArrayList();
    private SortedList<StringPropertyContact>     sortedParentsData;
    private FilteredList<StringPropertyContact>   filteredParentsData;


    /*             Doctors               */

    @FXML private TextField   searchFieldAvailableDoctors;

    @FXML private TableView  <StringPropertyContact>          availableDoctorsTable;
    @FXML private TableColumn<StringPropertyContact, String>  availableDoctorsCodFisColumn;
    @FXML private TableColumn<StringPropertyContact, String>  availableDoctorsNameColumn;
    @FXML private TableColumn<StringPropertyContact, String>  availableDoctorsSurnameColumn;

    @FXML private TableView  <StringPropertyContact>          chosenDoctorTable;
    @FXML private TableColumn<StringPropertyContact, String>  chosenDoctorCodFisColumn;
    @FXML private TableColumn<StringPropertyContact, String>  chosenDoctorNameColumn;
    @FXML private TableColumn<StringPropertyContact, String>  chosenDoctorSurnameColumn;

    /*          Doctors Lists            */

    private List<StringPropertyContact>           chosenDoctorsNormalList    = new ArrayList<StringPropertyContact>();
    private List<StringPropertyContact>           availableDoctorsNormalList = new ArrayList<StringPropertyContact>();
    private ObservableList<StringPropertyContact> availableDoctorsObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyContact> chosenDoctorsObservableList    = FXCollections.observableArrayList();
    private SortedList<StringPropertyContact>     sortedDoctorsData;
    private FilteredList<StringPropertyContact>   filteredDoctorsData;

    /*         Buttons and Images        */

    @FXML private ImageView   addParentButton;
    @FXML private ImageView   addDoctorButton;
    @FXML private Button      nextButton;
    @FXML private ImageView   goHomeImageView;

    private List<Contact> tempList = new ArrayList<Contact>();


    /*******     Initialization    *******/


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
        AbstractController.setCurrentController(this);
        setToCapsTextFieldTransform();
        AbstractController.setCurrentController(this);
        setTextFieldAutocaps(codFisTextField);
        datePickerStandardInitialize(birthdayDatePicker);
        setColumnAssociations();
        refreshLists();
        setFilters();
    }

    protected void setColumnAssociations() {
        setParentsColumnAssociations();
        setDoctorsColumnAssociations();
    }

    private void setParentsColumnAssociations() {
        availableParentsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toChosenParents(newValue));
        availableParentsCodFisColumn.setCellValueFactory(cellData -> cellData.getValue().codiceFiscaleProperty());
        availableParentsNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        availableParentsSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

        chosenParentsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toAvailableParents(newValue));
        chosenParentsCodFisColumn.setCellValueFactory(cellData -> cellData.getValue().codiceFiscaleProperty());
        chosenParentsNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        chosenParentsSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    private void setDoctorsColumnAssociations() {
        availableDoctorsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toChosenDoctor(newValue));
        availableDoctorsCodFisColumn.setCellValueFactory(cellData -> cellData.getValue().codiceFiscaleProperty());
        availableDoctorsNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        availableDoctorsSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

        chosenDoctorTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toAvailableDoctors(newValue));
        chosenDoctorCodFisColumn.setCellValueFactory(cellData -> cellData.getValue().codiceFiscaleProperty());
        chosenDoctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        chosenDoctorSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());
    }

    private void refreshLists() {
        refreshParentsLists();
        refreshDoctorsLists();
    }

    private void refreshParentsLists() {
        parentsListClear();

        tempList= CLIENT.clientExtractContactsFromDb();
        if(tempList!=null)
            for(Contact c: tempList){
                if(c.getTipo().equals(Integer.toString(Contact.ContactTypeFlag.GENITORE.getOrdernum())))
                    availableParentsNormalList.add(new StringPropertyContact(c));
            }
        availableParentsObservableList.addAll(availableParentsNormalList);
    }

    private void refreshDoctorsLists() {
        doctorsListClear();

        tempList= CLIENT.clientExtractContactsFromDb();
        if(tempList!=null)
            for(Contact c: tempList){
                if(c.getTipo().equals(Integer.toString(Contact.ContactTypeFlag.PEDIATRA.getOrdernum())))
                    availableDoctorsNormalList.add(new StringPropertyContact(c));
            }
        availableDoctorsObservableList.addAll(availableDoctorsNormalList);
    }

    private void parentsListClear() {
        chosenParentsObservableList.clear();
        availableParentsObservableList.clear();
        chosenParentsNormalList.clear();
        availableParentsNormalList.clear();
    }

    private void doctorsListClear() {
        chosenDoctorsObservableList.clear();
        availableDoctorsObservableList.clear();
        chosenDoctorsNormalList.clear();
        availableDoctorsNormalList.clear();
    }

    private void setFilters() {
        setParentsFilter();
        setDoctorsFilter();
    }

    private void setItems() {
       setParentsItems();
       setDoctorsItems();
    }

    private void setParentsItems() {
        chosenParentsObservableList.clear();
        chosenParentsObservableList.addAll(chosenParentsNormalList);
        chosenParentsTable.setItems(chosenParentsObservableList);
        availableParentsObservableList.clear();
        availableParentsObservableList.addAll(availableParentsNormalList);
        availableParentsTable.setItems(availableParentsObservableList);
    }

    private void setDoctorsItems() {
        chosenDoctorsObservableList.clear();
        chosenDoctorsObservableList.addAll(chosenDoctorsNormalList);
        chosenDoctorTable.setItems(chosenDoctorsObservableList);
        availableDoctorsObservableList.clear();
        availableDoctorsObservableList.addAll(availableDoctorsNormalList);
        availableDoctorsTable.setItems(availableDoctorsObservableList);
    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    private void setToCapsTextFieldTransform() {
        setTextFieldAutocaps(codFisTextField);
    }


    /*******      Gui Methods      *******/


    @FXML
    public void handleNextButtonAction() {
        if(constraintsRespected()){
            Child child = getNewChild();
            boolean success = CLIENT.clientInsertIntoDb(child);
            if (!success) {
                createErrorPopup("Errore", "Verifica i dati inseriti");
            } else {
                //Genera QR image
                QRGenerator.GenerateQR(child);
                createSuccessPopup();
                controllerType.close(goHomeImageView);
            }

        }
        else{
            createErrorPopup("Errore", "Hai lasciato campi vuoti o con formato sbagliato oppure " +
                    "non hai selezionato un genitore e un pediatra");
        }

    }

    @FXML
    public void addNewDoctor() throws IOException {
        openPopup(addDoctorButton,"../../resources/fxml/anagrafica_addContact.fxml",800,450);
        refreshDoctorsLists();
        setDoctorsFilter();
    }

    @FXML
    public void addNewParent() throws IOException {
        openPopup(addParentButton,"../../resources/fxml/anagrafica_addContact.fxml",800,450);
        refreshParentsLists();
        setParentsFilter();
    }


    @FXML
    public void handleGoHomebutton() {
        if(createConfirmationDialog("Sei sicuro di voler chiudere?",
                "I dati inseriti non verranno salvati."))
            controllerType.close(goHomeImageView);
    }


    /*******      Validation       *******/



    private boolean constraintsRespected() {
        return ( childErrors() + parentsErrors() + doctorErrors() ) == 0;
    }

    private int childErrors() {
        final int CODFISLENGTH = 16;
        int childErrors = 0;
        childErrors+= textFieldLengthRespected(codFisTextField, CODFISLENGTH) ? 0:1;
        childErrors+= textFieldConstraintsRespected(nameTextField) ? 0:1;
        childErrors+= textFieldConstraintsRespected(surnameTextField) ? 0:1;
        childErrors+= datePickerIsDateSelected(birthdayDatePicker) ? 0:1;
        return childErrors;
    }

    private int doctorErrors() {
        return chosenDoctorsNormalList.size()<1 ? 1: 0;
    }

    private int parentsErrors() {
        return chosenParentsNormalList.size()<1 ? 1: 0;
    }


    /*******    Support Methods    *******/


    public Child getNewChild() {
        String selectedDoctorCodF  = chosenDoctorsNormalList.get(0).getCodiceFiscale();
        String selectedParent1CodF = chosenParentsNormalList.get(0).getCodiceFiscale();
        String selectedParent2CodF = null;
        if(chosenParentsNormalList.size()==2) selectedParent2CodF = chosenParentsNormalList.get(1).getCodiceFiscale();
        String nome = nameTextField.getText();
        String cognome = surnameTextField.getText();
        String codFis = codFisTextField.getText();
        String dataN = birthdayDatePicker.getValue().format(DateTimeFormatter.ofPattern(DBDATEPATTERN));
        return new Child(nome, cognome, codFis, dataN, selectedDoctorCodF,
                selectedParent1CodF, selectedParent2CodF);
    }

    private void setParentsFilter() {
        filteredParentsData = new FilteredList<>(availableParentsObservableList, p -> true);
        //Set the filter
        searchFieldAvailableParents.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParentsData.setPredicate(parent -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (parent.getCodiceFiscale().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Code.
                } else if (parent.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (parent.getCognome().toLowerCase().contains(lowerCaseFilter)){
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        sortedParentsData = new SortedList<>(filteredParentsData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedParentsData.comparatorProperty().bind(availableParentsTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        availableParentsTable.setItems(sortedParentsData);
    }

    private void setDoctorsFilter() {
        filteredDoctorsData = new FilteredList<>(availableDoctorsObservableList, p -> true);
        //Set the filter
        searchFieldAvailableDoctors.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDoctorsData.setPredicate(doctor -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (doctor.getCodiceFiscale().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Code.
                } else if (doctor.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (doctor.getCognome().toLowerCase().contains(lowerCaseFilter)){
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        sortedDoctorsData = new SortedList<>(filteredDoctorsData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedDoctorsData.comparatorProperty().bind(availableDoctorsTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        availableDoctorsTable.setItems(sortedDoctorsData);
    }

    private void toAvailableParents(StringPropertyContact selected) {

        Platform.runLater(new Runnable() {
            @Override public void run() {
                if(selected!=null){
                    availableParentsNormalList.add(selected);
                    int initialsize = chosenParentsNormalList.size();
                    boolean done = false;
                    for(int i=0; i < initialsize ;i++){
                        if(chosenParentsNormalList.get(i).getCodiceFiscale().equals(selected.getCodiceFiscale())) {
                            chosenParentsNormalList.remove(i);
                            done = true;
                        }
                        if (done==true) break;
                    }
                    setParentsItems();
                    setParentsFilter();
                    parentsSort();
                }}});

    }

    private void toChosenParents(StringPropertyContact selected) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                if(selected!=null){
                    if(chosenParentsNormalList.size()<2) {
                        chosenParentsNormalList.add(selected);
                        int initialsize = availableParentsNormalList.size();
                        boolean done = false;
                        for (int i = 0; i < initialsize; i++) {
                            if (availableParentsNormalList.get(i).getCodiceFiscale().equals(selected.getCodiceFiscale())) {
                                availableParentsNormalList.remove(i);
                                done = true;
                            }
                            if (done == true) break;
                        }
                        setParentsItems();
                        setParentsFilter();
                    } else{
                        createErrorPopup("Errore", "Non puoi selezionare più di 2 genitori");
                    }

                }
            }});
    }

    private void parentsSort() {
        availableParentsTable.sort();
    }

    private void toAvailableDoctors(StringPropertyContact selected) {

        Platform.runLater(new Runnable() {
            @Override public void run() {
                if(selected!=null){
                    availableDoctorsNormalList.add(selected);
                    int initialsize = chosenDoctorsNormalList.size();
                    boolean done = false;
                    for(int i=0; i < initialsize ;i++){
                        if(chosenDoctorsNormalList.get(i).getCodiceFiscale().equals(selected.getCodiceFiscale())) {
                            chosenDoctorsNormalList.remove(i);
                            done = true;
                        }
                        if (done==true) break;
                    }
                    setDoctorsItems();
                    setDoctorsFilter();
                    doctorsSort();
                }}});

    }

    private void toChosenDoctor(StringPropertyContact selected) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                if(selected!=null){
                    if(chosenDoctorsNormalList.size()<1) {
                    chosenDoctorsNormalList.add(selected);
                    int initialsize = availableDoctorsNormalList.size();
                    boolean done = false;
                    for(int i=0; i < initialsize ;i++){
                        if(availableDoctorsNormalList.get(i).getCodiceFiscale().equals(selected.getCodiceFiscale())) {
                            availableDoctorsNormalList.remove(i);
                            done = true;
                        }
                        if (done==true) break;
                    }
                    setDoctorsItems();
                    setDoctorsFilter();
                    }else{
                        createErrorPopup("Errore", "Non puoi selezionare più di 1 pediatra");
                    }

                }
            }});
    }

    private void doctorsSort() {
        availableDoctorsTable.sort();
    }

    @Override
    public void refresh() {

    }
}
