package com.vqc.dccyou.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class News implements Comparable<News>, Serializable {
    private String content;
    private String date;
    private String id;
    private String image;
    private String title;
    private String upLoadBy;
    private String key;
    private String keyUpload;
    public News() {
    }

    public News(String content, String date, String id, String image, String title, String upLoadBy, String key, String keyUpload) {
        this.content = content;
        this.date = date;
        this.id = id;
        this.image = image;
        this.title = title;
        this.upLoadBy = upLoadBy;
        this.key = key;
        this.keyUpload = keyUpload;
    }

    public String getKeyUpload() {
        return keyUpload;
    }

    public void setKeyUpload(String keyUpload) {
        this.keyUpload = keyUpload;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpLoadBy() {
        return upLoadBy;
    }

    public void setUpLoadBy(String upLoadBy) {
        this.upLoadBy = upLoadBy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "News{" +
                "content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", upLoadBy='" + upLoadBy + '\'' +
                ", key='" + key + '\'' +
                ", keyUpload='" + keyUpload + '\'' +
                '}';
    }

    @Override
    public int compareTo(News news) {
        long date1 = Long.parseLong(news.getDate());
        long date2 = Long.parseLong(date);
         if(date2 < date1){
          return  1;
      }else if(date ==news.getDate()){
          return 0;
      }
      else{
         return -1;
      }//chay di

    }
}
