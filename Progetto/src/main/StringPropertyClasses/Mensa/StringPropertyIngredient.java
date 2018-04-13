package main.StringPropertyClasses.Mensa;

import javafx.beans.property.SimpleStringProperty;

public class StringPropertyIngredient {
    private SimpleStringProperty nome;

    public StringPropertyIngredient(String nome) {
        this.nome = new SimpleStringProperty(nome);
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
