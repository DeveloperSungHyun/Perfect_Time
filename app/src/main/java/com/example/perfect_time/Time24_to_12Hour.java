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

        if(Time_12to24 == false) {//12시
            if (Time24Hour > 12) {
                Time24Hour -= 12;
            }
            if(Time24Hour == 0) Time24Hour = 12;
        }else {
            if (Time24Hour == 0) Time24Hour = 24;
        }
        return Time24Hour;
    }

    public String getAmPm(){
        if(Time_12to24 == false) {
            if (Time24Hour > 12 && Time24Hour < 24) {
                return "오후";
            } else {
                return "오전";
            }
        }else{
            return null;
        }
    }
}
