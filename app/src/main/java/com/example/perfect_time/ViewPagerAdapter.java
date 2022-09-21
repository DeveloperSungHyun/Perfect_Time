package com.example.perfect_time;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.perfect_time.FragmentActivity.FragDate;
import com.example.perfect_time.FragmentActivity.FragEveryDay;
import com.example.perfect_time.FragmentActivity.FragHome;
import com.example.perfect_time.FragmentActivity.FragWeek;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return FragHome.newInstance();
            case 1: return FragEveryDay.newInstance();
            case 2: return FragWeek.newInstance();
            case 3: return FragDate.newInstance();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "홈";
            case 1: return "매일";
            case 2: return "요일";
            case 3: return "날짜";
            default: return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }





}
