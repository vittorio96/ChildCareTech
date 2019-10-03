package main.client.qrReader;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.*;

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
import main.client.Client;

public class WebcamQRReader extends JFrame implements Runnable, ThreadFactory, Closeable {

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
        setTitle("Mostra il QR alla webcam");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension size = WebcamResolution.QVGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        /*textarea = new JTextArea();
        textarea.setEditable(false);
        textarea.setPreferredSize(size);*/

        add(panel);
        //add(textarea);

        pack();
        setVisible(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
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
                //textarea.setText("Codice riconosciuto! " + dechipheredCode);
                    final int PREFIXSIZE=6;
                    boolean success = false;
                    if(dechipheredCode.startsWith("Child")){
                        success = client.clientChildQRAccess(dechipheredCode.substring(PREFIXSIZE));
                    }else{
                        success = client.clientStaffQRAccess(dechipheredCode.substring(PREFIXSIZE));
                    }
                    if(success) {

                        try {
                            this.close();
                            this.dispose();
                            showmessage("Presenza di " + dechipheredCode + " inserita con successo");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {showmessage("Si è verificato un errore nel inserimento");}
            }

        } while (true);
    }

    private void showmessage(String text) {
        JOptionPane.showMessageDialog(null, text);
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

    @Override
    public void close() throws IOException {
        webcam.close();
    }
}