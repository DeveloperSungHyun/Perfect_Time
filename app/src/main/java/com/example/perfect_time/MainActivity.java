package com.example.perfect_time;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.perfect_time.Activity.Preferences;
import com.example.perfect_time.FragmentActivity.FragDate;
import com.example.perfect_time.FragmentActivity.FragEveryDay;
import com.example.perfect_time.FragmentActivity.FragHome;
import com.example.perfect_time.FragmentActivity.FragWeek;
import com.example.perfect_time.Service.TimerService;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;

    ImageView SystemSetting;

    ImageButton AddButton;

    BottomAppBar bottomAppBar;


    int SceneNumber = 0;

    private void IdMapping(){
        //AddButton = findViewById(R.id.AddButton);
        //BottomAppBar = findViewById(R.id.BottomAppBar);

        SystemSetting = findViewById(R.id.SystemSetting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//디바이스 다크모드 강제 해제

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdMapping();

        PowerManager pm= (PowerManager) getSystemService(Context.POWER_SERVICE);
        String packageName= getPackageName();
        if (pm.isIgnoringBatteryOptimizations(packageName) ){

        } else {    // 메모리 최적화가 되어 있다면, 풀기 위해 설정 화면 띄움.
            Intent intent=new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivityForResult(intent,0);
        }

        Intent intent = new Intent(this, TimerService.class);
        intent.setAction("start");
        bottomNavigationView = findViewById(R.id.BottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragHome()).commit();

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragHome()).commit();
                        SceneNumber = 0;
                        break;
                    case R.id.EveryDay:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragEveryDay()).commit();
                        SceneNumber = 1;
                        break;
                    case R.id.DayOfTheWeek:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragWeek()).commit();
                        SceneNumber = 2;
                        break;
                    case R.id.Date:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragDate()).commit();
                        SceneNumber = 3;

                        //stopService(intent);
                        break;
                }
                return true;
            }
        });

        //bottomNavigationView = BottomAppBar.FAB_ANIMATION_MODE_SCALE;


//        AddButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                switch (SceneNumber){
//                    case 0:{
//                        AddButton.willNotDraw();
//                        break;
//                    }
//                    case 1:{
//                        Intent intent = new Intent(view.getContext(), TimerSettings.class);
//                        intent.putExtra("TimerSettingType", 1);//1 새로운 데이터 추가
//                        intent.putExtra("TimerViewType", FragmentType.fragEveryDay);
//                        startActivity(intent);
//
//                        break;
//                    }
//                    case 2:{
//                        Intent intent = new Intent(view.getContext(), TimerSettings.class);
//                        intent.putExtra("TimerSettingType", 1);//1 새로운 데이터 추가
//                        intent.putExtra("TimerViewType", FragmentType.fragWeek);
//                        startActivity(intent);
//
//                        break;
//                    }
//                    case 3:{
//                        Intent intent = new Intent(view.getContext(), TimerSettings.class);
//                        intent.putExtra("TimerSettingType", 1);//1 새로운 데이터 추가
//                        intent.putExtra("TimerViewType", FragmentType.fragDate);
//                        startActivity(intent);
//                    }
//                }
//            }
//        });

        SystemSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Preferences.class);
                startActivity(intent);
            }
        });



        if(TimerService.isServiceRunning(this) == false){//서비스가 실행이 아닐떄

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            }else {
                startService(intent);
            }

        }

    }


}