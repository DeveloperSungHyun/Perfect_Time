package com.project.perfect_time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.perfect_time.R;

import java.util.ArrayList;

public class DayOfTheWeek_Adapter extends BaseAdapter {

    Context context;

    ArrayList<DayOfTheWeek_Item> dayOfTheWeek_items = new ArrayList<DayOfTheWeek_Item>();

    public void addItem(DayOfTheWeek_Item item){
        dayOfTheWeek_items.add(item);
    }

    public void setItem(int ItemNumber, Boolean CheckMark){
        dayOfTheWeek_items.set(ItemNumber,
                new DayOfTheWeek_Item(CheckMark, dayOfTheWeek_items.get(ItemNumber).getDay_of_the_week(), dayOfTheWeek_items.get(ItemNumber).getDay_of_the_week_TextColor()));
    }

    @Override
    public int getCount() {
        return dayOfTheWeek_items.size();
    }

    @Override
    public Object getItem(int i) {
        return dayOfTheWeek_items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.day_of_the_week_itemview, viewGroup, false);

        TextView TextView_check_mark, TextView_DayOfTheWeek;

        TextView_check_mark = view.findViewById(R.id.TextView_check_mark);
        TextView_DayOfTheWeek = view.findViewById(R.id.TextView_DayOfTheWeek);

        DayOfTheWeek_Item dayOfTheWeekItem_getData = dayOfTheWeek_items.get(i);

        if(dayOfTheWeekItem_getData.getCheck_mark()) TextView_check_mark.setText("●");
        else TextView_check_mark.setText(" ");

        TextView_DayOfTheWeek.setText(dayOfTheWeekItem_getData.getDay_of_the_week());
        TextView_DayOfTheWeek.setTextColor(dayOfTheWeekItem_getData.getDay_of_the_week_TextColor());

        return view;
    }
}
