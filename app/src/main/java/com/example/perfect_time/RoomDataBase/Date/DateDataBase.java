package com.example.perfect_time.RoomDataBase.Date;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.WeekDao;

@Database(entities = {DB_Date.class}, version = 2)
public abstract class DateDataBase extends RoomDatabase {

    public abstract DateDao dateDao();
}
