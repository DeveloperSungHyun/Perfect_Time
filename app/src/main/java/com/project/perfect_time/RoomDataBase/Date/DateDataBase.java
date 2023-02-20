package com.project.perfect_time.RoomDataBase.Date;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DB_Date.class}, version = 9)
public abstract class DateDataBase extends RoomDatabase {

    public abstract DateDao dateDao();
}
