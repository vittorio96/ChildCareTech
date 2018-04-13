package main.NormalClasses.Gite;

import main.StringPropertyClasses.Gite.StringPropertyTrip;

import java.io.Serializable;

public class Trip implements Serializable {
    private String nomeGita;
    private String partenza;
    private String destinazione;
    private String data;

    public Trip(String nomeGita, String data, String partenza, String destinazione) {
        this.nomeGita = nomeGita;
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.data = data;
    }

    public Trip(String nomeGita, String data) {
        this.nomeGita = nomeGita;
        this.data = data;
    }

    public Trip(StringPropertyTrip spTrip) {
        this.nomeGita = spTrip.getNome();
        this.partenza = spTrip.getPartenza();
        this.destinazione = spTrip.getDestinazione();
        this.data = spTrip.getData();
    }

    public Trip(){

    }

    public String getNomeGita() {
        return nomeGita;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public String getData() {
        return data;
    }

    public String getPartenza() {
        return partenza;
    }

    public void setNomeGita(String nomeGita) {
        this.nomeGita = nomeGita;
    }

    public void setPartenza(String partenza) {
        this.partenza = partenza;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public void setData(String data) {
        this.data = data;
    }

}
