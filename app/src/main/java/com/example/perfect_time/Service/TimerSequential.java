package com.example.perfect_time.Service;

import android.content.Context;
import android.util.Log;

import com.example.perfect_time.All_Time;
import com.example.perfect_time.OneDayTimeList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimerSequential {

    Context context;
    Calendar calendar;

    int y, m, d;
    //==============================
    OneDayTimeList oneDayTimeList;

    ArrayList<TimeData> timeDataArrayList, Warning_timeDataArrayList;

    int Warning_ToDayTimer_H, Warning_ToDayTimer_M;
    String Warning_ToDayTimer_Name, Warning_ToDayTimer_Memo;
    boolean Warning_Important;

    private class TimeData{//알람 데이터 클레스
        int Timer_H, Timer_M;
        String Timer_Name, Timer_Memo;
        boolean Important;

        TimeData(int Timer_H,int Timer_M,String Timer_Name,String Timer_Memo,boolean Important){
            this.Timer_H = Timer_H;
            this.Timer_M = Timer_M;
            this.Timer_Name = Timer_Name;
            this.Timer_Memo = Timer_Memo;
            this.Important = Important;
        }
    }

    public TimerSequential(Context context){
        this.context = context;
    }

    public void TimeDataUpDate(){
//        int NowTime_H, NowTime_M;
        int MaxDoDay;

        calendar = Calendar.getInstance();

//        NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
//        NowTime_M = calendar.get(Calendar.MINUTE);

        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONDAY) + 1;
        d = calendar.get(Calendar.DATE);

        oneDayTimeList = new OneDayTimeList(context, y, m, d);//하루일정 가져오기

        timeDataArrayList = new ArrayList<>();
        Warning_timeDataArrayList = new ArrayList<>();


        for(All_Time all_time : oneDayTimeList.getTimeList()){

            if(all_time.isTimer_Activate()) {
                int Hour = all_time.getTime_Hour();
                int Minute = all_time.getTime_Minute();

                TimeData timeData = new TimeData(Hour, Minute, all_time.getName(), all_time.getMemo(), all_time.isImportant());
                timeDataArrayList.add(timeData);

                if(Minute - all_time.getBeforehandTime() < 0){
                    Minute = Minute - all_time.getBeforehandTime() + 60;
                    Hour--;
                }else{
                    Minute = Minute - all_time.getBeforehandTime();
                }

                if(Hour >= 0){
                    TimeData timeData_1 = new TimeData(Hour, Minute, all_time.getName(), all_time.getMemo(), all_time.isImportant());
                    Warning_timeDataArrayList.add(timeData_1);
                }

                Log.d("Time", all_time.getTime_Hour() + " : " + all_time.getTime_Minute() + " | " + all_time.getName());
            }
        }

        MaxDoDay = calendar.getActualMaximum(Calendar.DATE);
        Log.d("MaxDoDay", Integer.toString(MaxDoDay));
        if(MaxDoDay == d){
            if(m == 12){
                m = 0;
                y++;
            }
            m++;
        }
        oneDayTimeList = new OneDayTimeList(context, y, m, d);

        for(All_Time all_time : oneDayTimeList.getTimeList()){
            int Hour = all_time.getTime_Hour();
            int Minute = all_time.getTime_Minute();

            if(all_time.isTimer_Activate()){
                if(Hour == 0){
                    if(Minute - all_time.getBeforehandTime() < 0){
                        Minute = Minute - all_time.getBeforehandTime() + 60;
                        Hour = 23;

                        TimeData timeData_1 = new TimeData(Hour, Minute, all_time.getName(), all_time.getMemo(), all_time.isImportant());
                        Warning_timeDataArrayList.add(timeData_1);
                    }
                }

            }
        }

        //=======================================================================
        for (int i = 0; i < Warning_timeDataArrayList.size() - 1; i++) {
            int Min_Index = i;
            for (int j = i + 1; j < Warning_timeDataArrayList.size(); j++) {
                if(Warning_timeDataArrayList.get(Min_Index).Timer_H > Warning_timeDataArrayList.get(j).Timer_H ||
                        (Warning_timeDataArrayList.get(Min_Index).Timer_H == Warning_timeDataArrayList.get(j).Timer_H &&
                                Warning_timeDataArrayList.get(Min_Index).Timer_M > Warning_timeDataArrayList.get(j).Timer_M)){

                    Min_Index = j;
                }
            }
            TimeData SaveDate = Warning_timeDataArrayList.get(i);
            Warning_timeDataArrayList.set(i, Warning_timeDataArrayList.get(Min_Index));
            Warning_timeDataArrayList.set(Min_Index, SaveDate);
        }


        for(TimeData data : Warning_timeDataArrayList){//TEST
            Log.d("===timeDataArrayList====", data.Timer_Name + data.Timer_H + " : " + data.Timer_M);
        }
    }



    public void Warning_TimeListSetting(){


    }

    //=================================================

}
