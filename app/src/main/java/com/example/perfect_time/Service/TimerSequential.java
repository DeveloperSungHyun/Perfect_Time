package com.example.perfect_time.Service;

import android.util.Log;

import com.example.perfect_time.All_Time;
import com.example.perfect_time.OneDayTimeList;

import java.util.Calendar;

public class TimerSequential {

    Calendar calendar;
    int NowTime_H, NowTime_M;

    int Num_Count = 0;
    //==============================
    OneDayTimeList oneDayTimeList;

    int ToDayTimer_H, ToDayTimer_M;
    String ToDayTimer_Name, ToDayTimer_Memo;
    boolean Important;

    int Warning_ToDayTimer_H, Warning_ToDayTimer_M;
    String Warning_ToDayTimer_Name, Warning_ToDayTimer_Memo;
    boolean Warning_Important;

    public TimerSequential(OneDayTimeList oneDayTimeList){
        this.oneDayTimeList = oneDayTimeList;
    }

    public void TimeListSetting(){

        Num_Count = 0;
        for(All_Time all_time : oneDayTimeList.getTimeList()){

            calendar = Calendar.getInstance();

            NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
            NowTime_M = calendar.get(Calendar.MINUTE);

            if(all_time.getTime_Hour() >= NowTime_H){
                if((all_time.getTime_Hour() == NowTime_H && all_time.getTime_Minute() > NowTime_M) || all_time.getTime_Hour() > NowTime_H){
                    ToDayTimer_H = all_time.getTime_Hour();
                    ToDayTimer_M = all_time.getTime_Minute();
                    ToDayTimer_Name = all_time.getName();
                    ToDayTimer_Memo = all_time.getMemo();
                    Important = all_time.isImportant();

                    Num_Count++;
                    Log.d("Time", all_time.getTime_Hour() + " : " + all_time.getTime_Minute() + " | " + all_time.getName());
                    break;
                }
            }
        }
    }

    public int get_ToDayTimerSize(){//알람 리스트 개수
        return oneDayTimeList.getTimeList().size();
    }

    public int get_NextTimerSize(){//남은 알람 개수
        return get_ToDayTimerSize() - Num_Count;
    }

    public int getToDayTimer_H() {
        return ToDayTimer_H;
    }

    public int getToDayTimer_M() {
        return ToDayTimer_M;
    }

    public String getToDayTimer_Name() {
        return ToDayTimer_Name;
    }

    public String getToDayTimer_Memo() {
        return ToDayTimer_Memo;
    }

    public boolean isImportant() {
        return Important;
    }



    public int getWarning_ToDayTimer_H() {
        return Warning_ToDayTimer_H;
    }

    public int getWarning_ToDayTimer_M() {
        return Warning_ToDayTimer_M;
    }

    public String getWarning_ToDayTimer_Name() {
        return Warning_ToDayTimer_Name;
    }

    public String getWarning_ToDayTimer_Memo() {
        return Warning_ToDayTimer_Memo;
    }

    public boolean isWarning_Important() {
        return Warning_Important;
    }
}
