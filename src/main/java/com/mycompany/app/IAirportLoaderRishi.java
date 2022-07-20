// --== CS400 Project Two File Header ==--
// Name: Rishi Mandyam
// CSL Username: mandyam
// Email: rmandyam@wisc.edu
// Lecture #: 001 @11:00am

package com.mycompany.app;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author Rishi Mandyam
 */
public interface IAirportLoaderRishi {

  /**
   *
   * @param filePath filepath of datafile
   * @return List of  Airports
   * @throws FileNotFoundException if file cant be found
   */
  public ArrayList<IAirport> loadFlights(String filePath) throws IOException, FileNotFoundException, ParseException;

  /**
   *
   * @return List of airport names
   * @throws NullPointerException
   */
  public ArrayList<String> getAirportNames() throws NullPointerException, IOException, ParseException;

}
