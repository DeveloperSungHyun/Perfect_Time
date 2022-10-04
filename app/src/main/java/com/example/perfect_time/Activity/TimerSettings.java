package com.example.perfect_time.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.example.perfect_time.DayOfTheWeek_Adapter;
import com.example.perfect_time.DayOfTheWeek_Item;
import com.example.perfect_time.FragmentActivity.FragmentType;
import com.example.perfect_time.R;
import com.example.perfect_time.RoomDataBase.Date.DB_Date;
import com.example.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDao;
import com.example.perfect_time.RoomDataBase.Everyday.EveryDayDataBase;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;
import com.example.perfect_time.SettingValue;
import com.example.perfect_time.Time24_to_12Hour;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

public class TimerSettings extends Activity {
    Calendar calendar;

    Dialog TimeSetting_Dialog;

    SettingValue settingValue;

    EveryDay_TimerSettings everyDay_timerSettings;
    DayOfTheWeek_TimerSettings dayOfTheWeek_timerSettings;
    Date_TimerSettings date_timerSettings;

    GridView GridView_WeekSelectView;
    TextView TextView_Date;

    LinearLayout LinearLayout_Time_Setting;
    TextView TextView_Time_H;
    TextView TextView_Time_M;
    TextView TextView_AmPm;

    EditText EditText_Name;
    EditText EditText_Memo;

    Switch Switch_TimerActivate;//알람 활서화 유무

    Switch Switch_Important;//중요알림표시

    Switch Switch_sound;//소리알림
    Button sound_SettingButton;//소리설정

    Switch Switch_vibration;//진동알림
    Button Vibration_SettingButton;//진동설정

    Switch Switch_popup;//팝업설정

    Switch Switch_beforehand;//알림예고
    TextView TextView_beforehand_Set;//알림예고 시간설정

    TextView TextView_HolidayOff_Set;//알림자동꺼짐 시간설정

    TextView TextView_SaveButton;
    TextView TextView_No_SaveButton;


    private int TimerSettingType;
    private int TimerViewType;

    int nowTime_H, nowTime_M;

    private void IdMapping(){
        GridView_WeekSelectView = findViewById(R.id.GridView_WeekSelectView);
        TextView_Date = findViewById(R.id.TextView_Date);

        Switch_TimerActivate = findViewById(R.id.Switch_TimerActivate);

        LinearLayout_Time_Setting = findViewById(R.id.LinearLayout_Time_Setting);
        TextView_Time_H = findViewById(R.id.TextView_Time_H);
        TextView_Time_M = findViewById(R.id.TextView_Time_M);
        TextView_AmPm = findViewById(R.id.TextView_AmPm);

        EditText_Name = findViewById(R.id.EditText_Name);
        EditText_Memo = findViewById(R.id.EditText_Memo);

        Switch_Important = findViewById(R.id.Switch_Important);

        Switch_sound = findViewById(R.id.Switch_sound);
        sound_SettingButton = findViewById(R.id.sound_SettingButton);

        Switch_vibration = findViewById(R.id.Switch_vibration);
        Vibration_SettingButton = findViewById(R.id.Vibration_SettingButton);

        Switch_popup = findViewById(R.id.Switch_popup);

        Switch_beforehand = findViewById(R.id.Switch_beforehand);
        TextView_beforehand_Set = findViewById(R.id.TextView_beforehand_Set);

        TextView_HolidayOff_Set = findViewById(R.id.TextView_HolidayOff_Set);


        TextView_SaveButton = findViewById(R.id.TextView_SaveButton);
        TextView_No_SaveButton = findViewById(R.id.TextView_No_SaveButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        InterfaceSetting();
    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    private void InterfaceSetting(){
        Time24_to_12Hour time24_to_12Hour = new Time24_to_12Hour(settingValue.getTime_Hour());
        Switch_TimerActivate.setChecked(settingValue.isTimer_Activate());

        EditText_Name.setText(settingValue.getName());
        EditText_Memo.setText(settingValue.getMemo());

        TextView_Time_H.setText(Integer.toString(time24_to_12Hour.getTime12Hour()));
        TextView_Time_M.setText(Integer.toString(settingValue.getTime_Minute()));

        if(time24_to_12Hour.getAmPm()) TextView_AmPm.setText("오후");
        else TextView_AmPm.setText("오전");

        Switch_Important.setChecked(settingValue.isImportant());

        Switch_sound.setChecked(settingValue.isSound_Activate());
        Switch_vibration.setChecked(settingValue.isVibration_Activate());
        Switch_popup.setChecked(settingValue.isPopup_Activate());

        Switch_beforehand.setChecked(settingValue.isBeforehand());
        TextView_beforehand_Set.setText(new Second_to_Minute(settingValue.getBeforehandTime()).getTypeChange());

        TextView_HolidayOff_Set.setText(new Second_to_Minute(settingValue.getAutoOffTime()).getTypeChange());

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_settings_view);
        IdMapping();

        settingValue = new SettingValue();

        calendar = Calendar.getInstance();

        TimeSetting_Dialog = new Dialog(TimerSettings.this);
        TimeSetting_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        nowTime_H = calendar.get(Calendar.HOUR_OF_DAY);//24시 형식
        nowTime_M = calendar.get(Calendar.MINUTE);//24시 형식

        TimerViewType = getIntent().getIntExtra("TimerViewType", 0);//받은 데이터 알람타입
        TimerSettingType = getIntent().getIntExtra("TimerSettingType", 0);//1: 데이터 추가, 2: 데이터 변경, 3: 데이터 복사


        if(TimerSettingType == 1){
            NewTimerCommonLogic();//모든 알람이 새로 추가할 시 동일하게  초기값 설정
        }

        TimerDaySetting();

        LinearLayout_Time_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeSettingsView();
            }
        });

        TextView_beforehand_Set.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int TimeValue[] = {60, 180, 300, 600, 1800};

                TimeSettingDialog(TimeValue, 1);
            }
        });

        TextView_HolidayOff_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int TimeValue[] = {10, 30, 60, 180, 600};

                TimeSettingDialog(TimeValue, 2);
            }
        });


        TextView_SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //everyDay_timerSettings.NewAddTimer();


                settingValue.setName(EditText_Name.getText().toString());                                       //알람이름
                settingValue.setMemo(EditText_Memo.getText().toString());                                       //알람메모

                if(!settingValue.getName().equals("")){

                    switch (TimerViewType){
                        case FragmentType.fragEveryDay:{
                            if(TimerSettingType == 1){//데이터 추가
                                everyDay_timerSettings.NewAddTimer();
                            }else if(TimerSettingType == 2){//데이터 변경
                                everyDay_timerSettings.TimerUpData();
                            }else if(TimerSettingType == 3){
                                everyDay_timerSettings.NewAddTimer();
                            }
                            break;
                        }
                        case FragmentType.fragWeek:{
                            if(TimerSettingType == 1){//데이터 추가
                                dayOfTheWeek_timerSettings.NewAddTimer();
                            }else if(TimerSettingType == 2){//데이터 변경
                                dayOfTheWeek_timerSettings.TimerUpData();
                            }else if(TimerSettingType == 3){
                                dayOfTheWeek_timerSettings.NewAddTimer();
                            }
                            break;
                        }
                        case FragmentType.fragDate:{
                            if(TimerSettingType == 1){//데이터 추가
                                date_timerSettings.NewAddTimer();
                            }else if(TimerSettingType == 2){//데이터 변경
                                date_timerSettings.TimerUpData();
                            }else if(TimerSettingType == 3){
                                date_timerSettings.NewAddTimer();
                            }
                        }
                    }

                    Intent intent = new Intent();
                    intent.putExtra("UpData", 1);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "알람 이름을 적어주세요", Toast.LENGTH_SHORT).show();

                }

            }
        });

        ValueSetting();


    }

    @Override
    protected void onStop() {
        super.onStop();
        settingValue.setName(EditText_Name.getText().toString());                                       //알람이름
        settingValue.setMemo(EditText_Memo.getText().toString());                                       //알람메모
    }

    private void showTimeSettingsView(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(TimerSettings.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                settingValue.setTime_Hour(h);
                settingValue.setTime_Minute(m);

                settingValue.setName(EditText_Name.getText().toString());                                       //알람이름
                settingValue.setMemo(EditText_Memo.getText().toString());                                       //알람메모
                InterfaceSetting();
            }
        } ,settingValue.getTime_Hour(), settingValue.getTime_Minute(), false);
        timePickerDialog.show();
    }

    int Time;
    int ArrayTimeInDex;
    private void TimeSettingDialog(int TimeValue[], int division){

        int ArraySize = TimeValue.length;
        String[] versionArray = new String[ArraySize];
        if(division == 1) Time = settingValue.getBeforehandTime();
        else Time = settingValue.getAutoOffTime();

        for (int i = 0; i < ArraySize; i++) {
            ArrayTimeInDex = i;
            if(TimeValue[i] == Time) break;
        }



        for(int i = 0; i < ArraySize; i++){
            versionArray[i] = new Second_to_Minute(TimeValue[i]).getTypeChange();
        }

        AlertDialog.Builder dlg = new AlertDialog.Builder(TimerSettings.this);


        dlg.setSingleChoiceItems(versionArray, ArrayTimeInDex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Time = TimeValue[i];
            }
        });

        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(division == 1){// 알림 예고
                    settingValue.setBeforehandTime(Time);
                }else if(division == 2){// 자동 알링끄기
                    settingValue.setAutoOffTime(Time);
                }

                settingValue.setName(EditText_Name.getText().toString());                                       //알람이름
                settingValue.setMemo(EditText_Memo.getText().toString());                                       //알람메모
                InterfaceSetting();
            }
        });
        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dlg.show();
    }


    class Second_to_Minute{
        int Second;

        String value;

        public Second_to_Minute(int Second) {
            this.Second = Second;
        }

        public String getTypeChange(){

            if(Second >= 60){
                value = Second / 60 + "분";
            }else{
                value = Second + "초";
            }

            return value;
        }

    }

    private void NewTimerCommonLogic(){//알람 추가시 디폴트 값

        settingValue.setTimer_Activate(true);//알람 활성화 유무

        settingValue.setTime_Hour(nowTime_H);//시간
        settingValue.setTime_Minute(nowTime_M);//분

        settingValue.setName(null);
        settingValue.setMemo(null);

        settingValue.setImportant(false);

        settingValue.setSound_Activate(true);
        settingValue.setSound_volume(70);

        settingValue.setVibration_Activate(true);
        settingValue.setVibration_volume(70);//100분률

        settingValue.setPopup_Activate(false);

        settingValue.setBeforehand(true);
        settingValue.setBeforehandTime(60 * 5);//초단위

        settingValue.setAutoOffTime(60);

        InterfaceSetting();
    }

    private void ValueSetting(){

        Switch_TimerActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  //알람 활성화 유무
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setTimer_Activate(b);
            }
        });



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

        //=========================================================

        sound_SettingButton.setOnClickListener(new View.OnClickListener() {//소리알림 설정엑티비티 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerSettings.this, TimerSoundSetting.class);
                startActivity(intent);

            }
        });

        Vibration_SettingButton.setOnClickListener(new View.OnClickListener() {
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
                everyDay_timerSettings = new EveryDay_TimerSettings(this, TimerSettingType);

                if(TimerSettingType == 2 || TimerSettingType == 3){
                    everyDay_timerSettings.getTimer();
                }
                break;
            }
            case FragmentType.fragWeek:{
                dayOfTheWeek_timerSettings = new DayOfTheWeek_TimerSettings(this, TimerSettingType);

                if(TimerSettingType == 2 || TimerSettingType == 3){
                    dayOfTheWeek_timerSettings.getTimer();
                }
                break;
            }
            case FragmentType.fragDate:{
                date_timerSettings = new Date_TimerSettings(this, TimerSettingType);

                if(TimerSettingType == 2 || TimerSettingType == 3){
                    date_timerSettings.getTimer();
                }
                break;
            }
        }
    }

    private void TimeShow(){

    }
}

class EveryDay_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    SettingValue settingValue;

    EveryDay_DataBase_Management everyDay_dataBase_management;

    int TimerSettingType;
    public EveryDay_TimerSettings(Context context, int TimerSettingType){
        this.context = context;
        this.TimerSettingType = TimerSettingType;


        CommonLogic();
    }

    private void CommonLogic(){
        ActivityView = ((TimerSettings) context);

        ActivityView.TextView_Date.setText("매일");

        settingValue = new SettingValue();

        everyDay_dataBase_management = new EveryDay_DataBase_Management(context);

//        if(TimerSettingType == 1){
//            NewAddTimer();
//        }else if(TimerSettingType == 2){
//            EditTimer();
//        }
    }

    protected void NewAddTimer(){
        Log.d("NewAddTimer", "test====================");
        everyDay_dataBase_management.setInsert(settingValue);
    }

    protected void getTimer(){
        List<DB_EveryDay> db_everyDayList;
        DB_EveryDay db_everyDay;
        db_everyDayList = everyDay_dataBase_management.getData();

        db_everyDay = db_everyDayList.get(ActivityView.getIntent().getIntExtra("ItemID", 0));//ActivityView.getIntent().getIntExtra("ItemID", 0)
        Log.d("인텐트 데이터 입력", "ItemID " + ActivityView.getIntent().getIntExtra("ItemID", 0));

        settingValue.setTimer_Activate(db_everyDay.isTimer_Activate());

        settingValue.setTime_Hour(db_everyDay.getTime_Hour());
        settingValue.setTime_Minute(db_everyDay.getTime_Minute());

        settingValue.setName(db_everyDay.getName());
        settingValue.setMemo(db_everyDay.getMemo());

        settingValue.setImportant(db_everyDay.isImportant());

        settingValue.setSound_Activate(db_everyDay.isSound_Activate());
        settingValue.setSound_volume(db_everyDay.getSound_volume());

        settingValue.setVibration_Activate(db_everyDay.isVibration_Activate());
        settingValue.setVibration_volume(db_everyDay.getVibration_volume());

        settingValue.setPopup_Activate(db_everyDay.isPopup_Activate());

        settingValue.setBeforehand(db_everyDay.isBeforehand());
        settingValue.setBeforehandTime(db_everyDay.getBeforehandTime());

        settingValue.setAutoOffTime(db_everyDay.getAutoOffTime());

    }

    protected void TimerUpData(){
        everyDay_dataBase_management.setUpData(ActivityView.getIntent().getIntExtra("ItemID", 0), settingValue);
        Log.d("데이터 업데이트", "ItemID " + ActivityView.getIntent().getIntExtra("ItemID", 0));
    }
}

class DayOfTheWeek_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    SettingValue settingValue;

    DayOfTheWeek_Adapter dayOfTheWeek_adapter;

    Week_DataBase_Management week_dataBase_management;

    int TimerSettingType;

    int DayOfTheWeek = 0;//요일
    public DayOfTheWeek_TimerSettings(Context context, int TimerSettingType){
        this.context = context;
        this.TimerSettingType = TimerSettingType;

        CommonLogic();
    }

    private void CommonLogic(){
        ActivityView = ((TimerSettings) context);
        ActivityView.GridView_WeekSelectView.setVisibility(View.VISIBLE);
        String DayOfTheWeek_text[] = {"일", "월", "화", "수", "목", "금", "토"};
        dayOfTheWeek_adapter = new DayOfTheWeek_Adapter();

        settingValue = new SettingValue();

        week_dataBase_management = new Week_DataBase_Management(context);


        for(String WeekText : DayOfTheWeek_text) {
            DayOfTheWeek_Item dayOfTheWeekItem = new DayOfTheWeek_Item(false, WeekText, 0xFF000000);
            dayOfTheWeek_adapter.addItem(dayOfTheWeekItem);
        }

        if(TimerSettingType == 1){
            DayOfTheWeek = ActivityView.calendar.get(Calendar.DAY_OF_WEEK) - 1;//초기 설정시 현재 요일로
            dayOfTheWeek_adapter.setItem(DayOfTheWeek, true);
        }

        ActivityView.GridView_WeekSelectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DayOfTheWeek = i;
                for(int RadioBtn = 0; RadioBtn < DayOfTheWeek_text.length; RadioBtn++){
                    if(RadioBtn == DayOfTheWeek) dayOfTheWeek_adapter.setItem(RadioBtn, true);
                    else dayOfTheWeek_adapter.setItem(RadioBtn, false);
                }

                ActivityView.GridView_WeekSelectView.setAdapter(dayOfTheWeek_adapter);
            }
        });

//        if(TimerSettingType == 1){
//            NewAddTimer();
//        }else if(TimerSettingType == 2){
//            EditTimer();
//        }

        ActivityView.GridView_WeekSelectView.setAdapter(dayOfTheWeek_adapter);

    }

    protected void NewAddTimer(){

        week_dataBase_management.setInsert(settingValue, DayOfTheWeek);
    }

    protected void getTimer(){
        List<DB_Week> db_weekList;
        DB_Week db_week;
        db_weekList = week_dataBase_management.getData();

        db_week = db_weekList.get(ActivityView.getIntent().getIntExtra("ItemID", 0));//ActivityView.getIntent().getIntExtra("ItemID", 0)
        Log.d("인텐트 데이터 입력", "ItemID " + ActivityView.getIntent().getIntExtra("ItemID", 0));

        settingValue.setTimer_Activate(db_week.isTimer_Activate());

        DayOfTheWeek = db_week.getDayOfTheWeek();
        dayOfTheWeek_adapter.setItem(DayOfTheWeek, true);

        settingValue.setTime_Hour(db_week.getTime_Hour());
        settingValue.setTime_Minute(db_week.getTime_Minute());

        settingValue.setName(db_week.getName());
        settingValue.setMemo(db_week.getMemo());

        settingValue.setImportant(db_week.isImportant());

        settingValue.setSound_Activate(db_week.isSound_Activate());
        settingValue.setSound_volume(db_week.getSound_volume());

        settingValue.setVibration_Activate(db_week.isVibration_Activate());
        settingValue.setVibration_volume(db_week.getVibration_volume());

        settingValue.setPopup_Activate(db_week.isPopup_Activate());

        settingValue.setBeforehand(db_week.isBeforehand());
        settingValue.setBeforehandTime(db_week.getBeforehandTime());

        settingValue.setAutoOffTime(db_week.getAutoOffTime());
    }

    protected void TimerUpData(){
        week_dataBase_management.setUpData(ActivityView.getIntent().getIntExtra("ItemID", 0), settingValue, DayOfTheWeek);
    }
}

class Date_TimerSettings{
    Context context;
    TimerSettings ActivityView;

    SettingValue settingValue;

    Date_DataBase_Management date_dataBase_management;

    int TimerSettingType;

    int y, m, d;//날짜
    public Date_TimerSettings(Context context, int TimerSettingType){
        this.context = context;
        this.TimerSettingType = TimerSettingType;

        CommonLogic();
    }

    private void CommonLogic(){
        ActivityView = ((TimerSettings) context);

        settingValue = new SettingValue();

        date_dataBase_management = new Date_DataBase_Management(context);

        y = ActivityView.calendar.get(Calendar.YEAR);
        m = ActivityView.calendar.get(Calendar.MONDAY) + 1;
        d = ActivityView.calendar.get(Calendar.DATE);

        ActivityView.TextView_Date.setText(y + "년 " + m + "월 " + d + "일");

//        if(TimerSettingType == 1){
//            NewAddTimer();
//        }else if(TimerSettingType == 2){
//            EditTimer();
//        }

        ActivityView.TextView_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
                Log.d("===================", "test");
            }
        });

    }

    protected void NewAddTimer(){

        date_dataBase_management.setInsert(settingValue, y, m, d);
    }

    protected void getTimer(){
        List<DB_Date> db_dateList;
        DB_Date db_date;
        db_dateList = date_dataBase_management.getData();

        db_date = db_dateList.get(ActivityView.getIntent().getIntExtra("ItemID", 0));//ActivityView.getIntent().getIntExtra("ItemID", 0)
        Log.d("인텐트 데이터 입력", "ItemID " + ActivityView.getIntent().getIntExtra("ItemID", 0));

        settingValue.setTimer_Activate(db_date.isTimer_Activate());

        settingValue.setTime_Hour(db_date.getTime_Hour());
        settingValue.setTime_Minute(db_date.getTime_Minute());

        y = db_date.getDate_Year();
        m = db_date.getDate_Month();
        d = db_date.getDate_Day();
        ActivityView.TextView_Date.setText(y + "년 " + m + "월 " + d + "일");

        settingValue.setName(db_date.getName());
        settingValue.setMemo(db_date.getMemo());

        settingValue.setImportant(db_date.isImportant());

        settingValue.setSound_Activate(db_date.isSound_Activate());
        settingValue.setSound_volume(db_date.getSound_volume());

        settingValue.setVibration_Activate(db_date.isVibration_Activate());
        settingValue.setVibration_volume(db_date.getVibration_volume());

        settingValue.setPopup_Activate(db_date.isPopup_Activate());

        settingValue.setBeforehand(db_date.isBeforehand());
        settingValue.setBeforehandTime(db_date.getBeforehandTime());

        settingValue.setAutoOffTime(db_date.getAutoOffTime());
    }

    protected void TimerUpData(){
        date_dataBase_management.setUpData(ActivityView.getIntent().getIntExtra("ItemID", 0), settingValue, y, m, d);
    }


    protected void showDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.ActivityView, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

                ActivityView.TextView_Date.setText(y + "년 " + m + "월 " + d + "일");
            }
        },y, m - 1, d);


        datePickerDialog.show();
    }



}
