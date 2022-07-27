package com.example.perfect_time.RecyclerView;

public class RecyclerView_ListItem {
    private int ViewType;                   //리사이클러뷰 아이템뷰 디자인

    private boolean Timer_Activate;         //알람 활정화 유무

    private boolean Important;              //중요알람 표시

    private String Name;                    //알람 이름
    private String Memo;                    //알람 메모

    private int Time_Hour;                  //시간 시
    private int Time_Minute;                //시간 분

    private boolean Sound_Activate;         //소리알림 활성화 유무
    private boolean Vibration_Activate;     //진동알림 활성화 유무
    private boolean Popup_Activate;         //팝업알림 활성화 유무

    private int FragmentType;//프레그먼트 타입(매일, 요일, 날짜)

    public RecyclerView_ListItem
            (int viewType, boolean timer_Activate, boolean important, String name, String memo, int time_Hour,
             int time_Minute, boolean sound_Activate, boolean vibration_Activate, boolean popup_Activate, int fragment_Type) {

        ViewType = viewType;
        Timer_Activate = timer_Activate;
        Important = important;
        Name = name;
        Memo = memo;
        Time_Hour = time_Hour;
        Time_Minute = time_Minute;
        Sound_Activate = sound_Activate;
        Vibration_Activate = vibration_Activate;
        Popup_Activate = popup_Activate;
        FragmentType = fragment_Type;
    }

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
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

    public int getFragmentType() {
        return FragmentType;
    }

    public void setFragmentType(int fragmentType) {
        FragmentType = fragmentType;
    }
}
