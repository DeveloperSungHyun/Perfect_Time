package com.example.perfect_time;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.perfect_time.Activity.TimerSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimerService extends Service {
    NotificationManager manager;

    NotificationCompat.Builder builder;
    private List<All_Time> all_timeList;
    private Beforehand beforehand;
    private List<Beforehand> beforehandList;

    Calendar calendar;

    Thread thread;

    OneDayTimeList oneDayTimeList;

    int y, m, d;
    int NowTime_H, NowTime_M;

    int TimerCount = 0;
    int Beforehand_TimerCount = 0;

    public class Beforehand{

        int Time_h, Time_m;
        String TimerName = null;
        public Beforehand(int Time_h, int Time_m, String TimerName){
            this.Time_h = Time_h;
            this.Time_m = Time_m;
            this.TimerName = TimerName;

        }
    }

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void StartSettings(){

        calendar = Calendar.getInstance();

        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONDAY) + 1;
        d = calendar.get(Calendar.DATE);

        NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NowTime_M = calendar.get(Calendar.MINUTE);



        oneDayTimeList = new OneDayTimeList(this, y, m, d);//하루일정 가져오기
        beforehandList = new ArrayList<>();

        all_timeList = oneDayTimeList.getTimeList();//오늘 알림 리스트
        for(All_Time TimeList : all_timeList){
            if(TimeList.isBeforehand()){
                int time_h = 0, time_m = 0;
                String time_Name;

                time_Name = TimeList.getName();
                if(TimeList.getTime_Minute() >= TimeList.getBeforehandTime() / 60){
                    time_h = TimeList.getTime_Hour();
                    time_m = TimeList.getTime_Minute() - TimeList.getBeforehandTime() / 60;
                }else{
                    time_h = TimeList.getTime_Hour() - 1;
                    time_m = (TimeList.getTime_Minute() + 60) - TimeList.getBeforehandTime() / 60;
                }
                beforehand = new Beforehand(time_h, time_m , time_Name);

                beforehandList.add(beforehand);
                beforehand = null;
            }
        }

        for (int i = 0; i < all_timeList.size(); i++) {
            if(NowTime_H <= all_timeList.get(i).getTime_Hour() && NowTime_M < all_timeList.get(i).getTime_Minute()){
                TimerCount = i;
                break;
            }
        }
        for (int i = 0; i < beforehandList.size(); i++) {
            if(NowTime_H <= beforehandList.get(i).Time_h && NowTime_M < beforehandList.get(i).Time_m){
                Beforehand_TimerCount = i;
                break;
            }
        }

//        for (Beforehand beforehand : beforehandList){
//            Log.d("===================beforehand", beforehand.Time_h + " : " + beforehand.Time_m + " , " + beforehand.TimerName);
//        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //현재 날짜 시간 가져오기
        StartSettings();


        if("start".equals(intent.getAction())){

            ForeGroundService("오늘알림", "일정","일정 목록", false);

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
        NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NowTime_M = calendar.get(Calendar.MINUTE);

        if(all_timeList.get(TimerCount).getTime_Hour() == NowTime_H && all_timeList.get(TimerCount).getTime_Minute() == NowTime_M){

            ForeGroundService(all_timeList.get(TimerCount).getName(), all_timeList.get(TimerCount).getMemo(),all_timeList.get(TimerCount).getMemo(), false);

            TimerCount++;//다음 타이머 리스트
        }

        if(beforehandList.get(Beforehand_TimerCount).Time_h == NowTime_H && beforehandList.get(Beforehand_TimerCount).Time_m == NowTime_M){

            ForeGroundService(all_timeList.get(TimerCount).getName(), all_timeList.get(TimerCount).getMemo(), null, true);

            Beforehand_TimerCount++;//다음 예고 타이머 리스트

        }
    }

    private void ForeGroundService(String Title, String Content, String AllNextTimeList, boolean NotificationHead){

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(pendingIntent);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(all_timeList != null){
            if(NotificationHead){
                builder = new NotificationCompat.Builder(this, "HeadUp");
                builder.setFullScreenIntent(pendingIntent, true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(new NotificationChannel("HeadUp", "HeadUp", NotificationManager.IMPORTANCE_HIGH));
                }
            }else{
                builder = new NotificationCompat.Builder(this, "NoneHeadUp");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(new NotificationChannel("NoneHeadUp", "NoneHeadUp", NotificationManager.IMPORTANCE_LOW));
                }
            }

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
            builder.setSmallIcon(R.drawable.calendar_icon);
            builder.setTicker("알람 간단한 설명");
            builder.setContentTitle(Title);
            builder.setContentText(Content);
            builder.setContentIntent(pendingIntent);

            if(AllNextTimeList != null){

                builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(AllNextTimeList));
            }

        }
        startForeground(1, builder.build());
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