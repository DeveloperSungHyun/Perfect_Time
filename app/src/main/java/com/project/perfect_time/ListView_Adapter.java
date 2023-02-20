package com.project.perfect_time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perfect_time.R;

import java.util.ArrayList;

public class ListView_Adapter extends BaseAdapter {

    ArrayList<List_Item> list_items = new ArrayList<>();
    Context context;

    public void addItem(List_Item list_item){
        list_items.add(list_item);
    }

    @Override
    public int getCount() {
        return list_items.size();
    }

    @Override
    public Object getItem(int position) {
        return list_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        List_Item list_item = list_items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lise_item, parent, false);

            ImageView imageView = convertView.findViewById(R.id.ImageView_icon);
            TextView textView = convertView.findViewById(R.id.TextView_text);

            imageView.setImageResource(list_item.getImg());
            textView.setText(list_item.getText());
        }
        return convertView;
    }

}
