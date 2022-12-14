package com.example.perfect_time.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.perfect_time.AlarmServiceManagement;
import com.example.perfect_time.ListView_Adapter;
import com.example.perfect_time.List_Item;
import com.example.perfect_time.MainActivity;
import com.example.perfect_time.R;
import com.example.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;
import com.example.perfect_time.SystemDataSave;

public class Preferences extends Activity {

    AlertDialog.Builder builder;
    AlertDialog delete_view, check_view;

    SystemDataSave systemDataSave;

    Switch Switch_AllTimer_Off;
    Switch Switch_AutoTableMode;
    Switch Switch_BatteryLow_Notification;
    Switch Switch_Wear;
    RadioGroup RadioGroup_Time24_to_12;
    RadioButton RadioButton_12, RadioButton_24;
    TextView TextView_Time24_to_12;

    LinearLayout All_TimeDataDelete;

    Button BackButton;

    void IdMapping(){
        Switch_AllTimer_Off = findViewById(R.id.Switch_AllTimer_Off);
        Switch_AutoTableMode = findViewById(R.id.Switch_AutoTableMode);
        Switch_BatteryLow_Notification = findViewById(R.id.Switch_BatteryLow_Notification);
        Switch_Wear = findViewById(R.id.Switch_Wear);
        RadioGroup_Time24_to_12 = findViewById(R.id.RadioGroup_Time24_to_12);
        RadioButton_12 = findViewById(R.id.RadioButton_12);
        RadioButton_24 = findViewById(R.id.RadioButton_24);
        TextView_Time24_to_12 = findViewById(R.id.TextView_Time24_to_12);

        All_TimeDataDelete = findViewById(R.id.All_TimeDataDelete);

        BackButton = findViewById(R.id.BackButton);

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        IdMapping();

        systemDataSave = new SystemDataSave(getApplicationContext());

        DataSetting();
    }

    private void DataSetting(){
        boolean AllTimer_Off;
        boolean AutoTableMode;
        boolean BatteryLow_Notification;
        boolean Wear;
        boolean Time24_to_12;

        AllTimer_Off = systemDataSave.getData_AllTimerOff();
        AutoTableMode = systemDataSave.getData_TableMode();
        BatteryLow_Notification = systemDataSave.getData_BatteryNotification();
        Wear = systemDataSave.getData_WearData();
        Time24_to_12 = systemDataSave.getData_Time24_to_12();


        Switch_AllTimer_Off.setChecked(AllTimer_Off);
        Switch_AutoTableMode.setChecked(AutoTableMode);
        Switch_BatteryLow_Notification.setChecked(BatteryLow_Notification);
        Switch_Wear.setChecked(Wear);
        if(Time24_to_12 == false) {//0 = 12, 1 = 24
            RadioGroup_Time24_to_12.check(RadioButton_12.getId());
            TextView_Time24_to_12.setText("????????? 12??? ???????????? ???????????????.");
        }else {
            RadioGroup_Time24_to_12.check(RadioButton_24.getId());
            TextView_Time24_to_12.setText("????????? 24??? ???????????? ???????????????.");
        }

        Switch_AllTimer_Off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                systemDataSave.setData_AllTimerOff(b);
                AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(getApplicationContext());
                if(b == true) {//?????? ?????? off
                    alarmServiceManagement.All_Delete();
                    Toast.makeText(Preferences.this, "?????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch_AutoTableMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                systemDataSave.setData_TableMode(b);
            }
        });

        Switch_BatteryLow_Notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                systemDataSave.setData_BatteryNotification(b);
            }
        });

        Switch_Wear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                systemDataSave.setData_WearData(b);
            }
        });

        RadioGroup_Time24_to_12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.d("===============", " " + checkedId);

                if(checkedId == RadioButton_12.getId()) {
                    TextView_Time24_to_12.setText("????????? 12??? ???????????? ???????????????.");
                    systemDataSave.setData_Time24_to_12(false);
                }else {
                    TextView_Time24_to_12.setText("????????? 24??? ???????????? ???????????????.");
                    systemDataSave.setData_Time24_to_12(true);
                }
            }
        });

        All_TimeDataDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListView_Adapter listView_adapter = new ListView_Adapter();

                listView_adapter.addItem(new List_Item(R.drawable.every_icon, "?????? ????????????"));
                listView_adapter.addItem(new List_Item(R.drawable.week_icon, "????????? ?????? ??????"));
                listView_adapter.addItem(new List_Item(R.drawable.calendar_icon, "????????? ?????? ??????"));
                listView_adapter.addItem(new List_Item(R.drawable.all_calender_delete_icon, "?????? ?????? ??????"));
                listView_adapter.addItem(new List_Item(R.drawable.close_icon, "??????"));

                builder = new AlertDialog.Builder(Preferences.this);

                builder.setSingleChoiceItems(listView_adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(Preferences.this);
                        dlg.setTitle("???????????? ????????????"); //??????
                        dlg.setMessage("??????????????? ???????????? ???????????? ?????????."); // ?????????
//                ?????? ????????? ??????
                        dlg.setPositiveButton("??????",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int a) {
                                //????????? ?????????
                                Toast.makeText(Preferences.this,"????????? ??????????????????.",Toast.LENGTH_SHORT).show();

                                switch (which){
                                    case 0:{
                                        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(getApplicationContext());
                                        while(everyDay_dataBase_management.getData().size() > 0){
                                            everyDay_dataBase_management.setDelete(0);
                                        }

                                        Toast.makeText(Preferences.this, "?????? ????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case 1:{
                                        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(getApplicationContext());
                                        while(week_dataBase_management.getData().size() > 0){
                                            week_dataBase_management.setDelete(0);
                                        }

                                        Toast.makeText(Preferences.this, "????????? ????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case 2:{
                                        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(getApplicationContext());
                                        while(date_dataBase_management.getData().size() > 0){
                                            date_dataBase_management.setDelete(0);
                                        }

                                        Toast.makeText(Preferences.this, "????????? ????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case 3:{
                                        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(getApplicationContext());
                                        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(getApplicationContext());
                                        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(getApplicationContext());

                                        while(everyDay_dataBase_management.getData().size() > 0){
                                            everyDay_dataBase_management.setDelete(0);
                                        }

                                        while(week_dataBase_management.getData().size() > 0){
                                            week_dataBase_management.setDelete(0);
                                        }

                                        while(date_dataBase_management.getData().size() > 0){
                                            date_dataBase_management.setDelete(0);
                                        }
                                        Toast.makeText(Preferences.this, "????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                                        break;
                                    }
                                    case 4:{
                                        delete_view.dismiss();
                                    }

                                }

                            }

                        });

                        dlg.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                check_view.dismiss();
                            }
                        });
                        delete_view.dismiss();

                        if(which != 4) {
                            check_view = dlg.create();
                            check_view.show();
                        }
                    }
                });
//                builder.show();
                delete_view = builder.create();
                delete_view.show();
            }

        });



        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
