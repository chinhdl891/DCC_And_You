package com.vqc.dccyou.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vqc.dccyou.FireBase.FireBaseDataBase;
import com.vqc.dccyou.Model.Acc;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;
import java.util.List;

public class AdminAccAdapter extends RecyclerView.Adapter<AdminAccAdapter.ViewHolder> {
    List<Acc> accList;
    Context context;

    public AdminAccAdapter(List<Acc> accList, Context context) {
        this.accList = accList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qlacc,parent,false);
       return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Acc acc = accList.get(position);
        String email = acc.getEmail();
        holder.tvEmail.setText(email);
        holder.imageView.setImageResource(R.drawable.user);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acc.setRule(2);
                FireBaseDataBase.setAccRule(acc,context);
            }
        });
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acc.setRule(0);
                FireBaseDataBase.setAccRule(acc,context);
            }
        });

    }

    @Override
    public int getItemCount() {
        return accList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail;
        ImageView imageView;
        Button btnAdd, btnCancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageIconAvata);
            tvEmail = itemView.findViewById(R.id.tvEmailAcc);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
