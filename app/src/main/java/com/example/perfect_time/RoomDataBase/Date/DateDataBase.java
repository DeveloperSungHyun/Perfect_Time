package com.example.perfect_time.RoomDataBase.Date;

import androidx.room.Database;

import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.WeekDao;

@Database(entities = {DB_Date.class}, version = 0)
public abstract class DateDataBase {

    public abstract DateDao dateDao();
}
