package main.entities.normal_entities.Mensa;

import main.entities.normal_entities.NormalClass;
import main.entities.string_property_entities.Mensa.StringPropertyDish;
import main.entities.string_property_entities.StringPropertyClass;

import java.io.Serializable;

public class Dish implements Serializable, NormalClass{

    private String nomeP;
    private DishTypeFlag tipoPiatto;

    public DishTypeFlag getTipo() {
        return tipoPiatto;
    }

    @Override
    public StringPropertyClass toStringProperty() {
        return new StringPropertyDish(this);
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

    public Dish(StringPropertyDish sp){
        this.nomeP = sp.getNomeP();
        this.tipoPiatto = DishTypeFlag.valueOf(sp.getTipoPiatto());
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