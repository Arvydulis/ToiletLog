package com.example.lecture10;

public class Item {

    private String title;
    private String value;

    public Item(String title, String value){
        this.title = title;
        this.value = value;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
