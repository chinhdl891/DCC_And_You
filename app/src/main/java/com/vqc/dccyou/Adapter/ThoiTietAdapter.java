package com.vqc.dccyou.Adapter;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.vqc.dccyou.Model.ThoiTiet;
import com.vqc.dccyou.R;
import com.vqc.dccyou.Ultils.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ThoiTietAdapter extends RecyclerView.Adapter<ThoiTietAdapter.ViewHolder> {
    List<ThoiTiet> thoiTietList;
    Context context;
    String city;
    Boolean checkInternet;

    public ThoiTietAdapter(List<ThoiTiet> thoiTietList, Context context, Boolean checkInternet) {
        this.thoiTietList = thoiTietList;
        this.context = context;
        this.checkInternet = checkInternet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
        ThoiTiet thoiTiet = thoiTietList.get(position);
        holder.tpMax.setText(thoiTiet.getMaxTemp() + "/ " + thoiTiet.getMinTemp() + "°C ");
        holder.day.setText(thoiTiet.getDay());
        String icon = thoiTiet.getIcon();
        int resId = context.getResources().getIdentifier("i"+icon, "drawable", context.getPackageName());
        holder.imageView.setImageResource(resId);
//        if (checkInternet) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        URL url = new URL("https://openweathermap.org/img/wn/" + icon + ".png");
//                        InputStream inputStream = url.openConnection().getInputStream();
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        holder.imageView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                holder.imageView.setImageBitmap(bitmap);
//                            }
//                        });
//                    } catch (Exception e) {
//                        Bitmap bitmap = Utils.decode(icon);
//                        holder.imageView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                holder.imageView.setImageBitmap(bitmap);
//                            }
//                        });
//                    }
//                }
//            }).start();
//        }

    }

    @Override
    public int getItemCount() {
        return thoiTietList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tpMax, day, tpMin;
        ImageView imageView;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tpMax = itemView.findViewById(R.id.tvMax);
            tpMin = itemView.findViewById(R.id.tvMin);
            imageView = itemView.findViewById(R.id.imageIcon);
            day = itemView.findViewById(R.id.tvNgay);
        }
    }

}

