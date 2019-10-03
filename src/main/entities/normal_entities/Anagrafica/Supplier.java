package main.entities.normal_entities.Anagrafica;

import main.entities.string_property_entities.Anagrafica.StringPropertySupplier;

import java.io.Serializable;

public class Supplier implements Serializable {

    private String pIva;
    private String nomeF;
    private String tel;
    private String email;

    public Supplier() {
    }

    public Supplier(String pIva, String nomeF, String tel, String email) {
        this.pIva = pIva;
        this.nomeF = nomeF;
        this.tel = tel;
        this.email = email;
    }

    public Supplier(StringPropertySupplier s) {
        this.pIva = s.getpIva();
        this.nomeF = s.getNomeF();
        this.tel = s.getTel();
        this.email = s.getEmail();
    }


    public String getpIva() {
        return pIva;
    }

    public void setpIva(String pIva) {
        this.pIva = pIva;
    }

    public String getNomeF() {
        return nomeF;
    }

    public void setNomeF(String nomeF) {
        this.nomeF = nomeF;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}