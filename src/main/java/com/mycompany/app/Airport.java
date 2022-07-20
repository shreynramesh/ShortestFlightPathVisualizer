package com.mycompany.app;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Airport implements IAirport {

    private String name;
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private ArrayList<IAirport> routes;

    /**
     * Constructor. Routes are not set since we dynamically generate routes after
     * the user selects which airport they would like to travel to. All other data
     * is in the JSON file flightData.json.
     * 
     * @param name      name of the airport
     * @param city      the city in which the airport is in
     * @param country   the country in which the airport is in
     * @param latitude  latitude of the airport
     * @param longitude longitude of the airport
     */
    public Airport(String name, String city, String country, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routes = new ArrayList<IAirport>();
    }

    /**
     * Returns the name of this airport
     * 
     * @return airport name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns the city in which this airport is in
     * 
     * @returns airport city
     */
    @Override
    public String getCity() {
        return this.city;
    }

    /**
     * Returns the country in which this airport is in
     * 
     * @return airport country
     */
    @Override
    public String getCountry() {
        return this.country;
    }

    /**
     * Returns the latitude of this airport in Decimal Degrees
     * 
     * @return airport latitude
     */
    @Override
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * Returns the longitude of this airport in Decimal Degrees
     * 
     * @return airport longitude
     */
    @Override
    public Double getLongitude() {
        return this.longitude;
    }

    /**
     * Returns an ArrayList containing the other airports that this airport flys to.
     * 
     * @return airport routes
     */
    @Override
    public ArrayList<IAirport> getRoutes() {
        return this.routes;
    }

    /**
     * Sets 1 route for this airport representing that the airport offers a flight
     * to the specified airport.
     * 
     * @param route the other airport that this airport should offer a flight to
     */
    public void setRoute(IAirport route) {
        this.routes.add(route);
        // for (IAirport a : routes) {
        // this.routes.add(a);
        // }
    }

    /**
     * Sets a list of routes for this airport representing that this airport offers
     * a flight to all other airports in that list.
     * 
     * @param routes the list of routes
     */
    public void setRoutes(ArrayList<IAirport> routes) {
        this.routes = new ArrayList<IAirport>(
                routes.stream().filter(a -> !a.equals(this)).collect(Collectors.toList()));
    }

    /**
     * Prints out the Airport name, city, and country
     * Ex: Los Angeles International Airport (Los Angeles, United States)
     * 
     * @return String of airport name, city, country
     */
    public String toString() {
        return name + " (" + city + ", " + country + ")";
    }

    /**
     * Prints out the Airport name to be put in the trie. The format is slightly
     * different from the toString() method to allow the user to serach for airports
     * by city.
     * Ex: Los Angeles, United States (Los Angeles International Airport)
     * 
     * @return A String with airport city, country and name formatted to be used
     *         with the tries.
     */
    public String toTrieString() {
        return city + ", " + country + " (" + name + ")";
    }

    /**
     * Returns a boolean value that represents whether this airport is an
     * international airport. An international airport has international in its
     * name.
     * 
     * @return true if the airport is international, false if the airport is
     *         domestic
     */
    @Override
    public boolean isInternational() {
        if (this.getName().contains("International")) {
            return true;
        }
        return false;
    }
}
