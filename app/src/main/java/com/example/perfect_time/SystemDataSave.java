package com.example.perfect_time;

import android.content.Context;
import android.content.SharedPreferences;

public class SystemDataSave {

    Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SystemDataSave(Context context){
        this.context = context;

        sharedPreferences = context.getSharedPreferences("SystemSettingData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void setData_AllTimerOff(boolean AllTimerOff){

        editor.putBoolean("AllTimerOff", AllTimerOff);

        editor.commit();

    }
    public void setData_TableMode(boolean TableMode){

        editor.putBoolean("TableMode", TableMode);

        editor.commit();

    }
    public void setData_BatteryNotification(boolean BatteryNotification){

        editor.putBoolean("BatteryNotification", BatteryNotification);

        editor.commit();

    }
    public void setData_WearData(boolean WearData){

        editor.putBoolean("WearData", WearData);

        editor.commit();

    }
    public void setData_Time24_to_12(boolean Time24_to_12){

        editor.putBoolean("Time24_to_12", Time24_to_12);

        editor.commit();
    }

    public boolean getData_AllTimerOff(){
        return sharedPreferences.getBoolean("AllTimerOff", false);
    }
    public boolean getData_TableMode(){
        return sharedPreferences.getBoolean("TableMode", true);
    }
    public boolean getData_BatteryNotification(){
        return sharedPreferences.getBoolean("BatteryNotification", true);
    }
    public boolean getData_WearData(){
        return sharedPreferences.getBoolean("WearData", false);
    }
    public boolean getData_Time24_to_12(){
        return sharedPreferences.getBoolean("Time24_to_12", false);
    }

}
