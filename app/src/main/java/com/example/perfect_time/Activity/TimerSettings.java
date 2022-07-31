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

import java.sql.Time;
import java.util.Calendar;

public class TimerSettings extends Activity {
    Calendar calendar;

    DayOfTheWeek_Adapter dayOfTheWeek_adapter;
    SettingValue settingValue;

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

    int nowTime_H, nowTime_M;

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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        InterfaceSetting();

    }

    private void InterfaceSetting(){
        Switch_TimerActivate.setChecked(settingValue.isTimer_Activate());

        EditText_Name.setText(settingValue.getName());
        EditText_Memo.setText(settingValue.getMemo());

        Switch_Important.setChecked(settingValue.isImportant());

        Switch_sound.setChecked(settingValue.isSound_Activate());
        Switch_vibration.setChecked(settingValue.isVibration_Activate());
        Switch_popup.setChecked(settingValue.isPopup_Activate());

        Switch_beforehand.setChecked(settingValue.isBeforehand());
        TextView_beforehand_Set.setText(Integer.toString(settingValue.getBeforehandTime()));

        TextView_HolidayOff_Set.setText(Integer.toString(settingValue.getAutoOffTime()));

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_settings_view);
        IdMapping();

        settingValue = new SettingValue();

        calendar = Calendar.getInstance();

        nowTime_H = calendar.get(Calendar.HOUR_OF_DAY);//24시 형식
        nowTime_M = calendar.get(Calendar.MINUTE);//24시 형식

        TimerViewType = getIntent().getIntExtra("TimerViewType", 0);//받은 데이터 알람타입
        TimerSettingType = getIntent().getIntExtra("TimerSettingType", 0);//1: 데이터 추가, 2: 데이터 변경

        if(TimerSettingType == 1){
            NewTimerCommonLogic();//모든 알람이 새로 추가할 시 동일하게  초기값 설정
        }

        TimerDaySetting();

        ValueSetting();

    }

    private void NewTimerCommonLogic(){//알람 추가시 디폴트 값

        settingValue.setTimer_Activate(true);//알람 활성화 유무

        settingValue.setTime_Hour(nowTime_H);//시간
        settingValue.setTime_Minute(nowTime_M);//분

        settingValue.setName(null);
        settingValue.setMemo(null);

        settingValue.setImportant(true);

        settingValue.setSound_Activate(true);
        settingValue.setSound_volume(70);

        settingValue.setVibration_Activate(true);
        settingValue.setVibration_volume(70);//100분률

        settingValue.setPopup_Activate(false);

        settingValue.setBeforehand(true);
        settingValue.setBeforehandTime(60 * 5);//초단위

        settingValue.setAutoOffTime(10);

        InterfaceSetting();
    }

    private void ValueSetting(){

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

        Switch_vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {      //진동알림
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setVibration_Activate(b);
            }
        });

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

        //=========================================================

        ImageView_sound_SettingButton.setOnClickListener(new View.OnClickListener() {//소리알림 설정엑티비티 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerSettings.this, TimerSoundSetting.class);
                startActivity(intent);

            }
        });

        ImageView_Vibration_SettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerSettings.this, TimerVibrationSetting.class);
                startActivity(intent);
            }
        });

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
                if(TimerSettingType == 2) everyDay_timerSettings.EditTimer();//알람 변경시
                break;
            }
            case FragmentType.fragWeek:{
                DayOfTheWeek_TimerSettings dayOfTheWeek_timerSettings = new DayOfTheWeek_TimerSettings(this, TimerSettingType);
                if(TimerSettingType == 2) dayOfTheWeek_timerSettings.EditTimer();//알람 변경시
                break;
            }
            case FragmentType.fragDate:{
                Date_TimerSettings date_timerSettings = new Date_TimerSettings(this, TimerSettingType);
                if(TimerSettingType == 2) date_timerSettings.EditTimer();//알람 변경시
                break;
            }
        }
    }

}

class EveryDay_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    SettingValue settingValue = new SettingValue();

    int TimerSettingType;
    public EveryDay_TimerSettings(Context context, int TimerSettingType){
        this.context = context;
        this.TimerSettingType = TimerSettingType;

        CommonLogic();
    }

    private void CommonLogic(){
        ActivityView = ((TimerSettings) context);

        if(TimerSettingType == 1){
            NewAddTimer();
        }else if(TimerSettingType == 2){
            EditTimer();
        }
    }

    protected void NewAddTimer(){

    }

    protected void EditTimer(){

    }
}

class DayOfTheWeek_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    int TimerSettingType;

    int DayOfTheWeek = 0;
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

        if(TimerSettingType == 1){
            NewAddTimer();
        }else if(TimerSettingType == 2){
            EditTimer();
        }

        ActivityView.GridView_WeekSelectView.setAdapter(ActivityView.dayOfTheWeek_adapter);

    }

    protected void NewAddTimer(){
        DayOfTheWeek = ActivityView.calendar.get(Calendar.DAY_OF_WEEK) - 1;//초기 설정시 현재 요일로
        ActivityView.dayOfTheWeek_adapter.setItem(DayOfTheWeek, true);
    }

    protected void EditTimer(){

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

        if(TimerSettingType == 1){
            NewAddTimer();
        }else if(TimerSettingType == 2){
            EditTimer();
        }
    }

    protected void NewAddTimer(){

    }

    protected void EditTimer(){

    }
}
