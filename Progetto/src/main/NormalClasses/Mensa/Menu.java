package main.NormalClasses.Mensa;

import java.io.Serializable;

public class Menu implements Serializable{

    private String nomeM;
    private MenuTypeFlag codMenu;

    public enum MenuTypeFlag{

        MONDAY(0), TUESDAY(1), WEDNESDAY(2), THURSDAY(3), FRIDAY(4), SATURDAY(5), SUNDAY(6);

        private int orderNum;

        MenuTypeFlag(int i){
            this.orderNum = i;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public static MenuTypeFlag fromInteger(int x) {
            switch(x) {
                case 0: return MONDAY;

                case 1: return TUESDAY;

                case 2: return WEDNESDAY;

                case 3: return THURSDAY;

                case 4: return FRIDAY;

                case 5: return SATURDAY;

                case 6: return SUNDAY;

            }
            return null;
        }
        //ASSURDO MenuTypeFlag.values()[x]; ........
    }

    public Menu() {
    }

    public Menu(String nomeM, MenuTypeFlag codMenu) {
        this.nomeM = nomeM;
        this.codMenu = codMenu;

    }

    public String getNomeM() {
        return nomeM;
    }

    public void setNomeM(String nomeM) {
        this.nomeM = nomeM;
    }

    public MenuTypeFlag getCodMenu() {
        return codMenu;
    }

    public void setCodMenu(MenuTypeFlag codMenu) {
        this.codMenu = codMenu;
    }
}