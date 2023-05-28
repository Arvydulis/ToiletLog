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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    private  AppDatabase db;

    private ListView personsListTextView;

    //Calendar things
    //---------------------------------------------------
    //CalendarView calendarView;
    CollapsibleCalendar collapsibleCalendar;
    Date currentDate;

    //---------------------------------------------------

    //appbar
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    List<LogEntry> logEntryList;

    Navbar navbar = new Navbar();

    Spinner dropDown;

    FloatingActionButton btn_addNewEntry;
    private Button btn_showAll;

    List<LogEntry> SortDate(List<LogEntry> list){
        int n = list.size();

        for(int i=0; i < n; i++) //For each sublist
        {
            LogEntry m = list.get(i);
            int position = i;
            Date t1 = list.get(i).getDate();//LocalDate.parse( list.get(i).getDate(), DateTimeFormat.forPattern("dd/MM/yyyy")) ;

            for (int j=i+1; j < n; j++)   //Find minimum
            {
                Date t2 = list.get(j).getDate();//LocalDate.parse( list.get(j).getDate(), DateTimeFormat.forPattern("dd/MM/yyyy")) ;
                if (t1.compareTo(t2) < 0)
                {
                    m = list.get(j);
                    position = j;
                }
            }
            LogEntry temp = list.get(position);      //Swap
            list.set(position, list.get(i));
            list.set(i, temp);
        }

        return list;
    }

    List<LogEntry> SortDateAndTIme(List<LogEntry> list){
        int n = list.size();

        for(int i=0; i < n; i++) //For each sublist
        {
            LogEntry m = list.get(i);
            int position = i;
            Date t1 = list.get(i).getDate();//LocalDate.parse( list.get(i).getDate(), DateTimeFormat.forPattern("dd/MM/yyyy")) ;
            Date time1 = list.get(i).getTime();

            for (int j=i+1; j < n; j++)   //Find minimum
            {
                Date t2 = list.get(j).getDate();//LocalDate.parse( list.get(j).getDate(), DateTimeFormat.forPattern("dd/MM/yyyy")) ;
                Date time2 = list.get(j).getTime();
                if (t1.compareTo(t2) < 0)
                {
                    m = list.get(j);
                    position = j;
                } else if (t1.equals(t2)) {
                    if (time1.compareTo(time2) < 0){
                        m = list.get(j);
                        position = j;
                    }
                }
            }
            LogEntry temp = list.get(position);      //Swap
            list.set(position, list.get(i));
            list.set(i, temp);
        }

        return list;
    }

    List<LogEntry> SortTime(List<LogEntry> list){
        int n = list.size();

        for(int i=0; i < n; i++) //For each sublist
        {
            LogEntry m = list.get(i);
            int position = i;
            Date t1 = list.get(i).getTime();//LocalTime.parse( list.get(i).getTime() ) ;

            for (int j=i+1; j < n; j++)   //Find minimum
            {
                Date t2 = list.get(j).getTime();//LocalTime.parse( list.get(j).getTime() ) ;

                if (t1.compareTo(t2) < 0)
                {
                    m = list.get(j);
                    position = j;
                }
            }
            LogEntry temp = list.get(position);      //Swap
            list.set(position, list.get(i));
            list.set(i, temp);
        }

        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //InstantiateAppBarAndNav();
        navbar.InstantiateAppBarAndNav(this, R.string.title_list);
        drawerLayout = navbar.drawerLayout;

        db = AppActivity.getDatabase();
        personsListTextView = (ListView) findViewById(R.id.list_view);
        btn_addNewEntry = findViewById(R.id.btn_addNewEntry_listActivity);
        btn_showAll = findViewById(R.id.showAll);

        //--------------------------------------------------------------------------------------------
        //test for now
        //date pick thing
        //--------------------------------------------------------------------------------------------

        collapsibleCalendar = findViewById(R.id.calendarView);

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onWeekChange(int i) {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onItemClick(@NonNull View view) {

            }

            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                Log.d(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());

                Calendar calendar = Calendar.getInstance();

                calendar.set(day.getYear(), day.getMonth() , day.getDay(), 0, 0);

                //calendar.set(Calendar.HOUR, 0);
                //calendar.set(Calendar.MINUTE, 0);
                currentDate = calendar.getTime();

//                int d = calendar.get(Calendar.DAY_OF_MONTH);
//                calendar.set(Calendar.DAY_OF_MONTH, d+1);
                calendar.set(day.getYear(), day.getMonth() , day.getDay()+1, 0, 0);
                Date endDate = calendar.getTime();

                logEntryList = db.logEntryDAO().getLogEntriesByDate(currentDate, endDate);
                logEntryList = SortTime(logEntryList);

                LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
                personsListTextView.setAdapter(listAdapter);
                btn_showAll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onDayChanged() {
            }

            @Override
            public void onClickListener() {

            }
        });

//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
//                Calendar calendar = Calendar.getInstance();
//
//                calendar.set(year, month, dayOfMonth, 0, 0);
//
//                //calendar.set(Calendar.HOUR, 0);
//                //calendar.set(Calendar.MINUTE, 0);
//                currentDate = calendar.getTime();
//
////                int d = calendar.get(Calendar.DAY_OF_MONTH);
////                calendar.set(Calendar.DAY_OF_MONTH, d+1);
//                calendar.set(year, month, dayOfMonth+1, 0, 0);
//                Date endDate = calendar.getTime();
//
//                logEntryList = db.logEntryDAO().getLogEntriesByDate(currentDate, endDate);
//                logEntryList = SortTime(logEntryList);
//
//                LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
//                personsListTextView.setAdapter(listAdapter);
//                btn_showAll.setVisibility(View.VISIBLE);
//            }
//        });

        //--------------------------------------------------------------------------------------------

        logEntryList = db.logEntryDAO().getAllLogEntries();
        SortDateAndTIme(logEntryList);

        LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
        personsListTextView.setAdapter(listAdapter);
        personsListTextView.setClickable(true);

//        dropDown = findViewById(R.id.drop_down);
//
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.drop_down_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        dropDown.setAdapter(adapter);

//        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//                switch (adapter.getItem(position).toString()){
//                    case "Date":
//                        logEntryList = SortDate(logEntryList);
//                        break;
//                    case "Time":
//                        logEntryList = SortTime(logEntryList);
//
//                        break;
//                    case "Size":
//                        logEntryList = SortDate(logEntryList);
//                        break;
//                }
//                LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
//                personsListTextView.setAdapter(listAdapter);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });

        personsListTextView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), SingleEntryViewActivity.class);
                intent.putExtra("date", logEntryList.get(i).getDate());
                intent.putExtra("time", logEntryList.get(i).getTime());
                intent.putExtra("type", logEntryList.get(i).getType());
                intent.putExtra("size", logEntryList.get(i).getSize());
                intent.putExtra("location", logEntryList.get(i).getLocation());
                intent.putExtra("id", logEntryList.get(i).getId());
                startActivity(intent);
            }
        });
        //getLogEntryList();

        btn_addNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewItemActivity.class);
                startActivity(intent);
            }
        });

        btn_showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logEntryList = db.logEntryDAO().getAllLogEntries();
                logEntryList = SortDateAndTIme(logEntryList);

                LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
                personsListTextView.setAdapter(listAdapter);
                btn_showAll.setVisibility(View.GONE);
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ListActivity.this);
                Message.ShowNotification(ListActivity.this, getApplicationContext(),
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private String getFormattedDate(long dateInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(new Date(dateInMillis));
    }

}
