package com.example.perfect_time.RoomDataBase.Everyday;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DB_EveryDay.class}, version = 0)
public abstract class EveryDayDataBase extends RoomDatabase {

    public abstract EveryDao everyDao();
}
