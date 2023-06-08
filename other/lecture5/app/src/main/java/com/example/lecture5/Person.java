package com.example.lecture5;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Person {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "first_name")
    private String name;

    @NonNull
    @ColumnInfo(name = "last_name")
    private String surname;

    @NonNull
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setSurname(@NonNull String surname) {
        this.surname = surname;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
