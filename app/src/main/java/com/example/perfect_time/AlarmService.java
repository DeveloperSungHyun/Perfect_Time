package com.example.perfect_time;

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
import android.provider.Settings;

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

        calendar = Calendar.getInstance();


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

        if(intent.getIntExtra("Type", 3) == 3){
            SystemDataSave systemDataSave = new SystemDataSave(context.getApplicationContext());//시스템 셋팅값
            if(!systemDataSave.getData_AllTimerOff()) {//알림이 ON 되어있다면 모든알림 설정
                AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(context);
                alarmServiceManagement.All_TimerSetting();
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



//            NotificationChannel None_Vibration_HeadUp = new NotificationChannel("None_Vibration_HeadUp", "무진동 헤드업알림", NotificationManager.IMPORTANCE_HIGH);//체널 생성
//            None_Vibration_HeadUp.setBypassDnd(true);
//            None_Vibration_HeadUp.enableVibration(false);//진동 사용안함
//            None_Vibration_HeadUp.setVibrationPattern(new long[]{ 100, 0, 0 });//진동 사용안함
//            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
//            None_Vibration_HeadUp.setLightColor(0xFFFF0000);
//            notificationManager.createNotificationChannel(None_Vibration_HeadUp);
//
//            NotificationChannel Vibration = new NotificationChannel("Vibration", "진동알림", NotificationManager.IMPORTANCE_DEFAULT);//체널 생성
//            Vibration.setBypassDnd(true);
//            Vibration.setShowBadge(true);
//            Vibration.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
//            Vibration.setLightColor(0xFFFFFFFF);
//            //timer.setLockscreenVisibility();
//            notificationManager.createNotificationChannel(Vibration);
//
//            NotificationChannel None = new NotificationChannel("None", "무음알림", NotificationManager.IMPORTANCE_LOW);//체널 생성
//            None.setBypassDnd(true);
//            None.setShowBadge(true);
//            None.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
//            None.setLightColor(0xFFFFFFFF);
//            //timer.setLockscreenVisibility();
//            notificationManager.createNotificationChannel(None);

        }


        if(intent.getBooleanArrayExtra("alarm")[1] == true){//화면 켜짐

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

        switch (intent.getIntExtra("century", 0)){
            case 0:{//중유도 하
                builder = new NotificationCompat.Builder(context, "IMPORTANCE_LOW");
                break;
            }
            case 1:{//중유도 중
                builder = new NotificationCompat.Builder(context, "IMPORTANCE_DEFAULT");
                break;
            }
            case 3:{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(Settings.canDrawOverlays(context)){
                        Intent intent1 = new Intent(context, PopupView.class);

                        intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent1.putExtra("name", intent.getStringExtra("Name"));
                        intent1.putExtra("memo", intent.getStringExtra("Memo"));
                        context.startActivity(intent1);
                    }
                }
            }
            case 2:{//중유도 상
                builder = new NotificationCompat.Builder(context, "IMPORTANCE_HIGH");
                break;
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
