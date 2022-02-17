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

import com.vqc.dccyou.Model.ThoiTiet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataWeatherOffline5Day extends SQLiteOpenHelper {

    public final static String ID = "id";
    public final static String TEMP_MAX = "temp_max";
    public final static String TEMP_MIN = "tem_min";
    public final static String DAY = "day";
    public final static String ICON = "icon";
    public final static String TB_FORECAST = "forecast";
    String stringBase64 = "";

    public final static String DB_NAME = "database2.db";
    public final static String CREATE_TB = "create table " + TB_FORECAST + "("
            + ID + " integer primary key,"
            + TEMP_MAX + " text,"
            + TEMP_MIN + " text,"
            + DAY + " text,"
            + ICON + " text)";


    public DataWeatherOffline5Day(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, 2);

    }

    public DataWeatherOffline5Day(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void execte(String sql) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TB_FORECAST);

    }

    public void updateOrInsert(ThoiTiet thoiTiet) {
        if (isExist(thoiTiet)) {
            updateData(thoiTiet);
        } else {
            insertData(thoiTiet);
        }
    }

    public boolean isExist(ThoiTiet thoiTiet) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select count(*) from " + TB_FORECAST + " where " + ID + "=?", new String[]{String.valueOf(thoiTiet.getId())});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        return count > 0;
    }

    public void insertData(ThoiTiet thoiTiet) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, thoiTiet.getId());
        contentValues.put(TEMP_MAX, thoiTiet.getMaxTemp());
        contentValues.put(TEMP_MIN, thoiTiet.getMinTemp());
        contentValues.put(DAY, thoiTiet.getDay());
        contentValues.put(ICON,thoiTiet.getIcon());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TB_FORECAST, null, contentValues);

    }

    public void updateData(ThoiTiet thoiTiet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TEMP_MAX, thoiTiet.getMaxTemp());
        contentValues.put(TEMP_MIN, thoiTiet.getMinTemp());
        contentValues.put(DAY, thoiTiet.getDay());
        contentValues.put(ICON, thoiTiet.getIcon());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update(TB_FORECAST, contentValues, ID + "=?", new String[]{String.valueOf(thoiTiet.getId())});
    }

    public List<ThoiTiet> get5Day() {
        List<ThoiTiet> thoiTietList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
      try {
          Cursor cursor = database.rawQuery("select * from (select * from forecast  limit 24) as w order by id asc "  ,null);
          while (cursor.moveToNext()) {
              int id = cursor.getInt(0);
              String tempMax = cursor.getString(1);
              String tempMin = cursor.getString(2);
              String day = cursor.getString(3);
              String icon = cursor.getString(4);
              thoiTietList.add(new ThoiTiet(id, day, icon, tempMax, tempMin));
          }
      }catch (Exception e){

      }
        return thoiTietList;

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
                    stringBase64 = icon;
                }
            }
        }).start();
        return stringBase64;
    }

}
