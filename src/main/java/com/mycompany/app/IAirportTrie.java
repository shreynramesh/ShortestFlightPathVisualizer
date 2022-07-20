package com.mycompany.app;

import java.util.ArrayList;

/**
 * this interface is used to store Airport object with airport name as the key,
 * find airport object by it's name and efficiently extract airport name with
 * certain prefix
 * 
 * @author Yun Li
 */
public interface IAirportTrie {

    /**
     * This is method is used to insert an Airport name to the AirportTrie
     * 
     * @param name airport name
     */
    void insert(String name);

    /**
     * This is method is used to check if AirportTrie contains certain name
     * 
     * @param name airport name
     */
    boolean containName(String name);

    /**
     * This method is used to delete an airport name from the AirportTrie
     * 
     * @param name airport name
     */
    boolean delete(String name);

    /**
     * This method is used to check if the AirportTrie contains an airport name with
     * a certain prefix
     * 
     * @param prefix
     * @return boolean
     */
    boolean containsPrefix(String prefix);

    /**
     * This method is used to return an Arraylist of the first 10 airport name with
     * the prefix
     * 
     * @param prefix
     * @return an ArrayList<airportname>
     */
    ArrayList<String> searchPrefix(String prefix);

}
