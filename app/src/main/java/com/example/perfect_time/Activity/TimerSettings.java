package com.example.perfect_time.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.perfect_time.Service.AlarmServiceManagement;
import com.example.perfect_time.DayOfTheWeek_Adapter;
import com.example.perfect_time.DayOfTheWeek_Item;
import com.example.perfect_time.FragmentActivity.FragmentType;
import com.example.perfect_time.R;
import com.example.perfect_time.RoomDataBase.Date.DB_Date;
import com.example.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;
import com.example.perfect_time.SettingValue;
import com.example.perfect_time.SystemDataSave;
import com.example.perfect_time.Time24_to_12Hour;

import java.util.Calendar;
import java.util.List;

public class TimerSettings extends Activity {
    SharedPreferences sharedPreferences;
    Calendar calendar;

    Dialog TimeSetting_Dialog;

    SettingValue settingValue;

    EveryDay_TimerSettings everyDay_timerSettings;
    DayOfTheWeek_TimerSettings dayOfTheWeek_timerSettings;
    Date_TimerSettings date_timerSettings;

    AlarmServiceManagement alarmServiceManagement;

    GridView GridView_WeekSelectView;
    TextView TextView_Date;

    LinearLayout LinearLayout_AlarmSettingView;

    LinearLayout LinearLayout_Time_Setting;
    TextView TextView_Time_H;
    TextView TextView_Time_M;
    TextView TextView_AmPm;

    EditText EditText_Name;
    EditText EditText_Memo;

    Switch Switch_TimerActivate;//알람 활서화 유무

    Switch Switch_Important;//중요알림표시

    ImageView ImageView_None, ImageView_Notifications, ImageView_Notifications_HeadUp, ImageView_Alarm;

    LinearLayout LinearLayout_AutoOffTimer_SettingsViewShow;
    TextView TextView_AutoOffTimer_num;

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

        ImageView_None = findViewById(R.id.ImageView_None);
        ImageView_Notifications = findViewById(R.id.ImageView_Notifications);
        ImageView_Notifications_HeadUp = findViewById(R.id.ImageView_Notifications_HeadUp);
        ImageView_Alarm = findViewById(R.id.ImageView_Alarm);

        LinearLayout_AlarmSettingView = findViewById(R.id.LinearLayout_AlarmSettingView);

        LinearLayout_AutoOffTimer_SettingsViewShow = findViewById(R.id.LinearLayout_AutoOffTimer_SettingsViewShow);
        TextView_AutoOffTimer_num = findViewById(R.id.TextView_AutoOffTimer_num);

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
        Time24_to_12Hour time24_to_12Hour = new Time24_to_12Hour(settingValue.getTime_Hour(), getApplicationContext());
        Switch_TimerActivate.setChecked(settingValue.isTimer_Activate());

        EditText_Name.setText(settingValue.getName());
        EditText_Memo.setText(settingValue.getMemo());

        TextView_Time_H.setText(Integer.toString(time24_to_12Hour.getTime_Hour()));
        TextView_Time_M.setText(Integer.toString(settingValue.getTime_Minute()));

        TextView_AmPm.setText(time24_to_12Hour.getAmPm());

        Switch_Important.setChecked(settingValue.isImportant());

        AlarmCh();


    }

    void AlarmCh(){
        ImageView_None.setBackgroundResource(R.drawable.background_style2);
        ImageView_Notifications.setBackgroundResource(R.drawable.background_style2);
        ImageView_Notifications_HeadUp.setBackgroundResource(R.drawable.background_style2);
        ImageView_Alarm.setBackgroundResource(R.drawable.background_style2);
        LinearLayout_AlarmSettingView.setVisibility(View.GONE);
        switch (settingValue.getAlarm_Method()){
            case 0:{
                ImageView_None.setBackgroundResource(R.drawable.background_style2_1);
                break;
            }
            case 1:{
                ImageView_Notifications.setBackgroundResource(R.drawable.background_style2_1);
                break;
            }
            case 2:{
                ImageView_Notifications_HeadUp.setBackgroundResource(R.drawable.background_style2_1);
                break;
            }
            case 3:{
                ImageView_Alarm.setBackgroundResource(R.drawable.background_style2_1);
                LinearLayout_AlarmSettingView.setVisibility(View.VISIBLE);
                break;
            }
        }

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
        nowTime_M = calendar.get(Calendar.MINUTE);

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

        TextView_SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //everyDay_timerSettings.NewAddTimer();

                alarmServiceManagement = new AlarmServiceManagement(getApplicationContext());


                settingValue.setName(EditText_Name.getText().toString());                                       //알람이름
                settingValue.setMemo(EditText_Memo.getText().toString());                                       //알람메모

                if(!settingValue.getName().equals("")){

                    switch (TimerViewType){
                        case FragmentType.fragEveryDay:{
                            if(TimerSettingType == 1){//데이터 추가
                                everyDay_timerSettings.NewAddTimer();

                                SystemDataSave systemDataSave = new SystemDataSave(getApplicationContext());
                                if(systemDataSave.getData_AllTimerOff() == false) {
                                    alarmServiceManagement.All_AddAlarm();
                                }

                            }else if(TimerSettingType == 2){//데이터 변경
                                everyDay_timerSettings.TimerUpData();

                                EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(getApplicationContext());
                                int UniqueID = everyDay_dataBase_management.getData().get(getIntent().getIntExtra("ItemID", 0)).getUniqueID();
                                Log.d("UniqueID", " 번호 " + UniqueID);
                                alarmServiceManagement.AlarmUpDate(UniqueID);

                            }else if(TimerSettingType == 3){
                                everyDay_timerSettings.NewAddTimer();

                                SystemDataSave systemDataSave = new SystemDataSave(getApplicationContext());
                                if(systemDataSave.getData_AllTimerOff() == false) {
                                    alarmServiceManagement.All_AddAlarm();
                                }
                            }
                            break;
                        }
                        case FragmentType.fragWeek:{
                            if(TimerSettingType == 1){//데이터 추가
                                dayOfTheWeek_timerSettings.NewAddTimer();

                                SystemDataSave systemDataSave = new SystemDataSave(getApplicationContext());
                                if(systemDataSave.getData_AllTimerOff() == false) {
                                    alarmServiceManagement.All_AddAlarm_week();
                                }

                            }else if(TimerSettingType == 2){//데이터 변경
                                dayOfTheWeek_timerSettings.TimerUpData();

                                Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(getApplicationContext());
                                int UniqueID = week_dataBase_management.getData().get(getIntent().getIntExtra("ItemID", 0)).getUniqueID();
                                Log.d("UniqueID", " 번호 " + UniqueID);
                                alarmServiceManagement.AlarmUpDate_week(UniqueID);

                            }else if(TimerSettingType == 3){
                                dayOfTheWeek_timerSettings.NewAddTimer();

                                SystemDataSave systemDataSave = new SystemDataSave(getApplicationContext());
                                if(systemDataSave.getData_AllTimerOff() == false) {
                                    alarmServiceManagement.All_AddAlarm_week();
                                }
                            }
                            break;
                        }
                        case FragmentType.fragDate:{
                            if(TimerSettingType == 1){//데이터 추가
                                date_timerSettings.NewAddTimer();

                                SystemDataSave systemDataSave = new SystemDataSave(getApplicationContext());
                                if(systemDataSave.getData_AllTimerOff() == false) {
                                    alarmServiceManagement.All_AddAlarm_data();
                                }

                            }else if(TimerSettingType == 2){//데이터 변경
                                date_timerSettings.TimerUpData();

                                Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(getApplicationContext());
                                int UniqueID = date_dataBase_management.getData().get(getIntent().getIntExtra("ItemID", 0)).getUniqueID();
                                Log.d("UniqueID", " 번호 " + UniqueID);
                                alarmServiceManagement.AlarmUpDate_data(UniqueID);
                            }else if(TimerSettingType == 3){
                                date_timerSettings.NewAddTimer();

                                SystemDataSave systemDataSave = new SystemDataSave(getApplicationContext());
                                if(systemDataSave.getData_AllTimerOff() == false) {
                                    alarmServiceManagement.All_AddAlarm_data();
                                }
                            }
                        }
                    }

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    //========================================================================설정완료

                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "알람 이름을 적어주세요", Toast.LENGTH_SHORT).show();

                }

            }
        });


        LinearLayout_AutoOffTimer_SettingsViewShow.setOnClickListener(new View.OnClickListener() {//자동꺼짐시간 설정창 Show
            @Override
            public void onClick(View v) {
                String timer_number[] = {"30초", "1분", "2분", "3분", "5분"};

                AlertDialog.Builder builder = new AlertDialog.Builder(TimerSettings.this);

                builder.setSingleChoiceItems(timer_number, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });


        TextView_No_SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        SystemDataSave systemDataSave = new SystemDataSave(getApplicationContext());
        TimePickerDialog timePickerDialog = new TimePickerDialog(TimerSettings.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                settingValue.setTime_Hour(h);
                settingValue.setTime_Minute(m);

                settingValue.setName(EditText_Name.getText().toString());                                       //알람이름
                settingValue.setMemo(EditText_Memo.getText().toString());                                       //알람메모
                InterfaceSetting();
            }
        } ,settingValue.getTime_Hour(), settingValue.getTime_Minute(), systemDataSave.getData_Time24_to_12());
        timePickerDialog.show();
    }




    private void NewTimerCommonLogic(){//알람 추가시 디폴트 값

        settingValue.setTimer_Activate(true);//알람 활성화 유무

        settingValue.setTime_Hour(nowTime_H);//시간
        settingValue.setTime_Minute(nowTime_M);//분

        settingValue.setName(null);
        settingValue.setMemo(null);

        settingValue.setImportant(false);

        settingValue.setAlarm_Method(1);

        InterfaceSetting();
    }

    private void ValueSetting(){

        ImageView_None.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingValue.setAlarm_Method(0);
                AlarmCh();
            }
        });
        ImageView_Notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingValue.setAlarm_Method(1);
                AlarmCh();
            }
        });
        ImageView_Notifications_HeadUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingValue.setAlarm_Method(2);
                AlarmCh();
            }
        });
        ImageView_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getApplicationContext().getSharedPreferences("PopupCheck", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(sharedPreferences.getBoolean("PopupCheck", false) == false){

                    AlertDialog.Builder dlg = new AlertDialog.Builder(TimerSettings.this);
                    dlg.setTitle("다른앱 위에 표시(권한)"); //제목
                    dlg.setMessage("권한을 승인하셔야 팝업 알림을 받을 수 있습니다."); // 메시지

//                버튼 클릭시 동작
                    dlg.setPositiveButton("허용",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            //토스트 메시지

                            editor.putBoolean("PopupCheck", true);
                            editor.commit();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));

                                startActivityForResult(intent, 0);
                            }



                        }
                    });

                    dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(TimerSettings.this, "권한을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dlg.show();

                }

                if(sharedPreferences.getBoolean("PopupCheck", false) == true){
                    settingValue.setAlarm_Method(3);
                    AlarmCh();
                }

            }
        });



        Switch_Important.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingValue.setImportant(isChecked);
            }
        });

        Switch_TimerActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  //알람 활성화 유무
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settingValue.setTimer_Activate(b);
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

        settingValue.setAlarm_Method(db_everyDay.getAlarm_Method());

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

        settingValue.setAlarm_Method(db_week.getAlarm_Method());

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

        Log.d("인텐트 데이터 입력", "ItemID " + ActivityView.getIntent().getIntExtra("ItemID", 0));
        db_date = db_dateList.get(ActivityView.getIntent().getIntExtra("ItemID", 0));//ActivityView.getIntent().getIntExtra("ItemID", 0)

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

        settingValue.setAlarm_Method(db_date.getAlarm_Method());
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
