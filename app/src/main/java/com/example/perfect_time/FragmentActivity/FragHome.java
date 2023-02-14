package com.example.perfect_time.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    Thread thread;
    boolean isThread = true;
    boolean ThreadLoop = false;

    boolean TextTimer_none = false;

    Calendar calendar;

    View view;

    List<All_Time> all_times;

    OneDayTimeList oneDayTimeList;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    RecyclerView_ListAdapter recyclerView_listAdapter;
    RecyclerView_ListItem recyclerView_listItem;    //리사이클러뷰 아이템

    ArrayList<RecyclerView_ListItem> ListItem;      //리사이클러뷰 아이템 리스트데이터

    TextView TextView_NextTimerCount;

    private int ViewType = 0;                       //리사이클러뷰 뷰 타입

    All_Time next_Time = null;

    int y, m, d;
    Boolean ToDay = false;

    private void IdMapping(View view){
        recyclerView = view.findViewById(R.id.recyclerview);
        TextView_NextTimerCount = view.findViewById(R.id.TextView_NextTimerCount);

    }

    @Override
    public void onStart() {
        super.onStart();

        isThread = true;
        TextTimer_none = false;
        handler.sendEmptyMessage(0);
        NewTimer();

        ListItem.clear();//아이템 초기화

        ListLayout_View();

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

    private void recyclerView_ListShow(){

        for(All_Time data : all_times){

            if(data.isTimer_Activate() == true){

                recyclerView_listItem =
                        new RecyclerView_ListItem(1, data.isTimer_Activate(), data.isImportant(), data.getName(), data.getMemo(), data.getTime_Hour(),
                                data.getTime_Minute(), data.getAlarm_Method(),null, 0xFF000000, FragmentType.fragHome);

                ListItem.add(recyclerView_listItem);//리스트 아이템 추가
                recyclerView_listItem = null;
            }
        }

        recyclerView_listAdapter.notifyDataSetChanged();//
    }

    void NewTimer(){
        thread = new Thread() {
            public void run() {
                while (isThread) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }

            }
        };
        thread.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        isThread = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //Toast.makeText(view.getContext(), "test", Toast.LENGTH_SHORT).show();

            calendar = Calendar.getInstance();

            int Time_h = calendar.get(Calendar.HOUR_OF_DAY);
            int Time_m = calendar.get(Calendar.MINUTE);

            if(TextTimer_none == true && Time_h == 0 && Time_m == 0){
                TextTimer_none = false;
            }

            if(next_Time == null && TextTimer_none == false){
                for(All_Time data : all_times) {

                    if (data.isTimer_Activate() == true) {
                        if(Time_h < data.getTime_Hour() || (Time_h == data.getTime_Hour() && Time_m < data.getTime_Minute())){
                            next_Time = data;
                            break;
                        }else{
                            if(data == all_times.get(all_times.size() - 1)){
                                TextTimer_none = true;
                            }
                        }
                    }
                }
                ListItem.clear();
                recyclerView_ListShow();
            }else{
                if(TextTimer_none == false) {
                    if (Time_h == next_Time.getTime_Hour() && Time_m == next_Time.getTime_Minute()) {
                        next_Time = null;
                    }
                }
            }

            if(next_Time != null){
                int h, m;
                String nextTimer_count = null;
                h = next_Time.getTime_Hour() - Time_h;
                if(0 > next_Time.getTime_Minute() - Time_m){
                    h--;
                    m = (next_Time.getTime_Minute() - Time_m) + 60;
                }else{
                    m = next_Time.getTime_Minute() - Time_m;
                }

                if(h > 0){
                    nextTimer_count = h + "시간 " + m + "분 뒤 " + "\"" + next_Time.getName() + "\"" + " 일정이 있습니다.";
                }else{
                    nextTimer_count = m + "분 뒤 " + "\"" + next_Time.getName() + "\"" + " 일정이 있습니다.";
                }
                TextView_NextTimerCount.setText(nextTimer_count);

            }else{
                TextView_NextTimerCount.setText("이후 일정은 없습니다.");
            }

        }
    };
}
