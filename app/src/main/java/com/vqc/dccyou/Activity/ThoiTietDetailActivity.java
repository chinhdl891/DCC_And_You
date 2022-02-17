package com.vqc.dccyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;



import com.vqc.dccyou.Model.ThoiTiet;
import com.vqc.dccyou.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThoiTietDetailActivity extends AppCompatActivity {

RecyclerView recyclerView;
List<ThoiTiet>  thoiTietList = new ArrayList<>();
    int batDau=0;
    int ketThuc=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoi_tiet);
        recyclerView = findViewById(R.id.listView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
         batDau = bundle.getInt("batDau");
         ketThuc = bundle.getInt("ketThuc");
        String city = bundle.getString("cityName");
        new getData().execute("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&lang=vi&units=metric&appid=7228850b3794cc91f88182d08536c5d4");

    }
    public class getData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line ="";
                while ((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i =batDau ; i < ketThuc; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String dt_txt = object.getString("dt_txt");
                    JSONObject objTemp = object.getJSONObject("main");
                    String temp_min = objTemp.getString("temp_min");
                    String temp_max = objTemp.getString("temp_max");
                    JSONArray jsonArrayIcon = object.getJSONArray("weather");
                    JSONObject jsonObjectIcon = jsonArrayIcon.getJSONObject(0);
                    String icon = jsonObjectIcon.getString("icon");
                    String trangThai = jsonObjectIcon.getString("description");
                 //   thoiTietList.add(new ThoiTiet(dt_txt, icon, trangThai, temp_max, temp_min));
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            recyclerView.setAdapter(new ThoiTietDeltailAdapter(thoiTietList,ThoiTietDetailActivity.this));
//                            recyclerView.setLayoutManager(new LinearLayoutManager(ThoiTietDetailActivity.this,RecyclerView.VERTICAL,false));
//                        }
//                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}