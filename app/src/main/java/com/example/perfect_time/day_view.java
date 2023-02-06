package com.example.perfect_time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class day_view extends BaseAdapter {

    Context context;

    ArrayList<String> day_items = new ArrayList<String>();

    public void addItem(int num){
        if(num != 0){
            day_items.add("" + num);
        } else {
            day_items.add("마지막");
        }

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
