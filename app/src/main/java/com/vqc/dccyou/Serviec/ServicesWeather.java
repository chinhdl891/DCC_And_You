package com.vqc.dccyou.Serviec;

import static com.vqc.dccyou.Application.MyApplicationContext.CHANNEL_ID;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.vqc.dccyou.Activity.ControlActivity;
import com.vqc.dccyou.R;

public class ServicesWeather extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String nhietdo = intent.getStringExtra("nhiet_do");
        String thanhpho = intent.getStringExtra("thanhpho");
        String mota = intent.getStringExtra("mota");
        String icon = intent.getStringExtra("icon");
        UserdataNotifications(nhietdo, thanhpho, mota, icon);
        return START_NOT_STICKY;
    }

    private void UserdataNotifications(String nhietdo, String thanhpho, String mota, String icon) {
        Intent intent = new Intent(this, ControlActivity.class);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_IMMUTABLE);
        }
        int resId = getResources().getIdentifier("i" + icon, "drawable", getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),resId);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(nhietdo+"  Tại "+thanhpho)
                .setContentText(mota+" . Nhấn để xem thêm")
                .setSmallIcon(R.drawable.iconapp)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
