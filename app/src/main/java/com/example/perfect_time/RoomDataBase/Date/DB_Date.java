package com.example.perfect_time.RoomDataBase.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DB_Date {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private int UniqueID;

    private boolean Timer_Activate;         //알람 활성화
    private boolean Important;              //중요알람 표시

    private String Name;                    //알람 이름
    private String Memo;                    //알람 메모

    private int Time_Hour;                  //시간 시
    private int Time_Minute;                //시간 분

    private boolean SoundVibration;         //알림
    private boolean HeadUp;                 //노티피케이션
    private boolean Popup_Activate;         //팝업알림 활성화 유무

    public boolean isSoundVibration() {
        return SoundVibration;
    }

    public void setSoundVibration(boolean soundVibration) {
        SoundVibration = soundVibration;
    }

    public boolean isHeadUp() {
        return HeadUp;
    }

    public void setHeadUp(boolean headUp) {
        HeadUp = headUp;
    }

    private int Date_Year;
    private int Date_Month;
    private int Date_Day;

    public int getId() {
        return id;
    }

    public int getUniqueID() {
        return UniqueID;
    }

    public void setUniqueID(int uniqueID) {
        UniqueID = uniqueID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTimer_Activate() {
        return Timer_Activate;
    }

    public void setTimer_Activate(boolean timer_Activate) {
        Timer_Activate = timer_Activate;
    }

    public boolean isImportant() {
        return Important;
    }

    public void setImportant(boolean important) {
        Important = important;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public int getTime_Hour() {
        return Time_Hour;
    }

    public void setTime_Hour(int time_Hour) {
        Time_Hour = time_Hour;
    }

    public int getTime_Minute() {
        return Time_Minute;
    }

    public void setTime_Minute(int time_Minute) {
        Time_Minute = time_Minute;
    }

    public boolean isPopup_Activate() {
        return Popup_Activate;
    }

    public void setPopup_Activate(boolean popup_Activate) {
        Popup_Activate = popup_Activate;
    }

    public int getDate_Year() {
        return Date_Year;
    }

    public void setDate_Year(int date_Year) {
        Date_Year = date_Year;
    }

    public int getDate_Month() {
        return Date_Month;
    }

    public void setDate_Month(int date_Month) {
        Date_Month = date_Month;
    }

    public int getDate_Day() {
        return Date_Day;
    }

    public void setDate_Day(int date_Day) {
        Date_Day = date_Day;
    }
}
