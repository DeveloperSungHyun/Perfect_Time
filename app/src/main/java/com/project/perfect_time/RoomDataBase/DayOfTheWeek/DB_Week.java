package com.project.perfect_time.RoomDataBase.DayOfTheWeek;

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

    private int Alarm_Method;                //알림 방식

    private int AutoTimerOff;               //자동 꺼짐시간
    private int Sound_value;                //볼륨

    private int dayOfTheWeek;               //요일

    public int getAutoTimerOff() {
        return AutoTimerOff;
    }

    public void setAutoTimerOff(int autoTimerOff) {
        AutoTimerOff = autoTimerOff;
    }

    public int getSound_value() {
        return Sound_value;
    }

    public void setSound_value(int sound_value) {
        Sound_value = sound_value;
    }


    public int getAlarm_Method() {
        return Alarm_Method;
    }

    public void setAlarm_Method(int alarm_Method) {
        Alarm_Method = alarm_Method;
    }

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

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }
}
