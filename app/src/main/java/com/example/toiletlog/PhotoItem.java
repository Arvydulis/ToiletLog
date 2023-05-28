package com.example.toiletlog;

public class PhotoItem {
    private String Name;
    private String Url;

    public PhotoItem(String name, String url) {
        Name = name;
        Url = url;
    }

    public PhotoItem(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
