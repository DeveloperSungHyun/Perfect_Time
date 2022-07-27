package com.example.perfect_time;

public class SettingValue {

    private boolean Timer_Activate;         //알람 활성화
    private boolean Important;              //중요알람 표시

    private String Name;                    //알람 이름
    private String Memo;                    //알람 메모

    private int Time_Hour;                  //시간 시
    private int Time_Minute;                //시간 분

    private boolean Sound_Activate;         //소리알림 활성화 유무
    private boolean Vibration_Activate;     //진동알림 활성화 유무
    private boolean Popup_Activate;         //팝업알림 활성화 유무

    private boolean warning;                //알림 예고
    private int WarningTime;                //알림 예고 시간

    private int AutoOff;                    //알람 자동끄기

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

    public boolean isSound_Activate() {
        return Sound_Activate;
    }

    public void setSound_Activate(boolean sound_Activate) {
        Sound_Activate = sound_Activate;
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

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public int getWarningTime() {
        return WarningTime;
    }

    public void setWarningTime(int warningTime) {
        WarningTime = warningTime;
    }

    public int getAutoOff() {
        return AutoOff;
    }

    public void setAutoOff(int autoOff) {
        AutoOff = autoOff;
    }
}
