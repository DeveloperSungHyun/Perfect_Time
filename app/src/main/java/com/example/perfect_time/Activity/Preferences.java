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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.perfect_time.AlarmServiceManagement;
import com.example.perfect_time.MainActivity;
import com.example.perfect_time.R;
import com.example.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;
import com.example.perfect_time.SystemDataSave;

public class Preferences extends Activity {

    SystemDataSave systemDataSave;

    Switch Switch_AllTimer_Off;
    Switch Switch_AutoTableMode;
    Switch Switch_BatteryLow_Notification;
    Switch Switch_Wear;

    LinearLayout All_TimeDataDelete;

    Button BackButton;

    void IdMapping(){
        Switch_AllTimer_Off = findViewById(R.id.Switch_AllTimer_Off);
        Switch_AutoTableMode = findViewById(R.id.Switch_AutoTableMode);
        Switch_BatteryLow_Notification = findViewById(R.id.Switch_BatteryLow_Notification);
        Switch_Wear = findViewById(R.id.Switch_Wear);

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

        AllTimer_Off = systemDataSave.getData_AllTimerOff();
        AutoTableMode = systemDataSave.getData_TableMode();
        BatteryLow_Notification = systemDataSave.getData_BatteryNotification();
        Wear = systemDataSave.getData_WearData();

        Switch_AllTimer_Off.setChecked(AllTimer_Off);
        Switch_AutoTableMode.setChecked(AutoTableMode);
        Switch_BatteryLow_Notification.setChecked(BatteryLow_Notification);
        Switch_Wear.setChecked(Wear);

        Switch_AllTimer_Off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                systemDataSave.setData_AllTimerOff(b);
                AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(getApplicationContext());
                if(b == true) {//모든 알람 off
                    alarmServiceManagement.All_Delete();
                    Toast.makeText(Preferences.this, "모든 알림을 껐습니다.", Toast.LENGTH_SHORT).show();
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

        All_TimeDataDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item[] = {"매일 알람", "요일별 알람", "날짜별 아람", "모든 알람", "취소"};

                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(v.getContext());

                builder.setItems(item, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder dlg = new AlertDialog.Builder(Preferences.this);
                        dlg.setTitle("계발에서 개발까지"); //제목
                        dlg.setMessage("안녕하세요 계발에서 개발까지 입니다."); // 메시지
//                버튼 클릭시 동작
                        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int a) {
                                //토스트 메시지
                                Toast.makeText(Preferences.this,"확인을 눌르셨습니다.",Toast.LENGTH_SHORT).show();

                                switch (which){
                                    case 0:{
                                        EveryDay_DataBase_Management everyDay_dataBase_management = new EveryDay_DataBase_Management(getApplicationContext());
                                        while(everyDay_dataBase_management.getData().size() > 0){
                                            everyDay_dataBase_management.setDelete(0);
                                        }

                                        Toast.makeText(Preferences.this, "매일 알람이 모두 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case 1:{
                                        Week_DataBase_Management week_dataBase_management = new Week_DataBase_Management(getApplicationContext());
                                        while(week_dataBase_management.getData().size() > 0){
                                            week_dataBase_management.setDelete(0);
                                        }

                                        Toast.makeText(Preferences.this, "요일별 알람이 모두 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    case 2:{
                                        Date_DataBase_Management date_dataBase_management = new Date_DataBase_Management(getApplicationContext());
                                        while(date_dataBase_management.getData().size() > 0){
                                            date_dataBase_management.setDelete(0);
                                        }

                                        Toast.makeText(Preferences.this, "날짜별 알람이 모두 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Preferences.this, "알람이 모두 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }

                            }
                        });
                        dlg.show();

                    }
                });
                builder.show();
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
