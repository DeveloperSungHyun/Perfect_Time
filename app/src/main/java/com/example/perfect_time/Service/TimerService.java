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
    private ScreenReceiver screenReceiver = null;
    private static PowerManager.WakeLock sCpuWakeLock;
    PowerManager pm;

    NotificationCompat.Builder builder_timer, builder_beforehandList;
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

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel beforehand = new NotificationChannel("beforehand", "알림예고", NotificationManager.IMPORTANCE_LOW);//체널 생성
            beforehand.setBypassDnd(true);
            //timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            beforehand.setLightColor(0xFFFF0000);
            notificationManager.createNotificationChannel(beforehand);


            NotificationChannel timer = new NotificationChannel("timer", "알람", NotificationManager.IMPORTANCE_HIGH);//체널 생성
            timer.setBypassDnd(true);
            timer.setShowBadge(true);
            timer.setDescription("설정한 알람이 시간이 되면 알림을 울립니다.");
            timer.setLightColor(0xFFFFFFFF);
            //timer.setLockscreenVisibility();
            notificationManager.createNotificationChannel(timer);

        }

    }

    @SuppressLint("InvalidWakeLockTag")
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

        if(all_timeList.get(TimerCount).getTime_Hour() == NowTime_H && all_timeList.get(TimerCount).getTime_Minute() == NowTime_M && all_timeList.size() > TimerCount){

            ForeGroundService(all_timeList.get(TimerCount).getName(), all_timeList.get(TimerCount).getMemo(),all_timeList.get(TimerCount).getMemo(), false);

            TimerCount++;//다음 타이머 리스트
        }

        if(beforehandList.get(Beforehand_TimerCount).Time_h == NowTime_H && beforehandList.get(Beforehand_TimerCount).Time_m == NowTime_M && beforehandList.size() > Beforehand_TimerCount){

            ForeGroundService(all_timeList.get(TimerCount).getName(), all_timeList.get(TimerCount).getBeforehandTime() + "분 뒤에 알림이 울립니다.", null, true);

            Beforehand_TimerCount++;//다음 예고 타이머 리스트

        }
    }

    private void ForeGroundService(String Title, String Content, String AllNextTimeList, boolean NotificationHead){

        if(all_timeList != null){
            if(NotificationHead){
                builder_timer = new NotificationCompat.Builder(this, "timer");

                builder_timer.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
                builder_timer.setSmallIcon(R.drawable.calendar_icon);
                builder_timer.setTicker("알람 간단한 설명");
                builder_timer.setContentTitle(Title);
                builder_timer.setContentText(Content);
                builder_timer.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                builder_timer.setPriority(0);
                builder_timer.setDefaults(Notification.DEFAULT_VIBRATE);

                startForeground(1, builder_timer.build());

            }else{
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

//                if(AllNextTimeList != null){
//                    builder_beforehandList.setStyle(new NotificationCompat.BigTextStyle().bigText(AllNextTimeList));
//                }

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
            if (TimerService.class.getName().equals(rsi.service.getClassName())) //[서비스이름]에 본인 것을 넣는다.
            return true;
        }

        return false;
    }

}