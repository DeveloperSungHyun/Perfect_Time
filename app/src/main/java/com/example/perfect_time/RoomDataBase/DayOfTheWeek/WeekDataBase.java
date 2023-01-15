package com.example.perfect_time.RoomDataBase.DayOfTheWeek;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDao;

@Database(entities = {DB_Week.class}, version = 4)
public abstract class WeekDataBase extends RoomDatabase {

    public abstract WeekDao weekDao();
}
