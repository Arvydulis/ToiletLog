package com.example.toiletlog;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {LogEntry.class}, version = 2)
@TypeConverters({MyTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract LogEntryDAO logEntryDAO();
}
