package com.example.toiletlog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

public class Navbar extends AppCompatActivity {
    public Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;

//    public Navbar (Toolbar toolbar, NavigationView navigationView,
//                   DrawerLayout drawerLayout, ActionBarDrawerToggle drawerToggle){
//        this.toolbar = toolbar;
//        this.navigationView = navigationView;
//        this.drawerLayout = drawerLayout;
//        this.drawerToggle = drawerToggle;
//    }

    public void InstantiateAppBarAndNav( AppCompatActivity activity, int resTitle){
        toolbar = activity.findViewById(R.id.toolbar);

        activity.setSupportActionBar(toolbar);
        navigationView= activity.findViewById(R.id.nav_view);
        drawerLayout = activity.findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(activity,drawerLayout, R.string.open, R.string.close);
        drawerToggle.syncState();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(resTitle);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        TextView headerName = ((TextView)navigationView.getHeaderView(0).findViewById(R.id.header_name));
        headerName.setText( "User: "+prefs.getString("name", ""));

        //DrawerLayout finalDrawerLayout = drawerLayout;
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
                        if (!activity.getClass().getName().equals(MainActivity.class.getName())) {
                            //Toast.makeText(activity.getApplicationContext(), "main menu", Toast.LENGTH_SHORT).show();
                            intent = new Intent(activity.getBaseContext(), MainActivity.class);
                            activity.startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_new_entry: {
                        //new entry activity
                        if (!activity.getClass().getName().equals(TestActivity.class.getName())) {
                            //Toast.makeText(activity.getApplicationContext(), "new entry", Toast.LENGTH_SHORT).show();
                            intent = new Intent(activity.getBaseContext(), NewItemActivity.class);
                            activity.startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_list: {
                        if (!activity.getClass().getName().equals(DailyLog.class.getName())) {
                            //new entry activity
                            //Toast.makeText(activity.getApplicationContext(), "list", Toast.LENGTH_SHORT).show();
                            intent = new Intent(activity.getBaseContext(), ListActivity.class);
                            activity.startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_map: {
                        if (!activity.getClass().getName().equals(MapActivity.class.getName())) {
                            //new entry activity
                            //Toast.makeText(activity.getApplicationContext(), "list", Toast.LENGTH_SHORT).show();
                            intent = new Intent(activity.getBaseContext(), MapActivity.class);
                            activity.startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_settings: {
                        if (!activity.getClass().getName().equals(SettingsActivity.class.getName())) {
                            //Toast.makeText(activity.getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                            intent = new Intent(activity.getBaseContext(), SettingsActivity.class);
                            activity.startActivity(intent);
                        }
                        break;
                    }
                    case R.id.nav_analysis: {
                        if (!activity.getClass().getName().equals(ChartActivity.class.getName())) {
                            //Toast.makeText(activity.getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
                            intent = new Intent(activity.getBaseContext(), ChartActivity.class);
                            activity.startActivity(intent);
                        }
                        break;
                    }

                }
                return false;
            }
        });
    }



//
}
