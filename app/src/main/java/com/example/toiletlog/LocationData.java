package com.example.toiletlog;

public class LocationData {
    String title;
    double latitude;
    double longitude;

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LocationData(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationData(){

    }

}
