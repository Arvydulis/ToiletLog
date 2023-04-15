package com.example.toiletlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button addBtn, listBtn, settingsBtn, notif_test, tempBtn;
    TextView title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        addBtn = findViewById(R.id.btn_addNew);
        listBtn = findViewById(R.id.btn_list);
        settingsBtn = findViewById(R.id.btn_settings);
        notif_test = findViewById(R.id.btn_notif_test);

        title = findViewById(R.id.menuTitle);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        title.setText("Welcome to ToiletLog,\n" + prefs.getString("name", "") + "!");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel("Daily", "Daily", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }




        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show add entry activity
                Toast.makeText(getApplicationContext(), "add entry", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), NewItemActivity.class);
                startActivity(intent);
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show list activity
                Toast.makeText(getApplicationContext(), "show list", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();

                //goes to settings
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);


            }
        });

        notif_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Daily")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ToiletLog")
                        .setContentText("Did you poop?")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                managerCompat.notify(1, builder.build());


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