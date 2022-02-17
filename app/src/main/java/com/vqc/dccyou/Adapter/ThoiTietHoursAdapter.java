package com.vqc.dccyou.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vqc.dccyou.Model.ThoiTiet;
import com.vqc.dccyou.Model.ThoiTietHours;
import com.vqc.dccyou.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class ThoiTietHoursAdapter extends RecyclerView.Adapter<ThoiTietHoursAdapter.ViewHolder> {
    Context context;
    List<ThoiTietHours> thoiTietList;

    public ThoiTietHoursAdapter(Context context, List<ThoiTietHours> thoiTietList) {
        this.context = context;
        this.thoiTietList = thoiTietList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hours, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThoiTietHours thoiTiet = thoiTietList.get(position);
        holder.temp.setText(thoiTiet.getTemp() + "°C");
        holder.time.setText(thoiTiet.getDate());
        String icon = thoiTiet.getIcon();
        int resId = context.getResources().getIdentifier("i"+icon, "drawable", context.getPackageName());
        holder.imageView.setImageResource(resId);

    }

    @Override
    public int getItemCount() {
        return thoiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, temp;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tvhours);
            temp = itemView.findViewById(R.id.tempHours);
            imageView = itemView.findViewById(R.id.imgHours);
        }
    }
}
