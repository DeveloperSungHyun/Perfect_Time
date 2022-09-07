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

public class Week_DataBase_Management {



    DB_Week db_week;
    WeekDao weekDao;
    WeekDataBase weekDataBase;

    public Week_DataBase_Management(Context context){


        weekDataBase = Room.databaseBuilder(context, WeekDataBase.class, "WeekDataBase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        weekDao = weekDataBase.weekDao();

        db_week = new DB_Week();


    }

    public void setInsert(SettingValue settingValue, int DayOfTheWeek){


        db_week.setTimer_Activate(settingValue.isTimer_Activate());
        db_week.setImportant(settingValue.isImportant());

        db_week.setDayOfTheWeek(DayOfTheWeek);

        db_week.setName(settingValue.getName());
        db_week.setMemo(settingValue.getMemo());

        db_week.setTime_Hour(settingValue.getTime_Hour());
        db_week.setTime_Minute(settingValue.getTime_Minute());

        db_week.setSound_Activate(settingValue.isSound_Activate());
        db_week.setSound_volume(settingValue.getSound_volume());

        db_week.setVibration_Activate(settingValue.isVibration_Activate());
        db_week.setVibration_volume(settingValue.getVibration_volume());

        db_week.setPopup_Activate(settingValue.isPopup_Activate());

        db_week.setBeforehand(settingValue.isBeforehand());
        db_week.setBeforehandTime(settingValue.getBeforehandTime());

        db_week.setAutoOffTime(settingValue.getAutoOffTime());

        weekDao.setInsert(db_week);
    }

    public List<DB_Week> getData(){

        List<DB_Week> db_weekList;

        db_weekList = weekDao.getWeek();


        return db_weekList;
    }

    public void setDelete(int DeleteItem_ID){
        db_week.setId(weekDao.getWeek().get(DeleteItem_ID).getId());
        weekDao.setDelete(db_week);
    }

    public void setUpData(int UpDataItem_ID, SettingValue settingValue, int DayOfTheWeek){

        db_week.setId(weekDao.getWeek().get(UpDataItem_ID).getId());

        db_week.setTimer_Activate(settingValue.isTimer_Activate());
        db_week.setImportant(settingValue.isImportant());

        db_week.setDayOfTheWeek(DayOfTheWeek);

        db_week.setName(settingValue.getName());
        db_week.setMemo(settingValue.getMemo());

        db_week.setTime_Hour(settingValue.getTime_Hour());
        db_week.setTime_Minute(settingValue.getTime_Minute());

        db_week.setSound_Activate(settingValue.isSound_Activate());
        db_week.setSound_volume(settingValue.getSound_volume());

        db_week.setVibration_Activate(settingValue.isVibration_Activate());
        db_week.setVibration_volume(settingValue.getVibration_volume());

        db_week.setPopup_Activate(settingValue.isPopup_Activate());

        db_week.setBeforehand(settingValue.isBeforehand());
        db_week.setBeforehandTime(settingValue.getBeforehandTime());

        db_week.setAutoOffTime(settingValue.getAutoOffTime());

        weekDao.setUpdate(db_week);
    }

    public void setTimeOnOff(int UpDataItem_ID, boolean Activate){

        int DataId = UpDataItem_ID;

        db_week.setId(weekDao.getWeek().get(UpDataItem_ID).getId());
        db_week.setTimer_Activate(Activate);

        db_week.setImportant(getData().get(DataId).isImportant());

        db_week.setName(getData().get(DataId).getName());
        db_week.setMemo(getData().get(DataId).getMemo());

        db_week.setTime_Hour(getData().get(DataId).getTime_Hour());
        db_week.setTime_Minute(getData().get(DataId).getTime_Minute());

        db_week.setSound_Activate(getData().get(DataId).isSound_Activate());
        db_week.setSound_volume(getData().get(DataId).getSound_volume());

        db_week.setVibration_Activate(getData().get(DataId).isVibration_Activate());
        db_week.setVibration_volume(getData().get(DataId).getVibration_volume());

        db_week.setPopup_Activate(getData().get(DataId).isPopup_Activate());

        db_week.setBeforehand(getData().get(DataId).isBeforehand());
        db_week.setBeforehandTime(getData().get(DataId).getBeforehandTime());

        db_week.setAutoOffTime(getData().get(DataId).getAutoOffTime());

        weekDao.setUpdate(db_week);
    }
}
