package main.entities.normal_entities.Gite;

import main.entities.string_property_entities.Gite.StringPropertyStop;

import java.io.Serializable;

public class Stop implements Serializable {
    private String nomeGita;
    private String dataGita;
    private String targa;
    private String luogo;
    private Integer numeroTappa;

    public Stop(String nomeGita, String dataGita, String targa, String luogo, Integer numeroTappa) {
        this.nomeGita = nomeGita;
        this.dataGita = dataGita;
        this.targa = targa;
        this.luogo = luogo;
        this.numeroTappa = numeroTappa;
    }

    public Stop(String nomeGita, String dataGita, String targa, String luogo) {
        this.nomeGita = nomeGita;
        this.dataGita = dataGita;
        this.targa = targa;
        this.luogo = luogo;
        this.numeroTappa = -1;
    }

    public Stop() {

    }

    public Stop (StringPropertyStop stop){
        this.nomeGita = stop.getNomeGita();
        this.dataGita = stop.getDataGita();
        this.targa = stop.getTarga();
        this.luogo = stop.getLuogo();
        this.numeroTappa = stop.getNumeroTappa();
    }

    public String getNomeGita() {
        return nomeGita;
    }

    public void setNomeGita(String nomeGita) {
        this.nomeGita = nomeGita;
    }

    public String getDataGita() {
        return dataGita;
    }

    public void setDataGita(String dataGita) {
        this.dataGita = dataGita;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Integer getNumeroTappa() {
        return numeroTappa;
    }

    public void setNumeroTappa(Integer numeroTappa) {
        this.numeroTappa = numeroTappa;
    }
}
