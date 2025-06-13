package com.example.appsqllite;

public class SimpleItem {
    public int id;
    public String name;

    public SimpleItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // what will be displayed in Spinner
    }
}
