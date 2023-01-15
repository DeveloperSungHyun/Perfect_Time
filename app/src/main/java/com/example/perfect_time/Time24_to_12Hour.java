package com.example.perfect_time;

import android.content.Context;

public class Time24_to_12Hour {
    int Time24Hour;
    boolean Time_12to24 = false;//f = 12, t = 24

    public Time24_to_12Hour(int time24Hour, Context context) {
        SystemDataSave systemDataSave = new SystemDataSave(context);
        Time24Hour = time24Hour;

        Time_12to24 = systemDataSave.getData_Time24_to_12();
    }

    public int getTime_Hour(){
        int t = Time24Hour;
        if(Time_12to24 == false) {//12시
            if (t > 12) {
                t -= 12;
            }
            if(t == 0) t = 12;
        }else {
            if (t == 0) t = 24;
        }
        return t;
    }

    public String getAmPm(){
        if(Time_12to24 == false) {
            //if(Time24Hour == 0) Time24Hour = 12;
            if (Time24Hour >= 12) {
                return "오후";
            } else {
                return "오전";
            }
        }else{
            return null;
        }
    }
}
