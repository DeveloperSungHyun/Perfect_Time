package com.example.perfect_time.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.DeviceType;
import com.example.perfect_time.MainActivity;
import com.example.perfect_time.R;
import com.example.perfect_time.RecyclerView.RecyclerView_ListAdapter;
import com.example.perfect_time.RecyclerView.RecyclerView_ListItem;
import com.example.perfect_time.Activity.TimerSettings;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Everyday.DB_EveryDay;
import com.example.perfect_time.SystemDataSave;

import java.util.ArrayList;

public class FragEveryDay extends Fragment {

    View view;

    TextView TextView_none_list;

    LinearLayout TimerAddButton;//알람 추가 버튼
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    RecyclerView_ListAdapter recyclerView_listAdapter;
    RecyclerView_ListItem recyclerView_listItem;    //리사이클러뷰 아이템

    ArrayList<RecyclerView_ListItem> ListItem;      //리사이클러뷰 아이템 리스트데이터

    EveryDay_DataBase_Management everyDay_dataBase_management;

    private int ViewType = 0;                       //리사이클러뷰 뷰 타입

    private void IdMapping(View view){
        TextView_none_list = view.findViewById(R.id.TextView_none_list);

        TimerAddButton = view.findViewById(R.id.TimerAddButton);//알람 추가 버튼
        recyclerView = view.findViewById(R.id.recyclerview);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("UpData","=====================");
        if(data.getIntExtra("UpData", 0) == 0){//데이터 업데이트가 일어나지 안을떄
            Toast.makeText(getContext(), "업데이트 안됨", Toast.LENGTH_SHORT).show();
            Log.d("UpData","===================== ok");
        }else if(data.getIntExtra("UpData", 0) == 1){//데이터가 업데이트 됬을떄
            Toast.makeText(getContext(), "업데이트 됨", Toast.LENGTH_SHORT).show();
            Log.d("UpData","===================== no");

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        ListItem.clear();//아이템 초기화

        for(DB_EveryDay data : everyDay_dataBase_management.getData()){

            if(data.isTimer_Activate()) ViewType = 0;
            else ViewType = 1;

            recyclerView_listItem =
                    new RecyclerView_ListItem(0, data.isTimer_Activate(), data.isImportant(), data.getName(), data.getMemo(), data.getTime_Hour(),
                            data.getTime_Minute(), data.isVibration_Activate(), data.isHeadUp_Activate(), data.isPopup_Activate(), data.isAutoDisplay_On(), "매일", 0xFF000000, FragmentType.fragEveryDay);

            ListItem.add(recyclerView_listItem);//리스트 아이템 추가
            recyclerView_listItem = null;
        }

        recyclerView_listAdapter.notifyDataSetChanged();//
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_everyday_view,container, false);
        IdMapping(view);


        everyDay_dataBase_management = new EveryDay_DataBase_Management(getContext());

        if(everyDay_dataBase_management.getData().size() == 0){
            TextView_none_list.setVisibility(View.VISIBLE);
        }else{
            TextView_none_list.setVisibility(View.GONE);
        }

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
                intent.putExtra("TimerViewType", FragmentType.fragEveryDay);
                startActivity(intent);
            }
        });

        return view;
    }
}
