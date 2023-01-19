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
    SystemDataSave systemDataSave;

    Calendar NowTime;

    public AlarmServiceManagement(Context context){
        this.context = context;

        systemDataSave = new SystemDataSave(context);
    }

    public void AlarmManager_add(Intent intent,int UniqueID, Calendar cal){

        if (cal.before(Calendar.getInstance())) {
            cal.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), UniqueID, intent, PendingIntent.FLAG_IMMUTABLE);
        Log.d("AlarmManager_add", intent.getStringExtra("Name") + " " + intent.getStringExtra("Memo"));
        AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(cal.getTimeInMillis(), alarmIntent);
        alarmManager.setAlarmClock(ac, alarmIntent);

    }

    public void All_AddAlarm(){
        NowTime = Calendar.getInstance();

        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

        for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()){
            if(everyDay.isTimer_Activate() && !(everyDay.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && everyDay.getTime_Minute() == NowTime.get(Calendar.MINUTE))){

                boolean alarm[] = new boolean[2];
                alarm[0] = everyDay.isPopup_Activate();
                alarm[1] = everyDay.isAutoDisplay_On();

                intent = new Intent(context, AlarmService.class);
                intent.putExtra("AlarmType", 0);
                intent.putExtra("Name", everyDay.getName());
                intent.putExtra("Memo", everyDay.getMemo());
                intent.putExtra("Important", everyDay.isImportant());
                intent.putExtra("century", everyDay.getCentury());

                intent.putExtra("alarm", alarm);

                intent.putExtra("Resetting", true);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.set(Calendar.HOUR_OF_DAY, everyDay.getTime_Hour());
                calendar.set(Calendar.MINUTE, everyDay.getTime_Minute());
                calendar.set(Calendar.SECOND, 1);
                calendar.set(Calendar.MILLISECOND, 1000);

                if(systemDataSave.getData_AllTimerOff() == false) AlarmManager_add(intent, everyDay.getUniqueID(), calendar);

                Log.d("All_AddAlarm_everyDay", everyDay.getName() + " | " + everyDay.getMemo() + " | " + everyDay.getTime_Hour() + " | " + everyDay.getTime_Minute());

            }
        }

    }

    public void All_AddAlarm_week(){
        NowTime = Calendar.getInstance();

        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);

        for (DB_Week db_week : week_dataBase_management.getData()){
            if(db_week.isTimer_Activate() && !(db_week.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_week.getTime_Minute() == NowTime.get(Calendar.MINUTE))){

                boolean alarm[] = new boolean[2];
                alarm[0] = db_week.isPopup_Activate();
                alarm[1] = db_week.isAutoDisplay_On();

                intent = new Intent(context, AlarmService.class);
                intent.putExtra("AlarmType", 1);
                intent.putExtra("Name", db_week.getName());
                intent.putExtra("Memo", db_week.getMemo());
                intent.putExtra("Important", db_week.isImportant());
                intent.putExtra("century", db_week.getCentury());
                intent.putExtra("Week", db_week.getDayOfTheWeek());

                intent.putExtra("alarm", alarm);

                intent.putExtra("Resetting", true);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.set(Calendar.HOUR_OF_DAY, db_week.getTime_Hour());
                calendar.set(Calendar.MINUTE, db_week.getTime_Minute());
                calendar.set(Calendar.SECOND, 1);
                calendar.set(Calendar.MILLISECOND, 1000);

                if(systemDataSave.getData_AllTimerOff() == false) AlarmManager_add(intent, db_week.getUniqueID(), calendar);
                Log.d("All_AddAlarm_week", "command================");
            }
        }

    }

    public void All_AddAlarm_data(){
        NowTime = Calendar.getInstance();
        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(context);

        for (DB_Date db_date : date_dataBase_management.getData()){
            if(db_date.isTimer_Activate() && !(db_date.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_date.getTime_Minute() == NowTime.get(Calendar.MINUTE))){

                boolean alarm[] = new boolean[2];
                alarm[0] = db_date.isPopup_Activate();
                alarm[1] = db_date.isAutoDisplay_On();

                intent = new Intent(context, AlarmService.class);
                intent.putExtra("AlarmType", 2);
                intent.putExtra("Name", db_date.getName());
                intent.putExtra("Memo", db_date.getMemo());
                intent.putExtra("Important", db_date.isImportant());
                intent.putExtra("century", db_date.getCentury());

                intent.putExtra("alarm", alarm);

                intent.putExtra("Resetting", false);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.set(Calendar.HOUR_OF_DAY, db_date.getTime_Hour());
                calendar.set(Calendar.MINUTE, db_date.getTime_Minute());
                calendar.set(Calendar.SECOND, 1);
                calendar.set(Calendar.MILLISECOND, 1000);

                calendar.set(Calendar.YEAR, db_date.getDate_Year());
                calendar.set(Calendar.MONDAY, db_date.getDate_Month() - 1);
                calendar.set(Calendar.DATE, db_date.getDate_Day());

                if(systemDataSave.getData_AllTimerOff() == false) AlarmManager_add(intent, db_date.getUniqueID(), calendar);
            }
        }

    }

    public void AlarmUpDate(int UniqueID){
        Delete_Alarm(UniqueID);

        NowTime = Calendar.getInstance();
        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

        for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()){
            if(everyDay.isTimer_Activate() && !(everyDay.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && everyDay.getTime_Minute() == NowTime.get(Calendar.MINUTE))){
                if(everyDay.getUniqueID() == UniqueID){
                    boolean alarm[] = new boolean[2];
                    alarm[0] = everyDay.isPopup_Activate();
                    alarm[1] = everyDay.isAutoDisplay_On();

                    intent = new Intent(context, AlarmService.class);
                    intent.putExtra("AlarmType", 0);
                    intent.putExtra("Name", everyDay.getName());
                    intent.putExtra("Memo", everyDay.getMemo());
                    intent.putExtra("Important", everyDay.isImportant());
                    intent.putExtra("century", everyDay.getCentury());

                    intent.putExtra("alarm", alarm);

                    intent.putExtra("Resetting", true);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, everyDay.getTime_Hour());
                    calendar.set(Calendar.MINUTE, everyDay.getTime_Minute());
                    calendar.set(Calendar.SECOND, 1);
                    calendar.set(Calendar.MILLISECOND, 1000);

                    if(systemDataSave.getData_AllTimerOff() == false) AlarmManager_add(intent, everyDay.getUniqueID(), calendar);

                    Log.d("All_AddAlarm_everyDay", everyDay.getName() + " | " + everyDay.getMemo() + " | " + everyDay.getTime_Hour() + " | " + everyDay.getTime_Minute());
                }
            }
        }

    }

    public void AlarmUpDate_week(int UniqueID){
        Delete_Alarm(UniqueID);

        NowTime = Calendar.getInstance();
        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);

        for (DB_Week db_week : week_dataBase_management.getData()){
            if(db_week.isTimer_Activate() && !(db_week.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_week.getTime_Minute() == NowTime.get(Calendar.MINUTE))){
                if(db_week.getUniqueID() == UniqueID){
                    boolean alarm[] = new boolean[2];
                    alarm[0] = db_week.isPopup_Activate();
                    alarm[1] = db_week.isAutoDisplay_On();

                    intent = new Intent(context, AlarmService.class);
                    intent.putExtra("AlarmType", 1);
                    intent.putExtra("Name", db_week.getName());
                    intent.putExtra("Memo", db_week.getMemo());
                    intent.putExtra("Important", db_week.isImportant());
                    intent.putExtra("century", db_week.getCentury());
                    intent.putExtra("Week", db_week.getDayOfTheWeek());

                    intent.putExtra("alarm", alarm);

                    intent.putExtra("Resetting", true);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, db_week.getTime_Hour());
                    calendar.set(Calendar.MINUTE, db_week.getTime_Minute());
                    calendar.set(Calendar.SECOND, 1);
                    calendar.set(Calendar.MILLISECOND, 1000);

                    calendar.set(Calendar.DAY_OF_WEEK, db_week.getDayOfTheWeek());

                    if(systemDataSave.getData_AllTimerOff() == false) AlarmManager_add(intent, db_week.getUniqueID(), calendar);
                }
            }
        }

    }

    public void AlarmUpDate_data(int UniqueID){
        Delete_Alarm(UniqueID);

        NowTime = Calendar.getInstance();
        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(context);

        for (DB_Date db_date : date_dataBase_management.getData()){
            if(db_date.isTimer_Activate() && !(db_date.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_date.getTime_Minute() == NowTime.get(Calendar.MINUTE))){
                if(db_date.getUniqueID() == UniqueID){
                    boolean alarm[] = new boolean[2];
                    alarm[0] = db_date.isPopup_Activate();
                    alarm[1] = db_date.isAutoDisplay_On();

                    intent = new Intent(context, AlarmService.class);
                    intent.putExtra("AlarmType", 2);
                    intent.putExtra("Name", db_date.getName());
                    intent.putExtra("Memo", db_date.getMemo());
                    intent.putExtra("Important", db_date.isImportant());
                    intent.putExtra("century", db_date.getCentury());

                    intent.putExtra("alarm", alarm);

                    intent.putExtra("Resetting", true);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, db_date.getTime_Hour());
                    calendar.set(Calendar.MINUTE, db_date.getTime_Minute());
                    calendar.set(Calendar.SECOND, 1);
                    calendar.set(Calendar.MILLISECOND, 1000);

                    calendar.set(Calendar.YEAR, db_date.getDate_Year());
                    calendar.set(Calendar.MONDAY, db_date.getDate_Month() - 1);
                    calendar.set(Calendar.DATE, db_date.getDate_Day());

                    if(systemDataSave.getData_AllTimerOff() == false) AlarmManager_add(intent, db_date.getUniqueID(), calendar);
                }
            }
        }

    }



    public void Delete_Alarm(int UniqueID){

        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmService.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, UniqueID, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();
        alarmIntent = null;
        alarmManager = null;
    }

    public void All_TimerSetting(boolean Every, boolean Week, boolean Date){
        if(Every) All_AddAlarm();
        if(Week) All_AddAlarm_week();
        if(Date) All_AddAlarm_data();

    }

    public void All_Delete(boolean everyDay_delete, boolean db_week_delete, boolean db_date_delete){
        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);
        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);
        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(context);

        if(everyDay_delete) {
            for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()) {
                Delete_Alarm(everyDay.getUniqueID());
            }
        }

        if(db_week_delete) {
            for (DB_Week db_week : week_dataBase_management.getData()) {
                Delete_Alarm(db_week.getUniqueID());
            }
        }

        if(db_date_delete) {
            for (DB_Date db_date : date_dataBase_management.getData()) {
                Delete_Alarm(db_date.getUniqueID());
            }
        }

    }
}

