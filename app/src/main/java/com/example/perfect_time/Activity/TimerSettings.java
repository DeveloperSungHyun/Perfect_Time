package com.example.perfect_time.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.example.perfect_time.DayOfTheWeek_Adapter;
import com.example.perfect_time.DayOfTheWeek_Item;
import com.example.perfect_time.R;

public class TimerSettings extends Activity {

    GridView GridView_WeekSelectView;
    DayOfTheWeek_Adapter dayOfTheWeek_adapter;

    private void IdMapping(){
        GridView_WeekSelectView = findViewById(R.id.GridView_WeekSelectView);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_settings_view);

        IdMapping();

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
                dayOfTheWeek_adapter.setItem(i, true);
                GridView_WeekSelectView.setAdapter(dayOfTheWeek_adapter);
            }
        });

        GridView_WeekSelectView.setAdapter(dayOfTheWeek_adapter);
    }
}
