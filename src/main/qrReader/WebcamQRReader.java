package main.qrReader;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import main.Client;

public class WebcamQRReader extends JFrame implements Runnable, ThreadFactory {

    private static final long serialVersionUID = 6441489157408381878L;

    private Executor executor = Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private JTextArea textarea = null;
    private static Client client;

    public static void setClient(Client client) {
        WebcamQRReader.client = client;
    }

    public WebcamQRReader() {
        super();
        setLayout(new FlowLayout());
        setTitle("Read QR / Bar Code With Webcam");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension size = WebcamResolution.VGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        textarea = new JTextArea();
        textarea.setEditable(false);
        textarea.setPreferredSize(size);

        add(panel);
        add(textarea);

        pack();
        setVisible(true);

        executor.execute(this);
    }

    @Override
    public void run() {

        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                String dechipheredCode = result.getText();
                textarea.setText("Codice riconosciuto! " + dechipheredCode);
                    final int PREFIXSIZE=6;
                    boolean success = false;
                    if(dechipheredCode.startsWith("Child")){
                        success = client.clientChildQRAccess(dechipheredCode.substring(PREFIXSIZE));
                    }else{
                        success = client.clientStaffQRAccess(dechipheredCode.substring(PREFIXSIZE));
                    }
                    if(success) {
                        textarea.append("\n Inserito con successo");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.dispose(); }
                    else {textarea.append("\n Ma si è verificato un errore nel inserimento");}
            }

        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "Webcam");
        t.setDaemon(true);
        return t;
    }

   public static void main(String[] args) {
        new WebcamQRReader();
    }
}