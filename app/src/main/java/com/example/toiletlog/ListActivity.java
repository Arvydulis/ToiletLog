package com.example.toiletlog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private  AppDatabase db;

    private TextView personsListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        db = AppActivity.getDatabase();
        personsListTextView = (TextView) findViewById(R.id.txt_list);
        getLogEnntryList();
    }

    private void getLogEnntryList()
    {
        personsListTextView.setText("");
        List<LogEntry> logEntryList = db.logEntryDAO().getAllLogEntries();
        for (LogEntry logEntry : logEntryList)
        {
            personsListTextView.append(logEntry.getDate() + " " + logEntry.getTime() + " " + logEntry.getType() + " " + logEntry.getSize());
            personsListTextView.append("\n");
        }
    }
}
