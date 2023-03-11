package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private  AppDatabase db;

    private ListView personsListTextView;

    //appbar
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        InstantiateAppBarAndNav();

        db = AppActivity.getDatabase();
        personsListTextView = (ListView) findViewById(R.id.list_view);

        List<LogEntry> logEntryList = db.logEntryDAO().getAllLogEntries();
        LogListAdapter listAdapter = new LogListAdapter(getApplicationContext(), logEntryList);
        personsListTextView.setAdapter(listAdapter);

        //getLogEntryList();
    }

    private void getLogEntryList()
    {
//        personsListTextView.setText("");
//        List<LogEntry> logEntryList = db.logEntryDAO().getAllLogEntries();
//        for (LogEntry logEntry : logEntryList)
//        {
//            personsListTextView.append(logEntry.getDate() + " " + logEntry.getTime() + " " + logEntry.getType() + " " + logEntry.getSize());
//            personsListTextView.append("\n");
//        }
//
//        personsListTextView.add;


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
