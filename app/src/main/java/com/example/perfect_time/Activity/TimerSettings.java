package com.example.perfect_time.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.perfect_time.DayOfTheWeek_Adapter;
import com.example.perfect_time.DayOfTheWeek_Item;
import com.example.perfect_time.FragmentActivity.FragmentType;
import com.example.perfect_time.R;

public class TimerSettings extends Activity {
    DayOfTheWeek_Adapter dayOfTheWeek_adapter;

    GridView GridView_WeekSelectView;
    TextView TextView_Date;


    private int TimerSettingType;
    private int TimerViewType;

    private void IdMapping(){
        GridView_WeekSelectView = findViewById(R.id.GridView_WeekSelectView);
        TextView_Date = findViewById(R.id.TextView_Date);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_settings_view);

        IdMapping();

        TimerViewType = getIntent().getIntExtra("TimerViewType", 0);//받은 데이터 알람타입
        TimerSettingType = getIntent().getIntExtra("TimerSettingType", 0);//1: 데이터 추가, 2: 데이터 변경

        if(TimerViewType == FragmentType.fragWeek){
            GridView_WeekSelectView.setVisibility(View.VISIBLE);
            TextView_Date.setVisibility(View.GONE);
        }else{
            GridView_WeekSelectView.setVisibility(View.GONE);
            TextView_Date.setVisibility(View.VISIBLE);
        }

        switch (TimerViewType){
            case FragmentType.fragEveryDay:{
                EveryDay_TimerSettings everyDay_timerSettings = new EveryDay_TimerSettings(this);
                switch (TimerSettingType){
                    case 1: everyDay_timerSettings.NewAddTimer(); break;
                    case 2: everyDay_timerSettings.EditTimer(); break;
                }
                break;
            }
            case FragmentType.fragWeek:{
                DayOfTheWeek_TimerSettings dayOfTheWeek_timerSettings = new DayOfTheWeek_TimerSettings(this);
                switch (TimerSettingType){
                    case 1: dayOfTheWeek_timerSettings.NewAddTimer(); break;
                    case 2: dayOfTheWeek_timerSettings.EditTimer(); break;
                }
                break;
            }
            case FragmentType.fragDate:{
                Date_TimerSettings date_timerSettings = new Date_TimerSettings(this);
                switch (TimerSettingType){
                    case 1: date_timerSettings.NewAddTimer(); break;
                    case 2: date_timerSettings.EditTimer(); break;
                }
                break;
            }
        }
    }

    private class EveryDay_TimerSettings{
        Context ActivityVIew;
        public EveryDay_TimerSettings(Context context){
            ActivityVIew = context;

            CommonLogic();
        }

        private void CommonLogic(){

        }

        protected void NewAddTimer(){

        }

        protected void EditTimer(){

        }
    }

    private class DayOfTheWeek_TimerSettings{
        Context ActivityVIew;
        public DayOfTheWeek_TimerSettings(Context context){
            ActivityVIew = context;

            CommonLogic();
        }

        private void CommonLogic(){
            GridView_WeekSelectView.setVisibility(View.VISIBLE);
            String DayOfTheWeek_text[] = {"일", "월", "화", "수", "목", "금", "토"};
            dayOfTheWeek_adapter = new DayOfTheWeek_Adapter();

            for(String WeekText : DayOfTheWeek_text) {
                DayOfTheWeek_Item dayOfTheWeekItem = new DayOfTheWeek_Item(false, WeekText, 0xFF000000);
                dayOfTheWeek_adapter.addItem(dayOfTheWeekItem);
            }

            GridView_WeekSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    for(int RadioBtn = 0; RadioBtn  < 7; RadioBtn++){
                        if(RadioBtn == i) dayOfTheWeek_adapter.setItem(RadioBtn, true);
                        else dayOfTheWeek_adapter.setItem(RadioBtn, false);
                    }

                    GridView_WeekSelectView.setAdapter(dayOfTheWeek_adapter);
                }
            });

            GridView_WeekSelectView.setAdapter(dayOfTheWeek_adapter);
        }

        protected void NewAddTimer(){

        }

        protected void EditTimer(){

        }
    }

    private class Date_TimerSettings{
        Context ActivityVIew;
        public Date_TimerSettings(Context context){
            ActivityVIew = context;

            CommonLogic();
        }

        private void CommonLogic(){

        }

        protected void NewAddTimer(){

        }

        protected void EditTimer(){

        }
    }
}
