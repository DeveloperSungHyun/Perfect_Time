package com.example.perfect_time;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

    boolean WhiteCheck = false;

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

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean WhiteCheck = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            /**
             * 등록이 되어있따면 TRUE
             * 등록이 안되있다면 FALSE
             */
            WhiteCheck = powerManager.isIgnoringBatteryOptimizations(this.getPackageName());
            /** 만약 화이트리스트에 등록이 되지않았다면 등록을 해줍니다. **/
            if(!WhiteCheck){
                Log.d("화이트리스트","화이트리스트에 등록되지않았습니다.");
                Intent intent  = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:"+ this.getPackageName()));
                this.startActivity(intent);
            }
            else Log.d("화이트리스트","화이트리스트에 등록되어있습니다.");
        }

//        if(isWhiteListing){
//            Log.d("화이트리스트", "등록됨");
//        }else{
//            Log.d("화이트리스트", "등록안됨");
//
//            AlertDialog.Builder setdialog = new AlertDialog.Builder(MainActivity.this);
//            setdialog.setTitle("추가 설정이 필요합니다.");
//            setdialog.setPositiveButton("네", new DialogInterface.OnClickListener(){
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                    intent.setData(Uri.parse("package:"+ getApplicationContext().getPackageName()));
//                    getApplicationContext().startActivity(intent);
//                }
//            });
//
//            setdialog.setNegativeButton("아니요", new DialogInterface.OnClickListener(){
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            setdialog.show();
//        }
        //===========================================================

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