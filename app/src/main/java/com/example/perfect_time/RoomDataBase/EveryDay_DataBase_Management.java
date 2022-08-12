package com.example.perfect_time.RoomDataBase;

import android.content.Context;

import androidx.room.Room;

import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDao;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDayDataBase;
import com.example.perfect_time.SettingValue;

public class EveryDay_DataBase_Management {

    SettingValue settingValue;

    DB_EveryDay db_everyDay;
    EveryDao everyDao;
    EveryDayDataBase everyDayDataBase;

    public EveryDay_DataBase_Management(Context context, SettingValue settingValue){

        this.settingValue = settingValue;

        everyDayDataBase = Room.databaseBuilder(context, EveryDayDataBase.class, "EveryDayDataBase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        everyDao = everyDayDataBase.everyDao();

        db_everyDay = new DB_EveryDay();


    }

    public void setInsert(){


        db_everyDay.setTimer_Activate(settingValue.isTimer_Activate());
        db_everyDay.setImportant(settingValue.isImportant());

        db_everyDay.setName(settingValue.getName());
        db_everyDay.setMemo(settingValue.getMemo());

        db_everyDay.setTime_Hour(settingValue.getTime_Hour());
        db_everyDay.setTime_Minute(settingValue.getTime_Minute());

        db_everyDay.setSound_Activate(settingValue.isSound_Activate());
        db_everyDay.setSound_volume(settingValue.getSound_volume());

        db_everyDay.setVibration_Activate(settingValue.isVibration_Activate());
        db_everyDay.setVibration_volume(settingValue.getVibration_volume());

        db_everyDay.setPopup_Activate(settingValue.isPopup_Activate());

        db_everyDay.setBeforehand(settingValue.isBeforehand());
        db_everyDay.setBeforehandTime(settingValue.getBeforehandTime());

        db_everyDay.setAutoOffTime(settingValue.getAutoOffTime());

        everyDao.setInsert(db_everyDay);
    }
}
