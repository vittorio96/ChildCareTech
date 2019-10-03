package main.entities.normal_entities.Anagrafica;

import main.entities.normal_entities.NormalClass;
import main.entities.normal_entities.Mensa.Intolerance;
import main.entities.string_property_entities.StringPropertyClass;

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
