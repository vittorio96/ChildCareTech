package main.entities.string_property_entities.Gite;

import javafx.beans.property.*;

public class StringPropertyStopPresence {

    private SimpleStringProperty codFiscale;
    private SimpleStringProperty nome;
    private SimpleStringProperty cognome;
    private SimpleIntegerProperty codiceBambino;
    private SimpleIntegerProperty codGita;
    private BooleanProperty presente;

    public StringPropertyStopPresence(String codFiscale, String nome, String cognome, Integer codiceBambino,
                                      Integer codGita, boolean presente) {

        this.nome = new SimpleStringProperty(nome) ;
        this.cognome = new SimpleStringProperty(cognome) ;
        this.codiceBambino = new SimpleIntegerProperty(codiceBambino) ;
        this.codFiscale = new SimpleStringProperty(codFiscale) ;
        this.codGita = new SimpleIntegerProperty(codGita);
        this.presente = new SimpleBooleanProperty(presente);
    }

    public String getCodFiscale() {
        return codFiscale.get();
    }

    public SimpleStringProperty codFiscaleProperty() {
        return codFiscale;
    }

    public void setCodFiscale(String codFiscale) {
        this.codFiscale.set(codFiscale);
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

    public String getCognome() {
        return cognome.get();
    }

    public SimpleStringProperty cognomeProperty() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    public int getCodiceBambino() {
        return codiceBambino.get();
    }

    public SimpleIntegerProperty codiceBambinoProperty() {
        return codiceBambino;
    }

    public void setCodiceBambino(int codiceBambino) {
        this.codiceBambino.set(codiceBambino);
    }

    public int getCodGita() {
        return codGita.get();
    }

    public SimpleIntegerProperty codGitaProperty() {
        return codGita;
    }

    public void setCodGita(int codGita) {
        this.codGita.set(codGita);
    }

    public boolean isPresente() {
        return presente.get();
    }

    public BooleanProperty presenteProperty() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente.set(presente);
    }
}
