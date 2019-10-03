package main.client.controllers.Mensa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import main.client.controllers.AbstractController;
import main.client.controllers.PopupController;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMensaAddIngredient extends AbstractController implements Initializable {

    /*
        GUI nodes
   */

    @FXML private TextField ingredientNameTextField;
    @FXML private ImageView goHomeImageView;
    /*
        Methods
   */

    @FXML private void handleSaveButton() {
        if(textConstraintsRespected()){
            boolean success = CLIENT.clientInsertIngredientIntoDb(ingredientNameTextField.getText().replaceAll("'","''").toUpperCase());
            if (!success) {
                createErrorPopup("Errore","Non è stato possibile aggiungere l'ingrediente");
            } else {
                createSuccessPopup();
                handleGoHomebutton();
            }
        }else{
            createErrorPopup("Verifica i dati inseriti", "Hai lasciato campi vuoti o con un formato sbagliato");
        }
    }

    @FXML private void handleGoHomebutton(){
        controllerType.close(goHomeImageView);
    }

    /*
        Utils
   */

    private boolean textConstraintsRespected() {
        int errors = 0;
        errors+= textFieldConstraintsRespected(ingredientNameTextField) ? 0:1;
        return errors == 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setControllerType();
    }

    protected void setControllerType() {
        controllerType = new PopupController();
    }

    @Override
    public void refresh() {

    }
}
