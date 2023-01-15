package com.example.perfect_time;

public class SettingValue {

    private static boolean Timer_Activate;         //알람 활성화
    private static boolean Important;              //중요알람 표시

    private static String Name;                    //알람 이름
    private static String Memo;                    //알람 메모

    private static int Time_Hour;                  //시간 시
    private static int Time_Minute;                //시간 분

    private static int century;//알림 강도
    private static boolean Popup_Activate;         //팝업알림 활성화 유무
    private static boolean AutoDisplay_On;


    public static int getCentury() {
        return century;
    }

    public static void setCentury(int century) {
        SettingValue.century = century;
    }

    public static boolean isAutoDisplay_On() {
        return AutoDisplay_On;
    }

    public static void setAutoDisplay_On(boolean autoDisplay_On) {
        AutoDisplay_On = autoDisplay_On;
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

}
