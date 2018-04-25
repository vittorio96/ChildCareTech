package main.Classes.StringPropertyClasses.Anagrafica;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import main.Classes.NormalClass;
import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Person;
import main.Classes.StringPropertyClass;

import java.io.Serializable;

public class StringPropertyChild extends StringPropertyPerson implements Serializable, StringPropertyClass{

    private SimpleStringProperty codR;
    private SimpleStringProperty codiceFiscaleGen1;
    private SimpleStringProperty codiceFiscaleGen2;
    private SimpleStringProperty codiceFiscalePediatra;
    private SimpleStringProperty dataNascita;
    private SimpleBooleanProperty booleanStatus;

    public StringPropertyChild(String nome, String cognome, String codiceFiscale, String dataNascita, String codR, String codiceFiscalePediatra,
                               String codiceFiscaleGen1, String codiceFiscaleGen2) {

        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.codiceFiscale = new SimpleStringProperty(codiceFiscale);
        this.dataNascita = new SimpleStringProperty(dataNascita);
        this.codR = new SimpleStringProperty(codR);
        this.codiceFiscalePediatra = new SimpleStringProperty(codiceFiscalePediatra);
        this.codiceFiscaleGen1 = new SimpleStringProperty(codiceFiscaleGen1);
        this.codiceFiscaleGen2 = new SimpleStringProperty(codiceFiscaleGen2);
        this.booleanStatus = new SimpleBooleanProperty(false);

    }

    public StringPropertyChild(Child c){
        this.nome = new SimpleStringProperty(c.getNome());
        this.cognome = new SimpleStringProperty(c.getCognome());
        this.codiceFiscale = new SimpleStringProperty(c.getCodiceFiscale());
        this.dataNascita = new SimpleStringProperty(c.getDataNascita());
        this.codR = new SimpleStringProperty(c.getCodR());
        this.codiceFiscalePediatra = new SimpleStringProperty(c.getCodiceFiscalePediatra());
        this.codiceFiscaleGen1 = new SimpleStringProperty(c.getCodiceFiscaleGen1());
        this.codiceFiscaleGen2 = new SimpleStringProperty(c.getCodiceFiscaleGen2());
        this.booleanStatus = new SimpleBooleanProperty(false);
    }

    public StringPropertyChild(StringPropertyChild c){
        this.nome = c.nomeProperty();
        this.cognome = c.cognomeProperty();
        this.codiceFiscale = c.codiceFiscaleProperty();
        this.dataNascita = c.dataNascitaProperty();
        this.codR = c.codRProperty();
        this.codiceFiscalePediatra = c.codiceFiscalePediatraProperty();
        this.codiceFiscaleGen1 = c.codiceFiscaleGen2Property();
        this.codiceFiscaleGen2 = c.codiceFiscaleGen2Property();
        this.booleanStatus = c.booleanStatusProperty();
    }

    public String getCodR() {
        return codR.get();
    }

    public SimpleStringProperty codRProperty() {
        return codR;
    }

    public String getCodiceFiscale() {
        return codiceFiscale.get();
    }

    public SimpleStringProperty codiceFiscaleProperty() {
        return codiceFiscale;
    }

    public String getCodiceFiscaleGen1() {
        return codiceFiscaleGen1.get();
    }

    public SimpleStringProperty codiceFiscaleGen1Property() {
        return codiceFiscaleGen1;
    }

    public String getCodiceFiscaleGen2() {
        return codiceFiscaleGen2.get();
    }

    public SimpleStringProperty codiceFiscaleGen2Property() {
        return codiceFiscaleGen2;
    }

    public String getCodiceFiscalePediatra() {
        return codiceFiscalePediatra.get();
    }

    public SimpleStringProperty codiceFiscalePediatraProperty() {
        return codiceFiscalePediatra;
    }

    public String getNome() {
        return nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public String getCognome() {
        return cognome.get();
    }

    public SimpleStringProperty cognomeProperty() {
        return cognome;
    }

    public String getDataNascita() {
        return dataNascita.get();
    }

    public SimpleStringProperty dataNascitaProperty() {
        return dataNascita;
    }

    public void setCodR(String codR) {
        this.codR.set(codR);
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale.set(codiceFiscale);
    }

    public void setCodiceFiscaleGen1(String codiceFiscaleGen1) {
        this.codiceFiscaleGen1.set(codiceFiscaleGen1);
    }

    public void setCodiceFiscaleGen2(String codiceFiscaleGen2) {
        this.codiceFiscaleGen2.set(codiceFiscaleGen2);
    }

    public void setCodiceFiscalePediatra(String codiceFiscalePediatra) {
        this.codiceFiscalePediatra.set(codiceFiscalePediatra);
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita.set(dataNascita);
    }

    public boolean isBooleanStatus() {
        return booleanStatus.get();
    }

    public SimpleBooleanProperty booleanStatusProperty() {
        return booleanStatus;
    }

    public void setBooleanStatus(boolean booleanStatus) {
        this.booleanStatus.set(booleanStatus);
    }

    @Override
    public Person toPerson() {
        return new Child(this);
    }

    @Override
    public NormalClass toNormalClass() {
        return new Child(this);
    }
}
