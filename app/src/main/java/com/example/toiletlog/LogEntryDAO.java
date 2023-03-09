package com.example.toiletlog;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogEntryDAO {

    @Insert
    void insert(LogEntry logEntry);

    @Query("SELECT * from LogEntry ORDER BY date, time ASC")
    List<LogEntry> getAllLogEntries();
}
