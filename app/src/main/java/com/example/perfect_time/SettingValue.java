package com.example.perfect_time;

public class SettingValue {

    private static boolean Timer_Activate;         //알람 활성화
    private static boolean Important;              //중요알람 표시

    private static String Name;                    //알람 이름
    private static String Memo;                    //알람 메모

    private static int Time_Hour;                  //시간 시
    private static int Time_Minute;                //시간 분

    private static int Alarm_Method;                //알림 방식

    public static int getAlarm_Method() {
        return Alarm_Method;
    }

    public static void setAlarm_Method(int alarm_Method) {
        Alarm_Method = alarm_Method;
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


}
