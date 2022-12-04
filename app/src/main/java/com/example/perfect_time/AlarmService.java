package com.example.perfect_time;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmService extends BroadcastReceiver {
    NotificationCompat.Builder builder_timer, builder_beforehandList;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel beforehand = new NotificationChannel("beforehand", "알림예고", NotificationManager.IMPORTANCE_HIGH);//체널 생성
            beforehand.setBypassDnd(true);
            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            beforehand.setLightColor(0xFFFF0000);
            notificationManager.createNotificationChannel(beforehand);


            NotificationChannel timer = new NotificationChannel("timer", "알람", NotificationManager.IMPORTANCE_LOW);//체널 생성
            timer.setBypassDnd(true);
            timer.setShowBadge(true);
            timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            timer.setLightColor(0xFFFFFFFF);
            //timer.setLockscreenVisibility();
            notificationManager.createNotificationChannel(timer);

        }

        builder_beforehandList = new NotificationCompat.Builder(context, "beforehand");

        builder_beforehandList.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_icon));
        builder_beforehandList.setSmallIcon(R.drawable.calendar_icon);
        builder_beforehandList.setTicker("알람 간단한 설명");
        builder_beforehandList.setContentTitle("Title");
        builder_beforehandList.setContentText("Content");
        builder_beforehandList.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder_beforehandList.setPriority(0);
        builder_beforehandList.setDefaults(Notification.DEFAULT_VIBRATE);
    }
}
