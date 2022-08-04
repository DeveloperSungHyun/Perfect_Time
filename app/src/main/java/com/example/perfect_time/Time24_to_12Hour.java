package com.example.perfect_time;

public class Time24_to_12Hour {
    int Time24Hour;
    int Time12Hour;
    boolean AmPm;

    public Time24_to_12Hour(int time24Hour) {
        Time24Hour = time24Hour;
    }

    public int getTime12Hour(){
        if(Time24Hour == 0) Time12Hour = 12;
        else if(Time24Hour >= 13) Time12Hour = Time24Hour - 12;
        else Time12Hour = Time24Hour;

        return Time12Hour;
    }

    public boolean getAmPm(){
        if(Time24Hour >= 12){
            AmPm = true;
        } else {
            AmPm = false;
        }

        return AmPm;
    }
}
