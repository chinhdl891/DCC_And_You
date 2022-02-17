package com.vqc.dccyou.Model;

public class Acc {
    public long id;
    public String email;
    public String pass;
    public int rule;
    public String username;
    public String key;
    public String image;
    public Acc() {
    }


    public Acc(long id, String email, String pass, int rule, String username, String key, String image) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.rule = rule;
        this.username = username;
        this.key = key;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Acc{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", rule='" + rule + '\'' +
                ", username='" + username + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
