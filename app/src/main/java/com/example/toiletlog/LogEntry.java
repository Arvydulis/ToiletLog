package com.example.toiletlog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LogEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "time")
    private String time;

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

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public String getTime() {
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
