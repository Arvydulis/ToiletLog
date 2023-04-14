package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private  AppDatabase db;

    private ListView personsListTextView;

    //appbar
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    List<LogEntry> logEntryList;

    Navbar navbar = new Navbar();

    Spinner dropDown;

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
                if (t1.compareTo(t2) > 0)
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

                if (t1.compareTo(t2) > 0)
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
        navbar.InstantiateAppBarAndNav(this);
        drawerLayout = navbar.drawerLayout;

        db = AppActivity.getDatabase();
        personsListTextView = (ListView) findViewById(R.id.list_view);

        //--------------------------------------------------------------------------------------------

        int day = 13;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023); // Set the year you want to get entries for
        calendar.set(Calendar.MONTH, Calendar.APRIL); // Set the month you want to get entries for (January is 0)
        calendar.set(Calendar.DAY_OF_MONTH, day); // Set the day you want to get entries for
        calendar.set(Calendar.HOUR, 0); // Set the day you want to get entries for
        calendar.set(Calendar.MINUTE, 0); // Set the day you want to get entries for

        Date startDate = calendar.getTime(); // Get the Date object representing the day

        calendar.set(Calendar.DAY_OF_MONTH, day+1); // Set the day you want to get entries for
        Date endDate = calendar.getTime(); // Get the Date object representing the day

        logEntryList = db.logEntryDAO().getLogEntriesByDate(startDate, endDate);
        //logEntryList = db.logEntryDAO().getAllLogEntries();
        LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
        personsListTextView.setAdapter(listAdapter);
        personsListTextView.setClickable(true);

        dropDown = findViewById(R.id.drop_down);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drop_down_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dropDown.setAdapter(adapter);

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                switch (adapter.getItem(position).toString()){
                    case "Date":
                        logEntryList = SortDate(logEntryList);
                        break;
                    case "Time":
                        logEntryList = SortTime(logEntryList);

                        break;
                    case "Size":
                        logEntryList = SortDate(logEntryList);
                        break;
                }
                LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
                personsListTextView.setAdapter(listAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        personsListTextView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), SingleEntryViewActivity.class);
                intent.putExtra("date", logEntryList.get(i).getDate());
                intent.putExtra("time", logEntryList.get(i).getTime());
                intent.putExtra("type", logEntryList.get(i).getType());
                intent.putExtra("size", logEntryList.get(i).getSize());
                intent.putExtra("id", logEntryList.get(i).getId());
                startActivity(intent);
            }
        });
        //getLogEntryList();
    }



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


}
