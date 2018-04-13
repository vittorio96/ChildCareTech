package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.NormalClasses.Anagrafica.Child;
import main.NormalClasses.Anagrafica.Staff;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.qrReader.QRGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAccessiGeneraQR extends AbstractController implements Initializable {
    //main list
    private ObservableList<StringPropertyChild> childObservableList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyStaff> staffObservableList = FXCollections.observableArrayList();

    //Tabella
    @FXML private TableView<StringPropertyChild> childTable;
    @FXML private TableColumn<StringPropertyChild, String> nameColumn;
    @FXML private TableColumn<StringPropertyChild, String> codeColumn;
    @FXML private TableColumn<StringPropertyChild, String> surnameColumn;

    @FXML private TableView<StringPropertyStaff> staffTable;
    @FXML private TableColumn<StringPropertyStaff, String> staffCodeColumn;
    @FXML private TableColumn<StringPropertyStaff, String> staffNameColumn;
    @FXML private TableColumn<StringPropertyStaff, String> staffSurnameColumn;

    @FXML private Button exitButton;
    @FXML private TextField codFisTextField;
    @FXML private ImageView qrDisplay;

    //Buttons
    @FXML private ImageView goHomeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codRProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

        staffCodeColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        staffNameColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        staffSurnameColumn.setCellValueFactory(cellData -> cellData.getValue().cognomeProperty());

        showChildDetails(null);
        showStaffDetails(null);

        childTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showChildDetails(newValue));

        staffTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showStaffDetails(newValue));

        List<Child> childArrayList = null;
        childArrayList = CLIENT.clientExtractChildrenFromDb();
        for(Child c : childArrayList){
            childObservableList.add(new StringPropertyChild(c));
        }
        childTable.setItems(childObservableList);

        List<Staff> staffArrayList = CLIENT.clientExtractStaffFromDb();
        for(Staff s : staffArrayList){
            staffObservableList.add(new StringPropertyStaff(s));
        }

        staffTable.setItems(staffObservableList);

    }

    @FXML
    private void showChildDetails(StringPropertyChild spchild) {
        if (spchild != null) {
            Child child = new Child(spchild);
            codFisTextField.setText(child.getCodiceFiscale());
            QRGenerator.GenerateQR(child);
            try{
                FileInputStream fileInputStream = new FileInputStream(new File("/Users/rafaelmosca/ChildCareTech/Progetto/src/main/resources/QRImages/"+child.getClass().getSimpleName()+"_"+child.getCodiceFiscale()+".png"));
                Image qr =  new Image(fileInputStream);
                qrDisplay.setImage(qr);
            }catch (Exception e){
                System.out.println("errore");
            }
        }
    }

    @FXML
    private void showStaffDetails(StringPropertyStaff spstaff) {

        if (spstaff != null) {
            Staff staff = new Staff(spstaff);
            codFisTextField.setText(staff.getCodiceFiscale());
            QRGenerator.GenerateQR(staff);
            try {
                FileInputStream fileInputStream = new FileInputStream(new File("/Users/rafaelmosca/ChildCareTech/Progetto/src/main/resources/QRImages/"+staff.getClass().getSimpleName()+"_"+staff.getCodiceFiscale()+".png"));
                Image qr =  new Image(fileInputStream);
                qrDisplay.setImage(qr);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("errore");
            }
        } else {
            codFisTextField.setText("");
        }
    }


    @FXML private void handleGoHomebutton(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

}
