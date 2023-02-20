package com.project.perfect_time.RoomDataBase.Everyday;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EveryDao {

    @Insert
    void setInsert(DB_EveryDay db_everyDay);

    @Update
    void setUpdate(DB_EveryDay db_everyDay);

    @Delete
    void setDelete(DB_EveryDay db_everyDay);

    @Query("SELECT * FROM DB_EveryDay ORDER BY Time_Hour, Time_Minute, Name")
    List<DB_EveryDay> getEveryDay();
}
