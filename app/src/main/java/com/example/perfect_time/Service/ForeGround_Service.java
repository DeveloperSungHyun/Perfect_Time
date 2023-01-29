package com.example.perfect_time.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.perfect_time.R;

import java.io.IOException;

public class ForeGround_Service extends Service {
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    boolean alarm_play = false;
    public ForeGround_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("서비스", "실행중...");
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {

            NotificationChannel Alarm = new NotificationChannel("Alarm", "알람", NotificationManager.IMPORTANCE_LOW);//체널 생성
            Alarm.setBypassDnd(true);
            Alarm.setShowBadge(true);
            Alarm.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            Alarm.setLightColor(0xFFFFFFFF);
            //timer.setLockscreenVisibility();
            notificationManager.createNotificationChannel(Alarm);

            builder = new NotificationCompat.Builder(this, "Alarm");

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
            builder.setSmallIcon(R.drawable.calendar_icon);
            builder.setTicker("알람 간단한 설명");
            builder.setContentTitle("test");
            builder.setContentText("Content");
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setPriority(0);
            builder.setDefaults(Notification.DEFAULT_VIBRATE);

            startForeground(1, builder.build());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(alarm_play == false) {
            alarm_play = true;

            try {

                Uri myUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // initialize Uri here
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(getApplicationContext(), myUri);
                mediaPlayer.prepare();
                mediaPlayer.start();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Service.START_STICKY_COMPATIBILITY;
    }
}