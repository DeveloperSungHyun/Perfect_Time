package com.example.perfect_time.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.perfect_time.R;
import com.example.perfect_time.SettingValue;

public class TimerSoundSetting extends Activity {

    SettingValue settingValue;

    Switch Switch_Sound_Activate;

    TextView TextView_SoundValue;
    SeekBar SeekBar_SoundValue;

    boolean Sound_Activate;
    int SoundValue;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

        Sound_Activate = settingValue.isSound_Activate();
        SoundValue = settingValue.getSound_volume();

        Switch_Sound_Activate.setChecked(Sound_Activate);
        TextView_SoundValue.setText(Integer.toString(SoundValue));
        SeekBar_SoundValue.setProgress(SoundValue, true);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_sound_setting_view);

        Switch_Sound_Activate = findViewById(R.id.Switch_Sound_Activate);
        TextView_SoundValue = findViewById(R.id.TextView_SoundValue);
        SeekBar_SoundValue = findViewById(R.id.SeekBar_SoundValue);

        settingValue = new SettingValue();


        Switch_Sound_Activate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Sound_Activate = b;
                settingValue.setSound_Activate(Sound_Activate);
            }
        });

        SeekBar_SoundValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SoundValue = i;
                TextView_SoundValue.setText(Integer.toString(SoundValue));
                settingValue.setSound_volume(SoundValue);
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
