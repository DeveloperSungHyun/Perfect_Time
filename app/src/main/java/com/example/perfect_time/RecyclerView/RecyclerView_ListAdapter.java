package com.example.perfect_time.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.Activity.TimerSettings;
import com.example.perfect_time.AlarmServiceManagement;
import com.example.perfect_time.FragmentActivity.FragmentType;
import com.example.perfect_time.ListView_Adapter;
import com.example.perfect_time.List_Item;
import com.example.perfect_time.R;
import com.example.perfect_time.RoomDataBase.Date_DataBase_Management;
import com.example.perfect_time.RoomDataBase.EveryDay_DataBase_Management;
import com.example.perfect_time.RoomDataBase.Week_DataBase_Management;
import com.example.perfect_time.SettingValue;
import com.example.perfect_time.Time24_to_12Hour;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerView_ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    View view;

    boolean another_day = true;

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
                another_day = true;
                view = inflater.inflate(R.layout.disabled_list_view, parent, false);
                return new DisabledListView(view);

            }
            case 2:{
                another_day = false;
                view = inflater.inflate(R.layout.next_day_line, parent, false);
                return new NextDayLine(view);
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

    int Time_h_12;
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void ActivateView(ActivateListView holder, int position){
        RecyclerView_ListItem getItem = listItems.get(position);

        holder.CardView_List.setOutlineSpotShadowColor(getItem.getDayTextColor());

        if(getItem.isTimer_Activate()){
            holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
        }else{
            holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFD6D6D6));
        }

        if(getItem.isImportant()){
            holder.ImageView_important.setVisibility(View.VISIBLE);
        }
        else{
            holder.ImageView_important.setVisibility(View.GONE);
        }

        holder.TextView_Day.setText(getItem.getDayText());
        holder.TextView_Day.setTextColor(getItem.getDayTextColor());

        holder.TextView_Name.setText(getItem.getName());
        holder.TextView_Memo.setText(getItem.getMemo());

        Time24_to_12Hour time24_to_12Hour = new Time24_to_12Hour(getItem.getTime_Hour(), view.getContext());
        holder.TextView_Time.setText(time24_to_12Hour.getTime_Hour() + " : " + getItem.getTime_Minute());
        holder.TextView_Time_AmPm.setText(time24_to_12Hour.getAmPm());

        if(getItem.isVibration_Activate()) holder.ImageView_vibration.setVisibility(View.VISIBLE);
        else holder.ImageView_vibration.setVisibility(View.GONE);

        if(getItem.isPopup_Activate()) holder.ImageView_popup.setVisibility(View.VISIBLE);
        else holder.ImageView_popup.setVisibility(View.GONE);

        holder.RelativeLayout_BackGround.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ListView_Adapter listView_adapter = new ListView_Adapter();

                listView_adapter.addItem(new List_Item(R.drawable.edit_icon, "????????????"));
                listView_adapter.addItem(new List_Item(R.drawable.copy_icon, "????????????"));
                listView_adapter.addItem(new List_Item(R.drawable.alarm_on_icon, "??????On/Off"));
                listView_adapter.addItem(new List_Item(R.drawable.delete_icon, "????????????"));
                listView_adapter.addItem(new List_Item(R.drawable.close_icon, "??????"));


                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(view.getContext());
                AlertDialog dialog = builder.create();

                builder.setSingleChoiceItems(listView_adapter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ToastText = null;

                        switch (which){
                            case 0:{

                                if(getItem.getFragmentType() == FragmentType.fragEveryDay){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 2);//1 ????????? ????????? ??????
                                    intent.putExtra("TimerViewType", FragmentType.fragEveryDay);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());
                                    Log.d("????????? ????????? ??????", "ItemID " + holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }
                                if(getItem.getFragmentType() == FragmentType.fragWeek){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 2);//1 ????????? ????????? ??????
                                    intent.putExtra("TimerViewType", FragmentType.fragWeek);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }
                                if(getItem.getFragmentType() == FragmentType.fragDate){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 2);//1 ????????? ????????? ??????
                                    intent.putExtra("TimerViewType", FragmentType.fragDate);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }

                                break;
                            }
                            case 1:{

                                if(getItem.getFragmentType() == FragmentType.fragEveryDay){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 3);//3 ????????? ??????
                                    intent.putExtra("TimerViewType", FragmentType.fragEveryDay);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());
                                    Log.d("????????? ????????? ??????", "ItemID " + holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }
                                if(getItem.getFragmentType() == FragmentType.fragWeek){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 3);//3 ????????? ??????
                                    intent.putExtra("TimerViewType", FragmentType.fragWeek);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }
                                if(getItem.getFragmentType() == FragmentType.fragDate){
                                    Intent intent = new Intent(view.getContext(), TimerSettings.class);
                                    intent.putExtra("TimerSettingType", 3);//3 ????????? ??????
                                    intent.putExtra("TimerViewType", FragmentType.fragDate);

                                    intent.putExtra("ItemID", holder.getAdapterPosition());

                                    view.getContext().startActivity(intent);
                                }

                                break;
                            }
                            case 2:{

                                if(getItem.getFragmentType() == FragmentType.fragEveryDay){
                                    EveryDay_DataBase_Management everyDay_dataBase_management =
                                            new EveryDay_DataBase_Management(view.getContext());
                                    AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(view.getContext());

                                    if(everyDay_dataBase_management.getData().get(position).isTimer_Activate()){
                                        holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFD6D6D6));
                                        everyDay_dataBase_management.setTimeOnOff(position, false);

                                        alarmServiceManagement.Delete_Alarm(everyDay_dataBase_management.getData().get(holder.getAdapterPosition()).getUniqueID());

                                        ToastText = "????????? ???????????????.";
                                    }else{
                                        holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
                                        everyDay_dataBase_management.setTimeOnOff(position, true);

                                        alarmServiceManagement.All_AddAlarm();
                                        ToastText = "????????? ???????????????.";
                                    }
                                }
                                if(getItem.getFragmentType() == FragmentType.fragWeek){
                                    Week_DataBase_Management week_dataBase_management =
                                            new Week_DataBase_Management(view.getContext());

                                    AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(view.getContext());

                                    if(week_dataBase_management.getData().get(position).isTimer_Activate()){
                                        holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFD6D6D6));
                                        week_dataBase_management.setTimeOnOff(holder.getAdapterPosition(), false);

                                        alarmServiceManagement.Delete_Alarm(week_dataBase_management.getData().get(holder.getAdapterPosition()).getUniqueID());

                                        ToastText = "????????? ???????????????.";
                                    }else{
                                        holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
                                        week_dataBase_management.setTimeOnOff(holder.getAdapterPosition(), true);

                                        alarmServiceManagement.All_AddAlarm_week();
                                        ToastText = "????????? ???????????????.";
                                    }

                                }
                                if(getItem.getFragmentType() == FragmentType.fragDate){
                                    Date_DataBase_Management date_dataBase_management =
                                            new Date_DataBase_Management(view.getContext());

                                    AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(view.getContext());

                                    if(date_dataBase_management.getData().get(position).isTimer_Activate()){
                                        holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFD6D6D6));
                                        date_dataBase_management.setTimeOnOff(holder.getAdapterPosition(), false);

                                        alarmServiceManagement.Delete_Alarm(date_dataBase_management.getData().get(holder.getAdapterPosition()).getUniqueID());

                                        ToastText = "????????? ???????????????.";
                                    }else{
                                        holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
                                        date_dataBase_management.setTimeOnOff(holder.getAdapterPosition(), true);

                                        alarmServiceManagement.All_AddAlarm_data();
                                        ToastText = "????????? ???????????????.";
                                    }

                                }
                                Toast.makeText(view.getContext(), ToastText, Toast.LENGTH_SHORT).show();
                                Log.d("????????? ??????", "????????????");
                                break;
                            }
                            case 3:{

                                if(getItem.getFragmentType() == FragmentType.fragEveryDay){
                                    EveryDay_DataBase_Management everyDay_dataBase_management =
                                            new EveryDay_DataBase_Management(view.getContext());

                                    AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(view.getContext());
                                    alarmServiceManagement.Delete_Alarm(everyDay_dataBase_management.getData().get(holder.getAdapterPosition()).getUniqueID());

                                    everyDay_dataBase_management.setDelete(holder.getAdapterPosition());
                                }
                                if(getItem.getFragmentType() == FragmentType.fragWeek){
                                    Week_DataBase_Management week_dataBase_management =
                                            new Week_DataBase_Management(view.getContext());

                                    AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(view.getContext());
                                    alarmServiceManagement.Delete_Alarm(week_dataBase_management.getData().get(holder.getAdapterPosition()).getUniqueID());

                                    week_dataBase_management.setDelete(holder.getAdapterPosition());

                                }
                                if(getItem.getFragmentType() == FragmentType.fragDate){
                                    Date_DataBase_Management date_dataBase_management =
                                            new Date_DataBase_Management(view.getContext());

                                    AlarmServiceManagement alarmServiceManagement = new AlarmServiceManagement(view.getContext());
                                    alarmServiceManagement.Delete_Alarm(date_dataBase_management.getData().get(holder.getAdapterPosition()).getUniqueID());

                                    date_dataBase_management.setDelete(holder.getAdapterPosition());

                                }

                                listItems.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());

                                Toast.makeText(view.getContext(), "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                            }
                        }
                        dialog.dismiss();
                    }
                });


                builder.show();

                return false;
            }
        });

    }
    Calendar calendar;
    int h, m;
//    int Day_y, Day_m, Day_d;

    private void DisabledView(DisabledListView holder, int position){

        calendar = Calendar.getInstance();

        holder.TextView_Day.setText(null);

        h = calendar.get(Calendar.HOUR_OF_DAY);
        m = calendar.get(Calendar.MINUTE);

//        Day_y = calendar.get(Calendar.YEAR);//24??? ??????
//        Day_m = calendar.get(Calendar.MONTH) + 1;//24??? ??????
//        Day_d = calendar.get((Calendar.DATE));

        RecyclerView_ListItem getItem = listItems.get(position);

        if(getItem.isImportant()) holder.ImageView_important.setVisibility(View.VISIBLE);
        else holder.ImageView_important.setVisibility(View.GONE);


        if(another_day){
            if(h < getItem.getTime_Hour() || (h == getItem.getTime_Hour() && m < getItem.getTime_Minute())){
                holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
            }else{
                holder.CardView_List.setBackgroundTintList(ColorStateList.valueOf(0xFFD6D6D6));
            }
        }

        if(getItem.isImportant()){
            holder.ImageView_important.setVisibility(View.VISIBLE);
        }
        else{
            holder.ImageView_important.setVisibility(View.GONE);
        }

        holder.TextView_Name.setText(getItem.getName());
        holder.TextView_Memo.setText(getItem.getMemo());

        Time24_to_12Hour time24_to_12Hour = new Time24_to_12Hour(getItem.getTime_Hour(), view.getContext());
        holder.TextView_Time.setText(time24_to_12Hour.getTime_Hour() + " : " + getItem.getTime_Minute());
        holder.TextView_Time_AmPm.setText(time24_to_12Hour.getAmPm());

        if(getItem.isVibration_Activate()) holder.ImageView_vibration.setVisibility(View.VISIBLE);
        else holder.ImageView_vibration.setVisibility(View.GONE);

        if(getItem.isPopup_Activate()) holder.ImageView_popup.setVisibility(View.VISIBLE);
        else holder.ImageView_popup.setVisibility(View.GONE);

    }

    public class ActivateListView extends RecyclerView.ViewHolder{//????????? ???
        CardView CardView_List;

        RelativeLayout RelativeLayout_BackGround;
        ImageView ImageView_important;//???????????? ?????? ?????????

        TextView TextView_Day;

        TextView TextView_Name;//?????? ??????
        TextView TextView_Memo;//?????? ??????

        TextView TextView_Time;//?????? ??? : ???
        TextView TextView_Time_AmPm;// (??????, ??????) ??????

        ImageView ImageView_sound;//???????????? ?????????
        ImageView ImageView_vibration;//???????????? ?????????
        ImageView ImageView_popup;//????????? ?????? ?????????

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

    public class DisabledListView extends RecyclerView.ViewHolder{//??? ????????? ???
        CardView CardView_List;

        TextView TextView_Day;

        ImageView ImageView_important;//???????????? ?????? ?????????

        TextView TextView_Name;//?????? ??????
        TextView TextView_Memo;//?????? ??????

        TextView TextView_Time;//?????? ??? : ???
        TextView TextView_Time_AmPm;// (??????, ??????) ??????

        ImageView ImageView_sound;//???????????? ?????????
        ImageView ImageView_vibration;//???????????? ?????????
        ImageView ImageView_popup;//????????? ?????? ?????????

        public DisabledListView(@NonNull View itemView) {
            super(itemView);

            CardView_List = itemView.findViewById(R.id.CardView_List);

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

    public class NextDayLine extends RecyclerView.ViewHolder{

        public NextDayLine(@NonNull View itemView) {
            super(itemView);
        }
    }
}

