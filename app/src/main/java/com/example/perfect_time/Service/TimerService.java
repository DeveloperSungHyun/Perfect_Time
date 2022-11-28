package com.example.perfect_time.Service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.example.perfect_time.R;

import java.util.Calendar;

public class TimerService extends Service {

    PowerManager powerManager;

    PowerManager.WakeLock wakeLock;

    NotificationCompat.Builder builder_timer, builder_beforehandList;
    Calendar calendar;
    Thread thread;

    TimerSequential timerSequential;

    All_Time Next_Timer = null;
    Warning_TimeData NextWarning_Timer = null;

    public TimerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void StartSettings(){

        timerSequential = new TimerSequential(this);//알람 정보
        timerSequential.TimeDataUpDate();//알람 업데이트

        Next_Timer = timerSequential.Next_getTimer();//다음 알림
        NextWarning_Timer = timerSequential.NextWarning_getTimer();//다음 알림예고
    }


    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate() {
        super.onCreate();


        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);

        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WAKELOCK");



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
            NextTimer_NotificationShow();

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

    int AutoOffTime = 0;
    int AutoOffTime_h = 0;
    private void BackgroundServiceLogic(Calendar calendar){

        int NewTime_H, NewTime_M;

        NewTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NewTime_M = calendar.get(Calendar.MINUTE);

        if(Next_Timer != null) {
            if (Next_Timer.getTime_Hour() == NewTime_H && Next_Timer.getTime_Minute() == NewTime_M) {
                ForeGroundService(Next_Timer.getName(), Next_Timer.getMemo(), Next_Timer.getMemo(), false);
                AutoOffTime = Next_Timer.getTime_Minute() + Next_Timer.getAutoOffTime();
                AutoOffTime_h = Next_Timer.getTime_Hour();
                if (AutoOffTime >= 60){
                    AutoOffTime = -60;

                    AutoOffTime_h++;
                    if(AutoOffTime_h == 24) AutoOffTime_h = 0;
                }

            } else if (AutoOffTime == NewTime_M) {
                Next_Timer = timerSequential.Next_getTimer();
                NextTimer_NotificationShow();
            }
        }else{

            if(NewTime_H == 0 && NewTime_M == 0 && timerSequential.NextTimerList().size() == 0){//하루가 지나면 알람데이터 갱신
                StartSettings();
            }
        }

        if(NextWarning_Timer != null) {
            if (NextWarning_Timer.Timer_H == NewTime_H && NextWarning_Timer.Timer_M == NewTime_M) {
                ForeGroundService(Next_Timer.getTime_Minute() + "분에 " + Next_Timer.getName(), Next_Timer.getMemo(), Next_Timer.getMemo(), true);

                NextWarning_Timer = timerSequential.NextWarning_getTimer();
            }
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
        String NameList = "";
        if(timerSequential.NextTimerList().size() > 0){
            for (int i = 0; i < timerSequential.NextTimerList().size(); i++){
                All_Time time = timerSequential.NextTimerList().get(i);
                NameList += "(" + time.getTime_Hour() + "시 " + time.getTime_Minute() + "분) " + time.getName();

                if(time.isImportant()){
                    NameList += "★";
                }
                if(i < timerSequential.NextTimerList().size() - 1){
                    NameList += "\n";
                }
            }
            ForeGroundService("다음일정", NameList, NameList, false);
        } else if(timerSequential.NextTimerList().size() == 0){
            if(timerSequential.ToDayTimerData.size() > 0){
                ForeGroundService("다음일정", "다음 일정은 없습니다.", null, false);
            }else{
                ForeGroundService("다음일정", "오늘 일정이 없습니다.", null, false);
            }

        }
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
            if (TimerService.class.getName().equals(rsi.service.getClassName())) { //[서비스이름]에 본인 것을 넣는다.
                return true;
            }
        }

        return false;
    }

}