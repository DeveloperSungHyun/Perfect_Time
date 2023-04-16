package com.project.perfect_time;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.perfect_time.Activity.Preferences;
import com.project.perfect_time.FragmentActivity.FragDate;
import com.project.perfect_time.FragmentActivity.FragEveryDay;
import com.project.perfect_time.FragmentActivity.FragHome;
import com.project.perfect_time.FragmentActivity.FragWeek;
import com.example.perfect_time.R;
import com.project.perfect_time.Service.AlarmServiceManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    ImageView SystemSetting;

    TextView TextView_Date;

    Calendar calendar;

    private int OVERLAY_PERMISSION_PEQ_CODE = 0;
    int SceneNumber = 0;

    int y, m, d, w;


    private void IdMapping(){

        SystemSetting = findViewById(R.id.SystemSetting);
        TextView_Date = findViewById(R.id.TextView_Date);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(getApplicationContext());
        alarmServiceManagement.DAY_Loop();
//        alarmServiceManagement.All_TimerSetting();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//디바이스 다크모드 강제 해제

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdMapping();


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS);
        if (permission == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1000);
            }
            return;
        }

        calendar = Calendar.getInstance();

        y = calendar.get(Calendar.YEAR);//24시 형식
        m = calendar.get(Calendar.MONTH) + 1;//24시 형식
        d = calendar.get((Calendar.DATE));
        w = calendar.get((Calendar.DAY_OF_WEEK)) - 1;

        String DayOfWeek = null;
        switch (w){
            case 0: DayOfWeek = "일"; break;
            case 1: DayOfWeek = "월"; break;
            case 2: DayOfWeek = "화"; break;
            case 3: DayOfWeek = "수"; break;
            case 4: DayOfWeek = "목"; break;
            case 5: DayOfWeek = "금"; break;
            case 6: DayOfWeek = "토"; break;
        }
        TextView_Date.setText(y + "년 " + m + "월 " + d + "일 " + DayOfWeek + "요일");


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

                        break;
                }
                return true;
            }
        });


        SystemSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Preferences.class);
                startActivity(intent);
            }
        });


    }
//=============12/26

}