package main.NormalClasses.Anagrafica;

import main.NormalClasses.Mensa.ChildIntolerance;
import main.NormalClasses.Mensa.Intolerance;
import main.NormalClasses.Mensa.PersonIntolerance;
import main.StringPropertyClasses.Anagrafica.StringPropertyChild;
import main.StringPropertyClasses.Anagrafica.StringPropertyPerson;
import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;

import java.io.Serializable;

public abstract class Person implements Serializable{
    protected String nome;
    protected String cognome;
    protected String codiceFiscale;

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    public abstract Intolerance createIntolerance(String ingredient);

    public abstract StringPropertyPerson toStringProperty();
}
