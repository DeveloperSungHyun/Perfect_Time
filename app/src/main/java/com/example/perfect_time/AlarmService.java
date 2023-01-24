package com.example.perfect_time;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.Trace;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.perfect_time.Activity.PopupView;
import com.example.perfect_time.Activity.TimerSettings;

import java.util.Calendar;

public class AlarmService extends BroadcastReceiver {
    NotificationCompat.Builder builder;

    PowerManager.WakeLock sCpuWakeLock;

    Calendar calendar;

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(context);
        calendar = Calendar.getInstance();

        if(intent.getIntExtra("AlarmType", 0) == 1){
            if(intent.getIntExtra("Week", 0) != calendar.get(Calendar.DAY_OF_WEEK) - 1) return;
        }

        NotificationShow(context, intent);

        if(intent.getBooleanExtra("Resetting", true) == true){//알림이 울리면 모든 알림을 다시 설정(안전을 위해)
            if(intent.getIntExtra("AlarmType", 0) == 0){
                Log.d("Every============", "Resetting");
                alarmServiceManagement.SetUp_Alarm();
            }else if(intent.getIntExtra("AlarmType", 1) == 1){
                Log.d("Week============", "Resetting");
                alarmServiceManagement.SetUp_Alarm();
            }
        }

        if(intent.getIntExtra("AlarmType", 0) == 3){
            alarmServiceManagement.All_TimerSetting(true, false, false);
            alarmServiceManagement.All_TimerSetting(false, true, false);
        }

    }

    @SuppressLint("InvalidWakeLockTag")
    void NotificationShow(Context context, Intent intent){


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //알림강도 "깅"
            NotificationChannel IMPORTANCE_HIGH = new NotificationChannel("IMPORTANCE_HIGH", "중요도 상", NotificationManager.IMPORTANCE_HIGH);//체널 생성
            IMPORTANCE_HIGH.setBypassDnd(true);
            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            IMPORTANCE_HIGH.setVibrationPattern(new long[]{ 0, 300, 200, 700 });//진동 사용안함
            IMPORTANCE_HIGH.setLightColor(0xFFFF0000);
            notificationManager.createNotificationChannel(IMPORTANCE_HIGH);

            NotificationChannel IMPORTANCE_DEFAULT = new NotificationChannel("IMPORTANCE_DEFAULT", "중요도 중", NotificationManager.IMPORTANCE_DEFAULT);//체널 생성
            IMPORTANCE_DEFAULT.setBypassDnd(true);
            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            IMPORTANCE_DEFAULT.setLightColor(0xFFFF0000);
            notificationManager.createNotificationChannel(IMPORTANCE_DEFAULT);

            NotificationChannel IMPORTANCE_LOW = new NotificationChannel("IMPORTANCE_LOW", "중요도 하", NotificationManager.IMPORTANCE_LOW);//체널 생성
            IMPORTANCE_LOW.setBypassDnd(true);
            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            IMPORTANCE_LOW.setLightColor(0xFFFF0000);
            notificationManager.createNotificationChannel(IMPORTANCE_LOW);

        }
        builder = new NotificationCompat.Builder(context, "IMPORTANCE_LOW");

        if(intent.getBooleanExtra("SoundVibration", true)){
            builder = new NotificationCompat.Builder(context, "IMPORTANCE_DEFAULT");
        }

        if(intent.getBooleanExtra("HeadUp", true)){
            builder = new NotificationCompat.Builder(context, "IMPORTANCE_HIGH");
        }

        if(intent.getBooleanExtra("Popup_Activate", true)){
            builder = new NotificationCompat.Builder(context, "IMPORTANCE_DEFAULT");

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            sCpuWakeLock = pm.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE, "hi");

            sCpuWakeLock.acquire();

            if (sCpuWakeLock != null) {
                sCpuWakeLock.release();
                sCpuWakeLock = null;
            }
        }

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_icon));
        builder.setSmallIcon(R.drawable.calendar_icon);
        builder.setTicker("알람 간단한 설명");
        builder.setContentTitle(intent.getStringExtra("Name"));
        builder.setContentText(intent.getStringExtra("Memo"));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getStringExtra("Memo")));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(0);
        builder.setDefaults(Notification.PRIORITY_HIGH);
        if(intent.getBooleanExtra("Important", false)){

            Intent busRouteIntent = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(busRouteIntent);
            PendingIntent busRoutePendingIntent =
                    stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);

            builder.setOngoing(true);//알림 못지우기
            builder.addAction(R.drawable.calendar_icon, "확인", busRoutePendingIntent);
        }


        int id=(int)System.currentTimeMillis();

        notificationManager.notify(id,builder.build());

    }
}
