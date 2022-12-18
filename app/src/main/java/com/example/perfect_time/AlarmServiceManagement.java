package com.example.perfect_time;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.perfect_time.RoomDataBase.Date.DB_Date;
import com.example.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;

import java.util.Calendar;

public class AlarmServiceManagement {
    public static Intent intent;
    public static Context context;
    public static AlarmManager alarmManager;
    public static PendingIntent alarmIntent;

    public AlarmServiceManagement(Context context){
        this.context = context;
    }

    public void AlarmManager_add(Intent intent,int UniqueID, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), UniqueID, intent, PendingIntent.FLAG_IMMUTABLE);
        Log.d("AlarmManager_add", intent.getStringExtra("Name") + " " + intent.getStringExtra("Memo"));
        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), alarmIntent);
        alarmManager.setAlarmClock(ac, alarmIntent);

    }

    public void All_AddAlarm(){
        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

        for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()){
            if(everyDay.isTimer_Activate()){
                intent = new Intent(context, AlarmService.class);
                intent.putExtra("Type", 0);
                intent.putExtra("Name", everyDay.getName());
                intent.putExtra("Memo", everyDay.getMemo());
                intent.putExtra("Important", everyDay.isImportant());

                AlarmManager_add(intent, everyDay.getUniqueID(), everyDay.getTime_Hour(), everyDay.getTime_Minute());

            }
        }

    }

    public void All_AddAlarm_week(){
        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);

        for (DB_Week db_week : week_dataBase_management.getData()){
            if(db_week.isTimer_Activate()){
                intent = new Intent(context, AlarmService.class);
                intent.putExtra("Type", 1);
                intent.putExtra("Name", db_week.getName());
                intent.putExtra("Memo", db_week.getMemo());
                intent.putExtra("week", db_week.getDayOfTheWeek());
                intent.putExtra("Important", db_week.isImportant());

                AlarmManager_add(intent, db_week.getUniqueID(), db_week.getTime_Hour(), db_week.getTime_Minute());
            }
        }

    }

    public void All_AddAlarm_data(){
        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(context);

        for (DB_Date db_date : date_dataBase_management.getData()){
            if(db_date.isTimer_Activate()){
                intent = new Intent(context, AlarmService.class);
                intent.putExtra("Type", 2);
                intent.putExtra("Name", db_date.getName());
                intent.putExtra("Memo", db_date.getMemo());
                intent.putExtra("y", db_date.getDate_Year());
                intent.putExtra("m", db_date.getDate_Month());
                intent.putExtra("d", db_date.getDate_Day());
                intent.putExtra("Important", db_date.isImportant());

                AlarmManager_add(intent, db_date.getUniqueID(), db_date.getTime_Hour(), db_date.getTime_Minute());
            }
        }

    }

    public void AlarmUpDate(int UniqueID){
        Delete_Alarm(UniqueID);
        Log.d("AlarmUpDate", " === " + UniqueID);

        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

        for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()){
            if(everyDay.isTimer_Activate()){
                if(everyDay.getUniqueID() == UniqueID){
                    Intent intent = new Intent(context, AlarmService.class);
                    intent.putExtra("Type", 0);
                    intent.putExtra("Name", everyDay.getName());
                    intent.putExtra("Memo", everyDay.getMemo());
                    intent.putExtra("Important", everyDay.isImportant());

                    AlarmManager_add(intent, everyDay.getUniqueID(), everyDay.getTime_Hour(), everyDay.getTime_Minute());
                }
            }
        }

    }

    public void AlarmUpDate_week(int UniqueID, int week){
        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);

        for (DB_Week db_week : week_dataBase_management.getData()){
            if(db_week.isTimer_Activate()){
                if(db_week.getUniqueID() == UniqueID){
                    Intent intent = new Intent(context, AlarmService.class);
                    intent.putExtra("Type", 1);
                    intent.putExtra("Name", db_week.getName());
                    intent.putExtra("Memo", db_week.getMemo());
                    intent.putExtra("week", week);
                    intent.putExtra("Important", db_week.isImportant());

                    AlarmManager_add(intent, db_week.getUniqueID(), db_week.getTime_Hour(), db_week.getTime_Minute());
                }
            }
        }

    }

    public void AlarmUpDate_data(int UniqueID, int y, int m, int d){
        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(context);

        for (DB_Date db_date : date_dataBase_management.getData()){
            if(db_date.isTimer_Activate()){
                if(db_date.getUniqueID() == UniqueID){
                    Intent intent = new Intent(context, AlarmService.class);
                    intent.putExtra("Type", 2);
                    intent.putExtra("Name", db_date.getName());
                    intent.putExtra("Memo", db_date.getMemo());
                    intent.putExtra("y", y);
                    intent.putExtra("m", m);
                    intent.putExtra("d", d);
                    intent.putExtra("Important", db_date.isImportant());

                    AlarmManager_add(intent, db_date.getUniqueID(), db_date.getTime_Hour(), db_date.getTime_Minute());
                }
            }
        }

    }



    public void Delete_Alarm(int UniqueID){
        Log.d("Delete_Alarm", " === " + UniqueID);
//
//        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        intent = new Intent(context, AlarmService.class);
//        intent.setAction("Name");
//        intent.setAction("Memo");
//
//
//        alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), UniqueID, intent,PendingIntent.FLAG_IMMUTABLE);
//
//        alarmManager.cancel(alarmIntent);
//        alarmIntent.cancel();
//        alarmManager = null;
//        alarmIntent = null;
//
//
//


        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmService.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, UniqueID, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();
        alarmIntent = null;
        alarmManager = null;
    }

    public void All_TImerSetting(){
        All_AddAlarm();
        All_AddAlarm_week();
        All_AddAlarm_data();

        intent = new Intent(context, AlarmService.class);
        intent.putExtra("Type", 3);

        AlarmManager_add(intent, 0, 0, 0);
    }
}