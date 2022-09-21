package com.example.perfect_time.FragmentActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.Activity.TimerSettings;
import com.example.perfect_time.All_Time;
import com.example.perfect_time.OneDayTimeList;
import com.example.perfect_time.R;
import com.example.perfect_time.RecyclerView.RecyclerView_ListAdapter;
import com.example.perfect_time.RecyclerView.RecyclerView_ListItem;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragHome extends Fragment {

    Calendar calendar;

    View view;

    List<All_Time> all_times;

    OneDayTimeList oneDayTimeList;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    RecyclerView_ListAdapter recyclerView_listAdapter;
    RecyclerView_ListItem recyclerView_listItem;    //리사이클러뷰 아이템

    ArrayList<RecyclerView_ListItem> ListItem;      //리사이클러뷰 아이템 리스트데이터

    private int ViewType = 0;                       //리사이클러뷰 뷰 타입

    int y, m, d;

    public static FragHome newInstance(){
        FragHome fragHome = new FragHome();

        return fragHome;
    }

    private void IdMapping(View view){
        recyclerView = view.findViewById(R.id.recyclerview);

    }

    @Override
    public void onStart() {
        super.onStart();



        ListItem.clear();//아이템 초기화

        recyclerView_ListShow();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_view,container, false);

        IdMapping(view);

        calendar = Calendar.getInstance();

        y = calendar.get(Calendar.YEAR);//24시 형식
        m = calendar.get(Calendar.MONTH) + 1;//24시 형식
        d = calendar.get((Calendar.DATE));

        oneDayTimeList = new OneDayTimeList(view.getContext(), y, m, d);

        all_times = oneDayTimeList.getTimeList();

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ListItem = new ArrayList<>();
        recyclerView_listAdapter = new RecyclerView_ListAdapter(ListItem);

        recyclerView.setAdapter(recyclerView_listAdapter);

        for (All_Time data : all_times){
            Log.d("======================", " " + data.getName());
        }

        String item[] = {"오늘 일정", "내일 일정", "모래 일정", "날짜 지정"};

        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(view.getContext());

        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:{
                        y = calendar.get(Calendar.YEAR);//24시 형식
                        m = calendar.get(Calendar.MONTH) + 1;//24시 형식
                        d = calendar.get((Calendar.DATE));

                        oneDayTimeList = new OneDayTimeList(view.getContext(), y, m, d);

                        all_times = oneDayTimeList.getTimeList();

                        recyclerView_ListShow();
                    }
                }
            }
        });

        return view;
    }

    private void recyclerView_ListShow(){
        for(All_Time data : all_times){


            recyclerView_listItem =
                    new RecyclerView_ListItem(1, data.isTimer_Activate(), data.isImportant(), data.getName(), data.getMemo(), data.getTime_Hour(),
                            data.getTime_Minute(), data.isSound_Activate(), data.isVibration_Activate(), data.isPopup_Activate(),null, 0xFF000000, FragmentType.fragHome);

            ListItem.add(recyclerView_listItem);//리스트 아이템 추가
        }

        recyclerView_listAdapter.notifyDataSetChanged();//
    }

}
