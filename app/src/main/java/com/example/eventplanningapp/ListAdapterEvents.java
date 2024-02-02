package com.example.eventplanningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapterEvents extends BaseAdapter {
    private Context context;
    private ArrayList<String> eventList;
    public ListAdapterEvents(Context context, ArrayList<String> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.eventpage, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textIDD);
        String eventItem = eventList.get(position);
        textView.setText(eventItem);


        return convertView;
    }
}