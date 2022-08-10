package com.example.perfect_time;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    public static ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    public static int ViewPagerPosition;

    private void IdMapping(){
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//디바이스 다크모드 강제 해제

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdMapping();

        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerPosition = viewPager.getCurrentItem();//현재 보여지는 뷰


    }
}