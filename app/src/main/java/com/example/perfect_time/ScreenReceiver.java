package com.example.perfect_time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.perfect_time.Activity.DeviceOff;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
//            Intent DeviceOffScreen = new Intent(context, DeviceOff.class);
//            DeviceOffScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(DeviceOffScreen);
//        }

    }
}
