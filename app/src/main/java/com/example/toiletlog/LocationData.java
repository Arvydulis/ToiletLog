package com.example.toiletlog;

import java.util.ArrayList;
import java.util.List;

public class LocationData {
    String title;
    double latitude;
    double longitude;
    List<RatingItem> ratingItemList;


    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public List<RatingItem> getRatingItemList() {
        return ratingItemList;
    }

    public LocationData(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        ratingItemList = new ArrayList<RatingItem>();
    }
    public LocationData(String title, double latitude, double longitude, List<RatingItem> ratingItemList) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ratingItemList = ratingItemList;
    }

    public LocationData(){}

}
