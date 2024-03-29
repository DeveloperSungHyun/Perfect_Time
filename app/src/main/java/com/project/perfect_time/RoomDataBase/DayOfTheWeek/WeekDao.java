package com.project.perfect_time.RoomDataBase.DayOfTheWeek;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeekDao {

    @Insert
    void setInsert(DB_Week db_week);

    @Update
    void setUpdate(DB_Week db_week);

    @Delete
    void setDelete(DB_Week db_week);

    @Query("SELECT * FROM DB_Week ORDER BY dayOfTheWeek, Time_Hour, Time_Minute, Name")
    List<DB_Week> getWeek();

}
