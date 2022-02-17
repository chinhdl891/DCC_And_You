package com.vqc.dccyou.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vqc.dccyou.Adapter.NewsAdapter;
import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.FireBase.FireBaseDataBase;
import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewsActivity extends AppCompatActivity {
    ImageView imgLogin, imgThoiTiet;
    TextView tvTemp, tvDes;
    List<News> newsList;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ProgressBar progressBar;
    EditText edSearch;
    boolean isLoading = false;
    RecyclerView.LayoutManager layoutManager;
    String key = "";
    String latstKey = "";
    String search = null;
    HashSet<News> newsHashSet;
    String icon = "";
    String temp = "";
    String des = "";
    String city = "";
    boolean reload = false;
//    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        anhXa();
        newsList = new ArrayList<>();
        newsHashSet = new HashSet<>();
        layoutManager = new LinearLayoutManager(this);
        imgThoiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsActivity.this, ControlActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            icon = intent.getStringExtra("icon");
            temp = intent.getStringExtra("nhiet_do");
            des = intent.getStringExtra("des");
            city = intent.getStringExtra("thanh_pho");
            reload = intent.getBooleanExtra("reload", true);
            if (icon == null || des == null || city == null || temp == null) {
                icon = MyApplicationContext.getInstance().getIconThoiTiet();
                temp = MyApplicationContext.getInstance().getNhietDo();
                des = MyApplicationContext.getInstance().getMota();
                city = MyApplicationContext.getInstance().getThanhpho();
            }
            MyApplicationContext.getInstance().setThanhpho(city);
            MyApplicationContext.getInstance().setMota(des);
            MyApplicationContext.getInstance().setNhietDo(temp);
            MyApplicationContext.getInstance().setIconThoiTiet(icon);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvDes.setText(des);
                    tvTemp.setText(temp + ", " + city);
                    int resId = getResources().getIdentifier("i" + icon, "drawable", getPackageName());
                    imgThoiTiet.setImageResource(resId);

                }
            });
        }
//        getData();
        getDataSearch("");
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = charSequence.toString();
                if (charSequence.toString() != null) {
                    getDataSearch(search);
                } else {
                    getDataSearch(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            Timer timer = new Timer();
            private long DELAY = 1000;

            @Override
            public void afterTextChanged(Editable editable) {
                search = editable.toString();
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getDataSearch(editable.toString());
                    }
                }, DELAY);

            }

        });
        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NewsActivity.this, LoginActivity.class);
                intent1.putExtra("reload", true);
                intent1.putExtra("icon", icon);
                intent1.putExtra("nhiet_do", temp);
                intent1.putExtra("des", des);
                intent1.putExtra("thanh_pho", city);
                startActivity(intent1);

            }
        });

    }
//    private void getData() {
//        FireBaseDataBase.getData(latstKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                newsList.clear();
//                swipeRefreshLayout.setRefreshing(true);
//                if (reload == true) {
//                    if (newsList.size() > 0) {
//                        newsList.clear();
//                    }
//                    if (newsHashSet.size() > 0) {
//                        newsHashSet.clear();
//                    }
//                }
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    reload = false;
//                    News news = dataSnapshot.getValue(News.class);
//                    if ((dataSnapshot.getValue(News.class) != null)) {
//                        key = dataSnapshot.getKey();
//                        newsHashSet.add(news);
//                        latstKey = dataSnapshot.getKey();
//                    } else {
//                        isLoading=true;
//                        break;
//                    }
//                }
//
//                newsList = new ArrayList<>(newsHashSet);
//                if (newsList.size()>5){
//                   newsList.remove(newsList.size()-1);
//                }
////                Collections.sort(newsList, (News news, News t1) -> {
////                    Long date = Long.parseLong(news.getDate());
////                    Long date1 = Long.parseLong(t1.getDate());
////                    if (date > date1) {
////                        return -1;
////                    } else {
////                        if (date == date1) {
////                            return 0;
////                        } else {
////                            return 1;
////                        }
////                    }
////                });
//                //cho cai nay no sap xep giong chieu bay gio cai array lam gi co lap
//
//                swipeRefreshLayout.setRefreshing(false);
//                progressBar.setVisibility(View.GONE);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
////                        if (newsList.size() > 0) {
////                        newsList.remove(0);
////                        } else if (newsList.size() > ) {
////                            for (int i = 0; i < newsList.size(); i++) {
////                                if (i == 0) {
////                                    newsList.remove(1);
////                                } else {
////                                    newsList.remove(i * 2);
////                                }
////                            }
////                        }
//                        newsAdapter = new NewsAdapter(newsList, NewsActivity.this);
//                        newsAdapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(newsAdapter);
//                        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                                int totalItem = linearLayoutManager.getItemCount();
//                                int pastVisiable = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//                                int visiable = linearLayoutManager.getChildCount();
//                                if (!isLoading) {
//                                    if ((pastVisiable + visiable) >= totalItem) {
//                                        isLoading = true;
//                                        swipeRefreshLayout.setRefreshing(false);
//                                        getData();
//                                    }else {
//                                        lastItem();
//                                    }
//                                }
//                            }
//                        });
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void lastItem() {
//    }


    public class getImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgThoiTiet.setImageBitmap(bitmap);
        }
    }

    private void getDataSearch(String search) {


        FireBaseDataBase.getDataSearch().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    News news = dataSnapshot.getValue(News.class);

                    if (search == "") {
                        newsList.add(news);
                        Collections.sort(newsList);
                    }
                    if (search != "") {
                        if (news.getTitle().contains(search)) {
                            newsList.add(news);//cai list nó dây
                            Collections.sort(newsList);
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsAdapter = new NewsAdapter(newsList, NewsActivity.this);
                        newsAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(newsAdapter);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void anhXa() {
//        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        progressBar = findViewById(R.id.newProgressBar);
        recyclerView = findViewById(R.id.recylerView);
        imgLogin = findViewById(R.id.btnGoToLogin);
        tvTemp = findViewById(R.id.tempNews);
        tvDes = findViewById(R.id.desTempNews);
        imgThoiTiet = findViewById(R.id.imageIcon);
        edSearch = findViewById(R.id.editTextTimKiem);
    }
}


