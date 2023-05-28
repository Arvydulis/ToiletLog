package com.example.toiletlog;

import java.util.ArrayList;
import java.util.List;

public class LocationData {
    String title;
    double latitude;
    double longitude;
    List<RatingItem> ratingItemList;
    List<PhotoItem> photoItemList;


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
    public List<PhotoItem> getPhotoItemList() {
        return photoItemList;
    }

    public LocationData(String title, double latitude, double longitude) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        ratingItemList = new ArrayList<RatingItem>();
        photoItemList = new ArrayList<>();
    }
    public LocationData(String title, double latitude, double longitude, List<RatingItem> ratingItemList, List<PhotoItem> photoItemList) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ratingItemList = ratingItemList;
        this.photoItemList = photoItemList;
    }

    public LocationData(){}

}
