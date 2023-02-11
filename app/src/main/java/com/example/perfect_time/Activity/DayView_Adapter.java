package com.example.perfect_time.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.perfect_time.R;

import java.util.ArrayList;

public class DayView_Adapter extends BaseAdapter {


    Context context;

    ArrayList<String> day_items = new ArrayList<String>();

    public void addItem(int num){
        day_items.add("" + num);
    }

    public void clear(){
        day_items.clear();
    }

    @Override
    public int getCount() {
        return day_items.size();
    }

    @Override
    public Object getItem(int position) {
        return day_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.day_dialog_item, parent, false);

        TextView TextView_num;

        TextView_num = convertView.findViewById(R.id.TextView_num);

        TextView_num.setText(day_items.get(position));

        return convertView;
    }
}
