package com.example.toiletlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SingleEntryViewActivity extends AppCompatActivity {

    private  AppDatabase db;
    private TextView singleEntryTextView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_entry_view);

        db = AppActivity.getDatabase();
        singleEntryTextView = (TextView) findViewById(R.id.singleEntry);
        button = (Button) findViewById(R.id.button2);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogEntry logEntry = db.logEntryDAO().getLogEntryById(1);
                singleEntryTextView.setText(logEntry.getDate() + " " + logEntry.getTime() + " " + logEntry.getType() + " " + logEntry.getSize());
            }
        });
    }
}