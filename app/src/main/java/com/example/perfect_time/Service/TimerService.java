package com.example.perfect_time.Service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.perfect_time.All_Time;
import com.example.perfect_time.MainActivity;
import com.example.perfect_time.OneDayTimeList;
import com.example.perfect_time.R;
import com.example.perfect_time.ScreenReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimerService extends Service {

    NotificationCompat.Builder builder_timer, builder_beforehandList;

    Calendar calendar;

    Thread thread;

    OneDayTimeList oneDayTimeList;
    TimerSequential timerSequential;

    int y, m, d;

    int Timer_H, Timer_M;
    String Timer_Name, Timer_Memo;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void StartSettings(){

        timerSequential = new TimerSequential(this);//알람 정보
        timerSequential.TimeDataUpDate();
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

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

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //현재 날짜 시간 가져오기
        StartSettings();

        if("start".equals(intent.getAction())){
            ForeGroundService("다음일정", "오늘 알림이 없습니다.", null, false);
            //ForeGroundService("오늘알림", "일정","일정 목록", false);

            if(thread == null){
                thread = new Thread("Service"){
                    @Override
                    public void run() {
                        while (true){
                            try {

                                calendar = Calendar.getInstance();
                                BackgroundServiceLogic(calendar);

                                Log.d("서비스 실행중", calendar.get(Calendar.HOUR_OF_DAY) + " : " + calendar.get(Calendar.MINUTE) + " : " + calendar.get(Calendar.SECOND));

                                Thread.sleep(5000);
                            }catch (InterruptedException e){
                                break;
                            }
                        }
                    }
                };
                thread.start();
            }

        }
        return START_STICKY;//서비스가 중지되도 다시시작

    }

    private void BackgroundServiceLogic(Calendar calendar){
        int NewTime_H, NewTime_M;

        NewTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NewTime_M = calendar.get(Calendar.MINUTE);

        Log.d("Timer_H", "====" + Timer_Name);
        if(Timer_H == NewTime_H && Timer_M == NewTime_M){
            ForeGroundService(Timer_Name, Timer_Memo, Timer_Memo, false);
        }

    }

    private void ForeGroundService(String Title, String Content, String AllNextTimeList, boolean NotificationHead){

        if(true){
            if(NotificationHead){
                builder_beforehandList = new NotificationCompat.Builder(this, "beforehand");

                builder_beforehandList.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
                builder_beforehandList.setSmallIcon(R.drawable.calendar_icon);
                builder_beforehandList.setTicker("알람 간단한 설명");
                builder_beforehandList.setContentTitle(Title);
                builder_beforehandList.setContentText(Content);
                builder_beforehandList.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                builder_beforehandList.setPriority(0);
                builder_beforehandList.setDefaults(Notification.DEFAULT_VIBRATE);

                startForeground(1, builder_beforehandList.build());

            }else{

                builder_timer = new NotificationCompat.Builder(this, "timer");

                builder_timer.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
                builder_timer.setSmallIcon(R.drawable.calendar_icon);
                builder_timer.setTicker("알람 간단한 설명");
                builder_timer.setContentTitle(Title);
                builder_timer.setContentText(Content);
                builder_timer.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                builder_timer.setPriority(0);
                builder_timer.setDefaults(Notification.DEFAULT_VIBRATE);

                if(AllNextTimeList != null){
                    builder_timer.setStyle(new NotificationCompat.BigTextStyle().bigText(AllNextTimeList));
                }

                startForeground(1, builder_timer.build());

            }

        }

    }

    void NextTimer_NotificationShow(){
        ForeGroundService("다음일정", "다음 일정은 없습니다.", null, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("서비스 중지", "OnDestroy");

        if(thread != null){
            thread.interrupt();
            thread = null;
        }
    }


    public static boolean isServiceRunning(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {
            if (TimerService.class.getName().equals(rsi.service.getClassName())) //[서비스이름]에 본인 것을 넣는다.
            return true;
        }

        return false;
    }

}