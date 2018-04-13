
package main.NormalClasses.Mensa;

public abstract class Intolerance {

    private String codF;
    private String nomeI;

    public Intolerance() {
    }

    public Intolerance(String codF, String nomeI) {
        this.codF = codF;
        this.nomeI = nomeI;
    }

    public String getCodF() {
        return codF;
    }

    public void setCodF(String codF) {
        this.codF = codF;
    }

    public String getNomeI() {
        return nomeI;
    }

    public void setNomeI(String nomeI) {
        this.nomeI = nomeI;
    }

}
