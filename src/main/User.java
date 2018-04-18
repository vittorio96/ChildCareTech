package main;

import java.io.Serializable;

public class User implements Serializable{
    private final String username;
    private final String password;

    public enum UserTypeFlag {
        AMMINISTRATIVO(0), SUPERVISORE(1), MENSA(2);
        private int ordernum;
        UserTypeFlag(int i){
            this.ordernum = i;
        }

        public int getOrdernum() {
            return ordernum;
        }
    }
    private UserTypeFlag userTypeFlag;
    private String codiceFiscale;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, UserTypeFlag userTypeFlag) {
        this.username = username;
        this.password = password;
        this.userTypeFlag = userTypeFlag;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() { return password; }

    public UserTypeFlag getUserTypeFlag() { return userTypeFlag; }
}
