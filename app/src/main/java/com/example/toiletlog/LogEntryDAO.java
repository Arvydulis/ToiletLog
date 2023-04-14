package com.example.toiletlog;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface LogEntryDAO {

    @Insert
    void insert(LogEntry logEntry);

    @Query("DELETE FROM LogEntry WHERE id = :id")
    void deleteById(long id);

    @Query("SELECT * from LogEntry ORDER BY date, time ASC")
    List<LogEntry> getAllLogEntries();

    @Query("SELECT * from LogEntry WHERE id = :searchId ORDER BY date, time ASC")
    LogEntry getLogEntryById(long searchId);

    @Query("SELECT * FROM LogEntry WHERE date BETWEEN :startDate AND :endDate ORDER BY time ASC")
    List<LogEntry> getLogEntriesByDate(Date startDate, Date endDate);
}
