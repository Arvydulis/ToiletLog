package com.example.toiletlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button addBtn, listBtn, settingsBtn, tempBtn;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        addBtn = findViewById(R.id.btn_addNew);
        listBtn = findViewById(R.id.btn_list);
        settingsBtn = findViewById(R.id.btn_settings);
        title = findViewById(R.id.menuTitle);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        title.setText( "Welcome to ToiletLog,\n" + prefs.getString("name", "") + "!");

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