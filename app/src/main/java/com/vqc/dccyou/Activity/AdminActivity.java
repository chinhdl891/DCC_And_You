package com.vqc.dccyou.Activity;




import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.Fragment.AdminAccFragment;
import com.vqc.dccyou.Fragment.AdminNewsFragment;
import com.vqc.dccyou.Fragment.ProfileFragment;

import com.vqc.dccyou.R;


public class AdminActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        anhXa();
        getFragment(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            int rule = MyApplicationContext.getInstance().getRule();
            switch (menuItem.getItemId()) {
                case R.id.actionUser:
                  if (rule==2){
                      fragment = new ProfileFragment();
                  }
                  if (rule==3){
                      fragment = new AdminAccFragment();
                  }
                    break;
                default:
                    fragment = new AdminNewsFragment();

                    break;
            }
            getFragment(fragment);
            return true;
        });
    }

    private void getFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment == null) {
            fragment = new AdminNewsFragment();
        }
        fragmentTransaction.replace(R.id.frameConten, fragment);
        fragmentTransaction.addToBackStack("hello");
        fragmentTransaction.commit();
    }

    private void anhXa() {
        bottomNavigationView = findViewById(R.id.navigationBottom);
    }
}