package com.example.lecture11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView batteryImageView;
    IntentFilter intentFilter;
    int deviceStatus;
    int batteryLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        textView = (TextView) findViewById(R.id.batteryStatusTextView);
        batteryImageView = (ImageView) findViewById(R.id.batteryImageView);
    }

    public void setBatteryImageView(int batteryLevel){
        if ((batteryLevel<=100)&&(batteryLevel>80)){
            batteryImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_100));
        }else if((batteryLevel<=80)&&(batteryLevel>50)){
            batteryImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_80));
        }else if ((batteryLevel<=50)&&(batteryLevel>20)){
            batteryImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_50));
        } else if (batteryLevel<=20) {
            batteryImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_battery_20));
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryLevel = (int) (((float) level / (float) scale) * 100.f);

            setBatteryImageView(batteryLevel);
            String text;

            if (deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                batteryImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_battery_charging));
                text = "Charging at " + batteryLevel + " %";
                textView.setText(text);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                text = "Charging at " + batteryLevel + " %";
                textView.setText(text);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL) {
                text = "Battery Full at " + batteryLevel + " %";
                textView.setText(text);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {
                text = "Unknown at " + batteryLevel + " %";
                textView.setText(text);
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                text = "Not charging at " + batteryLevel + " %";
                textView.setText(text);
            }
        }
    };

    @Override
    protected void onStart() {
        MainActivity.this.registerReceiver(broadcastReceiver,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        MainActivity.this.unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}