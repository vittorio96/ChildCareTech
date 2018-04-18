package main;

import java.io.Serializable;

public class IngredientSupplier implements Serializable{

    //inherited String nome;
    private String nome;
    private String piva;
    private String cellulare;
    private String email;

    public IngredientSupplier(String nome, String piva, String cellulare, String email) {
        this.nome = nome;
        this.piva = piva;
        this.cellulare = cellulare;
        this.email = email;
    }
}
