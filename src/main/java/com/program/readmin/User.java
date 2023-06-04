package com.program.readmin;

public class User {
    private int id;
    private String email;
    private String login;
    private String password;
    private int role;

    public User(int id, String email, String login, String password, int role){
        this.id = id;
        this.email = email;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public int getRole() {return this.role;}

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(int role){
        this.role = role;
    }
}
