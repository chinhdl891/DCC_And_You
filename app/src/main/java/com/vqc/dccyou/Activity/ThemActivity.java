package com.vqc.dccyou.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.FireBase.FireBaseDataBase;
import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;

import java.io.IOException;

public class ThemActivity extends AppCompatActivity {
    EditText tvTitle, tvContent, tvAuthor;
    ImageView imageViewNew;
    String strImage="";
    Button buttonAddImage, btnAddNew;

    String title = "";
    String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them);
        anhXa();
        tvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (content.equals("")){
                    tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }else {
                    tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                content = charSequence.toString().trim();
                if (content.equals("")){
                    tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }else {
                    tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (content.equals("")){
                    tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }else {
                    tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        tvTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (title.equals("")){
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }else {
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title = charSequence.toString();
                if (title.equals("")){
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }else {
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.equals("")){
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }else {
                    tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String image = strImage;
                long date = System.currentTimeMillis();
                long id = System.currentTimeMillis();
                if (image.equals("")) {
                    Toast.makeText(ThemActivity.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                }else {
                    if (!image.equals("") && !title.equals("") && !content.equals("")) {
                        News news = new News(content, String.valueOf(date), String.valueOf(id), image, title, MyApplicationContext.getInstance().getUsername(), "", MyApplicationContext.getInstance().getKey());
                        FireBaseDataBase.addNew(news, ThemActivity.this);
                    } else {
                        Toast.makeText(ThemActivity.this, "Điền đầy đủ thông tin ở các trường", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(ThemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(ThemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            selectImage();
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "select Image"), 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            try {
                checkPermission();
            } catch (Exception e) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                strImage = Utils.enCode(bitmap);
                imageViewNew.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void anhXa() {
        tvTitle = findViewById(R.id.tvTitleThem);
        tvContent = findViewById(R.id.tvContentThem);
        imageViewNew = findViewById(R.id.imageNewsThem);
        buttonAddImage = findViewById(R.id.addImageThem);
        btnAddNew = findViewById(R.id.addNewsThem);
    }
}