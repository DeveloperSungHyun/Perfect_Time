package com.example.perfect_time.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.perfect_time.All_Time;
import com.example.perfect_time.OneDayTimeList;
import com.example.perfect_time.R;

import java.util.List;

public class FragHome extends Fragment {

    View view;

    List<All_Time> all_times;

    public static FragHome newInstance(){
        FragHome fragHome = new FragHome();

        return fragHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_view,container, false);

        OneDayTimeList oneDayTimeList = new OneDayTimeList(view.getContext(), 2022, 9, 12);

        all_times = oneDayTimeList.getTimeList();

        for (All_Time data : all_times){
            Log.d("======================", " " + data.getName());
        }

        return view;
    }
}
