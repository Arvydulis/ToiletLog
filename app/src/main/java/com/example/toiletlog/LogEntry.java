package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.sql.Time;
import java.util.Date;


@Entity
public class LogEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "date")
    private Date date;

    @NonNull
    @ColumnInfo(name = "time")
    private Date time;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    @NonNull
    @ColumnInfo(name = "size")
    private String size;

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setTime(@NonNull Date time) {
        this.time = time;
    }

    @NonNull
    public Date getTime() {
        return time;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setSize(@NonNull String size) {
        this.size = size;
    }

    @NonNull
    public String getSize() {
        return size;
    }
}
