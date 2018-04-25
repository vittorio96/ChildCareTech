package main.Classes.StringPropertyClasses.Anagrafica;

import javafx.beans.property.SimpleStringProperty;
import main.Classes.NormalClasses.Anagrafica.Person;

import java.io.Serializable;

public abstract class StringPropertyPerson implements Serializable{
    protected SimpleStringProperty codiceFiscale;
    protected SimpleStringProperty nome;
    protected SimpleStringProperty cognome;

    public String getCodiceFiscale() {
        return codiceFiscale.get();
    }

    public SimpleStringProperty codiceFiscaleProperty() {
        return codiceFiscale;
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

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale.set(codiceFiscale);
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    public abstract Person toPerson();
}
