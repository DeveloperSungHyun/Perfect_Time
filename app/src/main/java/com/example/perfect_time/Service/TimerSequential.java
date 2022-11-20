package com.example.perfect_time.Service;

import android.content.Context;
import android.util.Log;

import com.example.perfect_time.All_Time;
import com.example.perfect_time.OneDayTimeList;

import java.util.ArrayList;
import java.util.Calendar;

public class TimerSequential {

    Context context;
    Calendar calendar;

    int y, m, d;
    //==============================
    OneDayTimeList oneDayTimeList;

    ArrayList<Warning_TimeData> Warning_timeDataArrayList;
    ArrayList<All_Time> ToDayTimerData;

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

        ToDayTimerData = new ArrayList<>();
        Warning_timeDataArrayList = new ArrayList<>();


        for(All_Time all_time : oneDayTimeList.getTimeList()){

            if(all_time.isTimer_Activate()) {
                int Hour = all_time.getTime_Hour();
                int Minute = all_time.getTime_Minute();

                ToDayTimerData.add(all_time);

                if(Minute - all_time.getBeforehandTime() < 0){
                    Minute = Minute - all_time.getBeforehandTime() + 60;
                    Hour--;
                }else{
                    Minute = Minute - all_time.getBeforehandTime();
                }

                if(Hour >= 0){
                    Warning_TimeData timeData_1 = new Warning_TimeData(Hour, Minute, all_time.getName(), all_time.getMemo(), all_time.isImportant());
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

                        Warning_TimeData timeData_1 = new Warning_TimeData(Hour, Minute, all_time.getName(), all_time.getMemo(), all_time.isImportant());
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
            Warning_TimeData SaveDate = Warning_timeDataArrayList.get(i);
            Warning_timeDataArrayList.set(i, Warning_timeDataArrayList.get(Min_Index));
            Warning_timeDataArrayList.set(Min_Index, SaveDate);
        }


        for(Warning_TimeData data : Warning_timeDataArrayList){//TEST
            Log.d("===timeDataArrayList====", data.Timer_Name + data.Timer_H + " : " + data.Timer_M);
        }

        for(All_Time data : ToDayTimerData){//TEST
            Log.d("===ToDayTimerData====", data.getName() + data.getTime_Hour() + " : " + data.getTime_Minute());
        }
    }

    //=================================================

    public ArrayList<All_Time> ToDayTimer_getData(){
        return ToDayTimerData;
    }

    public ArrayList<Warning_TimeData> ToDay_WarningTimer_getData(){
        return Warning_timeDataArrayList;
    }

    public All_Time Next_getTimer(){
        calendar = Calendar.getInstance();

        int NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        int NowTime_M = calendar.get(Calendar.MINUTE);

        for(All_Time time : ToDayTimer_getData()){
            if(NowTime_H < time.getTime_Hour() || (NowTime_H == time.getTime_Hour() && NowTime_M < time.getTime_Minute())){
                return time;
            }
        }

        return null;
    }

    public Warning_TimeData NextWarning_getTimer(){
        calendar = Calendar.getInstance();

        int NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        int NowTime_M = calendar.get(Calendar.MINUTE);

        Log.d("NowTime_H=========================", NowTime_H + " : " + NowTime_M);

        for(Warning_TimeData time : ToDay_WarningTimer_getData()){
            if(NowTime_H < time.Timer_H || (NowTime_H == time.Timer_H && NowTime_M < time.Timer_M)){
                return time;
            }
        }

        return null;
    }

    public ArrayList<All_Time> NextTimerList(){

        ArrayList<All_Time> all_times = new ArrayList<>();
        calendar = Calendar.getInstance();

        int NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        int NowTime_M = calendar.get(Calendar.MINUTE);

        for(All_Time time : ToDayTimer_getData()){
            if(NowTime_H < time.getTime_Hour() || (NowTime_H == time.getTime_Hour() && NowTime_M < time.getTime_Minute())){
                all_times.add(time);
            }
        }

        return all_times;
    }

}
