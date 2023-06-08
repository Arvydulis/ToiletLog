package com.example.lecture14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<WifiInfo> {

    private List<WifiInfo> wifiInfos;
    Context context;

    public CustomAdapter(List<WifiInfo> wifiInfos, Context context){
        super(context, R.layout.item_wifi, wifiInfos);
        this.wifiInfos = wifiInfos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return wifiInfos.size();
    }

    @Nullable
    @Override
    public WifiInfo getItem(int position) {
        return wifiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_wifi, null);
        }

        v.setPadding(0,10,0,10);
        WifiInfo wifiInfo = wifiInfos.get(position);
        TextView wifiNameTextView = (TextView) v.findViewById(R.id.wifiNameTextView);
        TextView RSSITextView = (TextView) v.findViewById(R.id.RSSITextView);
        TextView levelTextView = (TextView) v.findViewById(R.id.LevelTextView);
        TextView MacTextView = (TextView) v.findViewById(R.id.BSSIDTextView);

        wifiNameTextView.setText(wifiInfo.getSSID());
        RSSITextView.setText(String.valueOf(wifiInfo.getRSSI()));
        levelTextView.setText(String.valueOf(wifiInfo.getLevel()));
        MacTextView.setText(wifiInfo.getBSSID());

        return v;

    }
}
