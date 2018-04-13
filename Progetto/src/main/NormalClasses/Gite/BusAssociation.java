package main.NormalClasses.Gite;

import java.io.Serializable;

public class BusAssociation implements Serializable{

    private String codF;
    private String nomeG;
    private String dataG;
    private String targa;

    public BusAssociation() {
    }

    public BusAssociation(String codF, String nomeG, String dataG, String targa) {
        this.codF = codF;
        this.nomeG = nomeG;
        this.dataG = dataG;
        this.targa = targa;
    }

    public String getCodF() {
        return codF;
    }

    public void setCodF(String codF) {
        this.codF = codF;
    }

    public String getNomeG() {
        return nomeG;
    }

    public void setNomeG(String nomeG) {
        this.nomeG = nomeG;
    }

    public String getDataG() {
        return dataG;
    }

    public void setDataG(String dataG) {
        this.dataG = dataG;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }
}
