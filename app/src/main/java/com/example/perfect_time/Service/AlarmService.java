package com.example.perfect_time.Service;

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
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.perfect_time.MainActivity;
import com.example.perfect_time.R;

import java.io.IOException;
import java.util.Calendar;

public class AlarmService extends BroadcastReceiver {
    NotificationCompat.Builder builder;

    PowerManager.WakeLock sCpuWakeLock;

    Calendar calendar;

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(context);
        calendar = Calendar.getInstance();

        switch (intent.getIntExtra("AlarmType", 0)){

            case 0:{//매일 울리믄 알림

                if(intent.getIntExtra("AlarmMethod", 0) == 3){//포그라운드 알람으로 설정되어 있다면
                    ForGroundStart(context, intent.getStringExtra("Name"), intent.getStringExtra("Memo"), 10, true, true, 70);
                }else{
                    NotificationShow(context, intent);
                }

                alarmServiceManagement.SetUp_Alarm();//알림 재 설정
                break;
            }
            case 1:{//요일별 울리는 알림

                if(intent.getIntExtra("Week", 0) == calendar.get(Calendar.DAY_OF_WEEK) - 1) {//오늘 요일과 동일하다면
                    if(intent.getIntExtra("AlarmMethod", 0) == 3){//포그라운드 알람으로 설정되어 있다면
                        ForGroundStart(context, intent.getStringExtra("Name"), intent.getStringExtra("Memo"), 10, true, true, 70);
                    }else{
                        NotificationShow(context, intent);
                    }
                    alarmServiceManagement.SetUp_Alarm();//알림 재 설정
                }

                break;
            }
            case 2:{//특정 날짜에만 울리는 알림
                if(intent.getIntExtra("AlarmMethod", 0) == 3){//포그라운드 알람으로 설정되어 있다면
                    ForGroundStart(context, intent.getStringExtra("Name"), intent.getStringExtra("Memo"), 10, true, true, 70);
                }else{
                    NotificationShow(context, intent);
                }
                alarmServiceManagement.SetUp_Alarm();//알림 재 설정

                if(true){//데이터베이스에서 제거

                }
                break;
            }
            case 3:{//하루가 지나면 알림 재설정
                alarmServiceManagement.All_TimerSetting(true, true, true);
                break;
            }
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
            //IMPORTANCE_HIGH.setVibrationPattern(new long[]{ 0, 300, 200, 700 });//진동 사용안함
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

        switch (intent.getIntExtra("AlarmMethod", 0)){
            case 0:{
                builder = new NotificationCompat.Builder(context, "IMPORTANCE_LOW");
                break;
            }
            case 1:{
                builder = new NotificationCompat.Builder(context, "IMPORTANCE_DEFAULT");
                break;
            }
            case 2:{
                builder = new NotificationCompat.Builder(context, "IMPORTANCE_HIGH");
            }

        }

//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        sCpuWakeLock = pm.newWakeLock(
//                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
//                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
//                        PowerManager.ON_AFTER_RELEASE, "hi");
//
//        sCpuWakeLock.acquire();
//
//        if (sCpuWakeLock != null) {
//            sCpuWakeLock.release();
//            sCpuWakeLock = null;
//        }

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



        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        MediaPlayer mPlayer = new MediaPlayer();         // 객체생성
        mPlayer.start();

    }


    private void ForGroundStart(Context context, String Name, String Memo, int RunTime_Second, boolean Sound, boolean Vibration, int SoundValue){
        Log.d("===================", "======================");
        Intent foreground_intent = new Intent(context, ForeGround_Service.class);
        foreground_intent.putExtra("Name", Name);
        foreground_intent.putExtra("Memo", Memo);
        foreground_intent.putExtra("RunTime_Second", RunTime_Second);
        foreground_intent.putExtra("Sound", Sound);
        foreground_intent.putExtra("Vibration", Vibration);
        foreground_intent.putExtra("SoundValue", SoundValue);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            context.startForegroundService(foreground_intent);
        }else{
            context.startService(foreground_intent);
        }
    }
}
