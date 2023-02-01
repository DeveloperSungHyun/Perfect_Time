package com.example.perfect_time.Service;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.example.perfect_time.MainActivity;
import com.example.perfect_time.R;

import java.io.IOException;

public class ForeGround_Service extends Service {
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    Thread thread;

    int timer_count = 0;

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


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);//선언 후

        Intent snoozeIntent = new Intent(this, NotificationActionButton_1.class);
//        snoozeIntent.setAction(ACTION_SNOOZE);
//        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent ActionButton_Pending =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, FLAG_IMMUTABLE);

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
            builder.setContentTitle(intent.getStringExtra("Name"));
            builder.setContentText(intent.getStringExtra("Memo"));
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setPriority(0);
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            builder.addAction(R.drawable.calendar_icon, "알림끄기", ActionButton_Pending);

            startForeground(1, builder.build());
        }

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                (int)(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * intent.getIntExtra("SoundValue", 70) / 100),
                AudioManager.FLAG_PLAY_SOUND);

        try {
            Uri myUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // initialize Uri here
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//STREAM_ALARM
            this.mediaPlayer.setDataSource(getApplicationContext(), myUri);
            this.mediaPlayer.prepare();
            this.mediaPlayer.start();
            alarm_play = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        int timer_number[] = {30, 60, 120, 180, 300};
        thread = new Thread("timer"){//RunTime_Second SoundValue
            @Override
            public void run() {
                super.run();
                while (alarm_play){
                    try {
                        sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("thread", "run ============");

                    timer_count++;
                    if(timer_count >= timer_number[intent.getIntExtra("RunTime_Second", 2)]){//설정한 시간이 지나면 서비스 종료
                        alarm_play = false;
                        stopForeground(true);
                        stopService(intent);
                    }
                }
            }
        };
        thread.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        alarm_play = false;

    }
}