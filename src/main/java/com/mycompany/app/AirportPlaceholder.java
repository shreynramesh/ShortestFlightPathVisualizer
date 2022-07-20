package com.mycompany.app;

import java.util.ArrayList;

public class AirportPlaceholder implements IAirport {

    private String name;
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private ArrayList<IAirport> routes;

    public AirportPlaceholder(String name, String city, String country, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public Double getLatitude() {
        return this.latitude;
    }

    @Override
    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public ArrayList<IAirport> getRoutes() {
        return this.routes;
    }

    public void setRoutes(ArrayList<IAirport> routes) {
        this.routes = new ArrayList<IAirport>();
        for (IAirport a : routes) {
            this.routes.add(a);
        }
    }

    public String toString() {
        return name + " (" + city + ", " + country + ")";
    }

    public String toTrieString() {
        return city + ", " + country + " (" + name + ")";
    }

    @Override
    public boolean isInternational() {
        if (this.getName().contains("International")) {
            return true;
        }
        return false;
    }

    @Override
    public void setRoute(IAirport route) {
        // TODO Auto-generated method stub

    }
}
