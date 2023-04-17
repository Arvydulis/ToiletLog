package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import android.widget.Toolbar;

public class TestActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    Calendar time;
    TimePickerDialog timePickerDialog;
    TextView timePickerView;
    CheckBox cb;
    Button setBtn;

    Navbar navbar = new Navbar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //InstantiateAppBarAndNav();
        navbar.InstantiateAppBarAndNav(this, R.string.title_main_menu);

        timePickerView = findViewById(R.id.setTimeNotif);
        cb = findViewById(R.id.cb_repeat);
        setBtn = findViewById(R.id.btn_setNotif);

        timePickerView.setOnClickListener(timePickerOnClick);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time != null){
                    Message.SetNotif(getApplicationContext(), TestActivity.this,
                            time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), cb.isChecked());
                    Message.ShowToast(getApplicationContext(), "Notification set");
                }
                else{
                    Message.ShowToast(getApplicationContext(), "Failed");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TestActivity.this);
                Message.ShowNotification(TestActivity.this, getApplicationContext(),
                        "Test notification", "Hello " + prefs.getString("name", "") + "!!!");
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(navbar.drawerLayout.isDrawerOpen(GravityCompat.START)){
            navbar.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    View.OnClickListener timePickerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // time picker dialog
            timePickerDialog = new TimePickerDialog(TestActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            timePickerView.setText(hourOfDay + ":" + minute);
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            timePickerView.setText(sdf.format(c.getTime()));
                            time = c;
                        }
                    }, hour, minute, DateFormat.is24HourFormat(TestActivity.this));
            timePickerDialog.show();
        }
    };


}