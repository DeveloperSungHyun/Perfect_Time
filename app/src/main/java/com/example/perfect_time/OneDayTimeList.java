package com.example.perfect_time;

import android.content.Context;
import android.util.Log;

import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;

import java.util.ArrayList;
import java.util.List;

public class OneDayTimeList {
    Context context;
    int y, m, d;


    public OneDayTimeList(Context context, int y, int m, int d){
        this.context = context;
        this.y = y;
        this.m = m;
        this.d = d;
    }


    public List<All_Time> getTimeList(){
        List<All_Time> all_times = new ArrayList<>();

        all_times.addAll(getEveryDayTimeList());
        int count = 0;

        for (int i = 0; i < all_times.size(); i++) {
            for (int j = count; j < DayOfTheWeekTimeList().size(); j++) {
                if(all_times.get(i).getTime_Hour() >= DayOfTheWeekTimeList().get(j).getTime_Hour()){
                    if(all_times.get(i).getTime_Minute() >= DayOfTheWeekTimeList().get(j).getTime_Minute()){
                        all_times.add(i, DayOfTheWeekTimeList().get(j));
                        count = j;
                        i++;
                    }
                }
                else {
                    count = j;
                    break;
                }
            }
        }

        for(int i = count; i < DayOfTheWeekTimeList().size(); i++){
            all_times.add(DayOfTheWeekTimeList().get(i));
        }

        for(All_Time data : all_times){//테스트 출력
            Log.d("dataList", "============ " + data.getName());
        }



        return all_times;
    }

    private List<All_Time> getEveryDayTimeList(){
        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(context);
        All_Time all_time;
        List<All_Time> all_times = new ArrayList<>();


        for (DB_EveryDay db_everyDay : everyDay_dataBase_management.getData()){

            all_time = new All_Time();

            all_time.setTimer_Activate(db_everyDay.isTimer_Activate());
            all_time.setImportant(db_everyDay.isImportant());
            all_time.setName(db_everyDay.getName());
            all_time.setMemo(db_everyDay.getMemo());
            all_time.setTime_Hour(db_everyDay.getTime_Hour());
            all_time.setTime_Minute(db_everyDay.getTime_Minute());
            all_time.setSound_Activate(db_everyDay.isSound_Activate());
            all_time.setSound_volume(db_everyDay.getSound_volume());
            all_time.setVibration_Activate(db_everyDay.isVibration_Activate());
            all_time.setVibration_volume(db_everyDay.getVibration_volume());
            all_time.setPopup_Activate(db_everyDay.isPopup_Activate());
            all_time.setBeforehand(db_everyDay.isBeforehand());
            all_time.setBeforehandTime(db_everyDay.getBeforehandTime());
            all_time.setAutoOffTime(db_everyDay.getAutoOffTime());

            all_times.add(all_time);

        }

        return all_times;
    }

    private List<All_Time> DayOfTheWeekTimeList(){
        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(context);
        All_Time all_time;
        List<All_Time> all_times = new ArrayList<>();

        int week_num = 0;
        int week_day[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        week_num = (y - 1 * 365) + (int)(y - 1 / 4);
        for(int i = 0; i < m - 1; i++) week_num += week_day[i];
        if(y % 4 == 0 && m - 1 > 2) week_num += 1;
        week_num += d + 1;

        Log.d("=-===-=-==-==-==-=", "num" + week_num % 7);


        for (DB_Week db_week : week_dataBase_management.getData()){

            if(db_week.getDayOfTheWeek() == week_num % 7){
                all_time = new All_Time();

                all_time.setTimer_Activate(db_week.isTimer_Activate());
                all_time.setImportant(db_week.isImportant());
                all_time.setName(db_week.getName());
                all_time.setMemo(db_week.getMemo());
                all_time.setTime_Hour(db_week.getTime_Hour());
                all_time.setTime_Minute(db_week.getTime_Minute());
                all_time.setSound_Activate(db_week.isSound_Activate());
                all_time.setSound_volume(db_week.getSound_volume());
                all_time.setVibration_Activate(db_week.isVibration_Activate());
                all_time.setVibration_volume(db_week.getVibration_volume());
                all_time.setPopup_Activate(db_week.isPopup_Activate());
                all_time.setBeforehand(db_week.isBeforehand());
                all_time.setBeforehandTime(db_week.getBeforehandTime());
                all_time.setAutoOffTime(db_week.getAutoOffTime());

                all_times.add(all_time);
            }



        }

        return all_times;
    }
}
