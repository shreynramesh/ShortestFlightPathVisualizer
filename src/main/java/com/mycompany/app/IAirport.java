// --== CS400 Project Two File Header ==--
// Name: Rishi Mandyam
// CSL Username: mandyam
// Email: rmandyam@wisc.edu
// Lecture #: 001 @11:00am

package com.mycompany.app;

import java.util.ArrayList;

/**
 * @author Rishi Mandyam
 */
public interface IAirport {

    /**
     *
     * @return Name of the airport
     */
    public String getName();

    /**
     *
     * @return city of the airport
     */
    public String getCity();

    /**
     *
     * @return country of airport
     */
    public String getCountry();

    /**
     * Prints out the Airport name and 3 letter code
     * Ex: Los Angeles Airport
     * 
     * @return String of airport name and 3 letter code
     */
    public String toString();

    /**
     * Prints out the Airport name to be put in the trie
     * 
     * @return A String with airport city, country and name formatted to be used
     *         with the tries.
     */
    public String toTrieString();

    /**
     *
     * @return Latitude of coordinate
     */
    public Double getLatitude();

    /**
     *
     * @return Longitude coordinate of Airport
     */
    public Double getLongitude();

    /**
     *
     * @return list of departures to other airports and
     */
    public ArrayList<IAirport> getRoutes();

    /**
     * Setting a route for this airport
     * 
     * @param route the route
     */
    public void setRoute(IAirport route);

    /**
     * Setting the routes for this airport
     * 
     * @param routes the ArrayList of routes
     */
    public void setRoutes(ArrayList<IAirport> routes);

    /**
     * Returns a boolean value that represents whether this airport is an
     * international airport. An international airport has international in its
     * name.
     * 
     * @return true if the airport is international, false if the airport is
     *         domestic
     */
    public boolean isInternational();

}
