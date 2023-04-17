package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewItemActivity extends AppCompatActivity {

    private  AppDatabase db;
    EditText dateEditText;
    DatePickerDialog datePickerDialog;

    EditText timeEditText;
    TimePickerDialog timePickerDialog;

    private Button button;

    private Date date;
    private Date time;
    private String type;
    private String size;
    private String location;

    //appbar
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    
    Spinner locationListSpinner;
    Map<String, LocationData> locations = new HashMap<>();
    DatabaseReference db_ref;


    Navbar navbar = new Navbar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        //InstantiateAppBarAndNav();
        navbar.InstantiateAppBarAndNav(this, R.string.title_new_entry);
        drawerLayout = navbar.drawerLayout;


        db = AppActivity.getDatabase();
        // initiate the date picker and a button
        dateEditText = (EditText) findViewById(R.id.setDate);
        // perform click event on edit text
        dateEditText.setOnClickListener(datePickerOnClick);
        GetCurrDate();

        // initiate the date picker and a button
        timeEditText = (EditText) findViewById(R.id.setTime);
        // perform click event on edit text
        timeEditText.setOnClickListener(timePickerOnClick);
        GetCurrTime();

        button = (Button) findViewById(R.id.button);
        type = "";
        size = "";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateT = dateEditText.getText().toString().trim();
                String timeT = timeEditText.getText().toString().trim();

                if (TextUtils.isEmpty(dateT) || TextUtils.isEmpty(timeT) || TextUtils.isEmpty(type) || TextUtils.isEmpty(size)) {
                    Toast.makeText(getApplicationContext(), "Nae/Surname/Phone Number should not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LogEntry logEntry = new LogEntry();
                    logEntry.setDate(date);
                    logEntry.setTime(time);
                    logEntry.setType(type);
                    logEntry.setSize(size);
                    logEntry.setLocation(location);
                    db.logEntryDAO().insert(logEntry);
                    String temp = "\n" + dateT + " " + timeT + " " + type + " " + size;
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

        db_ref = FirebaseDatabase.getInstance().getReference("Locations");
        locationListSpinner = findViewById(R.id.location_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        db_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    LocationData data = snapshot.getValue(LocationData.class);
//                    locations.add(data);
                    locations.put(snapshot.getKey(), data);
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    adapter.add(snapshot.getKey());
                    // Specify the layout to use when the list of choices appears
                    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    locationListSpinner.setAdapter(adapter);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//


        locationListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                location = adapter.getItem(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                location = "No location";
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
                            //dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            c.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            dateEditText.setText(sdf.format(c.getTime()));
                            date = c.getTime();
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
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            timeEditText.setText(hourOfDay + ":" + minute);
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            timeEditText.setText(sdf.format(c.getTime()));
                            time = c.getTime();
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


        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(NewItemActivity.this);
                Message.ShowNotification(NewItemActivity.this, getApplicationContext(),
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
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    void GetCurrDate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        c.set(mYear, mMonth, mDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateEditText.setText(sdf.format(c.getTime()));
        date = c.getTime();
    }

    void GetCurrTime(){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeEditText.setText(sdf.format(c.getTime()));
        time = c.getTime();
    }


}