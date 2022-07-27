package com.example.perfect_time.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.perfect_time.R;

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ActivateListView){
            ActivateView(((ActivateListView) holder), position);
        }else if(holder instanceof DisabledListView){
            DisabledView(((DisabledListView) holder), position);
        }else{

        }
    }

    private void ActivateView(ActivateListView holder, int position){
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
        ImageView ImageView_important;//중요알림 표시 아이콘

        TextView TextView_Name;//알람 이름
        TextView TextView_Memo;//알람 메모

        TextView TextView_Time;//시간 시 : 분
        TextView TextView_Time_AmPm;// (오전, 오후) 구분

        ImageView ImageView_sound;//소리알림 아이콘
        ImageView ImageView_vibration;//진동알림 아이콘
        ImageView ImageView_popup;//팝업창 알림 아이콘

        public ActivateListView(@NonNull View itemView) {
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

