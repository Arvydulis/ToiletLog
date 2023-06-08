package com.example.lecture5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  AppDatabase db;
    private TextView personsListTextView;
    private Button button;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppActivity.getDatabase();
        personsListTextView = (TextView) findViewById(R.id.txt_list);
        firstNameEditText = (EditText) findViewById(R.id.edittext_name);
        lastNameEditText = (EditText) findViewById(R.id.edittext_surname);
        phoneNumberEditText = (EditText) findViewById(R.id.edittext_phone);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = firstNameEditText.getText().toString().trim();
                String surname = lastNameEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Nae/Surname/Phone Number should not be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Person person = new Person();
                    person.setName(name);
                    person.setSurname(surname);
                    person.setPhoneNumber(phoneNumber);
                    db.personDAO().insert(person);
                    Toast.makeText(
                            getApplicationContext(),
                            "Saved successfully",
                            Toast.LENGTH_SHORT
                    ).show();
                    firstNameEditText.setText("");
                    lastNameEditText.setText("");
                    phoneNumberEditText.setText("");
                    firstNameEditText.requestFocus();
                    getPersonList();
                }
            }
        });
    }

    private void getPersonList()
    {
        personsListTextView.setText("");
        List<Person> personList = db.personDAO().getAllPersons();
        for (Person person : personList)
        {
            personsListTextView.append(person.getName() + " " + person.getSurname() + " " + person.getPhoneNumber());
            personsListTextView.append("\n");
        }
    }
}