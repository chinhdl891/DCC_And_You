package com.vqc.dccyou.Model;

public class ThoiTiet {
    int id;
    String day;
    String icon;
    String status;
    String maxTemp;
    String minTemp;
    String temp;

    public ThoiTiet(int id, String day, String icon, String maxTemp, String minTemp) {
        this.id = id;
        this.day = day;
        this.icon = icon;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }



    @Override
    public String toString() {
        return "ThoiTiet{" +
                "id=" + id +
                ", day='" + day + '\'' +
                ", icon='" + icon + '\'' +
                ", maxTemp='" + maxTemp + '\'' +
                ", minTemp='" + minTemp + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
