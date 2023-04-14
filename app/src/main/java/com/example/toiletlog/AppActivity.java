package com.example.toiletlog;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class AppActivity extends Application {
    static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "log_entry_db3").allowMainThreadQueries().build();
    }

    public static AppDatabase getDatabase() {return db;}
}
