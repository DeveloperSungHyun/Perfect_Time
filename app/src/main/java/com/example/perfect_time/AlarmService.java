package com.example.perfect_time;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.perfect_time.Activity.TimerSettings;

import java.util.Calendar;

public class AlarmService extends BroadcastReceiver {
    NotificationCompat.Builder builder_timer, builder_beforehandList;

    Calendar calendar;

    @Override
    public void onReceive(Context context, Intent intent) {

        calendar = Calendar.getInstance();

//        switch (intent.getIntExtra("Type", 3)){
//            case 0: {
//                NotificationShow(context, intent);
//                break;
//            }
//            case 1: {
//                if(intent.getIntExtra("week", 7) == calendar.get(Calendar.DAY_OF_WEEK) - 1){
//                    NotificationShow(context, intent);
//                }
//                break;
//            }
//            case 2: {
//                if(intent.getIntExtra("y", 0) == calendar.get(Calendar.YEAR) &&
//                        intent.getIntExtra("m", 0) == calendar.get(Calendar.MONDAY) + 1 &&
//                        intent.getIntExtra("d", 0) == calendar.get(Calendar.DATE)){
//                    NotificationShow(context, intent);
//                }
//                break;
//            }
//        }

        if(intent.getIntExtra("Type", 3) == 0){
            NotificationShow(context, intent);
        }

        if(intent.getIntExtra("Type", 3) == 1){
            if(intent.getIntExtra("week", 7) == calendar.get(Calendar.DAY_OF_WEEK) - 1){
                NotificationShow(context, intent);
            }
        }

        if(intent.getIntExtra("Type", 3) == 2){
            if(intent.getIntExtra("y", 0) == calendar.get(Calendar.YEAR) &&
                    intent.getIntExtra("m", 0) == calendar.get(Calendar.MONDAY) + 1 &&
                    intent.getIntExtra("d", 0) == calendar.get(Calendar.DATE)){
                NotificationShow(context, intent);
            }
        }


    }

    void NotificationShow(Context context, Intent intent){
        Intent busRouteIntent = new Intent(context, TimerSettings.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(busRouteIntent);
        PendingIntent busRoutePendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);

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
        builder_beforehandList.setContentTitle(intent.getStringExtra("Name"));
        builder_beforehandList.setContentText(intent.getStringExtra("Memo"));
        builder_beforehandList.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder_beforehandList.setPriority(0);
        builder_beforehandList.setDefaults(Notification.PRIORITY_HIGH);
        builder_beforehandList.setContentIntent(busRoutePendingIntent);

        int id=(int)System.currentTimeMillis();

        notificationManager.notify(id,builder_beforehandList.build());
    }
}
