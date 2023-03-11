package com.example.toiletlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LogListAdapter extends BaseAdapter {

    Context context;
    List<LogEntry> list;
    LayoutInflater inflater;

    public LogListAdapter(Context context, List<LogEntry> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }

    // override other abstract methods here

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, container, false);
        }

        TextView date = (TextView) convertView.findViewById(R.id.entry_date);
        TextView time = (TextView) convertView.findViewById(R.id.entry_time);
        TextView size = (TextView) convertView.findViewById(R.id.entry_size);

        date.setText(list.get(position).getDate());
        time.setText(list.get(position).getTime());
        size.setText(list.get(position).getSize());

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
