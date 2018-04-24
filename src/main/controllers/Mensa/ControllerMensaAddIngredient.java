package main.controllers.Mensa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import main.controllers.AbstractController;
import main.controllers.AbstractPopOverController;

public class ControllerMensaAddIngredient extends AbstractPopOverController{

    /*
        GUI nodes
   */

    @FXML private TextField ingredientNameTextField;
    @FXML private ImageView goHomeImageView;
    @FXML private Button saveButton;

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
        close(goHomeImageView);
    }

    /*
        Utils
   */

    private boolean textConstraintsRespected() {
        int errors = 0;
        errors+= textFieldConstraintsRespected(ingredientNameTextField) ? 0:1;
        return errors == 0;

    }

}
