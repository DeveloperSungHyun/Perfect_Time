package com.example.perfect_time;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;

import java.util.Calendar;

public class AlarmServiceManagement {
    Context context;
    AlarmManager alarmManager;

    public AlarmServiceManagement(Context context){
        this.context = context;
    }

    private void AlarmManager_add(Intent intent,int UniqueID, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, UniqueID, intent, PendingIntent.FLAG_IMMUTABLE);

        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmIntent);
        alarmManager.setAlarmClock(ac, alarmIntent);

        Log.d("=====================", "Add_Alarm");
    }

    public void All_AddAlarm(){
        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

        for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()){
            if(everyDay.isTimer_Activate()){
                Intent intent = new Intent(context, AlarmService.class);
                intent.putExtra("Name", everyDay.getName());
                intent.putExtra("Memo", everyDay.getMemo());

                AlarmManager_add(intent, everyDay.getUniqueID(), everyDay.getTime_Hour(), everyDay.getTime_Minute());
            }
        }

    }

    public void Delete_Alarm(){

    }
}
