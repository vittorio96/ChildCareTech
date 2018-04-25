package main.Classes.StringPropertyClasses.Gite;

import javafx.beans.property.SimpleStringProperty;
import main.Classes.NormalClasses.Gite.Bus;

public class StringPropertyBus {
    private SimpleStringProperty targa;
    private SimpleStringProperty nomeA;
    private SimpleStringProperty nomeG;
    private SimpleStringProperty dataG;

    public StringPropertyBus(String targa, Integer postidisponibili, String nomeA, String nomeG, String dataG) {
        this.targa = new SimpleStringProperty(targa);
        this.nomeA = new SimpleStringProperty(nomeA);
        this.nomeG = new SimpleStringProperty(nomeG);
        this.dataG = new SimpleStringProperty(dataG);
    }

    public StringPropertyBus(Bus bus) {
        this.targa = new SimpleStringProperty(bus.getTarga());
        this.nomeA = new SimpleStringProperty(bus.getNomeA());
        this.nomeG = new SimpleStringProperty(bus.getNomeG());
        this.dataG = new SimpleStringProperty(bus.getDataG());
    }

    public String getTarga() {
        return targa.get();
    }

    public SimpleStringProperty targaProperty() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa.set(targa);
    }

    public String getNomeA() {
        return nomeA.get();
    }

    public SimpleStringProperty nomeAProperty() {
        return nomeA;
    }

    public void setNomeA(String nomeA) {
        this.nomeA.set(nomeA);
    }

    public String getNomeG() {
        return nomeG.get();
    }

    public SimpleStringProperty nomeGProperty() {
        return nomeG;
    }

    public void setNomeG(String nomeG) {
        this.nomeG.set(nomeG);
    }

    public String getDataG() {
        return dataG.get();
    }

    public SimpleStringProperty dataGProperty() {
        return dataG;
    }

    public void setDataG(String dataG) {
        this.dataG.set(dataG);
    }
}
