package main.client.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import main.client.Client;
import main.client.User;
import org.controlsfx.control.PopOver;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractController {
    //abstract to avoid instantiation

    protected static Client CLIENT;
    public static AbstractController currentController;
    protected static User loggedUser;
    protected static User.UserTypeFlag userTypeFlag;
    protected ControllerType controllerType;

    protected static final String ERRORCSS = "-fx-text-box-border: red ; -fx-focus-color: red ;";
    protected static final String NORMALCSS ="-fx-text-box-border: lightgray ; -fx-focus-color: #81CEE9;";
    protected static final String TRANSPARENTCSS = "-fx-border-color: transparent ; -fx-focus-color: transparent ;" ;
    protected static final String SERRORCSS = "-fx-border-color: red ;-fx-text-box-border: red ; -fx-focus-color: red ;";

    protected static final String USERDATEPATTERN = "dd/MM/yyyy";
    protected static final String DBDATEPATTERN = "yyyy-MM-dd";
    protected static final String PROMPTDATEPATTERN ="gg/mm/aaaa";

    public static void registerClient(Client c){
        CLIENT = c;
        System.out.println(CLIENT);
    }

    public static void setCurrentController(AbstractController controller){
        currentController = controller;
    }

    protected abstract void setControllerType();

    public void changeScene(Button button, String fxmlPath) throws IOException {

        Stage stage;
        Parent root;
        stage=(Stage) button.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root, 800, 450);
        stage.setResizable(false);
        root.requestFocus();
        stage.setScene(scene);
        stage.show();

    }

    public void changeSceneInPopup(Button button, String fxmlPath, int dimW, int dimH) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage;
        primaryStage =(Stage) button.getScene().getWindow();
        dialogStage.initOwner(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root,dimW,dimH);
        dialogStage.setScene(scene);
        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }

    public void openPopup(Node node, String fxmlPath, int dimW, int dimH) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage;
        primaryStage =(Stage) node.getScene().getWindow();
        dialogStage.initOwner(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root,dimW,dimH);
        dialogStage.setScene(scene);
        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }

    public void createFieldErrorPopup(){
        createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
    }

    public void createErrorPopup(String headerText, String contentText){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("Errore");
        alert2.setHeaderText(headerText);
        alert2.setContentText(contentText);
        alert2.showAndWait();
    }

    public void createGenericErrorPopup(){
        createErrorPopup("Errore", "Qualcosa è andato storto, riprova più tardi");
    }



    public void createSuccessPopup(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
        alert2.setGraphic(new javafx.scene.image.ImageView(this.getClass().getResource("/main/client/resources/images/checkmark.png").toString()));
        alert2.setTitle("Successo");
        alert2.setHeaderText("Successo!");
        alert2.setContentText("Dati inseriti correttamente nel database.\n");
        alert2.showAndWait();
    }
    public void createCustomSuccessPopup(String header, String content){
        Alert alert2 = new Alert(Alert.AlertType.ERROR); //truly a success
        alert2.setGraphic(new javafx.scene.image.ImageView(this.getClass().getResource("/main/client/resources/images/checkmark.png").toString()));
        alert2.setTitle("Successo");
        alert2.setHeaderText(header);
        alert2.setContentText(content);
        alert2.showAndWait();
    }

    public void createInfoPopup(String contentText){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("Informazioni");
        alert2.setHeaderText("Come utilizzare la finestra");
        alert2.setContentText(contentText);
        alert2.showAndWait();
    }

    public void openPopOver(String fxmlPath, PopOver.ArrowLocation arrowLocation, Node owner) throws IOException {
        PopOver popOver = new PopOver();
        popOver.setArrowLocation(arrowLocation);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.setAnimated(true);
        AnchorPane p = FXMLLoader.load(getClass().getResource(fxmlPath));
        popOver.setContentNode(p);

        popOver.show(owner);
    }

    public PopOver openPopOverWR(String fxmlPath, PopOver.ArrowLocation arrowLocation, Node owner) throws IOException {
        PopOver popOver = new PopOver();
        popOver.setArrowLocation(arrowLocation);
        popOver.setAutoFix(true);
        popOver.setAutoHide(true);
        popOver.setHideOnEscape(true);
        popOver.setDetachable(false);
        popOver.setAnimated(true);
        AnchorPane p = FXMLLoader.load(getClass().getResource(fxmlPath));
        popOver.setContentNode(p);

        popOver.show(owner);
        return popOver;
    }


    public void changeMenu(String fxmlPath, AnchorPane lateralPane) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(fxmlPath));
        lateralPane.getChildren().setAll(anchorPane);
    }

    public boolean createConfirmationDialog(String headerText, String contentText){
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType buttonTypeOne = new ButtonType("No");
        ButtonType buttonTypeTwo = new ButtonType("Si");
        alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        alert2.setHeaderText(headerText);
        alert2.setContentText(contentText);
        alert2.showAndWait();

        return alert2.getResult() == buttonTypeTwo;
    }

    public boolean createDeleteConfirmationDialog(){
        return createConfirmationDialog("Sei sicuro di voler eliminare?",
                "Una volta fatta la cancellazione è impossibile annullarla ");
    }

    public void createUnableToDeletePopup(String what){
        createErrorPopup("ERRORE", "Non è stato possibile cancellare il "+what);
    }


    public void createPleaseSelectRowPopup(String what){
        createErrorPopup("Non si è selezionato un "+ what, "Seleziona un "+what+" dalla tabella");
    }

    public boolean isUserSure(){
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType buttonTypeOne = new ButtonType("No");
        ButtonType buttonTypeTwo = new ButtonType("Si");
        alert2.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        alert2.setHeaderText("Sei sicuro ?");
        alert2.setContentText("Una volta fatta questa azione non si può più tornare indietro");
        alert2.showAndWait();

        return alert2.getResult() == buttonTypeTwo;
    }

    public boolean textFieldConstraintsRespected(TextField textField) {
        handleSQLInjection(textField);
        if(textField.getText().length() == 0){
            textField.setStyle(ERRORCSS);
            return false;
        }else{
            textField.setStyle(NORMALCSS);
            return true;
        }
    }

    public boolean textFieldLengthRespected(TextField textField, Integer size) {
        handleSQLInjection(textField);
        if(textField.getText().length() != size){
            textField.setStyle(ERRORCSS);
            return false;
        }else{
            textField.setStyle(NORMALCSS);
            return true;
        }
    }

    private void handleSQLInjection(TextField textField) {
        textField.setText(textField.getText().replaceAll("'", "’"));
    }

    public boolean datePickerIsDateSelected(DatePicker datePicker) {
        if(datePicker.getValue() == null){
            datePicker.setStyle(SERRORCSS);
            return false;
        }else {
            datePicker.setStyle(TRANSPARENTCSS);
            return true;
        }
    }

    public boolean listSizeConstraintsRespected(List list) {
        if(list.size()<1){
            return false;
        }
        return true;
    }

    public boolean comboBoxConstraintsRespected(ComboBox comboBox){
        if(comboBox.getSelectionModel().isEmpty()){
            comboBox.setStyle(SERRORCSS);
            return false;
        }else {
            comboBox.setStyle(TRANSPARENTCSS);
            return true;
        }
    }

    public boolean choiceBoxConstraintsRespected(ChoiceBox choiceBox){
        if(choiceBox.getSelectionModel().isEmpty()){
            choiceBox.setStyle(SERRORCSS);
            return false;
        }else {
            choiceBox.setStyle(TRANSPARENTCSS);
            return true;
        }
    }

    public void datePickerStandardInitialize(DatePicker datePicker){
        datePicker.setShowWeekNumbers(false);
        datePicker.setPromptText(PROMPTDATEPATTERN);

        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(USERDATEPATTERN);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    public void setTextFieldAutocaps(TextField textField){
        textField.textProperty().addListener((ov, oldValue, newValue) -> {
            textField.setText(newValue.toUpperCase());
        });
    }

    protected void setColumnAssociations() {

    }

    protected void setEventListeners(){

    }

    public abstract void refresh();



}
