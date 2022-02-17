package com.vqc.dccyou.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.vqc.dccyou.Activity.AdminActivity;
import com.vqc.dccyou.Activity.SuaActivity;
import com.vqc.dccyou.FireBase.FireBaseDataBase;
import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    List<News> newsList;
    Context context;

    public AdminAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.tvTitle.setText(news.getTitle());
        Bitmap bitmap = Utils.decode(news.getImage());
        holder.imgNew.post(new Runnable() {
            @Override
            public void run() {
                holder.imgNew.setImageBitmap(bitmap);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác Nhận");
                builder.setMessage(" Muốn xóa tin không ?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyItemRemoved(position);
                        FireBaseDataBase.deleteNews(news.getKey(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Intent intent = new Intent(context, AdminActivity.class);
                            }
                        });
                    }
                });
                builder.create().show();
            }

        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SuaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("objNew", news);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnUpdate, btnDelete;
        TextView tvTitle;
        ImageView imgNew;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.btnXoa);
            btnUpdate = itemView.findViewById(R.id.btnCapNhat);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgNew = itemView.findViewById(R.id.imageNews);
        }
    }
}
