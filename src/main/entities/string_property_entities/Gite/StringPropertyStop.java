package main.entities.string_property_entities.Gite;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import main.entities.normal_entities.Gite.Stop;

public class StringPropertyStop {

    private SimpleStringProperty nomeGita;
    private SimpleStringProperty dataGita;
    private SimpleStringProperty targa;
    private SimpleStringProperty luogo;
    private SimpleIntegerProperty numeroTappa;

    public StringPropertyStop(String nomeGita, String dataGita, String targa, String luogo, Integer numeroTappa) {
        this.nomeGita = new SimpleStringProperty(nomeGita);
        this.dataGita = new SimpleStringProperty(dataGita);
        this.targa = new SimpleStringProperty(targa);
        this.luogo = new SimpleStringProperty(luogo);
        this.numeroTappa = new SimpleIntegerProperty(numeroTappa);
    }

    public StringPropertyStop(Stop s) {
        this.nomeGita = new SimpleStringProperty(s.getNomeGita());
        this.dataGita = new SimpleStringProperty(s.getDataGita());
        this.targa = new SimpleStringProperty(s.getTarga());
        this.luogo = new SimpleStringProperty(s.getLuogo());
        this.numeroTappa = new SimpleIntegerProperty(s.getNumeroTappa());
    }

    public String getNomeGita() {
        return nomeGita.get();
    }

    public SimpleStringProperty nomeGitaProperty() {
        return nomeGita;
    }

    public void setNomeGita(String nomeGita) {
        this.nomeGita.set(nomeGita);
    }

    public String getDataGita() {
        return dataGita.get();
    }

    public SimpleStringProperty dataGitaProperty() {
        return dataGita;
    }

    public void setDataGita(String dataGita) {
        this.dataGita.set(dataGita);
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

    public String getLuogo() {
        return luogo.get();
    }

    public SimpleStringProperty luogoProperty() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo.set(luogo);
    }

    public int getNumeroTappa() {
        return numeroTappa.get();
    }

    public SimpleIntegerProperty numeroTappaProperty() {
        return numeroTappa;
    }

    public void setNumeroTappa(int numeroTappa) {
        this.numeroTappa.set(numeroTappa);
    }
}
