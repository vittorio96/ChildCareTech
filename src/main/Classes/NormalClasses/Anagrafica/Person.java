package main.Classes.NormalClasses.Anagrafica;

import main.Classes.NormalClass;
import main.Classes.NormalClasses.Mensa.Intolerance;
import main.Classes.StringPropertyClass;
import main.Classes.StringPropertyClasses.Anagrafica.StringPropertyPerson;

import java.io.Serializable;

public abstract class Person implements Serializable, NormalClass{
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

    public abstract StringPropertyClass toStringProperty();
}
