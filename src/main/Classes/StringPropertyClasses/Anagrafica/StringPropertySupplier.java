package main.Classes.StringPropertyClasses.Anagrafica;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.Classes.NormalClasses.Anagrafica.Supplier;

import java.io.Serializable;

public class StringPropertySupplier implements Serializable {

    private StringProperty pIva;
    private StringProperty nomeF;
    private StringProperty tel;
    private StringProperty email;

    public StringPropertySupplier() {
    }

    public StringPropertySupplier(String pIva, String nomeF, String tel, String email) {
        this.pIva = new SimpleStringProperty(pIva);
        this.nomeF = new SimpleStringProperty(nomeF);
        this.tel = new SimpleStringProperty(tel);
        this.email = new SimpleStringProperty(email);
    }

    public StringPropertySupplier(Supplier s) {
        this.pIva = new SimpleStringProperty(s.getpIva());
        this.nomeF = new SimpleStringProperty(s.getNomeF());
        this.tel = new SimpleStringProperty(s.getTel());
        this.email = new SimpleStringProperty(s.getEmail());
    }

    public String getpIva() {
        return pIva.get();
    }

    public StringProperty pIvaProperty() {
        return pIva;
    }

    public String getNomeF() {
        return nomeF.get();
    }

    public StringProperty nomeFProperty() {
        return nomeF;
    }

    public String getTel() {
        return tel.get();
    }

    public StringProperty telProperty() {
        return tel;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setpIva(String pIva) {
        this.pIva.set(pIva);
    }

    public void setNomeF(String nomeF) {
        this.nomeF.set(nomeF);
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
