package com.example.perfect_time.RoomDataBase;

import android.content.Context;

import androidx.room.Room;

import com.example.perfect_time.RoomDataBase.Date.DB_Date;
import com.example.perfect_time.RoomDataBase.Date.DateDao;
import com.example.perfect_time.RoomDataBase.Date.DateDataBase;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.WeekDao;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.WeekDataBase;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDao;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDayDataBase;
import com.example.perfect_time.SettingValue;

import java.util.Calendar;
import java.util.List;

public class Date_DataBase_Management {

    Calendar calendar;

    DB_Date db_date;
    DateDao dateDao;
    DateDataBase dateDataBase;

    public Date_DataBase_Management(Context context){


        dateDataBase = Room.databaseBuilder(context, DateDataBase.class, "DateDataBase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        dateDao = dateDataBase.dateDao();

        db_date = new DB_Date();


    }

    public void setInsert(SettingValue settingValue, int y, int m, int d){

        int ID_Number = 12;

        for (int i = 1; i <= getData().size() + 1; i++) {
            for (DB_Date db_date : getData()){
                if(db_date.getUniqueID() == i * 10 + 2){
                    break;
                }else if(db_date.getUniqueID() == getData().get(getData().size() - 1).getUniqueID()){
                    ID_Number = i * 10 + 2;
                }
            }

        }
        db_date.setUniqueID(ID_Number);

        db_date.setTimer_Activate(settingValue.isTimer_Activate());
        db_date.setImportant(settingValue.isImportant());

        db_date.setDate_Year(y);
        db_date.setDate_Month(m);
        db_date.setDate_Day(d);

        db_date.setName(settingValue.getName());
        db_date.setMemo(settingValue.getMemo());

        db_date.setTime_Hour(settingValue.getTime_Hour());
        db_date.setTime_Minute(settingValue.getTime_Minute());

        db_date.setAlarm_Method(settingValue.getAlarm_Method());

        db_date.setAutoTimerOff(settingValue.getAutoTimerOff());
        db_date.setSound_value(settingValue.getSound_value());

        dateDao.setInsert(db_date);
    }

    public void setDelete(int DeleteItem_ID){
        db_date.setId(dateDao.getDate().get(DeleteItem_ID).getId());
        dateDao.setDelete(db_date);
    }

    public void setUpData(int UpDataItem_ID, SettingValue settingValue, int y, int m, int d){
        db_date.setId(dateDao.getDate().get(UpDataItem_ID).getId());

        db_date.setTimer_Activate(settingValue.isTimer_Activate());
        db_date.setImportant(settingValue.isImportant());

        db_date.setDate_Year(y);
        db_date.setDate_Month(m);
        db_date.setDate_Day(d);

        db_date.setUniqueID(dateDao.getDate().get(UpDataItem_ID).getUniqueID());

        db_date.setName(settingValue.getName());
        db_date.setMemo(settingValue.getMemo());

        db_date.setTime_Hour(settingValue.getTime_Hour());
        db_date.setTime_Minute(settingValue.getTime_Minute());

        db_date.setAlarm_Method(settingValue.getAlarm_Method());

        db_date.setAutoTimerOff(settingValue.getAutoTimerOff());
        db_date.setSound_value(settingValue.getSound_value());

        dateDao.setUpdate(db_date);
    }

    public void setTimeOnOff(int UpDataItem_ID, boolean Activate){

        int DataId = UpDataItem_ID;

        db_date.setId(dateDao.getDate().get(UpDataItem_ID).getId());
        db_date.setTimer_Activate(Activate);

        db_date.setImportant(getData().get(DataId).isImportant());

        db_date.setDate_Year(getData().get(DataId).getDate_Year());
        db_date.setDate_Month(getData().get(DataId).getDate_Month());
        db_date.setDate_Day(getData().get(DataId).getDate_Day());

        db_date.setUniqueID(dateDao.getDate().get(UpDataItem_ID).getUniqueID());

        db_date.setName(getData().get(DataId).getName());
        db_date.setMemo(getData().get(DataId).getMemo());

        db_date.setTime_Hour(getData().get(DataId).getTime_Hour());
        db_date.setTime_Minute(getData().get(DataId).getTime_Minute());

        db_date.setAlarm_Method(getData().get(DataId).getAlarm_Method());

        db_date.setAutoTimerOff(getData().get(DataId).getAutoTimerOff());
        db_date.setSound_value(getData().get(DataId).getSound_value());

        dateDao.setUpdate(db_date);
    }

    public List<DB_Date> getData(){

        List<DB_Date> dbDateList;

        dbDateList = dateDao.getDate();


        return dbDateList;
    }
}
