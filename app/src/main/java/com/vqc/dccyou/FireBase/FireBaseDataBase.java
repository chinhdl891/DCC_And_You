package com.vqc.dccyou.FireBase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vqc.dccyou.Activity.AdminActivity;
import com.vqc.dccyou.Activity.LoginActivity;
import com.vqc.dccyou.Activity.RegisterActivity;
import com.vqc.dccyou.Activity.ThankYouActivitie;
import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.Interface.FireDataBaseInterface;
import com.vqc.dccyou.Model.AccCheck;
import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.Model.Acc;
import com.vqc.dccyou.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseDataBase {

    static DatabaseReference databaseReference = FirebaseDatabase.
            getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
            getReference().child("News");
    static DatabaseReference databaseReferenceAcc = FirebaseDatabase.
            getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("/Acc/");
    static DatabaseReference databaseReferenceAccUser = FirebaseDatabase.
            getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("/AccUser/");

    public static void addAcc(Acc acc, Context context) {
        databaseReferenceAcc.push().setValue(acc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Intent intent = new Intent(context, ThankYouActivitie.class);
                intent.putExtra("email", acc.getEmail());
                intent.putExtra("pass", acc.getPass());
                MyApplicationContext.getInstance().setEmail(acc.getEmail());
                MyApplicationContext.getInstance().setPass(acc.getPass());
                Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
        AccCheck accUser = new AccCheck(acc.getEmail(), acc.getUsername());
        databaseReferenceAccUser.push().setValue(accUser);
    }


    public static void deleteNews(String key, DatabaseReference.CompletionListener completionListener) {
        databaseReference.child(key).removeValue(completionListener);
    }

    public static void updateNews(News news, Context context) {
        String key = news.getKey();
        String image = news.getImage();
        Task<Void> databaseReference0 = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("/News/").child(key).child("content").setValue(news.getContent());
        Task<Void> databaseReference1 = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("/News/").child(key).child("date").setValue(news.getDate());
        Task<Void> databaseReference2 = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("/News/").child(key).child("id").setValue(news.getId());
        Task<Void> databaseReference3 = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("/News/").child(key).child("image").setValue(image);
        databaseReference3.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AdminActivity.class);
                context.startActivity(intent);
            }
        });
        Task<Void> databaseReference4 = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("/News/").child(key).child("key").setValue(news.getKey());
        Task<Void> databaseReference5 = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("/News/").child(key).child("title").setValue(news.getTitle());
        Task<Void> databaseReference6 = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("/News/").child(key).child("upLoadBy").setValue(news.getUpLoadBy());
    }

    public static void addNew(News news, Context context) {
        databaseReference.push().setValue(news, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Thêm tin thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AdminActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public static void updateAcc(Acc acc, Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("/Acc/");
        String keyAcc = acc.getKey();
        databaseReference.child(keyAcc).child("email").setValue(acc.getEmail());
        databaseReference.child(keyAcc).child("pass").setValue(acc.getPass());
        databaseReference.child(keyAcc).child("image").setValue(acc.getImage(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference.child(keyAcc).child("username").setValue(acc.getUsername());
    }

    public static void setAccRule(Acc acc, Context context) {
        databaseReferenceAcc.child(acc.getKey()).child("rule").setValue(acc.rule, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Query getDataSearch() {
            return databaseReference.orderByKey();
    }
    public static Query getData(String key) {
        Log.d("aaa", "getData: "+key);
      if (key==""){
          return databaseReference.orderByKey().limitToFirst(5);
      }
        return databaseReference.orderByKey().startAt(key).limitToFirst(5);
    }



}
