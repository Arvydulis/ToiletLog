package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class NewItemActivity extends AppCompatActivity {

    private  AppDatabase db;
    EditText dateEditText;
    DatePickerDialog datePickerDialog;

    EditText timeEditText;
    TimePickerDialog timePickerDialog;

    private Button button;

    private String type;
    private String size;

    //appbar
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        InstantiateAppBarAndNav();

        db = AppActivity.getDatabase();
        // initiate the date picker and a button
        dateEditText = (EditText) findViewById(R.id.setDate);
        // perform click event on edit text
        dateEditText.setOnClickListener(datePickerOnClick);

        // initiate the date picker and a button
        timeEditText = (EditText) findViewById(R.id.setTime);
        // perform click event on edit text
        timeEditText.setOnClickListener(timePickerOnClick);

        button = (Button) findViewById(R.id.button);
        type = "";
        size = "";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = dateEditText.getText().toString().trim();
                String time = timeEditText.getText().toString().trim();

                if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(type) || TextUtils.isEmpty(size)) {
                    Toast.makeText(getApplicationContext(), "Nae/Surname/Phone Number should not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LogEntry logEntry = new LogEntry();
                    logEntry.setDate(date);
                    logEntry.setTime(time);
                    logEntry.setType(type);
                    logEntry.setSize(size);
                    db.logEntryDAO().insert(logEntry);
                    String temp = "\n" + date + " " + time + " " + type + " " + size;
                    Toast.makeText(
                            getApplicationContext(),
                            "Saved successfully" + temp,
                            Toast.LENGTH_SHORT
                    ).show();
                    Intent intent = new Intent(getBaseContext(), ListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void onTypeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if(checked){
            type = ((RadioButton) view).getText().toString();
        }

//        switch (view.getId()) {
//            case R.id.radio_type1:
//                if (checked)
//                    type = ((RadioButton) view).getText().toString();
//                    Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
//                    break;
//            case R.id.radio_type2:
//                if (checked)
//                    type = "type2";
//                    Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
//                    break;
//            case R.id.radio_type3:
//                if (checked)
//                    type = "type3";
//                    Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radio_type4:
//                if (checked)
//                    type = "type4";
//                    Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radio_type5:
//                if (checked)
//                    type = "type5";
//                    Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radio_type6:
//                if (checked)
//                    type = "type6";
//                    Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.radio_type7:
//                if (checked)
//                    type = "type7";
//                    Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
//                break;
//        }
    }

    public void onSizeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.small:
                if (checked)
                    size = "small";
                    Toast.makeText(getApplicationContext(), size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.small_medium:
                if (checked)
                    size = "small-medium";
                    Toast.makeText(getApplicationContext(), size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.medium:
                if (checked)
                    size = "medium";
                    Toast.makeText(getApplicationContext(), size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.medium_large:
                if (checked)
                    size = "medium-large";
                    Toast.makeText(getApplicationContext(), size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.large:
                if (checked)
                    size = "large";
                    Toast.makeText(getApplicationContext(), size, Toast.LENGTH_SHORT).show();
                break;
            case R.id.very_large:
                if (checked)
                    size = "very large";
                    Toast.makeText(getApplicationContext(), size, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    View.OnClickListener datePickerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(NewItemActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    View.OnClickListener timePickerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // time picker dialog
            timePickerDialog = new TimePickerDialog(NewItemActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            timeEditText.setText(i + ":" + i1);
                        }
                    }, hour, minute, DateFormat.is24HourFormat(NewItemActivity.this));
            timePickerDialog.show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    void InstantiateAppBarAndNav(){
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView= findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.title_new_entry);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        TextView headerName = ((TextView)navigationView.getHeaderView(0).findViewById(R.id.header_name));
        headerName.setText( "User: "+prefs.getString("name", ""));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;

                switch (item.getItemId()){
                    case R.id.nav_main_menu: {
                        if (!getClass().getName().equals(MainActivity.class.getName())) {
                            Toast.makeText(getApplicationContext(), "main menu", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_new_entry: {
                        //new entry activity
                        if (!getClass().getName().equals(TestActivity.class.getName())) {
                            Toast.makeText(getApplicationContext(), "new entry", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getBaseContext(), NewItemActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_list: {
                        if (!getClass().getName().equals(DailyLog.class.getName())) {
                            //new entry activity
                            Toast.makeText(getApplicationContext(), "list", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getBaseContext(), ListActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_settings: {
                        if (!getClass().getName().equals(SettingsActivity.class.getName())) {
                            Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getBaseContext(), SettingsActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }

                }
                return false;
            }
        });
    }

}