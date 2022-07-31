package com.example.perfect_time.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.perfect_time.R;
import com.example.perfect_time.SettingValue;

public class TimerVibrationSetting extends Activity {

    SettingValue settingValue;

    Switch Switch_Vibration_Activate;

    TextView TextView_VibrationValue;
    SeekBar SeekBar_VibrationValue;

    boolean Vibration_Activate;
    int VibrationValue;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

        Vibration_Activate = settingValue.isVibration_Activate();
        VibrationValue = settingValue.getVibration_volume();

        Switch_Vibration_Activate.setChecked(Vibration_Activate);
        TextView_VibrationValue.setText(Integer.toString(VibrationValue));
        SeekBar_VibrationValue.setProgress(VibrationValue, true);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_vibration_setting_view);

        Switch_Vibration_Activate = findViewById(R.id.Switch_Vibration_Activate);
        TextView_VibrationValue = findViewById(R.id.TextView_VibrationValue);
        SeekBar_VibrationValue = findViewById(R.id.SeekBar_VibrationValue);

        settingValue = new SettingValue();

        SettingValue settingValue = new SettingValue();

        Switch_Vibration_Activate.setChecked(settingValue.isVibration_Activate());

        Switch_Vibration_Activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Vibration_Activate = b;
                settingValue.setVibration_Activate(Vibration_Activate);
            }
        });

        SeekBar_VibrationValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                VibrationValue = i;
                TextView_VibrationValue.setText(Integer.toString(VibrationValue));
                settingValue.setVibration_volume(VibrationValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
