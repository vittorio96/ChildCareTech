package main.entities.normal_entities.Anagrafica;

import main.entities.normal_entities.NormalClass;
import main.entities.normal_entities.Mensa.ChildIntolerance;
import main.entities.normal_entities.Mensa.Intolerance;
import main.entities.string_property_entities.StringPropertyClass;
import main.entities.string_property_entities.Anagrafica.StringPropertyChild;

public class Child extends Person implements NormalClass {

    //inherited String nome;
    //inherited String cognome;
    //inherited String codiceFiscale;
    private     String codR; //be careful could be not initialized properly
    private     String dataNascita;
    private     String codiceFiscalePediatra;
    private     String codiceFiscaleGen1;
    private     String codiceFiscaleGen2;


    //FROM SERVER
    //constructor with codR
    public Child(String nome, String cognome, String codiceFiscale, String dataNascita, String codR, String codiceFiscalePediatra,
                 String codiceFiscaleGen1, String codiceFiscaleGen2) {

        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.dataNascita = dataNascita;
        this.codR = codR;
        this.codiceFiscalePediatra = codiceFiscalePediatra;
        this.codiceFiscaleGen1 = codiceFiscaleGen1;
        this.codiceFiscaleGen2 = codiceFiscaleGen2;



    }
    public Child(){

    }

    public Child(StringPropertyChild stringPropertyChild){
        this.nome = stringPropertyChild.getNome();
        this.cognome = stringPropertyChild.getCognome();
        this.codiceFiscale = stringPropertyChild.getCodiceFiscale();
        this.dataNascita = stringPropertyChild.getDataNascita();
        this.codR = stringPropertyChild.getCodR();
        this.codiceFiscalePediatra = stringPropertyChild.getCodiceFiscalePediatra();
        this.codiceFiscaleGen1 = stringPropertyChild.getCodiceFiscaleGen1();
        this.codiceFiscaleGen2 = stringPropertyChild.getCodiceFiscaleGen2();
    }
    //TO SERVER
    //constructor without codR
    public Child(String nome, String cognome, String codiceFiscale, String dataNascita, String codiceFiscalePediatra,
                 String codiceFiscaleGen1, String codiceFiscaleGen2) {

        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.dataNascita = dataNascita;
        this.codiceFiscalePediatra = codiceFiscalePediatra;
        this.codiceFiscaleGen1 = codiceFiscaleGen1;
        this.codiceFiscaleGen2 = codiceFiscaleGen2;

        /*this.stringPropertyChild = new
                StringPropertyChild(nome, cognome, codiceFiscale, dataNascita,
                codR, codiceFiscalePediatra, codiceFiscaleGen1, codiceFiscaleGen2);*/

    }

    public String getCodR() {
        return codR;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public String getCodiceFiscalePediatra() {
        return codiceFiscalePediatra;
    }

    public String getCodiceFiscaleGen1() {
        return codiceFiscaleGen1;
    }

    public String getCodiceFiscaleGen2() {
        return codiceFiscaleGen2;
    }

    /*public StringPropertyChild getStringPropertyChild() {
        return stringPropertyChild;
    }*/

    public void setCodR(String codR) {
        this.codR = codR;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public void setCodiceFiscalePediatra(String codiceFiscalePediatra) {
        this.codiceFiscalePediatra = codiceFiscalePediatra;
    }

    public void setCodiceFiscaleGen1(String codiceFiscaleGen1) {
        this.codiceFiscaleGen1 = codiceFiscaleGen1;
    }

    public void setCodiceFiscaleGen2(String codiceFiscaleGen2) {
        this.codiceFiscaleGen2 = codiceFiscaleGen2;
    }

    @Override
    public Intolerance createIntolerance(String ingredient) {
        return new ChildIntolerance(this.getCodiceFiscale(), ingredient);
    }

    @Override
    public StringPropertyClass toStringProperty() {
        return new StringPropertyChild(this);
    }
}
