package sk.tuke.gamestudio.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private int ident;
    private String login;

    private String password;

    public Customer() {
    }

    public Customer(String login, String password){
        this.login=login;
        this.password=password;
    }

    public int getIdent() {
        return ident;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public void setIdent(int ident) {
        this.ident = ident;
    }
    public void setPassword(String password){
        this.password=password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

