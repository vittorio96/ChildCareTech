package main.NormalClasses.Mensa;

import java.io.Serializable;

public class Dish implements Serializable{

    private String nomeP;
    private DishTypeFlag tipoPiatto;

    public DishTypeFlag getTipo() {
        return tipoPiatto;
    }

    public enum DishTypeFlag{

        ANTIPASTO(0), PRIMO(1), SECONDO(2), DOLCE(3);
        private int orderNum;

        DishTypeFlag(int i){
            this.orderNum = i;
        }

        public static DishTypeFlag fromInteger(int x) {
            switch(x) {
                case 0: return ANTIPASTO;

                case 1: return PRIMO;

                case 2: return SECONDO;

                case 3: return DOLCE;

            }
            return null;
        }


        public int getOrderNum() {
            return orderNum;
        }
    }

    public Dish() {
    }

    public Dish(String nomeP, DishTypeFlag tipoPiatto) {
        this.nomeP = nomeP;
        this.tipoPiatto = tipoPiatto;
    }

    public String getNomeP() {
        return nomeP;
    }

    public void setNomeP(String nomeP) {
        this.nomeP = nomeP;
    }

    public DishTypeFlag getTipoPiatto() {
        return tipoPiatto;
    }

    public void setTipoPiatto(DishTypeFlag tipoPiatto) {
        this.tipoPiatto = tipoPiatto;
    }
}