package com.example.toiletlog;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LogEntry.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LogEntryDAO logEntryDAO();
}
