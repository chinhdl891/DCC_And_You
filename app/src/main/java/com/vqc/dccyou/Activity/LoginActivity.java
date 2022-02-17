package com.vqc.dccyou.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.Model.Acc;
import com.vqc.dccyou.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvGoToRegister;
    EditText edEmail, edPass;
    ImageView imgGoToRegister;
    Button loginButton;
    ImageView iconBack;
    String email, pass;
    List<Acc> accList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        anhXa();

        if (intent != null) {
            email = intent.getStringExtra("email");
            pass = intent.getStringExtra("pass");
            edEmail.setText(email);
            edPass.setText(pass);

        }
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LoginActivity.this, NewsActivity.class);
                startActivity(intent1);
            }
        });
        if (MyApplicationContext.getInstance().getEmail() != null && MyApplicationContext.getInstance().getPass() != null) {
            email = MyApplicationContext.getInstance().getEmail();
            pass = MyApplicationContext.getInstance().getPass();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edEmail.setText(email);
                    edPass.setText(pass);
                }
            });
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Acc");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Acc acc = dataSnapshot.getValue(Acc.class);
                    String key = dataSnapshot.getKey();
                    acc.setKey(key);
                    accList.add(acc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        tvGoToRegister.setOnClickListener(this);
        imgGoToRegister.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        edPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass = charSequence.toString();
                if (pass.equals("")) {
                    edPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                } else {
                    edPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edEmail.setBackgroundResource(R.drawable.backgruond_botron_ed_admin);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edEmail.setBackgroundResource(R.drawable.backgruond_botron_ed_admin_input);
                email = charSequence.toString();
                if (email.equals("")) {
                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                } else {
                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edEmail.setBackgroundResource(R.drawable.backgruond_botron_ed_admin);
            }
        });
    }

    private void anhXa() {
        iconBack = findViewById(R.id.iconBackToNew);
        tvGoToRegister = findViewById(R.id.createAcc1);
        imgGoToRegister = findViewById(R.id.createAcc2);
        loginButton = findViewById(R.id.cirLogButton);
        edEmail = findViewById(R.id.editTextEmail);
        edPass = findViewById(R.id.editTextPass);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.createAcc1:
            case R.id.createAcc2:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                break;
            case R.id.cirLogButton:
                Intent intent1 = new Intent(LoginActivity.this, AdminActivity.class);
                String email = edEmail.getText().toString().trim();
                String pass = edPass.getText().toString().trim();
                int flag1 = 1;
                if (!email.equals("") && !pass.equals("")) {
                    for (int i = 0; i < accList.size(); i++) {
                        Acc acc = accList.get(i);
                        String key = acc.getKey();
                        String accEmail = acc.getEmail();
                        String accPass = acc.getPass();
                        int rule = acc.getRule();
                        if (email.equals(accEmail) && pass.equals(accPass)) {
                            flag1 = 0;
                            if (rule == 2 || rule == 3) {
                                MyApplicationContext.getInstance().setKey(key);
                                MyApplicationContext.getInstance().setPass(acc.getPass());
                                MyApplicationContext.getInstance().setEmail(acc.getEmail());
                                MyApplicationContext.getInstance().setRule(acc.getRule());
                                MyApplicationContext.getInstance().setUsername(acc.getUsername());
                                startActivity(intent1);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                                break;
                            }
                            if (rule == 1) {
                                Intent intent2 = new Intent(LoginActivity.this, ThankYouActivitie.class);
                                startActivity(intent2);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                                break;
                            }
                            if (rule == 0) {
                                Toast.makeText(LoginActivity.this, "Tài khoản của bạn từ chối trong quá trình xác thực", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (flag1 == 1) {
                        Toast.makeText(LoginActivity.this, "Tài khoản mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu bị trống", Toast.LENGTH_SHORT).show();
                }


        }

    }
}