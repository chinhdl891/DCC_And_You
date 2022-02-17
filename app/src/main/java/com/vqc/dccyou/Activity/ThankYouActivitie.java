package com.vqc.dccyou.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vqc.dccyou.R;

public class ThankYouActivitie extends AppCompatActivity {
TextView textViewCamOn;
Button buttonbackToLogin;
String pass,email;
String content = "Cám ơn bạn đã tin tưởng và ủng hộ DCC & You. Hiện tại chúng tôi đã nhận được yêu cầu đăng tin của bạn. Chúng tôi sẽ liên hệ với bạn ngay sau khi tài khoản được duyệt";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_activitie);
        anhXa();
        Intent intent = getIntent();
        if (intent!=null){
            pass= intent.getStringExtra("pass");
            email = intent.getStringExtra("email");
        }
        buttonbackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThankYouActivitie.this, LoginActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("pass", pass);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
            }
        });
        textViewCamOn.setText(Html.fromHtml("<p>&ensp;&ensp;" + content + "</p>", Html.FROM_HTML_MODE_COMPACT));

    }

    private void anhXa() {
        buttonbackToLogin = findViewById(R.id.backToLogin);
        textViewCamOn = findViewById(R.id.textCamOn);
    }
}