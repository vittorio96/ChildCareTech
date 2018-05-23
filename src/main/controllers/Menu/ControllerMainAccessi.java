package main.controllers.Menu;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import main.controllers.AbstractController;
import main.controllers.StageController;
import main.qrReader.QRReader;
import main.qrReader.WebcamQRReader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.FutureTask;

public class ControllerMainAccessi extends AbstractController implements Initializable {

    /*
        Buttons & Initialization
    */

    @FXML Button readQRFromFileButton;
    @FXML Button makeQRButton;
    @FXML Button readQRFromWebcamButton;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        AbstractController.setCurrentController(this);
        setControllerType();
    }

    protected void setControllerType() {
        controllerType = new StageController();
    }

    /*
        Scene Redirects
    */

    @FXML protected void handleGenerateQRButtonAction(ActionEvent event) throws IOException {
        changeSceneInPopup(makeQRButton, "../../resources/fxml/qr_generate.fxml",800,450);
    }

    @FXML protected void handleReadQRFromFileButtonAction(ActionEvent event) throws IOException, NotFoundException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("QR CODE (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        try {
            File file = fileChooser.showOpenDialog(readQRFromFileButton.getScene().getWindow());
            Map< EncodeHintType, ErrorCorrectionLevel > hintMap = new HashMap< EncodeHintType, ErrorCorrectionLevel >();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            String decodedQr = QRReader.readQRCode(file,"UTF-8",hintMap);
            if(decodedQr != null){
                final int PREFIXSIZE=6;
                boolean success = false;
                if(decodedQr.startsWith("Child")){
                    success = CLIENT.clientChildQRAccess(decodedQr.substring(PREFIXSIZE));
                }else{
                    success = CLIENT.clientStaffQRAccess(decodedQr.substring(PREFIXSIZE));
                }
                if(success) createSuccessPopup(); else createGenericErrorPopup();

            }else {
                createErrorPopup("Errore", "Non è stato possibile leggere il QR");
            }
        }catch (Exception e){
            //no file selected and window closed...
        }

    }


    @FXML protected void handleReadQRFromWebcamButtonAction(ActionEvent event) throws IOException {
        WebcamQRReader.setClient(CLIENT);
        FutureTask<WebcamQRReader> launchWebcam = new FutureTask<>(WebcamQRReader::new) ;
        SwingUtilities.invokeLater(launchWebcam);
    }

    @Override
    public void refresh() {

    }
}
