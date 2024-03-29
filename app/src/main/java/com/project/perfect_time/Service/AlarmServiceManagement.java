package com.project.perfect_time.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.project.perfect_time.RoomDataBase.Date.DB_Date;
import com.project.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.project.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.project.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.project.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.project.perfect_time.RoomDataBase.Week_DataBase_Management;
import com.project.perfect_time.SystemDataSave;

import java.util.Calendar;

public class AlarmServiceManagement {
    public static Intent intent;
    public static Context context;
    public static AlarmManager alarmManager;
    public static PendingIntent alarmIntent;
    SystemDataSave systemDataSave;

    PendingIntent pIntent;

    Calendar calendar;
    Calendar NowTime;

    public AlarmServiceManagement(Context context){
        this.context = context;

        systemDataSave = new SystemDataSave(context);
    }

    public void DAY_Loop(){

        intent = new Intent(context, AlarmService.class);
        intent.putExtra("AlarmType", 3);

        alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), -1,intent, PendingIntent.FLAG_IMMUTABLE);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 1000);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pIntent);

        //===

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), -2, intent, PendingIntent.FLAG_IMMUTABLE);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 1000);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);


        //Toast.makeText(context, "알람 재설정", Toast.LENGTH_LONG).show();
    }

    public void DAY_LoopOff(){
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmService.class);

        alarmIntent = PendingIntent.getBroadcast(context, -1, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();

        alarmIntent = PendingIntent.getBroadcast(context, -2, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();
    }

    public void AlarmManager_add(Intent intent,int UniqueID, Calendar cal){

        if (cal.before(Calendar.getInstance())) {
            cal.add(Calendar.DATE, 1);
        }

        alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), UniqueID, intent, PendingIntent.FLAG_IMMUTABLE);
        Log.d("AlarmManager_add", intent.getStringExtra("Name") + " " + intent.getStringExtra("Memo"));
        AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(cal.getTimeInMillis(), alarmIntent);
        alarmManager.setAlarmClock(ac, alarmIntent);

    }

    public void All_AddAlarm(){
        NowTime = Calendar.getInstance();

        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

        for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()){
            if(everyDay.isTimer_Activate()) {

                if (everyDay.getTime_Hour() > NowTime.get(Calendar.HOUR_OF_DAY) ||
                        (everyDay.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && everyDay.getTime_Minute() > NowTime.get(Calendar.MINUTE))) {

                    intent = new Intent(context, AlarmService.class);
                    intent.putExtra("Notification_ID", everyDay.getUniqueID());
                    intent.putExtra("AlarmType", 0);
                    intent.putExtra("Name", everyDay.getName());
                    intent.putExtra("Memo", everyDay.getMemo());
                    intent.putExtra("Important", everyDay.isImportant());
                    intent.putExtra("AlarmMethod", everyDay.getAlarm_Method());
                    intent.putExtra("AutoTimerOff", everyDay.getAutoTimerOff());
                    intent.putExtra("SoundValue", everyDay.getSound_value());

                    intent.putExtra("Resetting", true);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, everyDay.getTime_Hour());
                    calendar.set(Calendar.MINUTE, everyDay.getTime_Minute());
                    calendar.set(Calendar.SECOND, 1);
                    calendar.set(Calendar.MILLISECOND, 1000);

                    if (systemDataSave.getData_AllTimerOff() == false)
                        AlarmManager_add(intent, everyDay.getUniqueID(), calendar);

                    Log.d("All_AddAlarm_everyDay", everyDay.getName() + " | " + everyDay.getMemo() + " | " + everyDay.getTime_Hour() + " | " + everyDay.getTime_Minute());

                }
            }
        }

    }

    public void All_AddAlarm_week(){
        NowTime = Calendar.getInstance();

        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);

        for (DB_Week db_week : week_dataBase_management.getData()) {
            if (db_week.isTimer_Activate()) {

                if (db_week.getTime_Hour() > NowTime.get(Calendar.HOUR_OF_DAY) ||
                        (db_week.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_week.getTime_Minute() > NowTime.get(Calendar.MINUTE))) {


                    intent = new Intent(context, AlarmService.class);
                    intent.putExtra("Notification_ID", db_week.getUniqueID());
                    intent.putExtra("AlarmType", 1);
                    intent.putExtra("Name", db_week.getName());
                    intent.putExtra("Memo", db_week.getMemo());
                    intent.putExtra("Important", db_week.isImportant());
                    intent.putExtra("Week", db_week.getDayOfTheWeek());
                    intent.putExtra("AlarmMethod", db_week.getAlarm_Method());
                    intent.putExtra("AutoTimerOff", db_week.getAutoTimerOff());
                    intent.putExtra("SoundValue", db_week.getSound_value());

                    intent.putExtra("Resetting", true);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, db_week.getTime_Hour());
                    calendar.set(Calendar.MINUTE, db_week.getTime_Minute());
                    calendar.set(Calendar.SECOND, 1);
                    calendar.set(Calendar.MILLISECOND, 1000);

                    if (systemDataSave.getData_AllTimerOff() == false)
                        AlarmManager_add(intent, db_week.getUniqueID(), calendar);
                    Log.d("All_AddAlarm_week", "command================");

                }
            }
        }

    }

    public void All_AddAlarm_data(){
        NowTime = Calendar.getInstance();
        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(context);

        for (DB_Date db_date : date_dataBase_management.getData()){

            if(db_date.isSelector() == false) {
                Log.d("===============================", "1");
                if (db_date.getDate_Year() == NowTime.get(Calendar.YEAR)) {
                    if (db_date.getDate_Month() == NowTime.get(Calendar.MONDAY) + 1) {
                        if (db_date.getDate_Day() == NowTime.get(Calendar.DATE)) {

                            Log.d("===============================", "2");

                            if (db_date.isTimer_Activate()) {

                                if (db_date.getTime_Hour() > NowTime.get(Calendar.HOUR_OF_DAY) ||
                                        (db_date.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_date.getTime_Minute() > NowTime.get(Calendar.MINUTE))) {

                                    Log.d("===============================", "3");

                                    Log.d("미래", db_date.getName());

                                    intent = new Intent(context, AlarmService.class);
                                    intent.putExtra("Notification_ID", db_date.getUniqueID());
                                    intent.putExtra("AlarmType", 2);
                                    intent.putExtra("Name", db_date.getName());
                                    intent.putExtra("Memo", db_date.getMemo());
                                    intent.putExtra("Important", db_date.isImportant());
                                    intent.putExtra("AlarmMethod", db_date.getAlarm_Method());
                                    intent.putExtra("AutoTimerOff", db_date.getAutoTimerOff());
                                    intent.putExtra("SoundValue", db_date.getSound_value());

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

                                    if (systemDataSave.getData_AllTimerOff() == false)
                                        AlarmManager_add(intent, db_date.getUniqueID(), calendar);

                                }

                            }
                        }
                    }
                }
            }else{
                if(db_date.getDay() >= NowTime.get(Calendar.DATE)) {
                    if (db_date.isTimer_Activate()) {

                        if (db_date.getTime_Hour() > NowTime.get(Calendar.HOUR_OF_DAY) ||
                                (db_date.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_date.getTime_Minute() > NowTime.get(Calendar.MINUTE))) {

                            Log.d("미래", db_date.getName());

                            intent = new Intent(context, AlarmService.class);
                            intent.putExtra("Notification_ID", db_date.getUniqueID());
                            intent.putExtra("AlarmType", 2);
                            intent.putExtra("Name", db_date.getName());
                            intent.putExtra("Memo", db_date.getMemo());
                            intent.putExtra("Important", db_date.isImportant());
                            intent.putExtra("AlarmMethod", db_date.getAlarm_Method());
                            intent.putExtra("AutoTimerOff", db_date.getAutoTimerOff());
                            intent.putExtra("SoundValue", db_date.getSound_value());

                            intent.putExtra("Resetting", false);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());

                            calendar.set(Calendar.HOUR_OF_DAY, db_date.getTime_Hour());
                            calendar.set(Calendar.MINUTE, db_date.getTime_Minute());
                            calendar.set(Calendar.SECOND, 1);
                            calendar.set(Calendar.MILLISECOND, 1000);

                            calendar.set(Calendar.YEAR, NowTime.get(Calendar.YEAR));
                            calendar.set(Calendar.MONDAY, NowTime.get(Calendar.MONDAY));

                            if(db_date.getDay() > 0) {
                                calendar.set(Calendar.DATE, db_date.getDay());
                            }else{
                                calendar.set(Calendar.DATE, 31);//마지막 날짜 지정
                            }

                            if (systemDataSave.getData_AllTimerOff() == false)
                                AlarmManager_add(intent, db_date.getUniqueID(), calendar);

                        }
                    }
                }
            }

        }

    }

    public void AlarmUpDate(int UniqueID){
        Delete_Alarm(UniqueID);

        NowTime = Calendar.getInstance();
        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

        for (DB_EveryDay everyDay : everyDay_dataBase_management.getData()){
            if(everyDay.isTimer_Activate()){

                if(everyDay.getTime_Hour() > NowTime.get(Calendar.HOUR_OF_DAY) ||
                        (everyDay.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && everyDay.getTime_Minute() > NowTime.get(Calendar.MINUTE))) {

                    if (everyDay.getUniqueID() == UniqueID) {

                        intent = new Intent(context, AlarmService.class);
                        intent.putExtra("Notification_ID", everyDay.getUniqueID());
                        intent.putExtra("AlarmType", 0);
                        intent.putExtra("Name", everyDay.getName());
                        intent.putExtra("Memo", everyDay.getMemo());
                        intent.putExtra("Important", everyDay.isImportant());
                        intent.putExtra("AlarmMethod", everyDay.getAlarm_Method());
                        intent.putExtra("AutoTimerOff", everyDay.getAutoTimerOff());
                        intent.putExtra("SoundValue", everyDay.getSound_value());

                        intent.putExtra("Resetting", true);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());

                        Log.d("=============Time", "" + everyDay.getTime_Hour() + " : " + everyDay.getTime_Minute());

                        calendar.set(Calendar.HOUR_OF_DAY, everyDay.getTime_Hour());
                        calendar.set(Calendar.MINUTE, everyDay.getTime_Minute());
                        calendar.set(Calendar.SECOND, 1);
                        calendar.set(Calendar.MILLISECOND, 1000);

                        if (systemDataSave.getData_AllTimerOff() == false)
                            AlarmManager_add(intent, everyDay.getUniqueID(), calendar);

                    }
                }
            }
        }

        Log.d("알람 변경", "================");
    }

    public void AlarmUpDate_week(int UniqueID){
        Delete_Alarm(UniqueID);

        NowTime = Calendar.getInstance();
        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);

        for (DB_Week db_week : week_dataBase_management.getData()){
            if(db_week.isTimer_Activate()){

                if(db_week.getTime_Hour() > NowTime.get(Calendar.HOUR_OF_DAY) ||
                        (db_week.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_week.getTime_Minute() > NowTime.get(Calendar.MINUTE))) {

                    if (db_week.getUniqueID() == UniqueID) {

                        intent = new Intent(context, AlarmService.class);
                        intent.putExtra("Notification_ID", db_week.getUniqueID());
                        intent.putExtra("AlarmType", 1);
                        intent.putExtra("Name", db_week.getName());
                        intent.putExtra("Memo", db_week.getMemo());
                        intent.putExtra("Important", db_week.isImportant());
                        intent.putExtra("Week", db_week.getDayOfTheWeek());
                        intent.putExtra("AlarmMethod", db_week.getAlarm_Method());
                        intent.putExtra("AutoTimerOff", db_week.getAutoTimerOff());
                        intent.putExtra("SoundValue", db_week.getSound_value());

                        intent.putExtra("Resetting", true);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());

                        calendar.set(Calendar.HOUR_OF_DAY, db_week.getTime_Hour());
                        calendar.set(Calendar.MINUTE, db_week.getTime_Minute());
                        calendar.set(Calendar.SECOND, 1);
                        calendar.set(Calendar.MILLISECOND, 1000);

                        calendar.set(Calendar.DAY_OF_WEEK, db_week.getDayOfTheWeek());

                        if (systemDataSave.getData_AllTimerOff() == false)
                            AlarmManager_add(intent, db_week.getUniqueID(), calendar);

                    }
                }
            }
        }

    }

    public void AlarmUpDate_data(int UniqueID){
        Delete_Alarm(UniqueID);

        NowTime = Calendar.getInstance();
        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(context);

        for (DB_Date db_date : date_dataBase_management.getData()){

            if(db_date.isSelector() == false) {
                if (db_date.getDate_Year() == NowTime.get(Calendar.YEAR)) {
                    if (db_date.getDate_Month() == NowTime.get(Calendar.MONDAY) + 1) {
                        if (db_date.getDate_Day() == NowTime.get(Calendar.DATE)) {

                            if (db_date.isTimer_Activate()) {

                                if(db_date.getTime_Hour() > NowTime.get(Calendar.HOUR_OF_DAY) ||
                                        (db_date.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_date.getTime_Minute() > NowTime.get(Calendar.MINUTE))) {

                                    if (db_date.getUniqueID() == UniqueID) {
                                        Log.d("미래", db_date.getName());

                                        intent = new Intent(context, AlarmService.class);
                                        intent.putExtra("Notification_ID", db_date.getUniqueID());
                                        intent.putExtra("AlarmType", 2);
                                        intent.putExtra("Name", db_date.getName());
                                        intent.putExtra("Memo", db_date.getMemo());
                                        intent.putExtra("Important", db_date.isImportant());
                                        intent.putExtra("AlarmMethod", db_date.getAlarm_Method());
                                        intent.putExtra("AutoTimerOff", db_date.getAutoTimerOff());
                                        intent.putExtra("SoundValue", db_date.getSound_value());

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

                                        if (systemDataSave.getData_AllTimerOff() == false)
                                            AlarmManager_add(intent, db_date.getUniqueID(), calendar);

                                    }
                                }
                            }

                        }
                    }
                }
            }else{
                if (db_date.getDay() >= NowTime.get(Calendar.DATE)) {

                    if (db_date.isTimer_Activate() && !(db_date.getTime_Hour() == NowTime.get(Calendar.HOUR_OF_DAY) && db_date.getTime_Minute() == NowTime.get(Calendar.MINUTE))) {

                        if (db_date.getUniqueID() == UniqueID) {
                            Log.d("미래", db_date.getName());

                            intent = new Intent(context, AlarmService.class);
                            intent.putExtra("Notification_ID", db_date.getUniqueID());
                            intent.putExtra("AlarmType", 2);
                            intent.putExtra("Name", db_date.getName());
                            intent.putExtra("Memo", db_date.getMemo());
                            intent.putExtra("Important", db_date.isImportant());
                            intent.putExtra("AlarmMethod", db_date.getAlarm_Method());
                            intent.putExtra("AutoTimerOff", db_date.getAutoTimerOff());
                            intent.putExtra("SoundValue", db_date.getSound_value());

                            intent.putExtra("Resetting", true);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());

                            calendar.set(Calendar.HOUR_OF_DAY, db_date.getTime_Hour());
                            calendar.set(Calendar.MINUTE, db_date.getTime_Minute());
                            calendar.set(Calendar.SECOND, 1);
                            calendar.set(Calendar.MILLISECOND, 1000);

                            calendar.set(Calendar.YEAR, NowTime.get(Calendar.YEAR));
                            calendar.set(Calendar.MONDAY, NowTime.get(Calendar.MONDAY));
                            if(db_date.getDay() > 0) {
                                calendar.set(Calendar.DATE, db_date.getDay());
                            }else{
                                calendar.set(Calendar.DATE, 31);//마지막 날짜 지정
                            }

                            if (systemDataSave.getData_AllTimerOff() == false)
                                AlarmManager_add(intent, db_date.getUniqueID(), calendar);

                        }
                    }
                }
            }
        }
    }


    public void Delete_Alarm(int UniqueID){

        Log.d("알람삭제", "=============" + UniqueID);

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

