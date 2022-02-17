package com.vqc.dccyou.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.FireBase.FireBaseDataBase;
import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SuaActivity extends AppCompatActivity {
    News news;
    EditText edTitle, edContent;
    String title = "", content = "";
    TextView tvId;
    ImageView imageView;
    Button buttonChangeImage, btnUpdate;
    String strImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            news = (News) bundle.getSerializable("objNew");
        }
        strImage = news.getImage();
        setContentView(R.layout.activity_sua);
        anhXa();
        edContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    edContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                } else {
                    edContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    edTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                } else {
                    edTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaActivity.this);
                AlertDialog alertDialog = builder.create();
                builder.setTitle("Xác Nhận");
                builder.setMessage("Bạn có muốn thay đổi nội dung tin ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        news.setContent(edContent.getText().toString());
                        news.setUpLoadBy(MyApplicationContext.getInstance().getUsername());
                        news.setTitle(edTitle.getText().toString());
                        if (!edContent.getText().toString().equals("") && !edTitle.getText().toString().equals("")) {
                            FireBaseDataBase.updateNews(news, SuaActivity.this);
                        } else {
                            Toast.makeText(SuaActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                });
                builder.show();
            }
        });
        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvId.setText(news.getId() + "");
                edContent.setText(news.getContent());
                edTitle.setText(news.getTitle());
                Bitmap bitmap = Utils.decode(news.getImage());
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });


    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(SuaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(SuaActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
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
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] bytes = stream.toByteArray();
                strImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                imageView.setImageBitmap(bitmap);
                news.setImage(strImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void anhXa() {
        imageView = findViewById(R.id.imageNews);
        buttonChangeImage = findViewById(R.id.btnUpdateImage);
        btnUpdate = findViewById(R.id.btnCapNhat);
        tvId = findViewById(R.id.tvId);
        edTitle = findViewById(R.id.tvTitle);
        edContent = findViewById(R.id.tvcontent);


    }

}