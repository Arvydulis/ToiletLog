package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SingleEntryViewActivity extends AppCompatActivity {

    private  AppDatabase db;
    private TextView date, time, type, size;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    TextView dateView, timeView, typeView, sizeView;
    FloatingActionButton backBtn, deleteBtn;

    Navbar navbar = new Navbar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_entry_view);

        db = AppActivity.getDatabase();

        //InstantiateAppBarAndNav();
        navbar.InstantiateAppBarAndNav(this, R.string.title_single_entry);
        drawerLayout = navbar.drawerLayout;

        dateView = findViewById(R.id.single_entry_date);
        timeView = findViewById(R.id.single_entry_time);
        typeView = findViewById(R.id.single_entry_type);
        sizeView = findViewById(R.id.single_entry_size);
        backBtn = findViewById(R.id.back_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        Intent intent = getIntent();

        if (intent != null){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = (Date) intent.getSerializableExtra("date");
            dateView.setText(sdf1.format(myDate));
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            Date myTime = (Date) intent.getSerializableExtra("time");
            timeView.setText(sdf2.format(myTime));
            typeView.setText(intent.getStringExtra("type"));
            sizeView.setText(intent.getStringExtra("size"));
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getBaseContext(), ListActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SingleEntryViewActivity.this);
                alertDialogBuilder.setTitle("Delete entry");
                alertDialogBuilder.setMessage("Confirm delete entry").setCancelable(false)
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Delete item from db
                                        long id = intent.getLongExtra("id",-1);
                                        db.logEntryDAO().deleteById(id);
                                        Message.ShowToast(getApplicationContext(), "deleted " + id);
                                        Intent intent = new Intent(getBaseContext(), ListActivity.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                alertDialogBuilder.create().show();

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
        toolbar.setTitle(R.string.title_single_entry);

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