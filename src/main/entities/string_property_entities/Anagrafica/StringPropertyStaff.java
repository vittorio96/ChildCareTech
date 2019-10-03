package main.entities.string_property_entities.Anagrafica;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.entities.normal_entities.NormalClass;
import main.entities.normal_entities.Anagrafica.Person;
import main.entities.normal_entities.Anagrafica.Staff;
import main.entities.string_property_entities.StringPropertyClass;

import java.io.Serializable;

public class StringPropertyStaff extends StringPropertyPerson implements Serializable, StringPropertyClass {

    private  SimpleStringProperty username;
    private  SimpleStringProperty password;
    private  SimpleStringProperty dataNascita;
    private  SimpleStringProperty tipo;

    public StringPropertyStaff(){

    }

    public StringPropertyStaff(String codF, String nome, String cognome, String username, String password, String dataNascita, StaffTypeFlag typeFlag) {
        this.codiceFiscale = new SimpleStringProperty(codF);
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.dataNascita = new SimpleStringProperty(dataNascita);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.tipo = new SimpleStringProperty(Integer.toString(typeFlag.getOrdernum()));
    }


    public StringPropertyStaff(Staff s) {
        this.codiceFiscale = new SimpleStringProperty(s.getCodiceFiscale());
        this.nome = new SimpleStringProperty(s.getNome());
        this.cognome = new SimpleStringProperty(s.getCognome());
        this.dataNascita = new SimpleStringProperty(s.getDataNascita());
        this.username = new SimpleStringProperty(s.getUsername());
        this.password = new SimpleStringProperty(s.getPassword());
        this.tipo = new SimpleStringProperty(s.getTipo());
    }

    @Override
    public NormalClass toNormalClass() {
        return new Staff(this);
    }

    public enum StaffTypeFlag {
        AMMINISTRATIVO(0), SUPERVISORE(1), MENSA(2);
        private int ordernum;
        StaffTypeFlag(int i){
            this.ordernum = i;
        }

        public int getOrdernum() {
            return ordernum;
        }
    }



    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getDataNascita() {
        return dataNascita.get();
    }

    public StringProperty dataNascitaProperty() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita.set(dataNascita);
    }

    public String getTipo() {
        return tipo.get();
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    @Override
    public Person toPerson() {
        return new Staff(this);
    }
}
