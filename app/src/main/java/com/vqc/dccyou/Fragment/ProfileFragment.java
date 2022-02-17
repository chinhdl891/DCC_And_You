package com.vqc.dccyou.Fragment;

import static android.app.Activity.RESULT_OK;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vqc.dccyou.Activity.LoginActivity;
import com.vqc.dccyou.Activity.RegisterActivity;
import com.vqc.dccyou.Activity.SuaActivity;
import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.FireBase.FireBaseDataBase;
import com.vqc.dccyou.Model.Acc;
import com.vqc.dccyou.Model.AccCheck;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {
    ImageView imageViewAvata, imageViewBg, imgChangeAvt;
    EditText  edEmail, edPass, edRePass;
    TextView edUserName;
    String email = "", pass = "", username = "", imageStr = "", repass = "", userName = "",imgOld="",emailOld,passOld,userNameOld;
    Button btnDangXuat, btnCapNhat;
    List<Acc> accList;
    boolean check = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        anhXa(view);
        accList = new ArrayList<>();
        String key = MyApplicationContext.getInstance().getKey();
        getAccInfo(key);
        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isEmailValid(charSequence.toString())) {
                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                    email = charSequence.toString();
                } else {
                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = editable.toString();
            }
        });
        edPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (isPass(charSequence.toString())) {
                    edPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                    pass = charSequence.toString();
                } else {
                    edPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edRePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                repass= charSequence.toString();
                if (isPass(repass)) {
                    if (pass.equals(charSequence.toString())) {
                        edRePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0);
                    } else {
                        edRePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                    }

                } else {
                    edRePass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        imgChangeAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog alertDialog = builder.create();
                builder.setTitle("Xác Nhận");
                TextView textView = new TextView(getActivity());
                textView.setPadding(50,15,0,0);
                textView.setText("Bạn có muốn cập nhật lại thông tin ?");
                builder.setView(textView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ( edPass.getText().toString().equals(passOld) && edEmail.getText().toString().equals(emailOld) && !imageStr.equals(imgOld)){
                            Acc acc1 = new Acc(MyApplicationContext.getInstance().getId(), email, pass, MyApplicationContext.getInstance().rule, username, key, imageStr);
                            FireBaseDataBase.updateAcc(acc1, getActivity());
                        }else {
                            int kt = 0;
                            if (!pass.equals(repass)) {
                                check = false;
                                Toast.makeText(getActivity(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                            } else if (!isEmailValid(email)) {
                                check = false;
                                Toast.makeText(getActivity(), "Sai định dạng Email", Toast.LENGTH_SHORT).show();
                            } else if (!isPass(pass)) {
                                edPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                            } else {
                                check = true;
                            }

                            for (int i = 0; i < accList.size(); i++) {
                                Acc acc = accList.get(i);

                                if (email.equals(acc.getEmail())) {
                                    kt = 1;
                                    edEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_error_outline_24, 0);
                                    Toast.makeText(getActivity(), "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }


                            if (check && kt == 0) {
                                Acc acc = new Acc(MyApplicationContext.getInstance().getId(), email, pass, MyApplicationContext.getInstance().rule, username, key, imageStr);
                                FireBaseDataBase.updateAcc(acc, getActivity());
                            }
                        }



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getAccInfo(key);
                        alertDialog.cancel();
                    }
                });
                builder.show();

            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    private void anhXa(View view) {
        imageViewBg = view.findViewById(R.id.imageBackgrond);
        imageViewAvata = view.findViewById(R.id.imageIconAvata);
        edUserName = view.findViewById(R.id.UserName);
        edPass = view.findViewById(R.id.editTextPassPro);
        edRePass = view.findViewById(R.id.editTextRePassPro);
        edEmail = view.findViewById(R.id.editTextEmailPro);
        imgChangeAvt = view.findViewById(R.id.btnChangeImage);
        btnCapNhat = view.findViewById(R.id.btnCapNhat);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
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
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageStr = Utils.enCode(bitmap);
                imageViewAvata.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAccInfo(String key) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Acc");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Acc acc = dataSnapshot.getValue(Acc.class);
                    acc.setKey(dataSnapshot.getKey());
                    accList.add(acc);
                    if (key == acc.getKey()) {
                        imageStr = acc.getImage();
                        imgOld= imageStr;
                        username = acc.getUsername();
                        userNameOld = acc.getUsername() ;
                        email = acc.getEmail();
                        emailOld = email;
                        pass = acc.getPass();
                        passOld = pass;
                        repass=pass;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                edEmail.setText(email);
                                edUserName.setText(username);
                                edPass.setText(pass);
                                edRePass.setText(repass);
                                imageViewAvata.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (imageStr==""){
                                                imageViewBg.setImageResource(R.drawable.iconapp);
                                            }else {
                                                Bitmap bitmap = Utils.decode(imageStr);
                                                imageViewAvata.setImageBitmap(bitmap);
                                                imageViewBg.setImageBitmap(bitmap);
                                            }

                                        } catch (Exception exception) {

                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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

