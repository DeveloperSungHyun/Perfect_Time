package com.example.perfect_time.Service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationActionButton_1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationActionButton_1", "start");

        Intent foreground_intent = new Intent(context, ForeGround_Service.class);

        if(intent.getStringExtra("Action").equals("Service_stop")) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            context.startForegroundService(foreground_intent);
                context.stopService(foreground_intent);
            } else {
//            context.startService(foreground_intent);
                context.stopService(foreground_intent);
            }

        }else if(intent.getStringExtra("Action").equals("delete")){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }
    }
}
