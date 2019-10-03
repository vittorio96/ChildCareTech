package main.entities.normal_entities.Gite;

import java.io.Serializable;

public class StopPresence implements Serializable {

    private String codF;
    private Integer numTappa;
    private String targa;
    private String dataG;
    private String nomeG;

    public StopPresence() {

    }

    public StopPresence(String codF, Integer numTappa, String targa, String dataG, String nomeG) {
        this.codF = codF;
        this.numTappa = numTappa;
        this.targa = targa;
        this.dataG = dataG;
        this.nomeG = nomeG;
    }

    public String getCodF() {
        return codF;
    }

    public void setCodF(String codF) {
        this.codF = codF;
    }

    public Integer getNumTappa() {
        return numTappa;
    }

    public void setNumTappa(Integer numTappa) {
        this.numTappa = numTappa;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public String getDataG() {
        return dataG;
    }

    public void setDataG(String dataG) {
        this.dataG = dataG;
    }

    public String getNomeG() {
        return nomeG;
    }

    public void setNomeG(String nomeG) {
        this.nomeG = nomeG;
    }
}
