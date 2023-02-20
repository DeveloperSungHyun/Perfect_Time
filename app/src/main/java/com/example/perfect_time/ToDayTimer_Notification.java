package com.example.perfect_time;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.perfect_time.Activity.Preferences;

import java.util.Calendar;
import java.util.List;

public class ToDayTimer_Notification {

    Context context;

    NotificationCompat.Builder builder;

    Calendar calendar;


    String Title = null;
    String Content = null;
    public ToDayTimer_Notification(Context context){

        this.context = context;

    }

    public void NotificationListShow(){
        calendar = Calendar.getInstance();

        int y = calendar.get(Calendar.YEAR);//24시 형식
        int m = calendar.get(Calendar.MONTH) + 1;//24시 형식
        int d = calendar.get((Calendar.DATE));

        OneDayTimeList oneDayTimeList = new OneDayTimeList(context.getApplicationContext(), y, m, d);

        List<All_Time> all_times = oneDayTimeList.getTimeList();

        String Next_Alarm = "";

        for (int i = 0; i < all_times.size(); i++) {
            All_Time data = all_times.get(i);
            if(data.isTimer_Activate() == true){

                if (data.getTime_Hour() > calendar.get(Calendar.HOUR_OF_DAY) ||
                        (data.getTime_Hour() == calendar.get(Calendar.HOUR_OF_DAY) && data.getTime_Minute() > calendar.get(Calendar.MINUTE))) {
                    Next_Alarm += (i + 1) + ". (" + data.getTime_Hour() + " : " + data.getTime_Minute() + ") " + data.getName();
                    if(i < all_times.size() - 1) Next_Alarm += "\n";
                }
            }
        }

        SystemDataSave systemDataSave = new SystemDataSave(context);
        if(systemDataSave.getData_AllTimerOff() == false){
            Title = "다음 일정";
        }else{
            Title = "다음 일정\n(알람이 꺼져 있습니다.)";
        }

        if(all_times.size() == 0){
            Content = "오늘은 일정이 없습니다.";
        }else {
            if(Next_Alarm.equals("")){
                Content = "다음 일정이 없습니다.";
            }else {
                Content = Next_Alarm;
            }
        }


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel IMPORTANCE_LOW = new NotificationChannel("IMPORTANCE_LOW", "중요도 하", NotificationManager.IMPORTANCE_LOW);//체널 생성
            IMPORTANCE_LOW.setBypassDnd(true);
            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            IMPORTANCE_LOW.setLightColor(0xFFFF0000);
            notificationManager.createNotificationChannel(IMPORTANCE_LOW);
        }

        Intent busRouteIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(busRouteIntent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);

        Intent Alarm_settings = new Intent(context, Preferences.class);

        TaskStackBuilder Alarm_settings_stackBuilder = TaskStackBuilder.create(context);
        Alarm_settings_stackBuilder.addNextIntentWithParentStack(Alarm_settings);
        PendingIntent pendingIntent_Alarm_settings =
                Alarm_settings_stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);


        builder = new NotificationCompat.Builder(context, "IMPORTANCE_LOW");
        //builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.perfect_time_top_icon3));
        builder.setSmallIcon(R.drawable.perfect_time_top_icon3);
        builder.setContentTitle(Title);
        builder.setContentText(Content);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(Content));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(0);
        builder.setDefaults(Notification.PRIORITY_HIGH);
        builder.setOngoing(true);//알림 못지우기
        builder.setContentIntent(pendingIntent);
        if(systemDataSave.getData_AllTimerOff() == true){
            builder.addAction(R.drawable.perfect_time_top_icon3, "설정", pendingIntent_Alarm_settings);
        }

        notificationManager.notify(0, builder.build());
    }
}
