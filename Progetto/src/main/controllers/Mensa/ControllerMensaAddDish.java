package main.controllers.Mensa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.NormalClasses.Mensa.Dish;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.StringPropertyClasses.Mensa.StringPropertyIngredient;
import main.controllers.AbstractController;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerMensaAddDish extends AbstractController implements Initializable {



    /*
        Stuff
    */
    @FXML Button saveButton;

    @FXML TextField dishNameTextField;
    @FXML TableView<StringPropertyIngredient> availableIngredientsTable;
    @FXML TableView<StringPropertyIngredient> ingredientsOnDishTable;
    @FXML TableColumn <StringPropertyIngredient,String> availableIngredients;
    @FXML TableColumn <StringPropertyIngredient,String> ingredientsOnDish;
    private ObservableList<StringPropertyIngredient> availableIngredientsList = FXCollections.observableArrayList();
    private ObservableList<StringPropertyIngredient> ingredientsOnDishList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        availableIngredients.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        ingredientsOnDish.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());

        availableIngredientsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toDish(newValue));
        ingredientsOnDishTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> toAvailableIngredients(newValue));
    }

    private void toAvailableIngredients(StringPropertyIngredient newValue) {
        availableIngredientsList.add(newValue);
        ingredientsOnDishList.remove(newValue);
    }

    private void toDish(StringPropertyIngredient newValue) {
        ingredientsOnDishList.add(newValue);
        availableIngredientsList.remove(newValue);
    }

    public void handleSaveButton(ActionEvent event) {
        if(textConstraintsRespected()){

        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }
        //TODO add Dish
    }

    public void addNewIngredient(MouseEvent mouseEvent) throws IOException {
        changeSceneInPopup(saveButton,"../../resources/fxml/mensa_addIngredient.fxml",380,380);
    }

    @FXML private void handleGoHomebutton(){
        //use a generic button
        PopOver stage = (PopOver) saveButton.getScene().getWindow();
        //Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.hide();
    }

    private boolean textConstraintsRespected() {
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(dishNameTextField.getText().length() == 0){
            dishNameTextField.setStyle(errorCss);
            errors++;
        }else {
            dishNameTextField.setStyle(normalCss);
        }

        if(ingredientsOnDishList.size()==0){
            errors++;
        }

        return errors == 0;

    }


}
