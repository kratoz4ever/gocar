package com.example.gocar;

public class Vehicles {
    private int id;
    private String name;
    private String year;
    private double latitude;
    private double longitude;
    private String image_path;
    private int  fuel_level;

    public Vehicles(int id, String name, String year, double latitude, double longitude, String image_path, int fuel_level) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image_path = image_path;
        this.fuel_level = fuel_level;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImage_path() {
        return image_path;
    }

    public int getFuel_level() {
        return fuel_level;
    }
}
