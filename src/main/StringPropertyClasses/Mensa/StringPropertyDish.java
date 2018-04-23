package main.StringPropertyClasses.Mensa;

import javafx.beans.property.SimpleStringProperty;
import main.NormalClasses.Mensa.Dish;
import main.NormalClasses.NormalClass;
import main.StringPropertyClasses.StringPropertyClass;

public class StringPropertyDish extends StringPropertyClass {
    private SimpleStringProperty nomeP;
    private SimpleStringProperty tipoPiatto;

    public StringPropertyDish(Dish d) {
        this.nomeP = new SimpleStringProperty(d.getNomeP().substring(0,1).toUpperCase()
                +d.getNomeP().substring(1).toLowerCase());
        this.tipoPiatto = new SimpleStringProperty(d.getTipoPiatto().toString());
    }

    public StringPropertyDish(String nomeP, String tipoPiatto) {
        this.nomeP = new SimpleStringProperty(nomeP);
        this.tipoPiatto = new SimpleStringProperty(tipoPiatto);
    }

    public String getNomeP() {
        return nomeP.get();
    }

    public SimpleStringProperty nomePProperty() {
        return nomeP;
    }

    public void setNomeP(String nomeP) {
        this.nomeP.set(nomeP);
    }

    public String getTipoPiatto() {
        return tipoPiatto.get();
    }

    public SimpleStringProperty tipoPiattoProperty() {
        return tipoPiatto;
    }

    public void setTipoPiatto(String tipoPiatto) {
        this.tipoPiatto.set(tipoPiatto);
    }

    @Override
    public NormalClass toNormalClass() {
        return new Dish(this);
    }
}
