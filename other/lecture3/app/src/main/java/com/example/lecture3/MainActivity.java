package com.example.lecture3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    Button generateRandomNumberBtn;
    EditText insertRandomNumberEditText;
    int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        generateRandomNumberBtn = (Button) findViewById(R.id.generateRandomNumberBtn);

        generateRandomNumberBtn.setOnClickListener(generateRandomNumberOnClick);
        insertRandomNumberEditText = (EditText) findViewById(R.id.editText);
        insertRandomNumberEditText.addTextChangedListener(insertRandomNumberTextWatcher);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    View.OnClickListener generateRandomNumberOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            randomNumber = (int) (Math.random()*1000);
            Log.i("randomNumber", String.valueOf(randomNumber));
            Toast toast = Toast.makeText(getApplicationContext(),
                    String.valueOf(randomNumber), Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    TextWatcher insertRandomNumberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.toString().length() > 0) {
                if (Integer.valueOf(charSequence.toString()) == randomNumber) {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
                else {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }
            }
            else {
                getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}

