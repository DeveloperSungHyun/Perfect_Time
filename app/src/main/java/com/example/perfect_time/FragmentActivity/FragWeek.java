package com.example.perfect_time.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.DeviceType;
import com.example.perfect_time.R;
import com.example.perfect_time.RecyclerView.RecyclerView_ListAdapter;
import com.example.perfect_time.RecyclerView.RecyclerView_ListItem;
import com.example.perfect_time.Activity.TimerSettings;
import com.example.perfect_time.RoomDataBase.DayOfTheWeek.DB_Week;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;
import com.example.perfect_time.SystemDataSave;

import java.util.ArrayList;

public class FragWeek extends Fragment {
    View view;

    TextView TextView_none_list;

    LinearLayout TimerAddButton;//알람 추가 버튼
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    RecyclerView_ListAdapter recyclerView_listAdapter;
    RecyclerView_ListItem recyclerView_listItem;    //리사이클러뷰 아이템

    ArrayList<RecyclerView_ListItem> ListItem;      //리사이클러뷰 아이템 리스트데이터

    Week_DataBase_Management week_dataBase_management;

    private int ViewType = 0;                       //리사이클러뷰 뷰 타입

    private boolean Timer_Activate = true;          //알람 활정화 유무

    private boolean Important = false;              //중요알람 표시

    private String Name = null;                     //알람 이름
    private String Memo = null;                     //알람 메모

    private int Time_Hour = 0;                      //시간 시
    private int Time_Minute = 0;                    //시간 분
    private int Time_second = 0;                    //시간 초

    private boolean Sound_Activate = true;          //소리알림 활성화 유무
    private boolean Vibration_Activate = true;      //진동알림 활성화 유무
    private boolean Popup_Activate = true;          //팝업알림 활성화 유무

    String WeekText[] = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
    int DayTextColor;

    private void IdMapping(View view){
        TextView_none_list = view.findViewById(R.id.TextView_none_list);

        TimerAddButton = view.findViewById(R.id.TimerAddButton);//알람 추가 버튼
        recyclerView = view.findViewById(R.id.recyclerview);

    }

    @Override
    public void onStart() {
        super.onStart();

        if(week_dataBase_management.getData().size() == 0){
            TextView_none_list.setVisibility(View.VISIBLE);
        }else{
            TextView_none_list.setVisibility(View.GONE);
        }

        ListLayout_View();

        ListItem.clear();//아이템 초기화

        for(DB_Week data : week_dataBase_management.getData()){

            if(data.isTimer_Activate()) ViewType = 0;
            else ViewType = 1;

            if(data.getDayOfTheWeek() == 0){
                DayTextColor = 0xFFFF0000;
            }else if(data.getDayOfTheWeek() == 6){
                DayTextColor = 0xFF0000FF;
            }else{
                DayTextColor = 0xFF000000;
            }
            recyclerView_listItem =
                    new RecyclerView_ListItem(0, data.isTimer_Activate(), data.isImportant(), data.getName(), data.getMemo(), data.getTime_Hour(),
                            data.getTime_Minute(), data.getCentury(), data.isPopup_Activate(), data.isAutoDisplay_On(), WeekText[data.getDayOfTheWeek()], 0xFF000000, FragmentType.fragWeek);

            ListItem.add(recyclerView_listItem);//리스트 아이템 추가

            recyclerView_listItem = null;
        }

        recyclerView_listAdapter.notifyDataSetChanged();//
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week_view,container, false);
        IdMapping(view);


        week_dataBase_management = new Week_DataBase_Management(getContext());

        DeviceType deviceType = new DeviceType(view.getContext());
        if(new SystemDataSave(this.getContext()).getData_TableMode() == true){
            if(deviceType.IsPhone()){
                linearLayoutManager = new LinearLayoutManager(this.getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
            }else if(deviceType.IsTablet()){
                gridLayoutManager = new GridLayoutManager(this.getContext(),2);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        }else{
            linearLayoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        ListItem = new ArrayList<>();
        recyclerView_listAdapter = new RecyclerView_ListAdapter(ListItem);

        recyclerView.setAdapter(recyclerView_listAdapter);

        TimerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TimerSettings.class);
                intent.putExtra("TimerSettingType", 1);//1 새로운 데이터 추가
                intent.putExtra("TimerViewType", FragmentType.fragWeek);
                startActivity(intent);
            }
        });

        ListLayout_View();


        return view;
    }

    void ListLayout_View(){
        DeviceType deviceType = new DeviceType(view.getContext());
        if(new SystemDataSave(this.getContext()).getData_TableMode() == true){
            if(deviceType.IsPhone()){
                linearLayoutManager = new LinearLayoutManager(this.getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
            }else if(deviceType.IsTablet()){
                gridLayoutManager = new GridLayoutManager(this.getContext(),2);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        }else{
            linearLayoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        ListItem = new ArrayList<>();
        recyclerView_listAdapter = new RecyclerView_ListAdapter(ListItem);

        recyclerView.setAdapter(recyclerView_listAdapter);
    }
}
