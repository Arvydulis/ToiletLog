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

    @Query("SELECT date, COUNT(time) as count, strftime('%d',date/1000,'unixepoch') as day " +
            "FROM LogEntry " +
            "WHERE strftime('%Y',date/1000,'unixepoch') = :year " +
            "AND strftime('%m',date/1000,'unixepoch') = :month " +
            "GROUP BY strftime('%d',date/1000,'unixepoch')"
    )
    //List<DateCountResult> getEntriesDateCounts();
    List<DateCountResult> getEntriesDateCounts(String year, String month);

    @Query("SELECT type, COUNT(type) as count " +
            "FROM LogEntry " +
            "WHERE strftime('%Y',date/1000,'unixepoch') = :year " +
            "AND strftime('%m',date/1000,'unixepoch') = :month " +
            "GROUP BY type"
    )
        //List<DateCountResult> getEntriesDateCounts();
    List<TypeCountResult> getEntriesTypeCounts(String year, String month);

    @Query("SELECT size, COUNT(size) as count " +
            "FROM LogEntry " +
            "WHERE strftime('%Y',date/1000,'unixepoch') = :year " +
            "AND strftime('%m',date/1000,'unixepoch') = :month " +
            "GROUP BY size"
    )
        //List<DateCountResult> getEntriesDateCounts();
    List<SizeCountResult> getEntriesSizeCounts(String year, String month);

    @Query("SELECT * from LogEntry WHERE id = :searchId ORDER BY date, time ASC")
    LogEntry getLogEntryById(long searchId);

    @Query("SELECT * FROM LogEntry WHERE date BETWEEN :startDate AND :endDate ORDER BY time ASC")
    List<LogEntry> getLogEntriesByDate(Date startDate, Date endDate);
}
