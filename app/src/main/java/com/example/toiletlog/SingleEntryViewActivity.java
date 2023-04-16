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
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Message.ShowToast(getApplicationContext(), item.getTitle().toString());

        switch (item.getItemId()) {
            case R.id.action_settings:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SingleEntryViewActivity.this);
                Message.ShowNotification(SingleEntryViewActivity.this, getApplicationContext(),
                        "Test notification", "Hello " + prefs.getString("name", "") + "!!!");
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}