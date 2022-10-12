package com.example.perfect_time;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.perfect_time.Activity.TimerSettings;

import java.util.Calendar;

public class TimerService extends Service {

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

        Calendar calendar = Calendar.getInstance();

        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONDAY) + 1;
        d = calendar.get(Calendar.DATE);

        oneDayTimeList = new OneDayTimeList(this, y, m, d);

        oneDayTimeList.getTimeList();

        if("start".equals(intent.getAction())){
            ForeGroundService();
            if(thread == null){
                thread = new Thread("Service"){
                    @Override
                    public void run() {
                        while (true){
                            try {
                                count++;
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                break;
                            }

                            Log.d("서비스 실행중", "count: " + count);
                        }
                    }
                };
                thread.start();
            }

        }
        return START_STICKY;//서비스가 중지되도 다시시작

    }

    private void ForeGroundService(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.calendar_icon);
        builder.setContentText("test");
        builder.setContentTitle("title");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "test", NotificationManager.IMPORTANCE_DEFAULT));

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
            count = 0;
        }
    }
}