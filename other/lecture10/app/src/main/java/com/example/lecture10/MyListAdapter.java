package com.example.lecture10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<Item> {

    private int resourceLayout;
    private Context mContext;

    public MyListAdapter(Context context, int resource,  List<Item> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View v = convertView;
        if (v == null) v = LayoutInflater.from(mContext).inflate(resourceLayout,parent,false);

        Item p = getItem(position);

        if (p != null){
            TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            TextView valueTextView = (TextView) v.findViewById(R.id.valueTextView);

            if (titleTextView != null){
                titleTextView.setText(p.getTitle());
            }
            if (valueTextView != null){
                valueTextView.setText(p.getValue());
            }
        }
        return v;
    }
}
