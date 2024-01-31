package com.Deadline.BackEnd.Backend.userLogin;

public class User {
    private long id;

    private String display_name;

    private String username;

    private String password;

    public User(){

    }

    public User(long id, String display_name, String username, String password) {
        this.id = id;
        this.display_name = display_name;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
