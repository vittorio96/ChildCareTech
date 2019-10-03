package main.entities.string_property_entities.Gite;

import javafx.beans.property.SimpleStringProperty;
import main.entities.normal_entities.Gite.Trip;

public class StringPropertyTrip {

    private SimpleStringProperty nome;
    private SimpleStringProperty data;
    private SimpleStringProperty partenza;
    private SimpleStringProperty destinazione;
    /*private final String inputPattern = "yyyy-MM-dd";
    private final String outputPattern = "dd/MM/yyyy";
    private DateTimeFormatter IN_DATE_FORMAT = DateTimeFormatter.ofPattern(inputPattern);
    private DateTimeFormatter OUT_DATE_FORMAT = DateTimeFormatter.ofPattern(outputPattern);

    OUT_DATE_FORMAT.format(LocalDate.parse(t.getData(), IN_DATE_FORMAT))
    */

    public StringPropertyTrip(String codice, String nome, String data) {
        this.nome = new SimpleStringProperty(nome);
        this.data = new SimpleStringProperty(data);

    }

    public StringPropertyTrip(String nome, String data, String partenza, String destinazione) {
        this.nome = new SimpleStringProperty(nome);
        this.data = new SimpleStringProperty(data);
        this.partenza = new SimpleStringProperty(partenza);
        this.destinazione = new SimpleStringProperty(destinazione);

    }

    public StringPropertyTrip(Trip t) {
        this.nome = new SimpleStringProperty(t.getNomeGita());
        this.data = new SimpleStringProperty(t.getData());
        this.partenza = new SimpleStringProperty(t.getPartenza());
        this.destinazione = new SimpleStringProperty(t.getDestinazione());
    }

    public String getPartenza() {
        return partenza.get();
    }

    public SimpleStringProperty partenzaProperty() {
        return partenza;
    }

    public void setPartenza(String partenza) {
        this.partenza.set(partenza);
    }

    public String getDestinazione() {
        return destinazione.get();
    }

    public SimpleStringProperty destinazioneProperty() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione.set(destinazione);
    }

    public String getNome() {
        return nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getData() {
        return data.get();
    }

    public SimpleStringProperty dataProperty() {
        return data;
    }

    public void setData(String data) {
        this.data.set(data);
    }


}
