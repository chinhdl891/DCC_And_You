package com.vqc.dccyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vqc.dccyou.Activity.LoginActivity;
import com.vqc.dccyou.Activity.ThemActivity;
import com.vqc.dccyou.Adapter.AdminAdapter;
import com.vqc.dccyou.Application.MyApplicationContext;
import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.R;

import java.util.ArrayList;
import java.util.List;

public class AdminNewsFragment extends Fragment {
    RecyclerView recyclerView;
    AdminAdapter adminAdapter;
    LinearLayoutManager linearLayoutManager;
    EditText editText;
    List<News> newsList;
    ProgressBar progressBar;
    ImageView btnBackAdmin, btnAddNews;
    Intent intent;
    int rule = MyApplicationContext.getInstance().getRule();
    String keyUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_news, container, false);
        btnAddNews = view.findViewById(R.id.addNews);
        btnBackAdmin = view.findViewById(R.id.back);
        progressBar = view.findViewById(R.id.newAdminProgressBar);
        keyUser = MyApplicationContext.getInstance().getKey();
        newsList = new ArrayList<>();
        btnBackAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        btnAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), ThemActivity.class);
                getActivity().startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recylerViewNewsAdmin);
        editText = view.findViewById(R.id.editTextTimKiem);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        if (rule == 3) {
            viewData("");
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString() != null) {
                        viewData("");
                    } else {
                        viewData(charSequence.toString());
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    viewData(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        if (rule == 2) {
            viewDataUser("");
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString() != null) {
                        viewDataUser("");
                    } else {
                        viewDataUser(charSequence.toString());
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    viewDataUser(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }


        return view;
    }

    private void viewData(String search) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("News");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                newsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    News news = dataSnapshot.getValue(News.class);
                    assert news != null;
                    news.setKey(dataSnapshot.getKey());
                    if (rule == 3) {
                        if (search == "") {
                            newsList.add(news);
                        }
                        if (search != "") {
                            if (news.getTitle().contains(search)) {
                                newsList.add(news);
                            }
                        }
                    }

                }
                progressBar.setVisibility(View.GONE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adminAdapter = new AdminAdapter(newsList, getActivity());
                        recyclerView.setAdapter(adminAdapter);
                        adminAdapter.notifyDataSetChanged();
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void viewDataUser(String search) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("News");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                newsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    News news = dataSnapshot.getValue(News.class);
                    assert news != null;
                    news.setKey(dataSnapshot.getKey());
                    if (news.getKeyUpload().equals(keyUser)) {
                        if (search == "") {
                            newsList.add(news);
                        }
                        if (search != "") {
                            if (news.getTitle().contains(search)) {
                                newsList.add(news);
                            }
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adminAdapter = new AdminAdapter(newsList, getActivity());
                        adminAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adminAdapter);
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                adminAdapter.notifyDataSetChanged();
                            }
                        });
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}
