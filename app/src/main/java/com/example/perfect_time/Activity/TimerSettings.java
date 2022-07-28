package com.example.perfect_time.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.perfect_time.DayOfTheWeek_Adapter;
import com.example.perfect_time.DayOfTheWeek_Item;
import com.example.perfect_time.FragmentActivity.FragmentType;
import com.example.perfect_time.R;
import com.example.perfect_time.SettingValue;

public class TimerSettings extends Activity {
    DayOfTheWeek_Adapter dayOfTheWeek_adapter;

    GridView GridView_WeekSelectView;
    TextView TextView_Date;

    EditText EditText_Name;
    EditText EditText_Memo;

    Switch Switch_TimerActivate;//알람 활서화 유무

    Switch Switch_Important;//중요알림표시

    Switch Switch_sound;//소리알림
    ImageView ImageView_sound_SettingButton;//소리설정

    Switch Switch_vibration;//진동알림
    ImageView ImageView_Vibration_SettingButton;//진동설정

    Switch Switch_popup;//팝업설정

    Switch Switch_beforehand;//알림예고
    TextView TextView_beforehand_Set;//알림예고 시간설정

    TextView TextView_HolidayOff_Set;//알림자동꺼짐 시간설정


    private int TimerSettingType;
    private int TimerViewType;

    private void IdMapping(){
        GridView_WeekSelectView = findViewById(R.id.GridView_WeekSelectView);
        TextView_Date = findViewById(R.id.TextView_Date);

        Switch_TimerActivate = findViewById(R.id.Switch_TimerActivate);

        EditText_Name = findViewById(R.id.EditText_Name);
        EditText_Memo = findViewById(R.id.EditText_Memo);

        Switch_Important = findViewById(R.id.Switch_Important);

        Switch_sound = findViewById(R.id.Switch_sound);
        ImageView_sound_SettingButton = findViewById(R.id.ImageView_sound_SettingButton);

        Switch_vibration = findViewById(R.id.Switch_vibration);
        ImageView_Vibration_SettingButton = findViewById(R.id.ImageView_Vibration_SettingButton);

        Switch_popup = findViewById(R.id.Switch_popup);

        Switch_beforehand = findViewById(R.id.Switch_beforehand);
        TextView_beforehand_Set = findViewById(R.id.TextView_beforehand_Set);

        TextView_HolidayOff_Set = findViewById(R.id.TextView_HolidayOff_Set);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_settings_view);
        IdMapping();

        TimerViewType = getIntent().getIntExtra("TimerViewType", 0);//받은 데이터 알람타입
        TimerSettingType = getIntent().getIntExtra("TimerSettingType", 0);//1: 데이터 추가, 2: 데이터 변경

        TimerDaySetting();

        ValueSetting();

    }

    private void ValueSetting(){
        SettingValue settingValue = new SettingValue();

        Switch_TimerActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  //알람 활성화 유무
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setTimer_Activate(b);
            }
        });

        settingValue.setName(EditText_Name.getText().toString());                                       //알람이름
        settingValue.setName(EditText_Memo.getText().toString());                                       //알람메모

        Switch_Important.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {      //중요알림
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setImportant(b);
            }
        });

        Switch_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {          //소리알림
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setSound_Activate(b);
            }
        });

        settingValue.setSound_volume(1);                                                                //소리크기

        Switch_vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {      //진동알림
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setVibration_Activate(b);
            }
        });

        settingValue.setVibration_volume(1);                                                            //진동세기

        Switch_popup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {          //팝업알림
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setPopup_Activate(b);
            }
        });

        Switch_beforehand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {     //알림예고
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setBeforehand(b);
            }
        });

        settingValue.setBeforehandTime(0);                                                              //알림예고 시간
        settingValue.setAutoOffTime(0);                                                                 //알림 자동꺼짐 시간
    }

    private void TimerDaySetting(){
        if(TimerViewType == FragmentType.fragWeek){
            GridView_WeekSelectView.setVisibility(View.VISIBLE);
            TextView_Date.setVisibility(View.GONE);
        }else{
            GridView_WeekSelectView.setVisibility(View.GONE);
            TextView_Date.setVisibility(View.VISIBLE);
        }

        switch (TimerViewType){
            case FragmentType.fragEveryDay:{
                EveryDay_TimerSettings everyDay_timerSettings = new EveryDay_TimerSettings(this, TimerSettingType);

                break;
            }
            case FragmentType.fragWeek:{
                DayOfTheWeek_TimerSettings dayOfTheWeek_timerSettings = new DayOfTheWeek_TimerSettings(this, TimerSettingType);

                break;
            }
            case FragmentType.fragDate:{
                Date_TimerSettings date_timerSettings = new Date_TimerSettings(this, TimerSettingType);

                break;
            }
        }
    }

}

class EveryDay_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    int TimerSettingType;
    public EveryDay_TimerSettings(Context context, int TimerSettingType){
        this.context = context;
        this.TimerSettingType = TimerSettingType;
        CommonLogic();
    }

    private void CommonLogic(){

        switch (TimerSettingType){
            case 1: NewAddTimer(); break;
            case 2: EditTimer(); break;
        }
    }

    private void NewAddTimer(){

    }

    private void EditTimer(){

    }
}

class DayOfTheWeek_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    int TimerSettingType;
    public DayOfTheWeek_TimerSettings(Context context, int TimerSettingType){
        this.context = context;
        this.TimerSettingType = TimerSettingType;
        CommonLogic();
    }

    private void CommonLogic(){
        ActivityView = ((TimerSettings) context);
        ActivityView.GridView_WeekSelectView.setVisibility(View.VISIBLE);
        String DayOfTheWeek_text[] = {"일", "월", "화", "수", "목", "금", "토"};
        ActivityView.dayOfTheWeek_adapter = new DayOfTheWeek_Adapter();

        for(String WeekText : DayOfTheWeek_text) {
            DayOfTheWeek_Item dayOfTheWeekItem = new DayOfTheWeek_Item(false, WeekText, 0xFF000000);
            ActivityView.dayOfTheWeek_adapter.addItem(dayOfTheWeekItem);
        }

        ActivityView.GridView_WeekSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int RadioBtn = 0; RadioBtn  < DayOfTheWeek_text.length; RadioBtn++){
                    if(RadioBtn == i) ActivityView.dayOfTheWeek_adapter.setItem(RadioBtn, true);
                    else ActivityView.dayOfTheWeek_adapter.setItem(RadioBtn, false);
                }

                ActivityView.GridView_WeekSelectView.setAdapter(ActivityView.dayOfTheWeek_adapter);
            }
        });

        ActivityView.GridView_WeekSelectView.setAdapter(ActivityView.dayOfTheWeek_adapter);

        switch (TimerSettingType){
            case 1: NewAddTimer(); break;
            case 2: EditTimer(); break;
        }
    }

    private void NewAddTimer(){

    }

    private void EditTimer(){

    }
}

class Date_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    int TimerSettingType;
    public Date_TimerSettings(Context context, int TimerSettingType){
        this.context = context;
        this.TimerSettingType = TimerSettingType;
        CommonLogic();
    }

    private void CommonLogic(){

        switch (TimerSettingType){
            case 1: NewAddTimer(); break;
            case 2: EditTimer(); break;
        }
    }

    private void NewAddTimer(){

    }

    private void EditTimer(){

    }
}
