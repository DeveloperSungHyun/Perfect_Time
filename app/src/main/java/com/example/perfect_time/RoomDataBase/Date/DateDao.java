package com.example.perfect_time.RoomDataBase.Date;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;

import java.util.List;

@Dao
public interface DateDao {

    @Insert
    void setInsert(DB_Date db_date);

    @Update
    void setUpdate(DB_Date db_date);

    @Delete
    void setDelete(DB_Date db_date);

    @Query("SELECT * FROM DB_Date ORDER BY Selector, Date_Year, Date_Month, Date_Day, Time_Hour, Time_Minute, Name")
    List<DB_Date> getDate();
}
