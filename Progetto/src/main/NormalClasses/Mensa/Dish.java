
package main.NormalClasses.Mensa;

public class Dish {

    private String nomeP;
    private DishTypeFlag tipo;

    public enum DishTypeFlag {

        ANTIPASTO(0), PRIMO(1), SECONDO(2), DOLCE(3);
        private int orderNum;

        DishTypeFlag(int i){
            this.orderNum = i;
        }

        public int getOrderNum() {
            return orderNum;
        }
    }

    public Dish() {
    }

    public Dish(String nomeP, DishTypeFlag tipo) {
        this.nomeP = nomeP;
        this.tipo = tipo;
    }

    public String getNomeP() {
        return nomeP;
    }

    public void setNomeP(String nomeP) {
        this.nomeP = nomeP;
    }

    public DishTypeFlag getTipo() {
        return tipo;
    }

    public void setTipo(DishTypeFlag tipo) {
        this.tipo = tipo;
    }
}