package com.project.perfect_time.RoomDataBase.Everyday;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DB_EveryDay.class}, version = 8)
public abstract class EveryDayDataBase extends RoomDatabase {

    public abstract EveryDao everyDao();
}
