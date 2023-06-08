package com.example.lecture4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private TextView currentMonthTextView;
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}