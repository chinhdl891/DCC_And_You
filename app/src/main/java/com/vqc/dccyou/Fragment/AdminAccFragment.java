package com.vqc.dccyou.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.vqc.dccyou.Adapter.AdminAccAdapter;
import com.vqc.dccyou.Model.Acc;
import com.vqc.dccyou.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminAccFragment extends Fragment {
    RecyclerView recyclerViewAcc;
    EditText editTextTimKiem;
    List<Acc> accList;
    ProgressBar progressBar;
    AdminAccAdapter adminAccAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qlacc, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAcc = view.findViewById(R.id.recylerViewAdminAcc);
        editTextTimKiem = view.findViewById(R.id.editTextTimKiemAcc);
        progressBar = view.findViewById(R.id.userProgesserbar);
        accList = new ArrayList<>();
        getData("");
        editTextTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString() != null) {
                    getData("");
                } else {
                    getData(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
           private Timer timer = new Timer();
            private  final long DELAY = 1000;
            @Override
            public void afterTextChanged(Editable editable) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getData(editable.toString());
                    }
                },DELAY);
            }
        });

        return view;
    }

    private void getData(String s) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://news-2d82f-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Acc");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
               accList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Acc acc = dataSnapshot.getValue(Acc.class);
                    acc.setKey(dataSnapshot.getKey());
                    if (acc.rule == 1) {
                        if (s == "") {
                            accList.add(acc);
                        }
                        if (s != "") {
                            if (acc.getEmail().contains(s)) {
                                accList.add(acc);
                            }
                        }
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAcc.setLayoutManager(layoutManager);
                        adminAccAdapter = new AdminAccAdapter(accList, getActivity());
                        adminAccAdapter.notifyDataSetChanged();
                        recyclerViewAcc.setAdapter(adminAccAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
