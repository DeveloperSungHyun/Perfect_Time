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

import java.util.Calendar;
import java.util.List;

public class TimerService extends Service {
    NotificationManager manager;

    NotificationCompat.Builder builder;
    private List<All_Time> all_timeList;

    All_Time NextTimer = null;
    All_Time NowTimer = null;

    All_Time all_time = null;

    Calendar calendar;

    Thread thread;
    int count;

    OneDayTimeList oneDayTimeList;

    int y, m, d;
    int NowTime_H, NowTime_M;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        calendar = Calendar.getInstance();

        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONDAY) + 1;
        d = calendar.get(Calendar.DATE);

        NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NowTime_M = calendar.get(Calendar.MINUTE);

        oneDayTimeList = new OneDayTimeList(this, y, m, d);

        all_timeList = oneDayTimeList.getTimeList();

        if("start".equals(intent.getAction())){
            NowTimer = null;

            for (int i = 0; i < all_timeList.size(); i++) {

                if(all_timeList.get(i).getTime_Hour() > NowTime_H ||
                        (all_timeList.get(i).getTime_Hour() == NowTime_H && all_timeList.get(i).getTime_Minute() > NowTime_M)){
                    if(i > 0) NowTimer = all_timeList.get(i - 1);//다음 알림
                    else NowTimer = all_timeList.get(0);
                    break;
                }

            }
            NowTimer.setPopup_Activate(!NowTimer.isPopup_Activate());
            NowTimer.setSound_Activate(!NowTimer.isSound_Activate());
            NowTimer.setVibration_Activate(!NowTimer.isVibration_Activate());

            ForeGroundService(NowTimer, false);

            if(thread == null){
                thread = new Thread("Service"){
                    @Override
                    public void run() {
                        while (true){
                            try {
                                count++;
                                Thread.sleep(5000);
                            }catch (InterruptedException e){
                                break;
                            }
                            calendar = Calendar.getInstance();
                            BackgroundServiceLogic(calendar);

                            Log.d("서비스 실행중", calendar.get(Calendar.HOUR_OF_DAY) + " : " + calendar.get(Calendar.MINUTE) + " : " + calendar.get(Calendar.SECOND));
                        }
                    }
                };
                thread.start();
            }

        }
        return START_STICKY;//서비스가 중지되도 다시시작

    }

    private void ForeGroundService(All_Time all_time, Boolean beforehandTime){

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(pendingIntent);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(all_time != null){
            if(beforehandTime){
                builder = new NotificationCompat.Builder(this, "HeadUp");
            }else{
                builder = new NotificationCompat.Builder(this, "NoneHeadUp");
            }

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
            builder.setSmallIcon(R.drawable.calendar_icon);
            builder.setTicker("알람 간단한 설명");
            builder.setContentTitle(all_time.getName());
            builder.setContentText(all_time.getMemo());
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(all_time.getMemo()));

            builder.setContentIntent(pendingIntent);
            if(beforehandTime){
                Log.d("Popup_Activate", "==========");
                builder.setFullScreenIntent(pendingIntent, true);
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if(beforehandTime){
                    manager.createNotificationChannel(new NotificationChannel("HeadUp", "HeadUp", NotificationManager.IMPORTANCE_HIGH));
                }else {
                    manager.createNotificationChannel(new NotificationChannel("NoneHeadUp", "NoneHeadUp", NotificationManager.IMPORTANCE_LOW));
                }

            }

        }

        startForeground(1, builder.build());
    }

    private void BackgroundServiceLogic(Calendar calendar){

        NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NowTime_M = calendar.get(Calendar.MINUTE);

        if(NextTimer == null){
            for (int i = 0; i < all_timeList.size(); i++) {

                if(all_timeList.get(i).getTime_Hour() > NowTime_H ||
                        (all_timeList.get(i).getTime_Hour() == NowTime_H && all_timeList.get(i).getTime_Minute() > NowTime_M)){
                    NextTimer = all_timeList.get(i);//다음 알림
                    break;
                }

            }


        }

        if(NextTimer.getTime_Hour() == NowTime_H && NextTimer.getTime_Minute() == NowTime_M){//알람시간이 됬을경우
            if(NextTimer != null) ForeGroundService(NextTimer, false);
            NextTimer = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("서비스 중지", "OnDestroy");

        if(thread != null){
            thread.interrupt();
            thread = null;
            count = 0;
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