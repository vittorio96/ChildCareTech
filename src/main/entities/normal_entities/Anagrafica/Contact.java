package main.entities.normal_entities.Anagrafica;

import main.entities.normal_entities.NormalClass;
import main.entities.normal_entities.Mensa.ChildIntolerance;
import main.entities.normal_entities.Mensa.Intolerance;
import main.entities.string_property_entities.StringPropertyClass;
import main.entities.string_property_entities.Anagrafica.StringPropertyContact;

public class Contact extends Person implements NormalClass{

    //inherited String nome;
    //inherited String cognome;
    //inherited String codiceFiscale;
    private     String cellulare;
    private     String tipo;

    public Contact(String nome, String cognome, String codiceFiscale, String cellulare, String tipo) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.cellulare = cellulare;
        this.tipo= tipo;
    }

    public Contact(StringPropertyContact contact) {
        this.nome = contact.getNome();
        this.cognome = contact.getCognome();
        this.codiceFiscale = contact.getCodiceFiscale();
        this.cellulare = contact.getCellphone();
        this.tipo= contact.getTypeFlag();
    }

    public Contact(String nome, String cognome, String codiceFiscale, String cellulare, ContactTypeFlag contactTypeFlag) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.cellulare = cellulare;
        this.tipo= Integer.toString(contactTypeFlag.getOrdernum());
    }

    public Contact(){}


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


    public String getCellulare() {
        return cellulare;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public Intolerance createIntolerance(String ingredient) {
        return new ChildIntolerance(this.codiceFiscale,ingredient);
    }

    @Override
    public StringPropertyClass toStringProperty() {
        return new StringPropertyContact(this);
    }
}
