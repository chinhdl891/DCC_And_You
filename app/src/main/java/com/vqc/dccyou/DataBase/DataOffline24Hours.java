package com.vqc.dccyou.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.Nullable;


import com.vqc.dccyou.Model.ThoiTietHours;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataOffline24Hours extends SQLiteOpenHelper {
    public final String ID = "id";
    public final String HOUR = "hour";
    public final String ICON = "icon";
    public final String TEMP = "temps";
    public final String TB_HOURS = "tb_hour";
    public final static String DB_NAME_HOURS = "database_hours3.db";
    public final String CREATE_TB_HOURS = " create table " + TB_HOURS + "(" + ID + " integer primary key, "
            + HOUR + " text,"
            + ICON + " text,"
            + TEMP + " text" + ")";
    String stringBase64 = "";

    public DataOffline24Hours(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void execute(String sql) {
        SQLiteDatabase database = getWritableDatabase();
//        database.execSQL(sql);
    }

    public DataOffline24Hours(Context context) {
        super(context, DB_NAME_HOURS, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TB_HOURS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TB_HOURS);
    }

    public void updateOrInsert(ThoiTietHours thoiTiet) {
        if (isExists(thoiTiet)) {
            update(thoiTiet);
        } else {
            insert(thoiTiet);
        }
    }

    public boolean isExists(ThoiTietHours thoiTietHours) {
        int count = 0;
        SQLiteDatabase database = getReadableDatabase();
        try {
            Cursor cursor = database.rawQuery("select count(*) from " + TB_HOURS + " where " + ID + "=?", new String[]{String.valueOf(thoiTietHours.getId())});

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }catch (Exception e){
            count = 0;
        }
        return count > 0;

    }

    public void insert(ThoiTietHours thoiTietHours) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, thoiTietHours.getId());
        contentValues.put(HOUR, thoiTietHours.getDate());
        contentValues.put(ICON, thoiTietHours.getIcon());
        contentValues.put(TEMP, thoiTietHours.getTemp());
        SQLiteDatabase database = getWritableDatabase();
        database.insert(TB_HOURS, null, contentValues);
    }

    public void update(ThoiTietHours thoiTietHours) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOUR, thoiTietHours.getDate());
        contentValues.put(ICON, thoiTietHours.getIcon());
        contentValues.put(TEMP, thoiTietHours.getTemp());
        SQLiteDatabase database = getWritableDatabase();
        database.update(TB_HOURS, contentValues, ID + "=?", new String[]{String.valueOf(thoiTietHours.getId())});
    }
    public List<ThoiTietHours> thoiTietHours(){

        SQLiteDatabase database = getReadableDatabase();
        List<ThoiTietHours> thoiTietHours = new ArrayList<>();
       try {
           Cursor cursor = database.rawQuery("select * from (select * from tb_hour order by id asc limit 24 ) as w order by id asc", null);
           while (cursor.moveToNext()){
               int id = cursor.getInt(0);
               String hour = cursor.getString(1);
               String icon = cursor.getString(2);
               String temp = cursor.getString(3);
               thoiTietHours.add(new ThoiTietHours(id,icon,temp,hour));
           }
       }catch (Exception e){

       }
        return thoiTietHours;
    }

    public String getIconImage(String icon) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://openweathermap.org/img/wn/" + icon + ".png");
                    InputStream inputStream = url.openConnection().getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    stringBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return stringBase64;
    }

}
