package com.project.perfect_time.RoomDataBase.DayOfTheWeek;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DB_Week.class}, version = 8)
public abstract class WeekDataBase extends RoomDatabase {

    public abstract WeekDao weekDao();
}
