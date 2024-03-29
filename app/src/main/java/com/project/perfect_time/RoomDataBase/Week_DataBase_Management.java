package com.project.perfect_time.RoomDataBase;

import android.content.Context;

import androidx.room.Room;

import com.project.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.project.perfect_time.RoomDataBase.DayOfTheWeek.WeekDao;
import com.project.perfect_time.RoomDataBase.DayOfTheWeek.WeekDataBase;
import com.project.perfect_time.SettingValue;

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
        int ID_Number = 11;

        for (int i = 1; i <= getData().size() + 1; i++) {
            for (DB_Week db_week : getData()){
                if(db_week.getUniqueID() == i * 10 + 1){
                    break;
                }else if(db_week.getUniqueID() == getData().get(getData().size() - 1).getUniqueID()){
                    ID_Number = i * 10 + 1;
                }
            }

        }
        db_week.setUniqueID(ID_Number);

        db_week.setTimer_Activate(settingValue.isTimer_Activate());
        db_week.setImportant(settingValue.isImportant());

        db_week.setDayOfTheWeek(DayOfTheWeek);

        db_week.setName(settingValue.getName());
        db_week.setMemo(settingValue.getMemo());

        db_week.setTime_Hour(settingValue.getTime_Hour());
        db_week.setTime_Minute(settingValue.getTime_Minute());

        db_week.setAlarm_Method(settingValue.getAlarm_Method());

        db_week.setAutoTimerOff(settingValue.getAutoTimerOff());
        db_week.setSound_value(settingValue.getSound_value());

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

        db_week.setUniqueID(weekDao.getWeek().get(UpDataItem_ID).getUniqueID());

        db_week.setName(settingValue.getName());
        db_week.setMemo(settingValue.getMemo());

        db_week.setTime_Hour(settingValue.getTime_Hour());
        db_week.setTime_Minute(settingValue.getTime_Minute());

        db_week.setAlarm_Method(settingValue.getAlarm_Method());

        db_week.setAutoTimerOff(settingValue.getAutoTimerOff());
        db_week.setSound_value(settingValue.getSound_value());

        weekDao.setUpdate(db_week);
    }

    public void setTimeOnOff(int UpDataItem_ID, boolean Activate){

        int DataId = UpDataItem_ID;

        db_week.setId(weekDao.getWeek().get(UpDataItem_ID).getId());
        db_week.setTimer_Activate(Activate);

        db_week.setImportant(getData().get(DataId).isImportant());

        db_week.setDayOfTheWeek(getData().get(DataId).getDayOfTheWeek());

        db_week.setUniqueID(weekDao.getWeek().get(UpDataItem_ID).getUniqueID());

        db_week.setName(getData().get(DataId).getName());
        db_week.setMemo(getData().get(DataId).getMemo());

        db_week.setTime_Hour(getData().get(DataId).getTime_Hour());
        db_week.setTime_Minute(getData().get(DataId).getTime_Minute());

        db_week.setAlarm_Method(getData().get(DataId).getAlarm_Method());

        db_week.setAutoTimerOff(getData().get(DataId).getAutoTimerOff());
        db_week.setSound_value(getData().get(DataId).getSound_value());

        weekDao.setUpdate(db_week);
    }
}
