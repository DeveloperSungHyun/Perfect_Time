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
    NotificationCompat.Builder builder;

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
            NotificationShow(context, intent, intent.getBooleanExtra("Important", false));
        }

        if(intent.getIntExtra("Type", 3) == 1){
            if(intent.getIntExtra("week", 7) == calendar.get(Calendar.DAY_OF_WEEK) - 1){
                NotificationShow(context, intent, intent.getBooleanExtra("Important", false));
            }
        }

        if(intent.getIntExtra("Type", 3) == 2){
            if(intent.getIntExtra("y", 0) == calendar.get(Calendar.YEAR) &&
                    intent.getIntExtra("m", 0) == calendar.get(Calendar.MONDAY) + 1 &&
                    intent.getIntExtra("d", 0) == calendar.get(Calendar.DATE)){
                NotificationShow(context, intent, intent.getBooleanExtra("Important", false));
            }
        }

        if(intent.getIntExtra("Type", 3) == 3){
            AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(context);
            alarmServiceManagement.All_TImerSetting();

        }


    }

    void NotificationShow(Context context, Intent intent, Boolean Important){
        Intent busRouteIntent = new Intent(context, TimerSettings.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(busRouteIntent);
        PendingIntent busRoutePendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel Important_timer = new NotificationChannel("Important_timer", "알림예고", NotificationManager.IMPORTANCE_HIGH);//체널 생성
            Important_timer.setBypassDnd(true);
            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            Important_timer.setLightColor(0xFFFF0000);
            notificationManager.createNotificationChannel(Important_timer);


            NotificationChannel timer = new NotificationChannel("timer", "알람", NotificationManager.IMPORTANCE_LOW);//체널 생성
            timer.setBypassDnd(true);
            timer.setShowBadge(true);
            timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            timer.setLightColor(0xFFFFFFFF);
            //timer.setLockscreenVisibility();
            notificationManager.createNotificationChannel(timer);

        }


        if(Important) builder = new NotificationCompat.Builder(context, "Important_timer");
        else builder = new NotificationCompat.Builder(context, "timer");

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_icon));
        builder.setSmallIcon(R.drawable.calendar_icon);
        builder.setTicker("알람 간단한 설명");
        builder.setContentTitle(intent.getStringExtra("Name"));
        builder.setContentText(intent.getStringExtra("Memo"));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(0);
        builder.setDefaults(Notification.PRIORITY_HIGH);
        builder.setContentIntent(busRoutePendingIntent);

        int id=(int)System.currentTimeMillis();

        notificationManager.notify(id,builder.build());
    }
}
