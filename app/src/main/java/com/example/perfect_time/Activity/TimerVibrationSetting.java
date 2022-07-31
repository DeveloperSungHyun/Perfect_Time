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

    boolean Sound_Activate;
    int SoundValue;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

        Sound_Activate = settingValue.isSound_Activate();
        SoundValue = settingValue.getSound_volume();

        Switch_Vibration_Activate.setChecked(Sound_Activate);
        TextView_VibrationValue.setText(Integer.toString(SoundValue));
        SeekBar_VibrationValue.setProgress(SoundValue, true);

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
                settingValue.setVibration_Activate(b);
            }
        });
    }
}
