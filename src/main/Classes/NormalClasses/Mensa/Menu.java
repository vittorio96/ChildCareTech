package main.Classes.NormalClasses.Mensa;

import java.io.Serializable;

public class Menu implements Serializable{

    private String nomeM;
    private MenuTypeFlag codMenu;

    public enum MenuTypeFlag implements Serializable{

        MONDAY(0), TUESDAY(1), WEDNESDAY(2), THURSDAY(3), FRIDAY(4), SATURDAY(5), SUNDAY(6);

        private int orderNum;

        MenuTypeFlag(int i){
            this.orderNum = i;
        }

        public int getOrderNum() {
            return orderNum;
        }
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