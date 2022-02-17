package com.vqc.dccyou.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplicationContext extends Application {
    public static MyApplicationContext instance;
    public long id;
    public String email;
    public String pass;
    public int rule;
    public String username;
    public String key;
    public static final String CHANNEL_ID = "channel_services";
    public String iconThoiTiet;
    public String nhietDo;
    public String mota ;
    public String thanhpho;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        createChannelNotification();
    }

    private void createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID
                    , "Channel Services"
                    , NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }

        }
    }

    public static MyApplicationContext getInstance() {
        return instance;
    }

    public static void setInstance(MyApplicationContext instance) {
        MyApplicationContext.instance = instance;
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

    public String getIconThoiTiet() {
        return iconThoiTiet;
    }

    public void setIconThoiTiet(String iconThoiTiet) {
        this.iconThoiTiet = iconThoiTiet;
    }

    public String getNhietDo() {
        return nhietDo;
    }

    public void setNhietDo(String nhietDo) {
        this.nhietDo = nhietDo;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getThanhpho() {
        return thanhpho;
    }

    public void setThanhpho(String thanhpho) {
        this.thanhpho = thanhpho;
    }
}
