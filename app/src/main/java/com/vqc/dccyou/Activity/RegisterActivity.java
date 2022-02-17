package com.vqc.dccyou.Activity;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;

import android.text.TextWatcher;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vqc.dccyou.FireBase.FireBaseDataBase;
import com.vqc.dccyou.Interface.FireDataBaseInterface;
import com.vqc.dccyou.Model.Acc;
import com.vqc.dccyou.Model.AccCheck;
import com.vqc.dccyou.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    ImageView imgGotoLogin;
    Button btnRegister;
    TextView tvGotoLogin;
    EditText tvUsername, tvEmail, tvPass, tvRePass;
    String userName = "";
    String pass = "";
    String email = "";
    String repass = "";
    List<AccCheck> accList;
    boolean check = false;

    DatabaseReference databaseReferenceAccUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhXa();

        databaseReferenceAccUser = FirebaseDatabase.
                getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("/AccUser/");
        databaseReferenceAccUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AccCheck accCheck = dataSnapshot.getValue(AccCheck.class);
                    accList.add(accCheck);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isPass(charSequence.toString())) {
                    tvUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                    userName = charSequence.toString();
                } else {
                    tvUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isEmailValid(charSequence.toString())) {
                    tvEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                    email = charSequence.toString();
                } else {
                    tvEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isPass(charSequence.toString())) {
                    tvPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                    pass = charSequence.toString();
                } else {
                    tvPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvRePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                repass = charSequence.toString();
                if (isPass(repass)) {
                    if (pass.equals(charSequence.toString())) {
                        tvRePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                    } else {
                        tvRePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                    }

                } else {
                    tvRePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgGotoLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("username", tvEmail.getText().toString());
            intent.putExtra("pass", tvPass.getText().toString());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.stay);

        });
        btnRegister.setOnClickListener(view -> {
            int kt = 0;
            long id = System.currentTimeMillis();
            if (!pass.equals(repass)) {
                check = false;
                Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            } else if (!isEmailValid(email)) {
                check = false;
                Toast.makeText(RegisterActivity.this, "Sai định dạng Email", Toast.LENGTH_SHORT).show();
            }else if (!isPass(pass)){
                tvPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
            }else {
                check = true;
            }


            for (int i = 0; i < accList.size(); i++) {
                AccCheck accCheck = accList.get(i);
                if (email.equals(accCheck.getEmail())) {
                    kt = 1;
                    tvEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                    Toast.makeText(RegisterActivity.this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
                    break;
                }else if (userName.equals(accCheck.getUsername())) {
                    kt = 1;
                    tvUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                    Toast.makeText(RegisterActivity.this, "Username đã được sử dụng", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            if (check && kt == 0) {
              Acc acc = new Acc(id,email,pass,1,userName,"","");
              FireBaseDataBase.addAcc(acc,RegisterActivity.this);

            }


        });

    }

    private void anhXa() {
        imgGotoLogin = findViewById(R.id.GotoLogin2);
        tvGotoLogin = findViewById(R.id.GotoLogin1);
        tvUsername = findViewById(R.id.editTextName);
        tvPass = findViewById(R.id.editTextPass);
        tvRePass = findViewById(R.id.editTextRePass);
        tvEmail = findViewById(R.id.editTextEmail);
        btnRegister = findViewById(R.id.cirRegisterButton);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPass(String pass) {
        return pass.length() >= 6;

    }

}
