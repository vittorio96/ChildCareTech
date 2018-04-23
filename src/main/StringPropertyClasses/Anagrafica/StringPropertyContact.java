package main.StringPropertyClasses.Anagrafica;

import javafx.beans.property.SimpleStringProperty;
import main.NormalClasses.Anagrafica.Contact;
import main.NormalClasses.Anagrafica.Person;

import java.io.Serializable;

public class StringPropertyContact extends StringPropertyPerson implements Serializable {

    private SimpleStringProperty cellphone;
    private SimpleStringProperty typeFlag;



    public enum ContactTypeFlag {
        GENITORE(0), PEDIATRA(1);
        private int ordernum;
        ContactTypeFlag(int i){
            this.ordernum = i;
        }

        public int getOrdernum() {
            return ordernum;
        }
    }

    public StringPropertyContact(){

    }

    public StringPropertyContact(String nome, String cognome, String codiceFiscale, String cellulare, ContactTypeFlag typeFlag) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.codiceFiscale = new SimpleStringProperty(codiceFiscale);
        this.cellphone = new SimpleStringProperty(cellulare);
        this.typeFlag = new SimpleStringProperty(Integer.toString(typeFlag.getOrdernum()));
    }

    public StringPropertyContact(Contact c) {
        this.nome = new SimpleStringProperty(c.getNome());
        this.cognome = new SimpleStringProperty(c.getCognome());
        this.codiceFiscale = new SimpleStringProperty(c.getCodiceFiscale());
        this.cellphone = new SimpleStringProperty(c.getCellulare());
        this.typeFlag = new SimpleStringProperty(c.getTipo());
    }

    @Override
    public Person toPerson() {
        return new Contact(this);
    }

    public String getCellphone() {
        return cellphone.get();
    }

    public SimpleStringProperty cellphoneProperty() {
        return cellphone;
    }

    public String getTypeFlag() {
        return typeFlag.get();
    }

    public void setCellphone(String cellphone) {
        this.cellphone.set(cellphone);
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag.set(typeFlag);
    }

    public SimpleStringProperty typeFlagProperty() {
        return typeFlag;
    }
}
