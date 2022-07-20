package com.mycompany.app;
// --== CS400 Project Two File Header ==--
// Name: Rishi Mandyam
// CSL Username: mandyam
// Email: rmandyam@wisc.edu
// Lecture #: 001 @11:00am

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;

/**
 * @author Rishi Mandyam
 */
public interface IAirportLoader {

  /**
   * Generates list of airports without routes from the JSON data file.
   *
   * @param filePath filepath of datafile
   * @return a list of Airports
   * @throws IOException           for file errors
   * @throws FileNotFoundException for file errors
   * @throws ParseException        for file errors
   */
  public ArrayList<IAirport> loadFlights(String filePath) throws IOException, FileNotFoundException, ParseException;

  /**
   *
   * @return List of airport names
   * @throws NullPointerException
   */
  public ArrayList<String> getAirportNames() throws NullPointerException, IOException, ParseException;

  /**
   * This method loads routes into the source, destination, and all possible
   * airports in between.
   * If the source airport is an international airport, then it routes are all
   * other international airports in the destination country.
   * If the source airport is a domestic airport, then it routes to all
   * international airports in its country and those international airports route
   * to all internation airports in the destination country.
   * Then, then all airports in the destination country route to the destination
   * airport if they themselves are not the destination.
   * 
   * @param source
   * @param destination
   */
  public void loadRoutes(IAirport source, IAirport destination);

}
