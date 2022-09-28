package com.example.perfect_time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.perfect_time.Activity.Preferences;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity{

    TabLayout tabLayout;
    public static ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    Button SystemSettings;

    public static int ViewPagerPosition;

    private void IdMapping(){
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        SystemSettings = findViewById(R.id.SystemSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//디바이스 다크모드 강제 해제

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdMapping();

        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        fragmentPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerPosition = viewPager.getCurrentItem();//현재 보여지는 뷰


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SystemSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Preferences.class);
                startActivity(intent);
            }
        });



    }



    private void refresh(){
        fragmentPagerAdapter.notifyDataSetChanged();
    }


}