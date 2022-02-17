package com.vqc.dccyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vqc.dccyou.Activity.DetailActivity;
import com.vqc.dccyou.Model.News;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolderNews> {
    List<News> newsList;
    Context context;
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy");
    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolderNews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolderNews(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderNews holder, int position) {

        News news = newsList.get(position);
        holder.tvTitle.setText(news.getTitle());
        holder.tvAuthor.setText("Đăng bởi: "+news.getUpLoadBy());
        long date = Long.parseLong(news.getDate());
        holder.tvDate.setText(simpleDateFormat.format(date)+"");
        String image = news.getImage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Utils.decode(image);
                holder.imgNews.post(new Runnable() {
                    @Override
                    public void run() {
                      holder.imgNews.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newDetail",news);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
    public class ViewHolderNews extends RecyclerView.ViewHolder {
        TextView tvDate, tvTitle, tvAuthor;
        ImageView imgNews;
        public ViewHolderNews(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.txtDate);
            imgNews = itemView.findViewById(R.id.imgNews);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.txtTacGia);
        }
    }

}
