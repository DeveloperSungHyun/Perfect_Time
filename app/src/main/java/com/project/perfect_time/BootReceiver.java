package com.project.perfect_time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.project.perfect_time.Service.AlarmServiceManagement;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SystemDataSave systemDataSave = new SystemDataSave(context.getApplicationContext());//시스템 셋팅값
        ToDayTimer_Notification toDayTimer_notification = new ToDayTimer_Notification(context);

        if(!systemDataSave.getData_AllTimerOff()) {//알림이 ON 되어있다면 모든알림 설정
            AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(context);
            alarmServiceManagement.All_TimerSetting(true, true, true);
            alarmServiceManagement.DAY_Loop();

            toDayTimer_notification.NotificationListShow();
        }
    }
}
