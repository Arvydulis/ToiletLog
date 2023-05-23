package com.example.toiletlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    LinearLayout addBtn, listBtn, settingsBtn, mapBtn;
    TextView title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        addBtn = findViewById(R.id.btn_addNew);
        listBtn = findViewById(R.id.btn_list);
        settingsBtn = findViewById(R.id.btn_settings);
        mapBtn = findViewById(R.id.btn_map);

        title = findViewById(R.id.menuTitle);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        title.setText("Welcome to ToiletLog,\n" + prefs.getString("name", "") + "!");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel("Daily", "Daily", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 15);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show add entry activity
                //Toast.makeText(getApplicationContext(), "add entry", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), NewItemActivity.class);
                startActivity(intent);
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show list activity
                //Toast.makeText(getApplicationContext(), "show list", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(getBaseContext(), TestActivity.class);
//                startActivity(intent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show settings activity
                //Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();

                //goes to settings
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show settings activity
                //Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                //goes to settings
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                startActivity(intent);

            }
        });

//        tempBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // show settings activity
//                Toast.makeText(getApplicationContext(), "temp", Toast.LENGTH_SHORT).show();
//
//                //goes to settings
//                Intent intent = new Intent(getBaseContext(), SingleEntryViewActivity.class);
//                startActivity(intent);
//
//
//
//            }
//        });
    }


}