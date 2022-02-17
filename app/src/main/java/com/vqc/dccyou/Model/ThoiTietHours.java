package com.vqc.dccyou.Model;

public class ThoiTietHours {
    int id;
    String icon;
    String temp;
    String date;

    public ThoiTietHours(int id, String icon, String temp, String date) {
        this.id = id;
        this.icon = icon;
        this.temp = temp;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
