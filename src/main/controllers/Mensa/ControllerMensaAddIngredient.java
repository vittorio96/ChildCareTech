package main.controllers.Mensa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.NormalClasses.Gite.Bus;
import main.controllers.AbstractController;


import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMensaAddIngredient extends AbstractController implements Initializable {
    @FXML TextField ingredientNameTextField;
    @FXML Button saveButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void handleSaveButton(ActionEvent event) {
        if(textConstraintsRespected()){
            boolean success = CLIENT.clientInsertIngredientIntoDb(ingredientNameTextField.getText().toUpperCase());
            try {
                if (!success) {
                    createErrorPopup("Errore","Non è stato possibile aggiungere l'ingrediente");
                } else {
                    createSuccessPopup();
                    handleGoHomebutton();
                    ControllerMensaAddDish.setNewIng(true);
                    ControllerMensaAddDish.setLastIng(ingredientNameTextField.getText().toUpperCase());
                }
            } catch (Exception e){
                //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
            }
        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }
    }

    @FXML private void handleGoHomebutton(){
        //use a generic button
        hidePopOver(saveButton);
    }

    private boolean textConstraintsRespected() {
        final int LICENSEPLATELENGTH = 7;
        String errorCss = "-fx-text-box-border: red ; -fx-focus-color: red ;";
        String normalCss = "-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";
        int errors = 0;

        if(ingredientNameTextField.getText().length() == 0){
            ingredientNameTextField.setStyle(errorCss);
            errors++;
        }else {
            ingredientNameTextField.setStyle(normalCss);
        }



        return errors == 0;

    }
}
