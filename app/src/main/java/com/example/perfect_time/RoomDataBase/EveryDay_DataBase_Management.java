package com.example.perfect_time.RoomDataBase;

import android.content.Context;

import androidx.room.Room;

import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDao;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDayDataBase;
import com.example.perfect_time.SettingValue;

import java.util.Calendar;
import java.util.List;

public class EveryDay_DataBase_Management {

    Calendar calendar;

    DB_EveryDay db_everyDay;
    EveryDao everyDao;
    EveryDayDataBase everyDayDataBase;

    public EveryDay_DataBase_Management(Context context){


        everyDayDataBase = Room.databaseBuilder(context, EveryDayDataBase.class, "EveryDayDataBase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        everyDao = everyDayDataBase.everyDao();

        db_everyDay = new DB_EveryDay();


    }

    public void setInsert(SettingValue settingValue){
        int ID_Number = 10;

        db_everyDay.setTimer_Activate(settingValue.isTimer_Activate());
        db_everyDay.setImportant(settingValue.isImportant());

        for (int i = 1; i <= getData().size() + 1; i++) {
            for (DB_EveryDay everyDay : getData()){
                if(everyDay.getUniqueID() == i * 10){
                    break;
                }else if(everyDay.getUniqueID() == getData().get(getData().size() - 1).getUniqueID()){
                    ID_Number = i * 10;
                }
            }

        }

        db_everyDay.setUniqueID(ID_Number);

        db_everyDay.setName(settingValue.getName());
        db_everyDay.setMemo(settingValue.getMemo());

        db_everyDay.setTime_Hour(settingValue.getTime_Hour());
        db_everyDay.setTime_Minute(settingValue.getTime_Minute());

        db_everyDay.setVibration_Activate(settingValue.isVibration_Activate());
        db_everyDay.setHeadUp_Activate(settingValue.isHeadUp_Activate());
        db_everyDay.setPopup_Activate(settingValue.isPopup_Activate());
        db_everyDay.setAutoDisplay_On(settingValue.isAutoDisplay_On());

        everyDao.setInsert(db_everyDay);
    }

    public void setUpData(int UpDataItem_ID, SettingValue settingValue){

        db_everyDay.setId(everyDao.getEveryDay().get(UpDataItem_ID).getId());

        db_everyDay.setTimer_Activate(settingValue.isTimer_Activate());
        db_everyDay.setImportant(settingValue.isImportant());

        db_everyDay.setUniqueID(everyDao.getEveryDay().get(UpDataItem_ID).getUniqueID());

        db_everyDay.setName(settingValue.getName());
        db_everyDay.setMemo(settingValue.getMemo());

        db_everyDay.setTime_Hour(settingValue.getTime_Hour());
        db_everyDay.setTime_Minute(settingValue.getTime_Minute());

        db_everyDay.setVibration_Activate(settingValue.isVibration_Activate());
        db_everyDay.setHeadUp_Activate(settingValue.isHeadUp_Activate());
        db_everyDay.setPopup_Activate(settingValue.isPopup_Activate());
        db_everyDay.setAutoDisplay_On(settingValue.isAutoDisplay_On());
        everyDao.setUpdate(db_everyDay);
    }

    public void setTimeOnOff(int UpDataItem_ID, boolean Activate){

        int DataId = UpDataItem_ID;

        db_everyDay.setId(everyDao.getEveryDay().get(UpDataItem_ID).getId());
        db_everyDay.setTimer_Activate(Activate);

        db_everyDay.setImportant(getData().get(DataId).isImportant());

        db_everyDay.setUniqueID(everyDao.getEveryDay().get(UpDataItem_ID).getUniqueID());

        db_everyDay.setName(getData().get(DataId).getName());
        db_everyDay.setMemo(getData().get(DataId).getMemo());

        db_everyDay.setTime_Hour(getData().get(DataId).getTime_Hour());
        db_everyDay.setTime_Minute(getData().get(DataId).getTime_Minute());

        db_everyDay.setVibration_Activate(getData().get(DataId).isVibration_Activate());
        db_everyDay.setHeadUp_Activate(getData().get(DataId).isHeadUp_Activate());
        db_everyDay.setPopup_Activate(getData().get(DataId).isPopup_Activate());
        db_everyDay.setAutoDisplay_On(getData().get(DataId).isAutoDisplay_On());

        everyDao.setUpdate(db_everyDay);
    }

    public void setDelete(int DeleteItem_ID){
        db_everyDay.setId(everyDao.getEveryDay().get(DeleteItem_ID).getId());
        everyDao.setDelete(db_everyDay);
    }


    public List<DB_EveryDay> getData(){

        List<DB_EveryDay> dbEveryDayList;

        dbEveryDayList = everyDao.getEveryDay();


        return dbEveryDayList;
    }
}
