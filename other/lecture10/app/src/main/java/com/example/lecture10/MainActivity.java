package com.example.lecture10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected ListView listView;
    private MyListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        ArrayList<Item> myList = new ArrayList<Item>();
        //myList.add(new Item("SERIAL:" , Build.getSerial()));
        myList.add(new Item("MODEL:" , Build.MODEL));
        myList.add(new Item("ID:" , Build.ID));
        myList.add(new Item("MANUFACTURER:" , Build.MANUFACTURER));
        myList.add(new Item("BRAND:" , Build.BRAND));
        myList.add(new Item("TYPE:" , Build.TYPE));
        myList.add(new Item("USER:" , Build.USER));
        myList.add(new Item("USER:" , String.valueOf(Build.VERSION_CODES.BASE)));
        myList.add(new Item("INCREMENTAL:" , Build.VERSION.INCREMENTAL));
        myList.add(new Item("BOARD:" , Build.BOARD));
        myList.add(new Item("BRAND:" , Build.BRAND));
        myList.add(new Item("HOST:" , Build.HOST));
        myList.add(new Item("FINGERPRINT:" , Build.FINGERPRINT));
        myList.add(new Item("VERSION Code:" , Build.VERSION.RELEASE));

        mAdapter = new MyListAdapter(this, R.layout.item_main, myList);
        listView.setAdapter(mAdapter);
        setTitle("Info");
    }
}