package main.controllers.Mensa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.NormalClasses.Gite.Bus;
import main.controllers.AbstractController;


import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMensaAddIngredient extends AbstractController {

    /*
        GUI nodes
   */

    @FXML private TextField ingredientNameTextField;
    @FXML private ImageView goHomeImageView;
    @FXML private Button saveButton;

    /*
        GUI constants
   */

    private final String ERRORCSS = "-fx-text-box-border: red ; -fx-focus-color: red ;";
    private final String NORMALCSS ="-fx-text-box-border: lightgray ; -fx-focus-color: #81cee9;";

    /*
        Methods
   */

    @FXML private void handleSaveButton() {
        if(textConstraintsRespected()){
            boolean success = CLIENT.clientInsertIngredientIntoDb(ingredientNameTextField.getText().toUpperCase());
            try {
                if (!success) {
                    createErrorPopup("Errore","Non è stato possibile aggiungere l'ingrediente");
                } else {
                    createSuccessPopup();
                    handleGoHomebutton();
                }
            } catch (Exception e){
                //do nothing, sometimes images can't be loaded, such behaviour has no impact on the application itself.
            }
        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }
    }

    @FXML private void handleGoHomebutton(){
        closePopup(goHomeImageView);
    }

    /*
        Utils
   */

    private boolean textConstraintsRespected() {
        int errors = 0;
        errors+= textFieldConstraintsRespected(ingredientNameTextField) ? 0:1;
        return errors == 0;

    }

    private boolean textFieldConstraintsRespected(TextField textField) {
        if(textField.getText().length() == 0){
            textField.setStyle(ERRORCSS);
            return false;
        }else{
            textField.setStyle(NORMALCSS);
            return true;
        }
    }
}
