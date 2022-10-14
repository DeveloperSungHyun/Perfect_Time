package com.example.perfect_time;

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

    private List<All_Time> all_timeList;
    All_Time all_time = null;

    Calendar calendar;

    Thread thread;
    int count;

    OneDayTimeList oneDayTimeList;

    int y, m, d;

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

        oneDayTimeList = new OneDayTimeList(this, y, m, d);

        all_timeList = oneDayTimeList.getTimeList();

        if("start".equals(intent.getAction())){

            ForeGroundService("다음 일정", "일정이 없습니다.");

            all_time = null;
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

    private void ForeGroundService(String Name, String Memo){

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(pendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
        builder.setSmallIcon(R.drawable.calendar_icon);
        builder.setTicker("알람 간단한 설명");
        builder.setContentTitle("다음 일정");
        builder.setContentText(Name);
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setNumber(1);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("알람 이름: " + Name + "\n" + Memo));
//        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//        builder.setCategory(NotificationCompat.CATEGORY_CALL);
        builder.setColor(0xFFFF0000);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "test", NotificationManager.IMPORTANCE_LOW));

        }


        startForeground(1, builder.build());
    }

    private void BackgroundServiceLogic(Calendar calendar){

        if(all_time == null){
            for (int i = 0; i < all_timeList.size(); i++) {
                if(all_timeList.get(i).getTime_Hour() > calendar.get(Calendar.HOUR_OF_DAY) ||
                        (all_timeList.get(i).getTime_Minute() > calendar.get(Calendar.MINUTE) &&
                                all_timeList.get(i).getTime_Hour() == calendar.get(Calendar.HOUR_OF_DAY))){

                    all_time = all_timeList.get(i);

                    ForeGroundService(all_time.getName(), all_time.getMemo());
                    break;
                }
            }
        }

        Log.d("저장된 시간", " " + all_time.getTime_Hour());
        Log.d("현재 시간", " " + calendar.get(Calendar.HOUR_OF_DAY));

        if(all_time.getTime_Hour() == calendar.get(Calendar.HOUR_OF_DAY) &&
                all_time.getTime_Minute() == calendar.get(Calendar.MINUTE)){

            Log.d("===================", "");

            all_time = null;
        }

        //Log.d("Time=====", all_time.getMemo());
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
}