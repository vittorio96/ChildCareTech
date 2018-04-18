package main.NormalClasses.Anagrafica;

import main.StringPropertyClasses.Anagrafica.StringPropertyStaff;
import main.User;

public class Staff extends Person{

    //inherited String insertStatement;
    //inherited String nome;
    //inherited String cognome;
    //inherited String codiceFiscale;
    private     String username;
    private     String password;
    private     String dataNascita;
    private     String tipo;

    public Staff(String codF, String nome, String cognome, String username, String password, String dataNascita, User.UserTypeFlag typeFlag) {
        this.codiceFiscale = codF;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataNascita = dataNascita;
        this.tipo = Integer.toString(typeFlag.getOrdernum());
    }

    public Staff(StringPropertyStaff s){
        this.codiceFiscale = s.getCodiceFiscale();
        this.nome = s.getNome();
        this.cognome = s.getCognome();
        this.username = s.getUsername();
        this.password = s.getPassword();
        this.dataNascita = s.getDataNascita();
        this.tipo = s.getTipo();
    }

    public Staff(){
        
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public String getTipo() {
        return tipo;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
