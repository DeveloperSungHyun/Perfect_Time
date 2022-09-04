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

import java.util.List;

public class Date_DataBase_Management {

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


        db_date.setTimer_Activate(settingValue.isTimer_Activate());
        db_date.setImportant(settingValue.isImportant());

        db_date.setDate_Year(y);
        db_date.setDate_Month(m);
        db_date.setDate_Day(d);

        db_date.setName(settingValue.getName());
        db_date.setMemo(settingValue.getMemo());

        db_date.setTime_Hour(settingValue.getTime_Hour());
        db_date.setTime_Minute(settingValue.getTime_Minute());

        db_date.setSound_Activate(settingValue.isSound_Activate());
        db_date.setSound_volume(settingValue.getSound_volume());

        db_date.setVibration_Activate(settingValue.isVibration_Activate());
        db_date.setVibration_volume(settingValue.getVibration_volume());

        db_date.setPopup_Activate(settingValue.isPopup_Activate());

        db_date.setBeforehand(settingValue.isBeforehand());
        db_date.setBeforehandTime(settingValue.getBeforehandTime());

        db_date.setAutoOffTime(settingValue.getAutoOffTime());

        dateDao.setInsert(db_date);
    }

    public List<DB_Date> getData(){

        List<DB_Date> dbDateList;

        dbDateList = dateDao.getDate();


        return dbDateList;
    }
}
