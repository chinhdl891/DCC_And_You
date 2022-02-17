package com.vqc.dccyou.Model;

public class AccCheck {
    String email;
    String username;

    public AccCheck() {
    }

    public AccCheck(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
