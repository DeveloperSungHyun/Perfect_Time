package com.example.perfect_time.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.Activity.TimerSettings;
import com.example.perfect_time.FragmentActivity.FragmentType;
import com.example.perfect_time.R;
import com.example.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;

import java.util.ArrayList;

public class RecyclerView_ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    View view;

    private ArrayList<RecyclerView_ListItem> listItems;


    public RecyclerView_ListAdapter(ArrayList<RecyclerView_ListItem>listItems){
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType){
            case 0:{
                view = inflater.inflate(R.layout.activae_list_view, parent, false);
                return new ActivateListView(view);
            }
            case 1:{
                view = inflater.inflate(R.layout.disabled_list_view, parent, false);
                return new DisabledListView(view);
            }
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return (null == listItems ? 0 : listItems.size());
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).getViewType();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ActivateListView){
            ActivateView(((ActivateListView) holder), position);
        }else if(holder instanceof DisabledListView){
            DisabledView(((DisabledListView) holder), position);
        }else{

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void ActivateView(ActivateListView holder, int position){
        RecyclerView_ListItem getItem = listItems.get(position);

        holder.CardView_List.setOutlineSpotShadowColor(getItem.getDayTextColor());

        if(getItem.isImportant()) holder.ImageView_important.setVisibility(View.VISIBLE);
        else holder.ImageView_important.setVisibility(View.GONE);

        holder.TextView_Day.setText(getItem.getDayText());
        holder.TextView_Day.setTextColor(getItem.getDayTextColor());

        holder.TextView_Name.setText(getItem.getName());
        holder.TextView_Memo.setText(getItem.getMemo());

        holder.TextView_Time.setText(getItem.getTime_Hour() + " : " + getItem.getTime_Minute());

        if(getItem.isSound_Activate()) holder.ImageView_sound.setVisibility(View.VISIBLE);
        else holder.ImageView_sound.setVisibility(View.GONE);

        if(getItem.isVibration_Activate()) holder.ImageView_vibration.setVisibility(View.VISIBLE);
        else holder.ImageView_vibration.setVisibility(View.GONE);

        if(getItem.isPopup_Activate()) holder.ImageView_popup.setVisibility(View.VISIBLE);
        else holder.ImageView_popup.setVisibility(View.GONE);

        holder.RelativeLayout_BackGround.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String item[] = {"수정하기", "알림끄기", "삭제하기", "취소"};

                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(view.getContext());

                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:{

                                if(getItem.getFragmentType() == FragmentType.fragEveryDay){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 2);//1 새로운 데이터 추가
                                    intent.putExtra("TimerViewType", FragmentType.fragEveryDay);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());
                                    Log.d("인텐트 데이터 출력", "ItemID " + holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }
                                if(getItem.getFragmentType() == FragmentType.fragWeek){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 2);//1 새로운 데이터 추가
                                    intent.putExtra("TimerViewType", FragmentType.fragWeek);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }
                                if(getItem.getFragmentType() == FragmentType.fragDate){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 2);//1 새로운 데이터 추가
                                    intent.putExtra("TimerViewType", FragmentType.fragDate);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }

                                break;
                            }
                            case 1:{

                                Log.d("다이얼 로그", "알림끄기");
                                break;
                            }
                            case 2:{

                                if(getItem.getFragmentType() == FragmentType.fragEveryDay){
                                    EveryDay_DataBase_Management everyDay_dataBase_management =
                                            new EveryDay_DataBase_Management(view.getContext());

                                    everyDay_dataBase_management.setDelete(holder.getAdapterPosition());
                                }
                                if(getItem.getFragmentType() == FragmentType.fragWeek){
                                    Week_DataBase_Management week_dataBase_management =
                                            new Week_DataBase_Management(view.getContext());

                                    week_dataBase_management.setDelete(holder.getAdapterPosition());

                                    Log.d("=====================", "삭제");

                                }
                                if(getItem.getFragmentType() == FragmentType.fragDate){
                                    Date_DataBase_Management date_dataBase_management =
                                            new Date_DataBase_Management(view.getContext());

                                    date_dataBase_management.setDelete(holder.getAdapterPosition());

                                }

                                listItems.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        }
                    }
                });

                builder.show();

                return false;
            }
        });

    }

    private void DisabledView(DisabledListView holder, int position){
        RecyclerView_ListItem getItem = listItems.get(position);

        if(getItem.isImportant()) holder.ImageView_important.setVisibility(View.VISIBLE);
        else holder.ImageView_important.setVisibility(View.GONE);

        holder.TextView_Name.setText(getItem.getName());
        holder.TextView_Memo.setText(getItem.getMemo());

        holder.TextView_Time.setText(getItem.getTime_Hour() + " : " + getItem.getTime_Minute());

        if(getItem.isSound_Activate()) holder.ImageView_sound.setVisibility(View.VISIBLE);
        else holder.ImageView_sound.setVisibility(View.GONE);

        if(getItem.isVibration_Activate()) holder.ImageView_vibration.setVisibility(View.VISIBLE);
        else holder.ImageView_vibration.setVisibility(View.GONE);

        if(getItem.isPopup_Activate()) holder.ImageView_popup.setVisibility(View.VISIBLE);
        else holder.ImageView_popup.setVisibility(View.GONE);

    }

    public class ActivateListView extends RecyclerView.ViewHolder{//활성화 뷰
        CardView CardView_List;

        RelativeLayout RelativeLayout_BackGround;
        ImageView ImageView_important;//중요알림 표시 아이콘

        TextView TextView_Day;

        TextView TextView_Name;//알람 이름
        TextView TextView_Memo;//알람 메모

        TextView TextView_Time;//시간 시 : 분
        TextView TextView_Time_AmPm;// (오전, 오후) 구분

        ImageView ImageView_sound;//소리알림 아이콘
        ImageView ImageView_vibration;//진동알림 아이콘
        ImageView ImageView_popup;//팝업창 알림 아이콘

        public ActivateListView(@NonNull View itemView) {
            super(itemView);

            CardView_List = itemView.findViewById(R.id.CardView_List);

            RelativeLayout_BackGround = itemView.findViewById(R.id.RelativeLayout_BackGround);

            ImageView_important = itemView.findViewById(R.id.ImageView_important);

            TextView_Day = itemView.findViewById(R.id.TextView_Day);

            TextView_Name = itemView.findViewById(R.id.TextView_Name);
            TextView_Memo = itemView.findViewById(R.id.TextView_Memo);

            TextView_Time = itemView.findViewById(R.id.TextView_Time);
            TextView_Time_AmPm = itemView.findViewById(R.id.TextView_Time_AmPm);

            ImageView_sound = itemView.findViewById(R.id.ImageView_sound);
            ImageView_vibration = itemView.findViewById(R.id.ImageView_vibration);
            ImageView_popup = itemView.findViewById(R.id.ImageView_popup);

        }
    }

    public class DisabledListView extends RecyclerView.ViewHolder{//비 활성화 뷰
        ImageView ImageView_important;//중요알림 표시 아이콘

        TextView TextView_Name;//알람 이름
        TextView TextView_Memo;//알람 메모

        TextView TextView_Time;//시간 시 : 분
        TextView TextView_Time_AmPm;// (오전, 오후) 구분

        ImageView ImageView_sound;//소리알림 아이콘
        ImageView ImageView_vibration;//진동알림 아이콘
        ImageView ImageView_popup;//팝업창 알림 아이콘

        public DisabledListView(@NonNull View itemView) {
            super(itemView);

            ImageView_important = itemView.findViewById(R.id.ImageView_important);

            TextView_Name = itemView.findViewById(R.id.TextView_Name);
            TextView_Memo = itemView.findViewById(R.id.TextView_Memo);

            TextView_Time = itemView.findViewById(R.id.TextView_Time);
            TextView_Time_AmPm = itemView.findViewById(R.id.TextView_Time_AmPm);

            ImageView_sound = itemView.findViewById(R.id.ImageView_sound);
            ImageView_vibration = itemView.findViewById(R.id.ImageView_vibration);
            ImageView_popup = itemView.findViewById(R.id.ImageView_popup);

        }
    }
}

