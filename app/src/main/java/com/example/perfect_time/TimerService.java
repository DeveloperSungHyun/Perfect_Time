package com.example.perfect_time;

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

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class TimerService extends Service {

    BackGroundTask task;

    int value;
    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        task = new BackGroundTask();
        task.execute();

        //BackGroundLoopCode();
        //startForeground(1, new Notification());
        return START_NOT_STICKY;//서비스가 중지되도 다시시작
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void BackGroundLoopCode(){

    }

    class BackGroundTask extends AsyncTask<Integer, String, Integer>{
        String result = "";


        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        protected Integer doInBackground(Integer... values) {
            while (isCancelled() == false){
                try {
                    Thread.sleep(1000);
                    value++;
                    Log.d("count", "num---" + value);
                }catch (InterruptedException e){

                }
            }
            return value;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.d("업데이트", "test");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            value = 0;
        }

        @Override
        protected void onCancelled() {
            value = 0;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}