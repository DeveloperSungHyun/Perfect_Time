package com.example.perfect_time.RoomDataBase.DayOfTheWeek;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DB_Week {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private int UniqueID;

    private boolean Timer_Activate;         //알람 활성화
    private boolean Important;              //중요알람 표시

    private String Name;                    //알람 이름
    private String Memo;                    //알람 메모

    private int Time_Hour;                  //시간 시
    private int Time_Minute;                //시간 분

    private boolean Vibration_Activate;     //진동알림 활성화 유무
    private boolean isHeadUp_Activate;
    private boolean Popup_Activate;         //팝업알림 활성화 유무
    private boolean AutoDisplay_On;


    public boolean isHeadUp_Activate() {
        return isHeadUp_Activate;
    }

    public void setHeadUp_Activate(boolean headUp_Activate) {
        isHeadUp_Activate = headUp_Activate;
    }

    public boolean isAutoDisplay_On() {
        return AutoDisplay_On;
    }

    public void setAutoDisplay_On(boolean autoDisplay_On) {
        AutoDisplay_On = autoDisplay_On;
    }


    private int dayOfTheWeek;               //요일

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

    public boolean isVibration_Activate() {
        return Vibration_Activate;
    }

    public void setVibration_Activate(boolean vibration_Activate) {
        Vibration_Activate = vibration_Activate;
    }

    public boolean isPopup_Activate() {
        return Popup_Activate;
    }

    public void setPopup_Activate(boolean popup_Activate) {
        Popup_Activate = popup_Activate;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
}
