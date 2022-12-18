package com.example.perfect_time.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.Activity.Preferences;
import com.example.perfect_time.All_Time;
import com.example.perfect_time.DeviceType;
import com.example.perfect_time.OneDayTimeList;
import com.example.perfect_time.R;
import com.example.perfect_time.RecyclerView.RecyclerView_ListAdapter;
import com.example.perfect_time.RecyclerView.RecyclerView_ListItem;
import com.example.perfect_time.SystemDataSave;

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
    GridLayoutManager gridLayoutManager;

    RecyclerView_ListAdapter recyclerView_listAdapter;
    RecyclerView_ListItem recyclerView_listItem;    //리사이클러뷰 아이템

    Button DayTimeList;
    TextView DayTime_y_m_d, DayTime_week, disDay;

    ArrayList<RecyclerView_ListItem> ListItem;      //리사이클러뷰 아이템 리스트데이터

    private int ViewType = 0;                       //리사이클러뷰 뷰 타입

    int y, m, d;
    Boolean ToDay = false;

    public static FragHome newInstance(){
        FragHome fragHome = new FragHome();

        return fragHome;
    }

    private void IdMapping(View view){
        recyclerView = view.findViewById(R.id.recyclerview);
        DayTimeList = view.findViewById(R.id.DayTimeList);

//        DayTime_y_m_d = view.findViewById(R.id.DayTime_y_m_d);
//        DayTime_week = view.findViewById(R.id.DayTime_week);
//        disDay = view.findViewById(R.id.disDay);

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

        for (All_Time data : all_times){
            Log.d("======================", " " + data.getName());
        }

        DayTimeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item[] = {"오늘 일정", "내일 일정", "모래 일정", "날짜 지정"};

                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(view.getContext());

                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:{
                                ToDay = true;

                                y = calendar.get(Calendar.YEAR);//24시 형식
                                m = calendar.get(Calendar.MONTH) + 1;//24시 형식
                                d = calendar.get((Calendar.DATE));

                                DayTimeList.setText("오늘 일정");
                                break;
                            }
                            case 1:{
                                ToDay = false;

                                y = calendar.get(Calendar.YEAR);//24시 형식
                                m = calendar.get(Calendar.MONTH) + 1;//24시 형식
                                d = calendar.get((Calendar.DATE)) + 1;

                                DayTimeList.setText("내일 일정");
                                break;
                            }
                            case 2:{
                                ToDay = false;

                                y = calendar.get(Calendar.YEAR);//24시 형식
                                m = calendar.get(Calendar.MONTH) + 1;//24시 형식
                                d = calendar.get((Calendar.DATE)) + 2;

                                DayTimeList.setText("모래 일정");
                                break;
                            }
                        }

                        ListItem.clear();
                        oneDayTimeList = new OneDayTimeList(view.getContext(), y, m, d);

                        all_times = oneDayTimeList.getTimeList();

                        recyclerView_ListShow();
                    }
                });
                builder.show();

            }
        });


        return view;
    }

    int viewType = 1;
    private void recyclerView_ListShow(){

        for(All_Time data : all_times){

            if(data.isTimer_Activate() == true){

                if(ToDay) viewType = 1;
                else viewType = 2;

                recyclerView_listItem =
                        new RecyclerView_ListItem(0, data.isTimer_Activate(), data.isImportant(), data.getName(), data.getMemo(), data.getTime_Hour(),
                                data.getTime_Minute(), data.isVibration_Activate(), data.isHeadUp_Activate(), data.isPopup_Activate(), data.isAutoDisplay_On(), null, 0xFF000000, FragmentType.fragHome);

                ListItem.add(recyclerView_listItem);//리스트 아이템 추가
                recyclerView_listItem = null;
            }
        }




        //=======================

        recyclerView_listItem =
                new RecyclerView_ListItem(2,false, false, null, null, 0, 0,
                        false, false, false, false,null, 0xFF000000, FragmentType.fragHome);

        ListItem.add(recyclerView_listItem);//리스트 아이템 추가

        oneDayTimeList = new OneDayTimeList(view.getContext(), y, m, d + 1);

        int ItemNumCount = 0;
        for(All_Time data : oneDayTimeList.getTimeList()){

            ItemNumCount++;

            if(data.isTimer_Activate() == true){

                if(ToDay) viewType = 1;
                else viewType = 2;

                recyclerView_listItem =
                        new RecyclerView_ListItem(0, data.isTimer_Activate(), data.isImportant(), data.getName(), data.getMemo(), data.getTime_Hour(),
                                data.getTime_Minute(), data.isVibration_Activate(), data.isHeadUp_Activate(), data.isPopup_Activate(), data.isAutoDisplay_On(), null, 0xFF000000, FragmentType.fragDate);

                ListItem.add(recyclerView_listItem);//리스트 아이템 추가
            }

            if(ItemNumCount >= 3) break;
        }

        recyclerView_listAdapter.notifyDataSetChanged();//
    }


}
