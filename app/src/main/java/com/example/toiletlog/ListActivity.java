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
import java.time.format.DateTimeFormatter;
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

    Spinner dropDown;

    List<LogEntry> SortDate(List<LogEntry> list){
        int n = list.size();

        for(int i=0; i < n; i++) //For each sublist
        {
            LogEntry m = list.get(i);
            int position = i;
            LocalDate t1 = LocalDate.parse( list.get(i).getDate(), DateTimeFormat.forPattern("dd/MM/yyyy")) ;

            for (int j=i+1; j < n; j++)   //Find minimum
            {
                LocalDate t2 = LocalDate.parse( list.get(j).getDate(), DateTimeFormat.forPattern("dd/MM/yyyy")) ;
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
            LocalTime t1 = LocalTime.parse( list.get(i).getTime() ) ;

            for (int j=i+1; j < n; j++)   //Find minimum
            {
                LocalTime t2 = LocalTime.parse( list.get(j).getTime() ) ;

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

        InstantiateAppBarAndNav();

        db = AppActivity.getDatabase();
        personsListTextView = (ListView) findViewById(R.id.list_view);

        logEntryList = db.logEntryDAO().getAllLogEntries();
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

    void InstantiateAppBarAndNav(){
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView= findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.title_list);

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
