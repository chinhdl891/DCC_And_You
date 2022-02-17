package com.vqc.dccyou.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vqc.dccyou.Adapter.ThoiTietAdapter;
import com.vqc.dccyou.Adapter.ThoiTietHoursAdapter;
import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.Model.ThoiTiet;
import com.vqc.dccyou.Model.ThoiTietHours;
import com.vqc.dccyou.R;
import com.vqc.dccyou.DataBase.DataOffline24Hours;
import com.vqc.dccyou.DataBase.DataWeatherOffline5Day;
import com.vqc.dccyou.Serviec.ServicesWeather;

public class ControlActivity extends AppCompatActivity {
    String cityname;
    EditText editTextSearch;
    ImageButton ibtnSearch, ibtnGPS;
    TextView thanhPho, tvNhietDo, tvQuocGia, tvTrangThai, tvDoAm, tvGio, tvMay, tvNgayHomNay;
    ImageView imageIcon;
    RecyclerView recylerdubao5Ngay, recylerHours;
    ThoiTietAdapter thoiTietAdapter;
    DataWeatherOffline5Day dataWeatherOffline5Day;
    SharedPreferences sharedPreferences;
    BottomNavigationView bottomNavigationView;
    DataOffline24Hours dataOffline24Hours;
    ThoiTietHoursAdapter thoiTietHoursAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManagerHours;
    String icon = "";
    String des = "";
    ProgressBar progressBar;
    private final static int LOCATION_REQUEST_PERMISSION = 3000;
    private FusedLocationProviderClient fusedLocationClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        anhXa();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        File f = new File(
                "/data/data/" + getPackageName() + "/shared_prefs/data.xml");
        if (f.exists()) {
            progressBar.setVisibility(View.GONE);
            cityname = sharedPreferences.getString("thanh_pho", "");
        } else {
            checkServicesEnable();
            checkLocationPermission();
        }

        ibtnGPS.setOnClickListener(view -> {
            checkServicesEnable();
            checkLocationPermission();
        });
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManagerHours = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        dataWeatherOffline5Day = new DataWeatherOffline5Day(this);
        dataOffline24Hours = new DataOffline24Hours(this);

        if (!isNetworkConnected()) {
            getDataOfflien1();
        } else {
            getDataOfflien();

        }
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()) {
                    new getData().execute("https://api.openweathermap.org/data/2.5/weather?q=" + removeAccent(editTextSearch.getText().toString().replaceAll(" ", "")) + "&lang=vi&units=metric&appid=7228850b3794cc91f88182d08536c5d4");

                } else {
                    Toast.makeText(ControlActivity.this, "Cần kết nối internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionWeather:
                        break;
                    case R.id.action_News:
                        if (f.exists()) {
                            progressBar.setVisibility(View.GONE);
                            if (isNetworkConnected()) {
                                Intent intent = new Intent(ControlActivity.this, NewsActivity.class);
                                intent.putExtra("icon", sharedPreferences.getString("icon", ""));
                                intent.putExtra("nhiet_do", sharedPreferences.getString("nhiet_do", ""));
                                intent.putExtra("des", sharedPreferences.getString("mo_ta", ""));
                                intent.putExtra("thanh_pho", sharedPreferences.getString("thanh_pho", ""));
                                startActivity(intent);
                            } else {
                                Toast.makeText(ControlActivity.this, "Cần kết nối internet", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            checkServicesEnable();
                            checkLocationPermission();
                            if (isNetworkConnected()) {
                                Intent intent = new Intent(ControlActivity.this, NewsActivity.class);
                                intent.putExtra("icon", sharedPreferences.getString("icon", ""));
                                intent.putExtra("nhiet_do", sharedPreferences.getString("nhiet_do", ""));
                                intent.putExtra("des", sharedPreferences.getString("mo_ta", ""));
                                intent.putExtra("thanh_pho", sharedPreferences.getString("thanh_pho", ""));
                                startActivity(intent);
                            } else {
                                Toast.makeText(ControlActivity.this, "Cần kết nối internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void checkServicesEnable() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel, null)
                    .show();
        }
    }

    private void getDataOfflien1() {
        tvNgayHomNay.setText(sharedPreferences.getString("thoi_gian", "Ngay Thang Nam"));
        thanhPho.setText(sharedPreferences.getString("thanh_pho", "Ten thanh pho"));
        tvQuocGia.setText(" ," + sharedPreferences.getString("quoc_gia", ""));
        tvDoAm.setText(sharedPreferences.getString("do_am", ""));
        tvGio.setText(sharedPreferences.getString("gio", "Gio"));
        tvMay.setText(sharedPreferences.getString("may", "May"));
        tvTrangThai.setText(sharedPreferences.getString("mo_ta", "Trang Thai"));
        tvNhietDo.setText(sharedPreferences.getString("nhiet_do", "Nhiet Do"));
        int resId = this.getResources().getIdentifier("i" + sharedPreferences.getString("icon", ""), "drawable", this.getPackageName());
        imageIcon.setImageResource(resId);
        List<ThoiTiet> thoiTietList = dataWeatherOffline5Day.get5Day();
        thoiTietAdapter = new ThoiTietAdapter(thoiTietList, this, isNetworkConnected());
        recylerdubao5Ngay.setAdapter(thoiTietAdapter);
        recylerdubao5Ngay.setLayoutManager(layoutManager);
        List<ThoiTietHours> thoiTietHoursList = dataOffline24Hours.thoiTietHours();
        thoiTietHoursAdapter = new ThoiTietHoursAdapter(this, thoiTietHoursList);
        recylerHours.setAdapter(thoiTietHoursAdapter);
        recylerHours.setLayoutManager(layoutManagerHours);
    }

    private void getDataOfflien() {
        tvNgayHomNay.setText(sharedPreferences.getString("thoi_gian", "Ngay Thang Nam"));
        thanhPho.setText(sharedPreferences.getString("thanh_pho", "Ten thanh pho"));
        tvQuocGia.setText(" , " + sharedPreferences.getString("quoc_gia", ""));
        tvDoAm.setText(sharedPreferences.getString("do_am", ""));
        tvGio.setText(sharedPreferences.getString("gio", "Gio"));
        tvMay.setText(sharedPreferences.getString("may", "May"));
        tvTrangThai.setText(sharedPreferences.getString("mo_ta", "Trang Thai"));
        des = sharedPreferences.getString("mo_ta", "Trang Thai");
        tvNhietDo.setText(sharedPreferences.getString("nhiet_do", "Nhiet Do"));
        icon = sharedPreferences.getString("icon", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://openweathermap.org/img/wn/" + icon + "@2x.png");
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    imageIcon.post(new Runnable() {
                        @Override
                        public void run() {
                            imageIcon.setImageBitmap(bitmap);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        List<ThoiTiet> thoiTietList = dataWeatherOffline5Day.get5Day();
        thoiTietAdapter = new ThoiTietAdapter(thoiTietList, this, isNetworkConnected());
        recylerdubao5Ngay.setAdapter(thoiTietAdapter);
        recylerdubao5Ngay.setLayoutManager(layoutManager);
        List<ThoiTietHours> thoiTietHoursList = dataOffline24Hours.thoiTietHours();
        thoiTietHoursAdapter = new ThoiTietHoursAdapter(this, thoiTietHoursList);
        recylerHours.setAdapter(thoiTietHoursAdapter);
        recylerHours.setLayoutManager(layoutManagerHours);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION);
        } else {
            sendCurrentLocation();
        }

    }

    @SuppressLint("MissingPermission")
    private void sendCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            new getDataLoc().execute("https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&lang=vi&units=metric&appid=7228850b3794cc91f88182d08536c5d4");
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_PERMISSION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            sendCurrentLocation();
        } else {
            Toast.makeText(this, "You need to give app location permission", Toast.LENGTH_SHORT).show();
        }
    }

    final boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public class getDataLoc extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                int day = jsonObject.getInt("dt");
                long l = Long.valueOf(day);
                Date date = new Date(l * 1000);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE,dd-MM-yyyy HH:mm");
                String dayNow = simpleDateFormat.format(date);
                String cityName = jsonObject.getString("name");
                cityname = cityName;
                JSONObject object = jsonObject.getJSONObject("sys");
                String countryName = object.getString("country");
                JSONArray jsonArrayIcon = jsonObject.getJSONArray("weather");
                JSONObject jsonObjectIcon = jsonArrayIcon.getJSONObject(0);
                icon = jsonObjectIcon.getString("icon");
                getImage(icon);
//                String description = jsonObjectIcon.getString("description");
                final String[] description = {jsonObjectIcon.getString("description")};
                JSONObject jsonObjectTemp = jsonObject.getJSONObject("main");
                String temp = jsonObjectTemp.getString("temp");
                String humidity = jsonObjectTemp.getString("humidity");
                JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                String speed = jsonObjectWind.getString("speed");
                JSONObject jsonObjectMay = jsonObject.getJSONObject("clouds");
                String doMay = jsonObjectMay.getString("all");
                JSONObject jsonObjectLoc = jsonObject.getJSONObject("coord");
                double lon = jsonObjectLoc.getDouble("lon");
                double lat = jsonObjectLoc.getDouble("lat");
                getDataHouseAnd7Day(lon, lat);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String firstLetter = description[0].substring(0, 1);
                        String remainingLetters = description[0].substring(1, description[0].length());
                        firstLetter = firstLetter.toUpperCase(Locale.ROOT);
                        description[0] = firstLetter + remainingLetters;
                        tvTrangThai.setText(description[0]);
                        editTextSearch.setText(cityName + "," + countryName);
                        tvNgayHomNay.setText(dayNow);
                        thanhPho.setText(cityName);
                        tvNhietDo.setText(temp.replace(",", "") + "°C");
                        tvDoAm.setText(humidity.replace(",", "") + "%");
                        tvGio.setText(speed.replace(",", "") + "m/s");
                        tvMay.setText(doMay);
                        tvQuocGia.setText(" ," + countryName);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("thanh_pho", cityName);
                        editor.putString("quoc_gia", countryName);
                        editor.putString("thoi_gian", simpleDateFormat.format(date));
                        editor.putString("nhiet_do", temp.replace(",", "") + "°C");
                        editor.putString("mo_ta", description[0]);
                        editor.putString("do_am", (humidity.replace(",", "") + "%"));
                        editor.putString("may", doMay);
                        editor.putString("gio", speed.replace(",", "") + "m/s");
                        editor.putString("icon", icon);
                        editor.commit();
                        Intent intent = new Intent(ControlActivity.this, ServicesWeather.class);
                        intent.putExtra("nhiet_do", temp + "°C");
                        intent.putExtra("thanhpho", cityName);
                        intent.putExtra("mota", description[0]);
                        intent.putExtra("icon", icon);
                        startService(intent);
                        progressBar.setVisibility(View.GONE);

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ControlActivity.this, "Cần kết nối internet", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class getData extends AsyncTask<String, Void, String> {
        StringBuilder content = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "") {
                Toast.makeText(ControlActivity.this, "Hiện tại chưa có dữ liệu về thành phố " + editTextSearch.getText().toString() + "", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject objectLoc = jsonObject.getJSONObject("coord");
                    double lon = objectLoc.getDouble("lon");
                    double lat = objectLoc.getDouble("lat");
                    String name = jsonObject.getString("name");
                    thanhPho.setText(name);
                    cityname = name;
                    getDataHouseAnd7Day(lon, lat);
                    int day = jsonObject.getInt("dt");
                    long l = Long.valueOf(day);
                    Date date = new Date(l * 1000);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE,dd-MM-yyyy HH:mm");
                    String Day = simpleDateFormat.format(date);
                    tvNgayHomNay.setText(Day);
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String status = object.getString("main");
                    icon = object.getString("icon");
                    getImage(icon);
                    final String[] description = {object.getString("description")};
                    JSONObject objectMain = jsonObject.getJSONObject("main");
                    String doC = objectMain.getString("temp");
                    String doam = objectMain.getString("humidity");
                    Double nhietdo = Double.parseDouble(doC);
                    DecimalFormat f = new DecimalFormat("##.00");
                    JSONObject objectWind = jsonObject.getJSONObject("wind");
                    String speedGio = objectWind.getString("speed");
                    JSONObject clouds = jsonObject.getJSONObject("clouds");
                    String doMay = clouds.getString("all");
                    JSONObject sys = jsonObject.getJSONObject("sys");
                    String tenQuocGia = sys.getString("country");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String firstLetter = description[0].substring(0, 1);
                            String remainingLetters = description[0].substring(1, description[0].length());
                            firstLetter = firstLetter.toUpperCase(Locale.ROOT);
                            description[0] = firstLetter + remainingLetters;
                            tvTrangThai.setText(description[0]);
                            tvNhietDo.setText(nhietdo + "°C");
                            tvDoAm.setText(doam + "%");
                            tvGio.setText(speedGio + "m/s");
                            tvMay.setText(doMay);
                            tvQuocGia.setText(" ," + tenQuocGia);
                        }
                    });
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("thanh_pho", name);
                    editor.putString("quoc_gia", tenQuocGia);
                    editor.putString("thoi_gian", simpleDateFormat.format(date));
                    editor.putString("nhiet_do", nhietdo + "°C");
                    editor.putString("mo_ta", description[0]);
                    editor.putString("do_am", doam);
                    editor.putString("may", doMay);
                    editor.putString("gio", speedGio + "m/s");
                    editor.putString("icon", icon);
                    editor.commit();
                    Intent intent = new Intent(ControlActivity.this, ServicesWeather.class);
                    intent.putExtra("nhiet_do", nhietdo + "°C");
                    intent.putExtra("thanhpho", name);
                    intent.putExtra("mota", description[0]);
                    intent.putExtra("icon", icon);
                    startService(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void getImage(String icon) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://openweathermap.org/img/wn/" + icon + "@2x.png");
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    imageIcon.post(new Runnable() {
                        @Override
                        public void run() {
                            imageIcon.setImageBitmap(bitmap);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void getDataHouseAnd7Day(double lon, double lat) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<ThoiTietHours> thoiTietHoursList = new ArrayList<>();
                    List<ThoiTiet> thoiTietList = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    URL url = new URL("https://openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&lang=vi&units=metric&appid=439d4b804bc8187953eb36d2a8c26a02");
                    InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line);
                    }
                    JSONObject object = new JSONObject(content.toString());
                    JSONArray jsonArray = object.getJSONArray("daily");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object1 = jsonArray.getJSONObject(i);
                        int day = object1.getInt("dt");
                        long l = Long.valueOf(day);
                        Date date = new Date(l * 1000);
//                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm:ss");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String Day = simpleDateFormat.format(date);
                        JSONObject object2 = object1.getJSONObject("temp");
                        String tempDay = object2.getString("day");
                        String tempMax = object2.getString("max");
                        String temMin = object2.getString("min");
                        JSONArray arrayIcon = object1.getJSONArray("weather");
                        JSONObject jsonObjectIcon = arrayIcon.getJSONObject(0);
                        String icon = jsonObjectIcon.getString("icon");
                        ThoiTiet thoiTiet100 = new ThoiTiet(i, Day, icon, tempMax, temMin);
                        thoiTietList.add(thoiTiet100);
                        dataWeatherOffline5Day.updateOrInsert(thoiTiet100);
                    }
                    JSONArray jsonArrayTime = object.getJSONArray("hourly");
                    for (int i = 0; i < jsonArrayTime.length(); i++) {
                        JSONObject obj = jsonArrayTime.getJSONObject(i);
                        int day = obj.getInt("dt");
                        long l = Long.valueOf(day);
                        Date date = new Date(l * 1000);
//                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm:ss");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        String hour = simpleDateFormat.format(date);
                        String temp = obj.getString("temp");
                        JSONArray arrayIcon = obj.getJSONArray("weather");
                        JSONObject objIcon = arrayIcon.getJSONObject(0);
                        String icon = objIcon.getString("icon");
                        ThoiTietHours thoiTietHours1 = new ThoiTietHours(i, icon, temp, hour);
                        thoiTietHoursList.add(thoiTietHours1);
                        dataOffline24Hours.updateOrInsert(thoiTietHours1);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thoiTietAdapter = new ThoiTietAdapter(thoiTietList, ControlActivity.this, isNetworkConnected());
                            recylerdubao5Ngay.setAdapter(thoiTietAdapter);
                            recylerdubao5Ngay.setLayoutManager(layoutManager);
                            ThoiTietHoursAdapter thoiTietHoursAdapter = new ThoiTietHoursAdapter(ControlActivity.this, thoiTietHoursList);
                            recylerHours.setAdapter(thoiTietHoursAdapter);
                            recylerHours.setLayoutManager(layoutManagerHours);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }

    private void anhXa() {
        progressBar = findViewById(R.id.weatherProgressBar);
        recylerHours = findViewById(R.id.recylerHours);
        editTextSearch = findViewById(R.id.editCitySearch);
        ibtnSearch = findViewById(R.id.ibtnSearch);
        ibtnGPS = findViewById(R.id.ibtnGPS);
        thanhPho = findViewById(R.id.thanhPhoName);
        tvQuocGia = findViewById(R.id.quocGiaName);
        tvNhietDo = findViewById(R.id.nhietDo);
        tvTrangThai = findViewById(R.id.trangThai);
        tvDoAm = findViewById(R.id.doAm);
        tvMay = findViewById(R.id.may);
        tvGio = findViewById(R.id.gio);
        tvNgayHomNay = findViewById(R.id.ngayHomNay);
        recylerdubao5Ngay = findViewById(R.id.lvDuBao5Ngay);
        imageIcon = findViewById(R.id.imageIcon);
        bottomNavigationView = findViewById(R.id.navigationBottom);
    }
}