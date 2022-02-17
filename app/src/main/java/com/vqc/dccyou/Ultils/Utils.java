package com.vqc.dccyou.Ultils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static String enCode(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //[12,34,231,53,.....]
       return Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);
    }
    public static  Bitmap decode(String image){
        byte[] bytes = Base64.decode(image,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
     }
}
