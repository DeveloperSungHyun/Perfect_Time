package com.example.perfect_time;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.perfect_time.Activity.Preferences;
import com.example.perfect_time.FragmentActivity.FragDate;
import com.example.perfect_time.FragmentActivity.FragEveryDay;
import com.example.perfect_time.FragmentActivity.FragHome;
import com.example.perfect_time.FragmentActivity.FragWeek;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    ImageView SystemSetting;

    int SceneNumber = 0;

    private void IdMapping(){

        SystemSetting = findViewById(R.id.SystemSetting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//디바이스 다크모드 강제 해제

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdMapping();



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


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }


}