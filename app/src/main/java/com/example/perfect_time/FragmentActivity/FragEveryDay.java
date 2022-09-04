package com.example.perfect_time.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.R;
import com.example.perfect_time.RecyclerView.RecyclerView_ListAdapter;
import com.example.perfect_time.RecyclerView.RecyclerView_ListItem;
import com.example.perfect_time.Activity.TimerSettings;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;

import java.util.ArrayList;

public class FragEveryDay extends Fragment {

    View view;

    LinearLayout TimerAddButton;//알람 추가 버튼
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    RecyclerView_ListAdapter recyclerView_listAdapter;
    RecyclerView_ListItem recyclerView_listItem;    //리사이클러뷰 아이템

    ArrayList<RecyclerView_ListItem> ListItem;      //리사이클러뷰 아이템 리스트데이터

    EveryDay_DataBase_Management everyDay_dataBase_management;

    private int ViewType = 0;                       //리사이클러뷰 뷰 타입

    private boolean Timer_Activate = true;          //알람 활정화 유무

    private boolean Important = false;              //중요알람 표시

    private String Name = null;                     //알람 이름
    private String Memo = null;                     //알람 메모

    private int Time_Hour = 0;                      //시간 시
    private int Time_Minute = 0;                    //시간 분

    private boolean Sound_Activate = true;          //소리알림 활성화 유무
    private boolean Vibration_Activate = true;      //진동알림 활성화 유무
    private boolean Popup_Activate = true;          //팝업알림 활성화 유무

    public static FragEveryDay newInstance(){
        FragEveryDay fragEveryDay = new FragEveryDay();

        return fragEveryDay;
    }


    private void IdMapping(View view){
        TimerAddButton = view.findViewById(R.id.TimerAddButton);//알람 추가 버튼
        recyclerView = view.findViewById(R.id.recyclerview);

    }

    @Override
    public void onStart() {
        super.onStart();

        ListItem.clear();//아이템 초기화

        for(DB_EveryDay data : everyDay_dataBase_management.getData()){

            if(data.isTimer_Activate()) ViewType = 0;
            else ViewType = 1;

            recyclerView_listItem =
                    new RecyclerView_ListItem(ViewType, data.isTimer_Activate(), data.isImportant(), data.getName(), data.getMemo(), data.getTime_Hour(),
                            data.getTime_Minute(), data.isSound_Activate(), data.isVibration_Activate(), data.isPopup_Activate(),null, 0xFF000000, FragmentType.fragEveryDay);

            ListItem.add(recyclerView_listItem);//리스트 아이템 추가
        }

//        recyclerView_listItem =
//                new RecyclerView_ListItem(ViewType, Timer_Activate, Important, Name, Memo, Time_Hour,
//                        Time_Minute, Sound_Activate, Vibration_Activate, Popup_Activate, FragmentType.fragEveryDay);
//
//        ListItem.add(recyclerView_listItem);//리스트 아이템 추가
//
//        recyclerView_listItem =
//                new RecyclerView_ListItem(ViewType, Timer_Activate, Important, Name, Memo, Time_Hour,
//                        Time_Minute, Sound_Activate, Vibration_Activate, Popup_Activate, FragmentType.fragEveryDay);

        recyclerView_listAdapter.notifyDataSetChanged();//
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_everyday_view,container, false);
        IdMapping(view);

        everyDay_dataBase_management = new EveryDay_DataBase_Management(getContext());

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        ListItem = new ArrayList<>();
        recyclerView_listAdapter = new RecyclerView_ListAdapter(ListItem);

        recyclerView.setAdapter(recyclerView_listAdapter);

        TimerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TimerSettings.class);
                intent.putExtra("TimerSettingType", 1);//1 새로운 데이터 추가
                intent.putExtra("TimerViewType", FragmentType.fragEveryDay);
                startActivity(intent);
            }
        });

        return view;
    }
}
