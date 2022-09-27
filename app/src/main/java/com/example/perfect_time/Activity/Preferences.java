package com.example.perfect_time.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.example.perfect_time.R;
import com.example.perfect_time.SystemDataSave;

public class Preferences extends Activity {

    SystemDataSave systemDataSave;

    Switch Switch_AllTimer_Off;
    Switch Switch_AutoTableMode;
    Switch Switch_BatteryLow_Notification;
    Switch Switch_Wear;

    void IdMapping(){
        Switch_AllTimer_Off = findViewById(R.id.Switch_AllTimer_Off);
        Switch_AutoTableMode = findViewById(R.id.Switch_AutoTableMode);
        Switch_BatteryLow_Notification = findViewById(R.id.Switch_BatteryLow_Notification);
        Switch_Wear = findViewById(R.id.Switch_Wear);

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

    }
}
