package com.example.perfect_time.Service;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

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

import com.example.perfect_time.All_Time;
import com.example.perfect_time.FragmentActivity.FragmentType;
import com.example.perfect_time.MainActivity;
import com.example.perfect_time.OneDayTimeList;
import com.example.perfect_time.R;
import com.example.perfect_time.RecyclerView.RecyclerView_ListItem;
import com.example.perfect_time.ToDayTimer_Notification;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AlarmService extends BroadcastReceiver {
    NotificationCompat.Builder builder;
    Intent snoozeIntent;

    PowerManager.WakeLock sCpuWakeLock;

    Calendar calendar;

    @Override
    public void onReceive(Context context, Intent intent) {
        snoozeIntent = new Intent(context, NotificationActionButton_1.class);
        AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(context);
        calendar = Calendar.getInstance();

        Log.d("=======================", "=====================");

        switch (intent.getIntExtra("AlarmType", 0)){

            case 0:{//매일 울리믄 알림

                if(intent.getIntExtra("AlarmMethod", 0) == 3){//포그라운드 알람으로 설정되어 있다면
                    ForGroundStart(context, intent.getStringExtra("Name"), intent.getStringExtra("Memo"), intent.getIntExtra("AutoTimerOff", 2), intent.getIntExtra("SoundValue", 70));
                }else{
                    NotificationShow(context, intent);
                }

                break;
            }
            case 1:{//요일별 울리는 알림

                if(intent.getIntExtra("Week", 0) == calendar.get(Calendar.DAY_OF_WEEK) - 1) {//오늘 요일과 동일하다면
                    if(intent.getIntExtra("AlarmMethod", 0) == 3){//포그라운드 알람으로 설정되어 있다면
                        ForGroundStart(context, intent.getStringExtra("Name"), intent.getStringExtra("Memo"), intent.getIntExtra("AutoTimerOff", 2), intent.getIntExtra("SoundValue", 70));
                    }else{
                        NotificationShow(context, intent);
                    }
                }

                break;
            }
            case 2:{//특정 날짜에만 울리는 알림
                if(intent.getIntExtra("AlarmMethod", 0) == 3){//포그라운드 알람으로 설정되어 있다면
                    ForGroundStart(context, intent.getStringExtra("Name"), intent.getStringExtra("Memo"), intent.getIntExtra("AutoTimerOff", 2), intent.getIntExtra("SoundValue", 70));
                }else{
                    NotificationShow(context, intent);
                }

                if(true){//데이터베이스에서 제거

                }
                break;
            }
            case 3:{//하루가 지나면 알림 재설정
                alarmServiceManagement.All_TimerSetting(true, true, true);

                ToDayTimer_Notification toDayTimer_notification = new ToDayTimer_Notification(context);
                toDayTimer_notification.NotificationListShow();


                break;
            }
        }

        ToDayTimer_Notification toDayTimer_notification = new ToDayTimer_Notification(context);
        toDayTimer_notification.NotificationListShow();

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

        int id = intent.getIntExtra("Notification_ID", 0);

        builder.setSmallIcon(R.drawable.perfect_time_top_icon3);
        builder.setTicker("알람 간단한 설명");
        builder.setContentTitle(intent.getStringExtra("Name"));
        builder.setContentText(intent.getStringExtra("Memo"));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getStringExtra("Memo")));
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(0);
        builder.setDefaults(Notification.PRIORITY_HIGH);
        if(intent.getBooleanExtra("Important", false)){

            snoozeIntent.putExtra("Action", "delete");
            snoozeIntent.putExtra("Notification_ID", id);
            snoozeIntent.setAction(String.valueOf(id));
//        snoozeIntent.setAction(ACTION_SNOOZE);
//        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
            PendingIntent ActionButton_Pending =
                    PendingIntent.getBroadcast(context, 0, snoozeIntent, FLAG_IMMUTABLE);

            Intent busRouteIntent = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(busRouteIntent);
            PendingIntent busRoutePendingIntent =
                    stackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);

            builder.setOngoing(true);//알림 못지우기
            builder.addAction(R.drawable.perfect_time_top_icon3, "확인", busRoutePendingIntent);
            builder.addAction(R.drawable.perfect_time_top_icon3, "제거", ActionButton_Pending);
        }
//        intent.getIntExtra("ID", 0);

//        int id=(int)System.currentTimeMillis();

        Log.d("ID", "========================-----" + id);
        notificationManager.notify(id, builder.build());
    }


    private void ForGroundStart(Context context, String Name, String Memo, int RunTime_Second, int SoundValue){
        Log.d("===================", "======================");
        Intent foreground_intent = new Intent(context, ForeGround_Service.class);
        foreground_intent.putExtra("Name", Name);
        foreground_intent.putExtra("Memo", Memo);
        foreground_intent.putExtra("RunTime_Second", RunTime_Second);
        foreground_intent.putExtra("SoundValue", SoundValue);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            context.startForegroundService(foreground_intent);
        }else{
            context.startService(foreground_intent);
        }
    }
}
