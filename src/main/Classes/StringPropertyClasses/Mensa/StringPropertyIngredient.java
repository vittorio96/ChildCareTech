package main.Classes.StringPropertyClasses.Mensa;

import javafx.beans.property.SimpleStringProperty;

public class StringPropertyIngredient {
    private SimpleStringProperty nome;

    public StringPropertyIngredient(String nome) {
        this.nome = new SimpleStringProperty(nome.substring(0,1).toUpperCase() +nome.substring(1).toLowerCase());
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
}
