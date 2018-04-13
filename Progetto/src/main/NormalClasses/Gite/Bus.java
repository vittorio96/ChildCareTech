package main.NormalClasses.Gite;

import main.StringPropertyClasses.Gite.StringPropertyBus;

import java.io.Serializable;

public class Bus implements Serializable{

    private String targa;
    //private Integer postidisponibili;
    private String nomeA;
    private String nomeG;
    private String dataG;


    /*public Bus(String targa, Integer postidisponibili, String nomeA, String nomeG, String dataG) {
        this.targa = targa;
        this.nomeA = nomeA;
        this.nomeG = nomeG;
        this.dataG = dataG;
    }*/

    public Bus(String targa, String nomeA, String nomeG, String dataG) {
        this.targa = targa;
        this.nomeA = nomeA;
        this.nomeG = nomeG;
        this.dataG = dataG;
    }

    public Bus(StringPropertyBus stringPropertyBus) {
        this.targa = stringPropertyBus.getTarga();
        this.nomeA = stringPropertyBus.getNomeA();
        this.nomeG = stringPropertyBus.getNomeG();
        this.dataG = stringPropertyBus.getDataG();
    }

    public Bus() {
    }

    public String getTarga() {
        return targa;
    }



    public String getNomeA() {
        return nomeA;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }


    public void setNomeA(String nomeA) {
        this.nomeA = nomeA;
    }

    public String getNomeG() {
        return nomeG;
    }

    public String getDataG() {
        return dataG;
    }

    public void setNomeG(String nomeG) {
        this.nomeG = nomeG;
    }

    public void setDataG(String dataG) {
        this.dataG = dataG;
    }
}