package main.qrReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import main.NormalClasses.Anagrafica.Child;

public class QRReader{

    public static void main(String[] args) {
        Child c = new Child("nome", "cognome", "codiceFiscale123", "01/03/2018", "codiceFiscalePed",
                "codiceFiscaleGen", "codiceFiscaleGe22");
        QRGenerator.GenerateQR(c);
        example();
    }

    public static void example(){
        try {
            String filePath = "src/main/resources/QRImages/ChildcodiceFiscale123.png";
            String charset = "UTF-8";
            Map < EncodeHintType, ErrorCorrectionLevel > hintMap = new HashMap < EncodeHintType, ErrorCorrectionLevel > ();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //System.out.println("Data read from QR Code: " + readQRCode(filePath, charset, hintMap));
        } catch (Exception e) {
            System.out.println("Errore");
        }
    }

    public static String readQRCode(File file, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(file))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
        System.out.println(qrCodeResult.getText());
        return qrCodeResult.getText();
    }
}

