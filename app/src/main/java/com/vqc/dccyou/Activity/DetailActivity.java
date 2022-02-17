package com.vqc.dccyou.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;

import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView tvAuthor, tvDate, tvContent, tvTitle;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
    String imageString = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deltail);
        anhXa();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            News news = (News) bundle.getSerializable("newDetail");
            imageString = news.getImage();
            Bitmap bitmap = Utils.decode(imageString);
            imageView.setImageBitmap(bitmap);
            long day = Long.parseLong(news.getDate());
            tvDate.append(Html.fromHtml("" + simpleDateFormat.format(day) + "", Html.FROM_HTML_MODE_COMPACT));
            tvContent.setText(Html.fromHtml("<p>&ensp;" + news.getContent() + "</p>", Html.FROM_HTML_MODE_COMPACT));
            tvAuthor.append(news.getUpLoadBy());
            tvTitle.setText(Html.fromHtml("<h4>&ensp;" + news.getTitle() + "</h4>", Html.FROM_HTML_MODE_COMPACT));
        }

    }

    private void anhXa() {
        imageView = findViewById(R.id.imageNewsDetail);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvDate = findViewById(R.id.tvDate);
        tvContent = findViewById(R.id.tvContent);
        tvTitle = findViewById(R.id.tvTitle);
    }
}